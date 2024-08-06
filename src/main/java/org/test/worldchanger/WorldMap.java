package org.test.worldchanger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class WorldMap {
    private World bukkitWorld;
    private File worldFolder;
    private final WorldChanger plugin;

    public WorldMap(WorldChanger plugin) {
        this.plugin = plugin;
    }

    public void setWorld(String worldName, Player player) {
        unload();
        this.worldFolder = plugin.getServer().getWorldContainer();
        this.worldFolder = new File(worldFolder, worldName);
        this.bukkitWorld = Bukkit.createWorld(new WorldCreator(this.worldFolder.getName()));
        player.teleport(this.bukkitWorld.getSpawnLocation());
    }

    public void unload() {
        if (this.bukkitWorld != null) {
            Bukkit.unloadWorld(this.bukkitWorld, true);
            this.bukkitWorld = null;
        }
    }

    public List<String> getWorlds() {
        List<String> worlds = new ArrayList<>();
        try {
            File worldContainer = plugin.getServer().getWorldContainer();
            if (worldContainer == null) {
                plugin.getLogger().log(Level.SEVERE, "World container is null");
                return worlds;
            }

            File[] files = worldContainer.listFiles();
            if (files == null) {
                plugin.getLogger().log(Level.SEVERE, "No files found in world container");
                return worlds;
            }

            for (File file : files) {
                if (file.isDirectory() && new File(file, "level.dat").exists()) {
                    worlds.add(file.getName());
                }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Exception occurred while getting world list", e);
        }

        return worlds;
    }
}

