# Anvil Actions

GitHub Actions for projects that test with [Anvil](https://anvil.whereareiam.me).

A runner starts with an empty home directory, so every job downloads its servers, patches Paper, fetches the
protocol libraries and installs Java runtimes again before the first scenario starts. `setup` keeps all of that
in the GitHub Actions cache, and `report` keeps what you need to understand a failed scenario.

```yaml
steps:
  - uses: actions/checkout@v7
  - uses: actions/setup-java@v6
    with:
      distribution: temurin
      java-version: 21
  - uses: gradle/actions/setup-gradle@v6

  - uses: whereareiam/anvil-actions/setup@v1

  - run: ./gradlew anvilTest

  - uses: whereareiam/anvil-actions/report@v1
    if: failure()
```

Set up Java and Gradle the way your project already does; the actions only handle what Anvil keeps.

## setup

Restores Anvil's cache directory before the scenarios run and saves it when the job succeeds.

| Input | Default | |
|---|---|---|
| `cache` | `auto` | `auto`, `read-write` or `read-only`. With `auto`, runs on the default branch write the cache and every other run only reads it, so pull requests start from what the default branch left. |
| `key-prefix` | | Separates the caches of jobs that start different servers, such as the suites of a matrix. |
| `key-files` | Gradle build files, `gradle.properties` and version catalogs | Newline-separated globs of the files that decide which servers a run needs. A change to any of them starts a new cache. |
| `directory` | `~/.anvil` | Anvil's cache directory, if your build changes the engine's `cacheDirectory`. |

| Output | |
|---|---|
| `key` | Cache key this run restores and saves under. |
| `downloads-hit` | Whether the servers, protocol libraries and Java runtimes came from a cache of the same key. |
| `workspaces-hit` | Whether the prepared workspaces came from a cache of the same key. |

It keeps two caches:

- **Downloads**: server distributions, protocol libraries and Java runtimes. A run whose build files changed starts
  from the latest older cache and only downloads what is new.
- **Workspaces**: the prepared server files Anvil copies into every process, such as Paper's patched server and
  libraries. They depend on the exact configuration, so they are only restored from a cache of the same key and
  start empty after a change to the key files.

Login accounts, ports, locks and Docker images are never cached.

### A matrix of suites

Jobs that start different servers keep separate caches, and every job of a pull request reads the cache its
counterpart on the default branch wrote:

```yaml
jobs:
  scenarios:
    strategy:
      matrix:
        suite: [join, account, cluster]
    steps:
      # ...
      - uses: whereareiam/anvil-actions/setup@v1
        with:
          key-prefix: ${{ matrix.suite }}
      - run: ./gradlew "anvil${SUITE^}Test"
        env:
          SUITE: ${{ matrix.suite }}
      - uses: whereareiam/anvil-actions/report@v1
        if: failure()
        with:
          name: anvil-${{ matrix.suite }}
```

The cache of the default branch is only written by runs on the default branch. If those never run your scenarios,
add a job that does on pushes to it, or the pull requests have nothing to read.

## report

Uploads test results, test reports and the console logs of the processes Anvil kept after a failed scenario as one
artifact.

| Input | Default | |
|---|---|---|
| `name` | `anvil-<job id>` | Artifact name. Each job of a matrix passes its own. |
| `path` | `**/build/test-results/**/*.xml`, `**/build/reports/tests/**`, `**/build/anvil/**/anvil-console.log` | Newline-separated paths and globs to upload. |
| `retention-days` | `7` | Days GitHub keeps the artifact. |

| Output | |
|---|---|
| `artifact-url` | Address of the uploaded artifact, empty when nothing was found. |

## Versions

`@v1` follows every release of major version 1. Pin an exact release such as `@v1.0.0`, or a commit, if you prefer
to update by hand. A new major version is released when an input changes incompatibly or the actions stop
working with an older Anvil.

## License

[Apache License 2.0](LICENSE)
