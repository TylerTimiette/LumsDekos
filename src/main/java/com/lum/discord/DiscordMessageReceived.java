package com.lum.discord;

import com.lum.utilities.Util;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePostProcessEvent;

public class DiscordMessageReceived {


    @Subscribe
    public void discordMessageReceivedEvent(DiscordGuildMessagePostProcessEvent e) {
        StringBuilder sb = new StringBuilder();

        for(String word : e.getProcessedMessage().split("\\s+")) {
            sb.append(Util.replaceValidURL(word));
        }

        e.setProcessedMessage(sb.toString());
    }
}
