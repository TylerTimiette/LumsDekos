package com.timeist.listeners;

import com.timeist.TimeistsDecos;
import com.timeist.discordcommands.RegisterCommand;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordReadyEvent;
import github.scarsz.discordsrv.util.DiscordUtil;

public class DiscordReadyListener {

    @Subscribe
    public void discordConnectEvent(DiscordReadyEvent e) {
        TimeistsDecos.getPlugin().getLogger().info("Discord server has " + DiscordUtil.getJda().getUsers().size() + " users.");
        DiscordUtil.getJda().addEventListener(new RegisterCommand());
    }
}
