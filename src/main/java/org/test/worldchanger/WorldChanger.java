package org.test.worldchanger;

import org.bukkit.plugin.java.JavaPlugin;
import org.test.worldchanger.commands.SwichWorldCommand;

public final class WorldChanger extends JavaPlugin {

    private WorldMap map;

    @Override
    public void onEnable() {
        // Plugin startup logic
        map = new WorldMap(this);
        new SwichWorldCommand(this, map);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
