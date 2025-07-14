## 🌍 Fast & Simple SWM Temporary World API

Easily create **temporary worlds** using the power of [SlimeWorldManager](https://github.com/Grinderwolf/Slime-World-Manager) and [SnapAPI](https://github.com/sxbide/SnapAPI).  
Perfect for minigames, isolated environments, or fast-loading sandbox worlds.

---

## ⚡ Features

- 🚀 Instantly load and unload worlds
- 🧹 Automatically removed when no longer needed
- 🧪 Ideal for testing, games, and sandbox environments
- 🧩 Built on [SnapAPI](https://github.com/sxbide/SnapAPI) for clean, simple integration

---

## 🛠️ Installation

### 1. Add Repositories (build.gradle)

```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }

    // You'll also need to implement following so SnapAPI can use the SWM API.
    maven { url "https://repo.glaremasters.me/repository/concuncan/" }

}

dependencies {
    implementation 'com.github.sxbide:SnapAPI:{VERSION}'

    // You'll also need to implement following so SnapAPI can use the SWM API.
    compileOnly group: "com.grinderwolf", name: "slimeworldmanager-api", version: "2.2.1";
    compileOnly "com.grinderwolf:slimeworldmanager-plugin:2.2.0"
}



```
---
## 💻 Example Usage of **SnapAPI**

1. Create a SnapWorlds instance and initialize in your onEnable();
2. Create a SnapWorld & SnapWorldTemplate instance and configure them.
3. Use the SnapWorldCreator to construct and destruct the world.

## Important things to know:
1. The template of SnapWorldTemplate needs to exist in SWM! (/swm create templateName (file,mongodb)
2. Use the correct loader for your template (Also configure the loader correctly in sources.yml of SWM)
3. SlimeWorldManager needs to be fully setup on your server ([SWM Setup Explanation](https://github.com/cijaaimee/Slime-World-Manager/blob/develop/.docs/usage/install.md))

**Example TestCommand which creates a Testworld based on a file template called 'palermo' and destructs the world after 20 seconds.**

```java
@AllArgsConstructor
public class TestCommand implements CommandExecutor {

    private Main main;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player player) {
            SnapWorld snapWorld = SnapWorld.builder()
                    .worldName("Testworld")
                    .slimeProperties(SlimeWorld.SlimeProperties.builder()
                            .allowMonsters(false)
                            .allowAnimals(false)
                            .spawnX(0)
                            .spawnY(0)
                            .spawnZ(0)
                            .build())
                    .build();

            SnapWorldTemplate snapWorldTemplate = SnapWorldTemplate.as("palermo", SnapWorldLoader.FILE, true);

            if(snapWorld.alreadyExists()) {
                player.sendMessage("The Testworld already exists");
                return false;
            }

            main.snapWorlds().worldCreator().constructWorld(snapWorld, snapWorldTemplate, slimeWorld -> {
                player.teleport(Bukkit.getWorld("Testworld").getSpawnLocation());
                player.sendMessage("Teleported to Testworld.");
            });

            player.sendMessage("Testworld created based on palermo template.");

            Bukkit.getScheduler().runTaskLater(main, () -> {
                main.snapWorlds().worldCreator().destructWorld(snapWorld, snapWorldTemplate);
                Bukkit.broadcastMessage("Testworld was deleted.");
            },20*20L);

        }
        return false;
    }
}
```

