# Contributing to Bingo

Thanks for your interest in Bingo! Bug reports, ideas and pull requests are all welcome.

## Getting started

You need:
- a JDK 17 or newer
- [Maven](https://maven.apache.org/)
- a Paper 1.20 server to test the plugin

Build the plugin:
```
mvn package
```
The plugin jar is `target/Bingo-1.0.jar`. Copy it into the `plugins/` folder of your test server and restart it.

> The build will move to Gradle, with commands to start a test server and test clients directly from the project (see [#2](https://github.com/sny1411/Bingo/issues/2), [#3](https://github.com/sny1411/Bingo/issues/3) and [#4](https://github.com/sny1411/Bingo/issues/4)).

## Picking an issue

- Issues labeled [`good first issue`](https://github.com/sny1411/Bingo/labels/good%20first%20issue) are small and a good way to discover the code.
- Leave a comment on the issue before starting, so two people don't work on the same thing.
- If you want to work on something that has no issue yet, open one first so we can discuss it.

## Making changes

1. Fork the repository and create a branch from `main`, named after the issue, for example `fix/7-finish-message` or `feat/14-challenge-ids`.
2. Keep each pull request focused on **one issue**. Small pull requests are reviewed faster.
3. Match the style of the surrounding code (4-space indentation, same naming).
4. For new messages, use Adventure components (`Component.text("Text", NamedTextColor.GREEN)`) instead of `§` color codes (see [#18](https://github.com/sny1411/Bingo/issues/18)).
5. Refactoring pull requests must not change the gameplay.
6. Test your change on a Paper 1.20 server: start a game, and check the server console for errors.

## Adding a challenge

Challenges are listed in `src/main/resources/challenges.csv`, one per line:
```
name|description|difficulty|icon
```
- `difficulty`: `EASY`, `MEDIUM`, `HARD` or `EXTREME`
- `icon`: a Bukkit `Material` name in uppercase (e.g. `FIRE_CHARGE`), or a lowercase key for a custom icon defined in `Challenge.createItem()` (e.g. `dolphin`)

The detection of the challenge goes in `listener/ChallengesListener.java`, using the exact same name as in the CSV.

## Opening a pull request

- Describe what you changed and how you tested it.
- Link the issue with `Fixes #<number>` so it is closed automatically when the pull request is merged.

## License

By contributing, you agree that your contributions will be licensed under the [MIT License](LICENSE).
