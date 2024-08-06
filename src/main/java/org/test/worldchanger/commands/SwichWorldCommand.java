package org.test.worldchanger.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.test.worldchanger.WorldChanger;
import org.test.worldchanger.WorldMap;

import java.util.List;
import java.util.logging.Level;

public class SwichWorldCommand implements CommandExecutor, TabCompleter {
    private final WorldChanger plugin;
    private final WorldMap map;

    public SwichWorldCommand(WorldChanger plugin, WorldMap map) {
        this.plugin = plugin;
        this.map = map;
        plugin.getCommand("switchworld").setExecutor(this);
        plugin.getCommand("switchworld").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            String worldName = args[0];
            map.setWorld(worldName, (Player) sender);
        } else if (args.length == 2) {
            String worldName = args[0];
            String playerName = args[1];
            Player player = sender.getServer().getPlayer(playerName);
            if (player != null) {
                map.setWorld(worldName, player);
            } else {
                sender.sendMessage("Player not found");
            }
        } else {
            sender.sendMessage("Usage: /switchworld [player] <world>");
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        try {
            if (args.length == 1) {
                // Suggest world names for the first and second argument
                List<String> worlds = map.getWorlds();
                if (worlds.isEmpty()) {
                    plugin.getLogger().log(Level.WARNING, "No worlds found for tab completion");
                    return null;
                }
                return worlds;
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Exception during tab completion: ", e);
        }
        return null;
    }
}
