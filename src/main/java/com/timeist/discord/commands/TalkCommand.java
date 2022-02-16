package com.timeist.discord.commands;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.timeist.utilities.PlayerFile;
import com.timeist.utilities.Util;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class TalkCommand implements CommandExecutor {

    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (Util.checkPlayer(sender)) {
            return true;
        } else if (Util.checkArgs(sender, args, 1, true)) {
            sender.sendMessage("Usage: /talk (name) (blahblahblah) where Name is the character's name, and what you registered them with. After that, everything else is just text.");
            return true;
        } else {

            String message = String.join(" ", args);

            Player p = (Player) sender;
            PlayerFile pf = new PlayerFile(p.getUniqueId());


            if(pf.getConfig().isSet("characters." + args[0])) {

                WebhookClient client = WebhookClient.withUrl(DiscordUtil.getJda().getTextChannelById(pf.getConfig().getString("connectedchannel")).retrieveWebhooks().complete().get(0).getUrl());
                WebhookMessageBuilder builder = new WebhookMessageBuilder();
                builder.setUsername(pf.getConfig().getString("characters." + args[0] + ".name") + " // Owner: "  + p.getName());
                builder.setContent(message.replace(args[0] + " ", ""));


                if(pf.getConfig().isSet("characters." + args[0] + ".avatar"))
                    builder.setAvatarUrl(pf.getConfig().getString("characters." + args[0] + ".avatar"));
                else
                    builder.setAvatarUrl("https://cdn.discordapp.com/embed/avatars/0.png");


                client.send(builder.build());
            } else {
                p.sendMessage("That character doesn't exist! Are you sure that you typed their name properly?");
            }




        }
        return true;
        }
}
