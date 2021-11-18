package com.github.mrgeotech;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class SpigotLink extends JavaPlugin {

    private ChannelManager channelManager;

    @Override
    public void onEnable() {
        System.out.println("Enabling...");
        try {
            saveDefaultConfig();
            channelManager = new ChannelManager(this);
            Bukkit.getScheduler().runTaskTimer(this, channelManager, 1, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Enabled!");
    }

    @Override
    public void onDisable() {
        System.out.println("Disabling...");
        channelManager.shutdown();
        Bukkit.getScheduler().cancelTasks(this);
        saveConfig();
        System.out.println("Disabled!");
    }

    public int getPort() {
        return getConfig().getInt("port");
    }

    public String getKey() {
        return getConfig().getString("key");
    }

}
