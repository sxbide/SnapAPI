package com.github.sxbi;

import com.github.sxbi.world.SnapWorldCreator;
import com.github.sxbi.snap.SnapWorldLoader;
import com.google.common.collect.Maps;
import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.logging.Logger;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(fluent = true)
public class SnapWorlds {

    public SlimePlugin slimePlugin;
    SlimeLoader slimeLoader;

    SnapWorldCreator worldCreator;

    final Logger logger = Logger.getLogger("SnapWorlds");

    public SnapWorlds() {
        this.slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

        if (slimePlugin == null) {
            this.logger.warning("SlimeWorldManager needs to be on the server as a plugin!");
            return;
        }

        this.worldCreator = new SnapWorldCreator(this);
    }

}
