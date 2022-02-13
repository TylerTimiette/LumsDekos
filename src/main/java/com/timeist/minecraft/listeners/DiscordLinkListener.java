package com.timeist.minecraft.listeners;

import com.timeist.TimeistsDecos;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.AccountLinkedEvent;
import github.scarsz.discordsrv.util.DiscordUtil;

public class DiscordLinkListener {

    @Subscribe
    public void accountLinked(AccountLinkedEvent e) {
        DiscordUtil.getJda().getTextChannelById(TimeistsDecos.getPlugin().getConfig().getString("link-logging")).sendMessage("Minecraft player " + e.getPlayer().getName() + " (" + e.getPlayer().getUniqueId().toString() + ") linked to " + e.getUser().getAsMention() + " (" + e.getUser().getId() +")").queue();
    }
}
