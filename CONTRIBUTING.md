# Contributing to Bingo

Thanks for your interest in Bingo! Bug reports, ideas and pull requests are all welcome.

## Getting started

You need:
- Java 17 or newer to start Gradle. The build runs on JDK 25: Gradle uses it if it is installed and downloads it otherwise (no need to install Gradle either, the wrapper downloads it)
- a Minecraft 26.2 client to test the plugin

Build the plugin:
```
./gradlew build
```
On Windows, use `gradlew.bat build`. The plugin jar is `build/libs/Bingo-1.0.jar`.

The build also runs the unit tests (`src/test/java`, JUnit). Run them alone with `./gradlew test`. They cover the logic that doesn't need a server, such as the bingo and points rules in `ScoreRules`.

Start a local test server with the plugin:
```
./gradlew runServer
```
The server lives in `run/` (ignored by git) and listens on `localhost:25566`, in offline mode. Starting it means you accept the [Minecraft EULA](https://aka.ms/MinecraftEULA). Type server commands (`op <name>`, `stop`…) directly in the terminal.

To test what depends on the game time (end-of-game warnings, storm, bonus challenges, end of the game) without waiting, use `/timer` during a game: `/timer set 1:59:50` jumps to 10 seconds before the end of a 2-hour game, `/timer add 10:00` moves forward by 10 minutes, and `/timer speed 60` makes one second last a minute (`/timer speed 1` to go back to real time). What was planned in the skipped time happens once, and the skipped end-of-game warnings are not shown.

Then start test clients, in another terminal:
```
./gradlew runClients                      # Player1
./gradlew runClients -Pplayers=3          # Player1, Player2, Player3
./gradlew runClients -Pplayers=Alice,Bob
```
Each client joins the test server with an offline username (3 to 16 letters, digits or `_`) and has its own folder in `run/clients/<name>/`, with its logs in `logs/`. Closing a client doesn't close the others; Ctrl+C in the terminal closes them all. The first run downloads Minecraft and its assets, which takes a few minutes.

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
6. Test your change on a Paper 26.2 server: start a game, and check the server console for errors.

## Adding a challenge

Challenges are listed in `src/main/resources/challenges.csv`, one per line:
```
id|name|description|difficulty|icon
```
- `id`: a unique identifier in uppercase (e.g. `SUICIDE_SQUAD`), also added to the `ChallengeId` enum. The plugin logs an error on startup if an id is in the CSV but not in the enum, or the other way around
- `difficulty`: `EASY`, `MEDIUM`, `HARD` or `EXTREME`
- `icon`: a Bukkit `Material` name in uppercase (e.g. `FIRE_CHARGE`), or a lowercase key for a custom icon defined in `Challenge.createItem()` (e.g. `dolphin`)

The detection of the challenge goes in `listener/challenges/`, using its id (`ChallengeId.SUICIDE_SQUAD`): in the listener of its theme (mobs, player, advancements, world) when an event realizes it, or in `ChallengeVerifier.verifChallenge()` when it is checked by clicking it in the grid.

## Opening a pull request

- Describe what you changed and how you tested it.
- Link the issue with `Fixes #<number>` so it is closed automatically when the pull request is merged.

## License

By contributing, you agree that your contributions will be licensed under the [MIT License](LICENSE).
