package com.lum.discord.commands;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.lum.LumsDekos;
import com.lum.utilities.PlayerFile;
import com.lum.utilities.Util;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.Bukkit;
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

            StringBuilder content = new StringBuilder();

            for(int i = 1; i < args.length; i++) {
                content.append(args[i]).append(" ");
            }

            Player p = (Player) sender;
            PlayerFile pf = new PlayerFile(p.getUniqueId());



            if(pf.getConfig().isSet("characters." + args[0])) {



                Member m = DiscordUtil.getJda().getGuilds().get(0).getMember(DiscordUtil.getJda().getUserById(DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(p.getUniqueId())));


                //If the channel is a staff channel, do XYZ to ensure that they actually have access.
                if (DiscordUtil.getJda().getTextChannelById(pf.getConfig().getString("connectedchannel")).canTalk(m)) {


                    Bukkit.getScheduler().runTaskAsynchronously(LumsDekos.getPlugin(), new Runnable() {
                        public void run() {

                            try {

                                WebhookClient client = WebhookClient.withUrl(DiscordUtil.getJda().getTextChannelById(pf.getConfig().getString("connectedchannel")).retrieveWebhooks().complete().get(0).getUrl());
                                WebhookMessageBuilder builder = new WebhookMessageBuilder();
                                builder.setUsername(pf.getConfig().getString("characters." + args[0] + ".name") + " // Owner: " + p.getName());
                                builder.setContent(content.toString().replaceAll("@", ""));


                                if (pf.getConfig().isSet("characters." + args[0] + ".avatar"))
                                    builder.setAvatarUrl(pf.getConfig().getString("characters." + args[0] + ".avatar"));
                                else
                                    builder.setAvatarUrl("https://cdn.discordapp.com/embed/avatars/0.png");


                                client.send(builder.build());
                            } catch(IllegalStateException e) {
                                p.sendMessage("Message cannot be empty!");
                            }
                        }

                        ;
                    });
                } else {
                    p.sendMessage("You can't access this channel! Removing value from config.");
                    pf.getConfig().set("connectedchannel", "&");
                    pf.save();
                }
            } else {
                p.sendMessage("That character doesn't exist! Are you sure that you typed their name properly?");
            }




        }
        return true;
        }
}
