package com.lum.discord.commands;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.dependencies.jda.api.events.message.MessageReceivedEvent;
import github.scarsz.discordsrv.dependencies.jda.api.hooks.ListenerAdapter;

public class WhoAmICommand extends ListenerAdapter {

    @Subscribe
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if (e.getMessage().getContentRaw().toString().equalsIgnoreCase("~~whoami")) {

            System.out.println("Whois command found");

            if(DiscordSRV.getPlugin().getAccountLinkManager().getLinkedAccounts().containsKey(e.getAuthor().getId()))
                e.getMessage().reply("Your Minecraft UUID is " + DiscordSRV.getPlugin().getAccountLinkManager().getLinkedAccounts().get(e.getAuthor().getId())).queue();
            else
                e.getMessage().reply("You are not linked to a Minecraft account.").queue();
        }
    }


}
