package net.jadedmc.regiondisplayname.listeners;

import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import net.jadedmc.regiondisplayname.RegionDisplayNamePlugin;
import org.bukkit.event.Listener;

import java.util.Set;

public class WorldGuardListener extends Handler implements Listener {

    public static final Factory factory = new Factory();

    public static class Factory extends Handler.Factory<WorldGuardListener> {
        @Override
        public WorldGuardListener create(Session session) {
            return new WorldGuardListener(session);
        }
    }

    protected WorldGuardListener(Session session) {
        super(session);
    }

    @Override
    public boolean onCrossBoundary(LocalPlayer player, Location from, Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        int lastPriority = 0;
        ProtectedRegion lastRegion = null;
        for(final ProtectedRegion region : entered) {
            if(RegionDisplayNamePlugin.getInstance().getConfigManager().getConfig().isSet("Regions." + region.getId())) {
                if(region.getPriority() >= lastPriority){
                    lastPriority = region.getPriority();
                    lastRegion = region;
                }
            }
        }
        if(lastRegion != null) {
            if (!RegionDisplayNamePlugin.getInstance().getMessageManager().getBlacklist().contains(lastRegion.getId())){
                RegionDisplayNamePlugin.getInstance().getMessageManager().sendMessage(player,lastRegion);
            }
        }
        return true;
    }
}
