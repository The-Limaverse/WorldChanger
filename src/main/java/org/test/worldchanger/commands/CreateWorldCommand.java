package org.test.worldchanger.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.test.worldchanger.WorldChanger;
import org.test.worldchanger.WorldMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CreateWorldCommand implements CommandExecutor, TabCompleter {

    private final WorldChanger plugin;
    private final WorldMap map;

    public CreateWorldCommand(WorldChanger plugin, WorldMap map) {
        this.plugin = plugin;
        this.map = map;
        plugin.getCommand("createworld").setExecutor(this);
        plugin.getCommand("createworld").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("Usage: /createworld <world_name>");
            return true;
        }

        if (map.getWorlds().size() >= 100) {
            sender.sendMessage(ChatColor.RED + "Error: There are to many worlds in this server.");
            return false;
        }

        String newWorldName = args[0];
        File dataFolder = plugin.getDataFolder();
        File templateFolder = new File(dataFolder, "void_template");
        File newWorldFolder = new File(Bukkit.getWorldContainer(), newWorldName);

        if (!templateFolder.exists() || !templateFolder.isDirectory()) {
            sender.sendMessage("The void_template world is missing or invalid in the plugin's data folder.");
            return true;
        }

        if (newWorldFolder.exists()) {
            sender.sendMessage("A world with that name already exists.");
            return true;
        }

        try {
            copyFolder(templateFolder, newWorldFolder);
            WorldCreator creator = new WorldCreator(newWorldName);
            World newWorld = creator.createWorld();

            if (newWorld != null) {
                sender.sendMessage("World '" + newWorldName + "' created successfully!");
            } else {
                sender.sendMessage("Failed to load the world after copying. Please check the server logs.");
            }
        } catch (IOException e) {
            sender.sendMessage("An error occurred while creating the world: " + e.getMessage());
            e.printStackTrace();
        }

        return true;
    }

    private void copyFolder(File source, File target) throws IOException {
        if (!target.exists()) {
            target.mkdirs();
        }

        for (File file : source.listFiles()) {
            File destFile = new File(target, file.getName());
            if (file.isDirectory()) {
                copyFolder(file, destFile);
            } else {
                Files.copy(file.toPath(), destFile.toPath());
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        suggestions.add("<name>");
        return suggestions;
    }
}
