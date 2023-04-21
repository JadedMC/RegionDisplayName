package net.jadedmc.regiondisplayname.commands;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import net.jadedmc.regiondisplayname.RegionDisplayName;
import net.jadedmc.regiondisplayname.utils.ChatUtils;
import net.jadedmc.regiondisplayname.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class runs the /regiondisplayname command, which is the main admin command for the plugin.
 * aliases:
 * - /rdn
 */
public class RegionDisplayNameCMD implements CommandExecutor, TabCompleter {
    private final RegionDisplayName plugin;

    /**
     * To be able to access the configuration files, we need to pass an instance of the plugin to our listener.
     * @param plugin Instance of the plugin.
     */
    public RegionDisplayNameCMD(RegionDisplayName plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the command is executed.
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return If the command was successful.
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        // Makes sure the command has an argument.
        if(args.length == 0) {
            args = new String[]{"help"};
        }

        // Get the sub command used.
        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            // Adds a command to the command list.
            case "set":
                // Makes sure the command is being used properly.
                if(args.length < 3) {
                    ChatUtils.chat(sender, "&c&lUsage &8» &c/rdn set [region] [display name]");
                    return true;
                }

                String regionID = args[1];
                String displayName = StringUtils.join(Arrays.copyOfRange(args, 2, args.length), " ");

                plugin.getSettingsManager().getConfig().set("Regions." + regionID, displayName);
                plugin.getSettingsManager().save();
                plugin.getSettingsManager().reload();

                ChatUtils.chat(sender, "&a&lRegionDisplayName &8» &aRegion &f" + regionID + " &adisplay name set to &f" + displayName + "&a.");
                return true;

            // Reloads all plugin configuration files.
            case "reload":
                plugin.getSettingsManager().reload();
                ChatUtils.chat(sender, "&a&lRegionDisplayName &8» &aConfiguration files reloaded successfully!");
                return true;

            // Displays the plugin version.
            case "version":
                ChatUtils.chat(sender, "&a&lRegionDisplayName &8» &aCurrent version: &f" + plugin.getDescription().getVersion());
                return true;

            // Displays the help menu.
            default:
                ChatUtils.chat(sender, "&a&lRegionDisplayName Commands");
                ChatUtils.chat(sender, "&a/rdn set [region] [display name] &8» &fSet a region's display name.");
                ChatUtils.chat(sender, "&a/rdn reload &8» &fReloads the configuration file.");
                ChatUtils.chat(sender, "&a/rdn version< &8» &fDisplays the plugin version.");
                return true;
        }
    }

    /**
     * Processes command tab completion.
     * @param sender Command sender.
     * @param command Command.
     * @param label Command label.
     * @param args Arguments of the command.
     * @return Tab completion.
     */
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // Makes sure the sender is a player.
        // Required to process regions.
        if(!(sender instanceof Player)) {
            return Collections.emptyList();
        }

        Player player = (Player) sender;

        // Suggest sub commands if one hasn't been selected yet.
        if(args.length < 2) {
            return Arrays.asList("help", "reload", "set", "version");
        }

        // Processes sub command tab complete.
        if(args.length == 2) {
            // Get the sub command being used.
            String subCommand = args[0].toLowerCase();

            // Processes tab complete for the set command.
            if(subCommand.equals("set")) {
                LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionManager regions = container.get(localPlayer.getWorld());

                // Return an empty collection if there are no regions.
                if(regions == null) {
                    return Collections.emptyList();
                }

                List<String> regionIds = new ArrayList<>();

                // Region the region ids of all regions in the world.
                for(ProtectedRegion region : regions.getRegions().values()) {
                    regionIds.add(region.getId());
                }

                return regionIds;
            }
        }

        return Collections.emptyList();
    }
}
