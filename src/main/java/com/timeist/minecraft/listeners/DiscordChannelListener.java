package com.timeist.minecraft.listeners;

import com.timeist.utilities.PlayerFile;
import com.timeist.TimeistsDecos;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.events.message.MessageReceivedEvent;
import github.scarsz.discordsrv.dependencies.jda.api.hooks.ListenerAdapter;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class DiscordChannelListener extends ListenerAdapter {



    @Subscribe
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        Bukkit.getScheduler().runTaskAsynchronously(TimeistsDecos.getInstance(), () -> {
            Bukkit.getOnlinePlayers().forEach((target) -> {

                PlayerFile pf = new PlayerFile(target.getUniqueId());
                if (pf.getConfig().getString("connectedchannel").equalsIgnoreCase(event.getChannel().getId())) {


                    Member m = DiscordUtil.getJda().getGuilds().get(0).getMember(DiscordUtil.getJda().getUserById(DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(target.getUniqueId())));

                    Member m2 = DiscordUtil.getJda().getGuilds().get(0).getMember(event.getAuthor());

                    //If the channel is a staff channel, do XYZ to ensure that they actually have access.
                    if (DiscordUtil.getJda().getTextChannelById(pf.getConfig().getString("connectedchannel")).canTalk(m)) {

                        if (!event.getMessage().getAuthor().getName().equalsIgnoreCase(target.getName() + " // unity.exousia.online")) {


                            if (pf.getConfig().get("listen-mode").equals("bot-only") && event.getAuthor().isBot())
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l" + event.getMessage().getAuthor().getName() + " &7&l>> &f" + event.getMessage().getContentRaw()));

                            if (pf.getConfig().get("listen-mode").equals("all")) {
                                if (event.getAuthor().isBot())
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&l" + event.getMessage().getAuthor().getName() + " &7&l>> &f" + event.getMessage().getContentRaw()));
                                else
                                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&l" + event.getMessage().getAuthor().getName() + " &2&l//&a&l " + m2.getNickname() + "&7&l >> &f" + event.getMessage().getContentRaw()));
                            }

                            if (pf.getConfig().get("listen-mode").equals("users") && !event.getAuthor().isBot())
                                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&l" + event.getMessage().getAuthor().getName() + " &2&l//&a&l " + m2.getNickname() + "&7&l >> &f" + event.getMessage().getContentRaw()));
                        } else
                            System.out.println("Some weird bug.");

                    } else {
                        pf.getConfig().set("connectedchannel", "&");
                        pf.save();
                    }
                }
            });
        });


        }

}
