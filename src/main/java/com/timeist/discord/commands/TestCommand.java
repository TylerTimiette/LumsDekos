package com.timeist.discord.commands;

import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.dependencies.jda.api.events.message.MessageReceivedEvent;
import github.scarsz.discordsrv.dependencies.jda.api.hooks.ListenerAdapter;

public class TestCommand extends ListenerAdapter {

    @Subscribe
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        Message msg = e.getMessage();

        String[] argsw = msg.getContentRaw().toString().split(" ");
        if (argsw[0].equalsIgnoreCase("~~argstest")) {
            System.out.println("Valid command found, attempting to reply.");

            for (int i = 1; i < argsw.length; i++) {
                if (!argsw[i].equalsIgnoreCase("")) {
                    e.getChannel().sendMessage("arg \"" + argsw[i].replaceAll(" ", "") + "\" found").queue();
                }

            }
        }
    }
}
