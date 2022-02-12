package com.timeist;

import com.timeist.discordcommands.RegisterCommand;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordReadyEvent;
import github.scarsz.discordsrv.util.DiscordUtil;

public class DiscordReady {

    @Subscribe
    public void discordConnectEvent(DiscordReadyEvent e) {
        System.out.println("guys am i supposed to pee red");
        TimeistsDecos.getPlugin().getLogger().info("Discord server has " + DiscordUtil.getJda().getUsers().size() + " users.");
        DiscordUtil.getJda().addEventListener(new RegisterCommand());
        TimeistsDecos.getPlugin().getLogger().info("Fuck my asss");
    }
}
