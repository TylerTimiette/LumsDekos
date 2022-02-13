package com.timeist.discordcommands;

import com.timeist.TimeistsDecos;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.dependencies.jda.api.events.message.MessageReceivedEvent;
import github.scarsz.discordsrv.dependencies.jda.api.hooks.ListenerAdapter;
import github.scarsz.discordsrv.dependencies.json.simple.JSONArray;
import github.scarsz.discordsrv.dependencies.json.simple.JSONObject;
import github.scarsz.discordsrv.dependencies.json.simple.parser.JSONParser;
import github.scarsz.discordsrv.dependencies.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
