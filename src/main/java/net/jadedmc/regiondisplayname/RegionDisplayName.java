package net.jadedmc.regiondisplayname;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RegionDisplayName extends JavaPlugin {
    private SettingsManager settingsManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        settingsManager = new SettingsManager(this);

        // If PlaceholderAPI is installed, enables placeholders
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders(this).register();
        }
        else {
            // If not, logs a warning and disables the plugin
            Bukkit.getLogger().severe("RegionDisplayName requires PlaceholderAPI to be installed.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}