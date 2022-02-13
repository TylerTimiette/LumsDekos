package com.timeist.minecraft.botrelated;

import com.timeist.utilities.PlayerFile;
import com.timeist.TimeistsDecos;
import com.timeist.utilities.Util;
import github.scarsz.discordsrv.DiscordSRV;
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
                return true;
            } else {

                Player player = (Player) sender;

                //If the channel specified exists according to the bot


                if(DiscordSRV.getPlugin().getAccountLinkManager().getLinkedAccounts().containsValue(player.getUniqueId())) {
                    if (DiscordUtil.getJda().getTextChannels().contains(args[0])) {
                        if (!TimeistsDecos.getPlugin().getConfig().getString("webhook").equalsIgnoreCase(args[0])) {


                            //If the channel is a staff channel, do XYZ to ensure that they actually have access.
                            if (DiscordUtil.getJda().getTextChannelById(args[0]).getParent().getName().contains("STAFF")) {
                                if (DiscordUtil.getJda().getTextChannelById(args[0]).getMembers().contains(DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId()))) {
                                    //This is a security measure. If someone's not where they're supposed to be, staff can tell.
                                    onSuccess(player, DiscordUtil.getJda().getTextChannelById(args[0]));
                                    return true;
                                } else {
                                    onFailure(player, "You are not staff!");
                                    return true;
                                }
                            }

                        } else {
                            onFailure(player, "You are already connected to this channel, as it is game-chat.");
                            return true;
                        }


                        onSuccess(player, DiscordUtil.getJda().getTextChannelById(args[0]));
                    } else
                        onFailure(player, "Channel does not exist!");
                } else
                    onFailure(player, "You have not run /discord link!");
            }
        return true;
    }



    public void onSuccess(Player player, TextChannel textChannel) {
        player.sendMessage("Now listening to channel: " + textChannel.getName() + " (ID " + textChannel.getId() + ")");


        DiscordUtil.getTextChannelById(textChannel.getId()).sendMessage("Player " + player.getName() + " (" + player.getUniqueId().toString() + ") has connected to the channel!");
        PlayerFile pf = new PlayerFile(player.getUniqueId());
        pf.getConfig().set("connectedchannel", textChannel.getId());
        pf.save();

    }


    public void onFailure(Player player, String reason) {
        player.sendMessage("Failed to listen to channel with specified ID. \nReason: " + reason);
    }
}

