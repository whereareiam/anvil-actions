## Summary

<!-- What changes for workflows that use the actions, and why. -->

## Release notes

<!-- The title and the label decide what the release draft shows. See "Release notes" in AGENTS.md. -->

- [ ] The title follows `Area: Description` with a listed area, such as `Setup: Fixed ...`
- [ ] Exactly one label: `feature`, `change`, `bug`, `dependencies`, or `skip-changelog`
- [ ] `bug` only for a bug that shipped in a release; a fix for a bug that only existed on `dev` is `skip-changelog`
- [ ] `major` added when workflows must change on upgrade
- [ ] Upgrade steps added to `.github/release-notes.md`, if workflows need them

## Checks

- [ ] README updated for changed inputs, outputs or behavior
- [ ] The check project covers the change, if it changes what is cached or uploaded
