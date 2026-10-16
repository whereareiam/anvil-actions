# AGENTS.md

## Purpose and scope

Anvil Actions are the GitHub Actions for projects that test with Anvil. They are used by projects outside
whereareiam, so they stay independent of how a project builds: they never set up Java or Gradle and never call
`whereareiam/devops` or `whereareiam/blockops`. Only this repository's own workflows may use those.

## Layout

| Path | Responsibility |
|---|---|
| `setup/` | Restores and saves Anvil's cache directory: downloads with fallback to older caches, workspaces only from the same key |
| `report/` | Uploads test results and the console logs of failed scenarios |
| `.github/check/` | The project the checks run: one Paper server that one player joins |
| `.github/workflows/reusable-check.yml` | Lints the workflows and scripts, and runs the check project through both actions, once with an empty cache and once with the cache the first run saved |
| `README.md` | Usage, inputs and outputs of every action |

Each action is a folder with an `action.yml`; logic longer than a few lines goes into a script beside it.

## Rules

- Inputs and outputs are a public contract. Renaming or removing one, or changing a default so existing workflows
  behave differently, is a major change.
- Cache only what Anvil downloads or prepares. Never cache `accounts`, which holds login tokens, nor `locks`,
  `ports` or `docker-images`.
- When Anvil moves or adds a directory under its cache, update `setup` and the README in the same change.
- Pin actions by major version, such as `actions/cache@v6`; Dependabot updates them.
- Update the README with every change of an input, an output or what an action does.

## Checks

`reusable-check.yml` runs on every pull request and before a release. The check project runs on the Anvil build in
`ANVIL_VERSION`; move it forward when the actions need a newer Anvil. It only uses a small part of Anvil on purpose,
so it fails because of the actions rather than because of a scenario.

Run `actionlint` and `shellcheck setup/*.sh .github/scripts/*.sh` locally before pushing.

## Branching and pull requests

- Build each change in its own `feature/...` branch and merge it into `dev` through a pull request. Pull requests
  are merged by rebase, so a branch holds clean conventional commits.
- Label a pull request with exactly one of `feature`, `change`, `bug`, `dependencies`, or with `skip-changelog`;
  `major` may accompany a category.
- A suggestion opens with `suggestion`; an accepted one trades it for `feature` or `change`.

## Release notes

A merged pull request lands in the release draft as `* <title> ([#<number>](<url>))`, under the category of its
label. The draft is written for developers who use the actions.

- Titles follow `Area: Description` with a past-tense description of what changed for them: `Setup: Fixed ...`.
- Areas: `Setup`, `Report`. Add one, and list it here, when a new action is added.
- `skip-changelog` for changes nobody using the actions would notice: refactors, CI, documentation, and fixes of
  bugs that never shipped.
- `major` when workflows using the actions must change on upgrade.

Hand-written text of a release (subtitle, summary, upgrade notes) lives in `.github/release-notes.md`, whose
`version` must match the version the draft resolves to. Publishing a release runs the checks on its tag and moves
the major tag, such as `v1`, to it.
