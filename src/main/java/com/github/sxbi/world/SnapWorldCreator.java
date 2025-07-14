package com.github.sxbi.world;

import com.github.sxbi.SnapWorlds;
import com.github.sxbi.snap.SnapWorldLoader;
import com.github.sxbi.snap.SnapWorldTemplate;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.plugin.SWMPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public final class SnapWorldCreator {

    private final SnapWorlds snapWorlds;

    public SnapWorldCreator(SnapWorlds snapWorlds) {
        this.snapWorlds = snapWorlds;
    }

    public void constructWorld(SnapWorld snapWorld, SnapWorldTemplate snapWorldTemplate) {

        if (snapWorld.alreadyExists()) {
            this.snapWorlds.logger().warning("A Snapworld with the name " + snapWorld.worldName() + " already exists.");
            return;
        }

        AtomicReference<SlimeLoader> slimeLoader = new AtomicReference<>();

        if (snapWorldTemplate.snapWorldLoader().equals(SnapWorldLoader.MONGODB)) {
            slimeLoader.set(this.snapWorlds.slimePlugin().getLoader("mongodb"));
        }
        if (snapWorldTemplate.snapWorldLoader().equals(SnapWorldLoader.FILE)) {
            slimeLoader.set(this.snapWorlds.slimePlugin().getLoader("file"));
        }

        if (slimeLoader.get() == null) {
            this.snapWorlds.logger().warning(snapWorldTemplate.snapWorldLoader().name() + "is not configured correctly!");
            return;
        }

        CompletableFuture.supplyAsync(() -> {

            SlimeWorld slimeWorld;
            World originalWorld = Bukkit.getWorld(snapWorldTemplate.templateName());

            if (originalWorld == null) {
                try {

                    slimeWorld = this.snapWorlds.slimePlugin().loadWorld(
                            slimeLoader.get(),
                            snapWorldTemplate.templateName(),
                            snapWorld.slimeProperties());

                } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException |
                         WorldInUseException e) {
                    this.snapWorlds.logger().warning("Snapworld could not be loaded, make sure you have the template world created and you use the correct loader.");
                    //throw new RuntimeException(e);
                    return null;
                }
            } else {
                slimeWorld = SWMPlugin.getInstance().getNms().getSlimeWorld(originalWorld);
            }
            return slimeWorld;
        }).thenAccept(slimeWorld -> {
            if (slimeWorld == null) return;

            SlimeWorld clonedWorld = slimeWorld.clone(snapWorld.worldName());
            this.snapWorlds.slimePlugin().generateWorld(clonedWorld);
        });
    }

    public void constructWorld(SnapWorld snapWorld, SnapWorldTemplate snapWorldTemplate, Consumer<SlimeWorld> runAfter) {

        if (snapWorld.alreadyExists()) {
            this.snapWorlds.logger().warning("A Snapworld with the name " + snapWorld.worldName() + " already exists.");
            return;
        }

        AtomicReference<SlimeLoader> slimeLoader = new AtomicReference<>();

        if (snapWorldTemplate.snapWorldLoader().equals(SnapWorldLoader.MONGODB)) {
            slimeLoader.set(this.snapWorlds.slimePlugin().getLoader("mongodb"));
        }
        if (snapWorldTemplate.snapWorldLoader().equals(SnapWorldLoader.FILE)) {
            slimeLoader.set(this.snapWorlds.slimePlugin().getLoader("file"));
        }

        if (slimeLoader.get() == null) {
            this.snapWorlds.logger().warning(snapWorldTemplate.snapWorldLoader().name() + "is not configured correctly!");
            return;
        }

        CompletableFuture.supplyAsync(() -> {

            SlimeWorld slimeWorld;
            World originalWorld = Bukkit.getWorld(snapWorldTemplate.templateName());

            if (originalWorld == null) {
                try {

                    slimeWorld = this.snapWorlds.slimePlugin().loadWorld(
                            slimeLoader.get(),
                            snapWorldTemplate.templateName(),
                            snapWorld.slimeProperties());

                } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException |
                         WorldInUseException e) {
                    throw new RuntimeException(e);
                }
            } else {
                slimeWorld = SWMPlugin.getInstance().getNms().getSlimeWorld(originalWorld);
            }
            return slimeWorld;
        }).thenAccept(slimeWorld -> {
            SlimeWorld clonedWorld = slimeWorld.clone(snapWorld.worldName());
            this.snapWorlds.slimePlugin().generateWorld(clonedWorld);

            runAfter.accept(slimeWorld);
        });
    }

    public boolean destructWorld(SnapWorld snapWorld, SnapWorldTemplate snapWorldTemplate) {
        String worldName = snapWorld.worldName();
        World bukkitWorld = Bukkit.getWorld(worldName);

        if (bukkitWorld == null) {
            this.snapWorlds.logger().warning("Snapworld could not be destructed because it doesnt exist.");
            return false;
        }

        bukkitWorld.getPlayers().forEach(player -> player.teleport(Bukkit.getWorlds().getFirst().getSpawnLocation()));
        return Bukkit.unloadWorld(worldName, snapWorldTemplate.save());
    }

    public boolean destructWorld(String worldName, boolean save) {
        World bukkitWorld = Bukkit.getWorld(worldName);

        if (bukkitWorld == null) {
            this.snapWorlds.logger().warning("Snapworld could not be destructed because it doesnt exist.");
            return false;
        }

        bukkitWorld.getPlayers().forEach(player -> player.teleport(Bukkit.getWorlds().getFirst().getSpawnLocation()));
        return Bukkit.unloadWorld(worldName, save);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SnapWorldCreator) obj;
        return Objects.equals(this.snapWorlds, that.snapWorlds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(snapWorlds);
    }

    @Override
    public String toString() {
        return "SnapWorldCreator[" +
                "snapWorlds=" + snapWorlds + ']';
    }

}
