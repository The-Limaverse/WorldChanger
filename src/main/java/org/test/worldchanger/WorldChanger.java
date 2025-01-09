package org.test.worldchanger;

import org.bukkit.plugin.java.JavaPlugin;
import org.test.worldchanger.commands.CreateWorldCommand;
import org.test.worldchanger.commands.SwitchWorldCommand;

import java.io.File;
import java.util.logging.Level;

public final class WorldChanger extends JavaPlugin {

    private WorldMap map;

    @Override
    public void onEnable() {
        // Plugin startup logic
        map = new WorldMap(this);
        createVoidTemplateFolder();

        new SwitchWorldCommand(this, map);
        new CreateWorldCommand(this, map);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Creates a folder in the plugin's data folder named "void_template".
     * If the folder is empty, logs a message instructing the user to add a file.
     */
    private void createVoidTemplateFolder() {
        File dataFolder = getDataFolder(); // Plugin's data folder
        File voidTemplateFolder = new File(dataFolder, "void_template");

        // Ensure the plugin's data folder exists
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            getLogger().log(Level.SEVERE, "Failed to create the plugin's data folder!");
            return;
        }

        // Ensure the "void_template" folder exists
        if (!voidTemplateFolder.exists()) {
            if (voidTemplateFolder.mkdirs()) {
                getLogger().log(Level.INFO, "Created 'void_template' folder. Please add your void world template file(s) here.");
            } else {
                getLogger().log(Level.SEVERE, "Failed to create 'void_template' folder!");
            }
        } else {
            // Check if the folder is empty
            File[] files = voidTemplateFolder.listFiles();
            if (files == null || files.length == 0) {
                getLogger().log(Level.WARNING, "'void_template' folder is empty. Please add your void world template file(s) here.");
            } else {
                getLogger().log(Level.INFO, "'void_template' folder is present and contains files.");
            }
        }
    }
}
