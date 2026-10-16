#!/usr/bin/env bash
# Writes how long each scenario of the check project took into the job summary.
set -euo pipefail

{
	echo "### $1"
	echo
	echo "| Scenario | Seconds |"
	echo "|---|---|"
	for file in build/test-results/anvilTest/*.xml; do
		grep -o '<testcase [^>]*' "$file" | sed -E 's/.*name="([^"]*)".* time="([^"]*)".*/| \1 | \2 |/'
	done
} >> "$GITHUB_STEP_SUMMARY"
