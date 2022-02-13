package com.timeist.minecraft.listeners;

import com.timeist.utilities.Util;
import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    public PlayerQuitListener() {
    }

    @EventHandler
    public void onJoin(PlayerQuitEvent event) {
        UUID player = event.getPlayer().getUniqueId();
        if (Util.hasPlayerData(player)) {
            Util.getPlayerData(player).save();
            Util.removePlayerData(player);
        }

    }
}
