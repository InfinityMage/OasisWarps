package me.elliotbailey.oasiswarps;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class OasisWarps extends JavaPlugin {

    public File warpDataFile;
    private FileConfiguration warpData;

    @Override
    public void onEnable() {
        createWarpData();
        saveDefaultConfig();
        this.getCommand("setwarp").setExecutor(new SetWarpCommand(this));
        this.getCommand("warp").setExecutor(new WarpCommand(this));
        this.getCommand("warps").setExecutor(new ListWarpsCommand(this));
        this.getCommand("delwarp").setExecutor(new DelWarpCommand(this));
        this.getCommand("oasiswarps").setExecutor(new OasisWarpCommand(this));
    }

    public FileConfiguration getWarps() {
        return this.warpData;
    }

    private void createWarpData() {
        warpDataFile = new File(getDataFolder(), "warps.yml");
        if (!warpDataFile.exists()) {
            warpDataFile.getParentFile().mkdirs();
            saveResource("warps.yml", false);
        }

        warpData = new YamlConfiguration();

        try {
            warpData.load(warpDataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

}