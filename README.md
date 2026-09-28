## Description:

<p>Bingo is a Minecraft plugin for Paper. Each team gets a grid of 25 challenges and the goal is to complete as many of them as possible.</p>

## More information:
<p>If you want to know more, check out <a href="https://poulmouth.gitbook.io/untitled/">this page</a> (in French), which explains how the whole game works (it was written together with a friend).</p>

## Requirements:

- A Paper 1.20 server (Java 17 or newer)

## Building:

```
./gradlew build
```
The plugin jar is `build/libs/Bingo-1.0.jar`. See [CONTRIBUTING.md](CONTRIBUTING.md) for more details.

## Development:

```
./gradlew runServer
```
Builds the plugin and starts a local Paper 1.20.1 server with it, in the `run/` folder. Join it with a Minecraft 1.20.1 client at `localhost:25566`. The server runs in offline mode and only listens on `127.0.0.1`. Starting it means you accept the [Minecraft EULA](https://aka.ms/MinecraftEULA).

Then, in another terminal:
```
./gradlew runClients -Pplayers=3          # Player1, Player2, Player3
./gradlew runClients -Pplayers=Alice,Bob
```
Starts one Minecraft 1.20.1 client per player, each joining the test server with an offline username. See [CONTRIBUTING.md](CONTRIBUTING.md) for more details.

## Usage and commands:

- Once the plugin is installed on your server, a game is created right away. Before the game starts, players who join are teleported to the spawn and get the team selector. Players who join a game in progress without a team become spectators.

- The settings item is only given to operators. The other commands require either operator status or the permissions listed below:

| Command                                    | Description                                                                                            | Permission     |
|--------------------------------------------|--------------------------------------------------------------------------------------------------------|----------------|
| `/newGame`                                 | Creates a new game (during a game, use `/newGame confirm`)                                             | bingo.newGame  |
| `/start`                                   | Starts the game (every online player must be in a team)                                                | bingo.start    |
| `/bingo`                                   | Shows your challenge grid during a game                                                                | No permission  |
| `/testPack`                                | Shows a title and plays a sound so players can check whether the resource pack is installed            | bingo.testPack |
| `/valid add <team> <challenge>`            | Forces a challenge to be validated in case of a problem (use `_` instead of spaces in the name)        | bingo.valid    |
| `/stopGame`                                | Stops the game before the end                                                                          | bingo.stopGame |
| `/result`                                  | Shows the ranking to everyone once the game is over                                                    | bingo.result   |
| `/spec`                                    | Lets players switch to spectator mode once their team has finished                                     | No permission  |
| `/bonus <challenge>`                       | Validates an active bonus challenge                                                                    | No permission  |

---

- Finally, to make sure the plugin works properly, I recommend these server settings:
    <ul>
        <li>Do not disable monster spawning (the plugin sets the difficulty to hard when the game starts).</li>
        <li>Set the default game mode to survival and do not force it.</li>
        <li>Do not disable the Nether or the End.</li>
        <li>Make sure structures are enabled.</li>
        <li>Disable spawn protection.</li>
    </ul>

## Contributing:

Contributions are welcome! See [CONTRIBUTING.md](CONTRIBUTING.md) to get started, and the [`good first issue`](https://github.com/sny1411/Bingo/labels/good%20first%20issue) label for small tasks.

## License:

This project is licensed under the [MIT License](LICENSE).

---
this plugin is not approved or affiliated with Mojang or Microsoft.
