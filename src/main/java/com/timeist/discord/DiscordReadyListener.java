package com.timeist.discord;

import com.timeist.TimeistsDecos;
import com.timeist.discord.commands.TestCommand;
import com.timeist.discord.commands.WhoAmICommand;
import com.timeist.minecraft.listeners.DiscordChannelListener;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordReadyEvent;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;

public class DiscordReadyListener {

    @Subscribe
    public void discordConnectEvent(DiscordReadyEvent e) {
        TimeistsDecos.getPlugin().getLogger().info("Discord server has " + DiscordUtil.getJda().getUsers().size() + " users.");
        DiscordUtil.getJda().addEventListener(new TestCommand());
        DiscordUtil.getJda().addEventListener(new WhoAmICommand());
        DiscordUtil.getJda().addEventListener(new DiscordChannelListener());
        TimeistsDecos.getPlugin().getLogger().info("Creating webhooks for channels that do not already have them.");


        for(TextChannel channel : DiscordUtil.getJda().getTextChannels()) {
            if(channel.retrieveWebhooks().complete().size() < 1) {
                channel.createWebhook("JDA Default").queue();
                TimeistsDecos.getPlugin().getLogger().info("Applicable webhook not found for channel " + channel.getName() + " (" + channel.getId() + ") \nPlease check after server is loaded for hook 'JDA DEFAULT'");
            }
        }



    }
}
