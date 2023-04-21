package net.jadedmc.regiondisplayname;

import net.jadedmc.regiondisplayname.commands.RegionDisplayNameCMD;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class is the main class of the plugin.
 * It links all parts together and registers them with the server.
 */
public final class RegionDisplayName extends JavaPlugin {
    private SettingsManager settingsManager;

    /**
     * Runs when the server is started.
     */
    @Override
    public void onEnable() {
        // Plugin startup logic
        settingsManager = new SettingsManager(this);

        // Register commands
        getCommand("regiondisplayname").setExecutor(new RegionDisplayNameCMD(this));

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

    /**
     * Gets the current settings manager instance.
     * @return Settings Manager.
     */
    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}