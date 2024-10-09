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

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.jadedmc.regiondisplayname.utils.ChatUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class RegionMessageManager {

    RegionDisplayNamePlugin plugin;

    public RegionMessageManager(RegionDisplayNamePlugin plugin){
        this.plugin = plugin;
    }

    /**
     * Getting blacklist from config
     * @return
     */
    public List<String> getBlacklist(){
        return plugin.getConfigManager().getConfig().getStringList("settings.message.black-list");
    }
    /**
     * Checking if message mode is enabled in config
     * @return
     */
    public boolean isMessageModeEnabled(){
        return plugin.getConfigManager().getConfig().getBoolean("settings.message.enabled");
    }
    /**
     * Getting message type from config
     * @return
     */
    public String getType(){
        return plugin.getConfigManager().getConfig().getString("settings.message.type");
    }

    /**
     * Getting message from config
     * 0 = title, 1 = sub-title
     * @return
     */
    private String[] getMessage(){
        return new String[]{
                plugin.getConfigManager().getConfig().getString("settings.message.title"),
                plugin.getConfigManager().getConfig().getString("settings.message.sub-title"),
        };
    }

    /**
     * Sending message based on message type in config
     */
    public void sendMessage(LocalPlayer localPlayer, ProtectedRegion region){
        Player player = Bukkit.getPlayer(localPlayer.getUniqueId());
        String displayName = "";
        if(plugin.getConfigManager().getConfig().isSet("Regions." + region.getId())) {
            displayName = plugin.getConfigManager().getConfig().getString("Regions." + region.getId());
        }

        if (player == null) return;
        if (isMessageModeEnabled()){
            String mode = getType();
            switch (mode){
                case "actionbar":
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatUtils.translate(getMessage()[0].replaceAll("%display%", ChatUtils.translate(displayName)))));
                    break;
                case "title":
                    player.sendTitle(
                            ChatUtils.translate(getMessage()[0].replaceAll("%display%", ChatUtils.translate(displayName))),
                            ChatUtils.translate(getMessage()[1].replaceAll("%display%", ChatUtils.translate(displayName))),
                            10,
                            70,
                            20
                    );
                    break;
                default:
            }

        }
    }

}
