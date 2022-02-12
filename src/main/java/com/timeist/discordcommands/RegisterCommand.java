package com.timeist.discordcommands;

import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.dependencies.jda.api.entities.ChannelType;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.dependencies.jda.api.events.message.MessageReceivedEvent;
import github.scarsz.discordsrv.dependencies.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Emote;
public class RegisterCommand extends ListenerAdapter {

    @Subscribe @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        Message msg = e.getMessage();
        if(msg.getContentRaw().equalsIgnoreCase("~~alftest")) {
            System.out.println("Valid command found, attempting to reply.");

            e.getChannel().sendMessage("FUCK YOU SHUT UP I HOPE YOU DIE").queue();
        }
    }
}
