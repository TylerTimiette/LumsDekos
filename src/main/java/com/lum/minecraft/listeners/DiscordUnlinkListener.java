package com.lum.minecraft.listeners;

import com.lum.LumsDekos;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.AccountUnlinkedEvent;
import github.scarsz.discordsrv.util.DiscordUtil;

public class DiscordUnlinkListener {


    @Subscribe
    public void accountLinked(AccountUnlinkedEvent e) {
        DiscordUtil.getJda().getTextChannelById(LumsDekos.getPlugin().getConfig().getString("link-logging")).sendMessage("Minecraft player " + e.getPlayer().getName() + " (" + e.getPlayer().getUniqueId().toString() + ") has unlinked their account from " + e.getDiscordUser().getAsMention() + " (" + e.getDiscordUser().getId() +")").queue();
    }
}
