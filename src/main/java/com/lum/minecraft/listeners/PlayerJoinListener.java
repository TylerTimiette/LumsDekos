package com.lum.minecraft.listeners;

import com.lum.LumsDekos;
import com.lum.utilities.PlayerData;
import com.lum.utilities.PlayerFile;
import com.lum.utilities.Util;
import java.util.UUID;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    public PlayerJoinListener() {
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onJoin(PlayerJoinEvent event) {
        UUID player = event.getPlayer().getUniqueId();
        if (!Util.hasPlayerData(player)) {
            Util.addPlayerData(player);
        }

        PlayerData playerData = Util.getPlayerData(player);
        String nickname = playerData.getNickname().length() > 0 ? ChatColor.translateAlternateColorCodes('&', playerData.getNickname()) : event.getPlayer().getName();
        event.getPlayer().setDisplayName(nickname);
        PlayerFile pf = new PlayerFile(event.getPlayer().getUniqueId());
        event.getPlayer().sendMessage("Current talking mode is set to " + pf.getConfig().getString("talk-mode").toUpperCase());

try {
    if (DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(event.getPlayer().getUniqueId()) != null) {

        if (DiscordUtil.getJda().getGuilds().get(0).getMember(DiscordUtil.getJda().getUserById(DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(event.getPlayer().getUniqueId()))) != null) {
            Member m = DiscordUtil.getJda().getGuilds().get(0).getMember(DiscordUtil.getJda().getUserById(DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(event.getPlayer().getUniqueId())));


            //If the channel is a staff channel, do XYZ to ensure that they actually have access.
            if(pf.getConfig().getString("connectedchannel") != null || !pf.getConfig().getString("connectedchannel").equalsIgnoreCase("&")) {
                if (DiscordUtil.getJda().getTextChannelById(pf.getConfig().getString("connectedchannel")).canTalk(m)) {


                    if (!pf.getConfig().getString("connectedchannel").equalsIgnoreCase("&"))


                        Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(LumsDekos.getPlugin(), new Runnable() {
                            public void run() {
                                event.getPlayer().sendMessage("Currently connected to Discord channel with ID " + pf.getConfig().getString("connectedchannel") + " (" + DiscordUtil.getJda().getTextChannelById(pf.getConfig().getString("connectedchannel")).getName() + ")");
                                event.getPlayer().sendMessage(("Talking mode set to ") + pf.getConfig().getString("talk-mode").toUpperCase());
                                event.getPlayer().sendMessage(("Listening mode set to ") + pf.getConfig().getString("listen-mode").toUpperCase());
                            }
                        }, 3 * 20);


                } else
                    pf.getConfig().set("connectedchannel", "&");
            }
        }
    }
} catch (IllegalArgumentException e) {
    LumsDekos.getPlugin().getLogger().info("Player is not registered on Discord.");
    event.getPlayer().sendMessage("Did you know you can communicate on our Discord? Run /discord link and look at the pins in news-page to find out more!");
}
    }
}
