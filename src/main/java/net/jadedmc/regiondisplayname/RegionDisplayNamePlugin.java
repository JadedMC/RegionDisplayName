/*
 * This file is part of RegionDisplayName, licensed under the MIT License.
 *
 *  Copyright (c) JadedMC
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package net.jadedmc.regiondisplayname;

import net.jadedmc.regiondisplayname.commands.RegionDisplayNameCMD;
import net.jadedmc.regiondisplayname.listeners.ReloadListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class is the main class of the plugin.
 * It links all parts together and registers them with the server.
 */
public final class RegionDisplayNamePlugin extends JavaPlugin {
    private HookManager hookManager;
    private ConfigManager configManager;

    /**
     * Runs when the server is started.
     */
    @Override
    public void onEnable() {
        // Plugin startup logic
        configManager = new ConfigManager(this);
        hookManager = new HookManager(this);

        // Register commands
        getCommand("regiondisplayname").setExecutor(new RegionDisplayNameCMD(this));

        // If PlaceholderAPI is installed, enables placeholders
        if(getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders(this).register();
        }
        else {
            // If not, logs a warning and disables the plugin
            getLogger().severe("RegionDisplayName requires PlaceholderAPI to be installed.");
            getServer().getPluginManager().disablePlugin(this);
        }

        // Supports BetterReload if installed.
        if(this.hookManager.useBetterReload()) getServer().getPluginManager().registerEvents(new ReloadListener(this), this);

        // Enables bStats statistics tracking.
        new Metrics(this, 18257);
    }

    /**
     * Gets the current settings manager instance.
     * @return Config Manager.
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Reloads the plugin configuration and updates important values.
     */
    public void reload() {
        this.configManager.reloadConfig();
    }
}