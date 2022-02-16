package com.timeist.minecraft.botrelated;

import com.timeist.utilities.PlayerFile;
import com.timeist.TimeistsDecos;
import com.timeist.utilities.Util;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class ChannelListenCommand implements CommandExecutor {


    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
            if (Util.checkPlayer(sender)) {
                return true;
            } else if (Util.checkArgs(sender, args, 1, false)) {
                Player player = (Player) sender;

                PlayerFile pf = new PlayerFile(player.getUniqueId());
                pf.getConfig().set("connectedchannel", "&");
                return true;
            } else {

                Player player = (Player) sender;

                //If the channel specified exists according to the bot


                if(DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId()) != null) {
                    if (DiscordUtil.getJda().getTextChannelById(args[0]) != null) {
                        if(!DiscordUtil.getJda().getTextChannelById(args[0]).retrieveWebhooks().complete().get(0).getUrl().equalsIgnoreCase(TimeistsDecos.getPlugin().getConfig().getString("webhook"))) {


                            Member m = DiscordUtil.getJda().getGuilds().get(0).getMember(DiscordUtil.getJda().getUserById(DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId())));


                            //If the channel is a staff channel, do XYZ to ensure that they actually have access.
                            if (DiscordUtil.getJda().getTextChannelById(args[0]).canTalk(m)) {



                                    //This is a security measure. If someone's not where they're supposed to be, staff can tell.
                                    onSuccess(player, DiscordUtil.getJda().getTextChannelById(args[0]));
                                    return true;

                            } else {
                            onFailure(player, "You are not able to access this channel!");
                            return true;
                        }

                        } else {
                            onFailure(player, "You are already connected to this channel, as it is game-chat.");
                            return true;
                        }

                    } else
                        onFailure(player, "Channel does not exist! If you believe this is in error, right click the channel in question and copy its ID; the plugin does not check for channel names and uses channel IDs as the arg.\n\nAdditionally, this plugin does not support threads.");
                } else
                    onFailure(player, "You have not run /discord link!");
            }
        return true;
    }



    public void onSuccess(Player player, TextChannel textChannel) {
        player.sendMessage("Now listening to channel: " + textChannel.getName() + " (ID " + textChannel.getId() + ")");

        System.out.println(textChannel.retrieveWebhooks().complete().get(0).getUrl());

        DiscordUtil.getTextChannelById(textChannel.getId()).sendMessage("Player " + player.getName() + " (" + player.getUniqueId().toString() + ") has connected to the channel!").queue();
        PlayerFile pf = new PlayerFile(player.getUniqueId());
        pf.getConfig().set("connectedchannel", textChannel.getId());
        pf.save();

    }


    public void onFailure(Player player, String reason) {
        player.sendMessage("Failed to listen to channel with specified ID. \nReason: " + reason);
    }
}

