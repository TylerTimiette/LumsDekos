package com.timeist.minecraft.listeners;

import com.timeist.utilities.PlayerData;
import com.timeist.utilities.PlayerFile;
import com.timeist.utilities.Util;
import java.util.UUID;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.util.DiscordUtil;
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

        Member m = DiscordUtil.getJda().getGuilds().get(0).getMember(DiscordUtil.getJda().getUserById(DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(event.getPlayer().getUniqueId())));


        //If the channel is a staff channel, do XYZ to ensure that they actually have access.
        if (DiscordUtil.getJda().getTextChannelById(pf.getConfig().getString("connectedchannel")).canTalk(m)) {


            if (!pf.getConfig().getString("connectedchannel").equalsIgnoreCase("&"))
                event.getPlayer().sendMessage("Currently connected to Discord channel with ID " + pf.getConfig().getString("connectedchannel") + " (" + DiscordUtil.getJda().getTextChannelById(pf.getConfig().getString("connectedchannel")).getName() + ")");
        } else
            pf.getConfig().set("connectedchannel", "&");
    }
}
