package com.github.sxbi;

import com.github.sxbi.world.SnapWorldCreator;
import com.grinderwolf.swm.api.SlimePlugin;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;

import java.util.logging.Logger;

@Getter
@Accessors(fluent = true)
public class SnapWorlds {

    private final SlimePlugin slimePlugin;
    private SnapWorldCreator worldCreator;

    private final Logger logger = Logger.getLogger("SnapWorlds");

    public SnapWorlds() {
        this.slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

        if (slimePlugin == null) {
            this.logger.warning("SlimeWorldManager needs to be on the server as a plugin!");
            return;
        }

        this.worldCreator = new SnapWorldCreator(this);
        this.logger.info("SnapWorlds API successfully initialized!");
    }

}
