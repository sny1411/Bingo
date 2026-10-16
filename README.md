## Description:

<p>Bingo is a Minecraft plugin for Paper. Each team gets a grid of 25 challenges and the goal is to complete as many of them as possible.</p>

## More information:
<p>If you want to know more, check out <a href="https://poulmouth.gitbook.io/untitled/">this page</a> (in French), which explains how the whole game works (it was written together with a friend).</p>

## Requirements:

- A Paper 26.2 server (Java 25 or newer)

## Installation:

Download the plugin jar and the resource pack from the [latest release](https://github.com/sny1411/Bingo/releases/latest), put the jar in the `plugins/` folder of your server and restart it.

Players need the resource pack to see the icons of the plugin (the BINGO title, the medals of the ranking), otherwise they see boxes. Either each player adds it to their `resourcepacks/` folder and enables it in the game, or the server sends it when they join: set `resource-pack` in `server.properties` to the download link of the resource pack in the release.

## Building:

```
./gradlew build
```
The plugin jar is `build/libs/Bingo-dev.jar`. See [CONTRIBUTING.md](CONTRIBUTING.md) for more details.

## Development:

```
./gradlew runServer
```
Builds the plugin and starts a local Paper 26.2 server with it, in the `run/` folder. Join it with a Minecraft 26.2 client at `localhost:25566`. The server runs in offline mode and only listens on `127.0.0.1`. Starting it means you accept the [Minecraft EULA](https://aka.ms/MinecraftEULA).

Then, in another terminal:
```
./gradlew runClients -Pplayers=3          # Player1, Player2, Player3
./gradlew runClients -Pplayers=Alice,Bob
```
Starts one Minecraft 26.2 client per player, each joining the test server with an offline username. See [CONTRIBUTING.md](CONTRIBUTING.md) for more details.

## Usage and commands:

- Once the plugin is installed on your server, a game is created right away. Before the game starts, players who join are teleported to the spawn and get the team selector. Players who join a game in progress without a team become spectators.

- The settings item is given to players with the `bingo.settings` permission (operators by default), and is given or removed when it changes during the setup. The commands require either operator status or the permissions listed below:

| Command                                    | Description                                                                                            | Permission            |
|--------------------------------------------|--------------------------------------------------------------------------------------------------------|-----------------------|
| `/bingo`                                   | Shows your challenge grid during a game                                                                | No permission         |
| `/bingo spectate`                          | Lets players switch to spectator mode once their team has finished                                     | No permission         |
| `/bonus <challenge>`                       | Validates an active bonus challenge                                                                    | No permission         |
| `/bingo new`                               | Creates a new game (during a game, use `/bingo new confirm`)                                           | bingo.command.new     |
| `/bingo start`                             | Starts the game (every online player must be in a team)                                                | bingo.command.start   |
| `/bingo stop`                              | Stops the game before the end                                                                          | bingo.command.stop    |
| `/bingo results`                           | Shows the ranking to everyone once the game is over                                                    | bingo.command.results |
| `/valid <add\|remove> <team> <challenge>`  | Forces a challenge to be validated, or un-validated, during or after a game (ids are suggested)        | bingo.valid           |
| `/timer <set\|add\|speed> <value>`         | Moves the game timer forward (`set 59:59`, `add 10:00`) or speeds it up (`speed 60`)                   | bingo.timer           |
| `/bingo pack`                              | Shows a title and plays a sound so players can check whether the resource pack is installed            | bingo.command.pack    |

---

- Finally, to make sure the plugin works properly, I recommend these server settings (the test server started by `./gradlew runServer` applies them):
    <ul>
        <li>Do not disable monster spawning, with the <code>spawn_monsters</code> game rule (the plugin sets the difficulty to hard when the game starts).</li>
        <li>Set the default game mode to survival and do not force it: <code>gamemode=survival</code> and <code>force-gamemode=false</code> in <code>server.properties</code>.</li>
        <li>Do not disable the Nether or the End: <code>enable-nether</code> in <code>config/paper-global.yml</code> and <code>allow-end</code> in <code>bukkit.yml</code>.</li>
        <li>Make sure structures are enabled: <code>generate-structures=true</code> in <code>server.properties</code>.</li>
        <li>Disable spawn protection: <code>spawn-protection=0</code> in <code>server.properties</code>.</li>
    </ul>

## Contributing:

Contributions are welcome! See [CONTRIBUTING.md](CONTRIBUTING.md) to get started, and the [`good first issue`](https://github.com/sny1411/Bingo/labels/good%20first%20issue) label for small tasks.

## License:

This project is licensed under the [MIT License](LICENSE).

---
this plugin is not approved or affiliated with Mojang or Microsoft.
