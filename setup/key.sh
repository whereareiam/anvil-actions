#!/usr/bin/env bash
# Decides whether this run writes the cache and derives its key from the files that decide which servers it needs.
set -euo pipefail

case "$POLICY" in
	read-only) read_only=true ;;
	read-write) read_only=false ;;
	auto) [[ "$GITHUB_REF" == "refs/heads/$DEFAULT_BRANCH" ]] && read_only=false || read_only=true ;;
	*) echo "::error::cache must be auto, read-write or read-only, not '$POLICY'" >&2; exit 1 ;;
esac

[[ "$PREFIX" =~ ^[A-Za-z0-9._-]*$ ]] || { echo "::error::key-prefix may only hold letters, digits, '.', '_' and '-'" >&2; exit 1; }

cd "$GITHUB_WORKSPACE"
shopt -s globstar nullglob dotglob
files=()
while IFS= read -r pattern; do
	pattern="${pattern#"${pattern%%[![:space:]]*}"}"
	pattern="${pattern%"${pattern##*[![:space:]]}"}"
	[[ -z "$pattern" ]] && continue
	# shellcheck disable=SC2206 # the pattern is expanded on purpose
	matches=($pattern)
	for file in "${matches[@]}"; do
		[[ -f "$file" ]] || continue
		# Build output and Gradle's own state never decide which servers a run needs.
		[[ "/$file" == */build/* || "/$file" == */.gradle/* || "/$file" == */node_modules/* ]] && continue
		files+=("$file")
	done
done <<< "$KEY_FILES"

if (( ${#files[@]} == 0 )); then
	hash=none
else
	hash="$(printf '%s\n' "${files[@]}" | sort -u | while IFS= read -r file; do
		printf '%s %s\n' "$(sha256sum "$file" | cut -d' ' -f1)" "$file"
	done | sha256sum | cut -c1-32)"
fi

scope="$RUNNER_OS-${PREFIX:-default}"
{
	echo "read-only=$read_only"
	echo "scope=$scope"
	echo "key=$scope-$hash"
} >> "$GITHUB_OUTPUT"
echo "Anvil cache key $scope-$hash from ${#files[@]} files, $([[ $read_only == true ]] && echo read-only || echo read-write)"
