package com.timeist.discord.commands;

import com.timeist.utilities.PlayerFile;
import com.timeist.utilities.Util;
import github.scarsz.discordsrv.DiscordSRV;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class TalkModeCommand implements CommandExecutor {

    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (Util.checkPlayer(sender)) {
            return true;
        } else if (Util.checkArgs(sender, args, 1, false)) {
            sender.sendMessage("Usage: /tmode (discord|minecraft|both)");
            return true;
        } else {

            Player p = (Player) sender;
            PlayerFile pf = new PlayerFile(p.getUniqueId());

            if(DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(p.getUniqueId()) != null) {


                switch (args[0]) {
                    case "discord":
                    case "disc":
                    case "urp":

                        if (pf.getConfig().isSet("connectedchannel")) {
                            pf.getConfig().set("talk-mode", "discord");
                            p.sendMessage("Talking mode set to DISCORD");
                            break;
                        } else {
                            p.sendMessage("You aren't connected to a channel! Run /channel to connect!");
                            break;
                        }


                    case "both":
                    case "all":
                        if (pf.getConfig().isSet("connectedchannel")) {
                            pf.getConfig().set("talk-mode", "both");
                            p.sendMessage("Talking mode set to BOTH");
                        } else {
                            p.sendMessage("You aren't connected to a channel! Run /channel to connect!");
                            p.sendMessage("Talking mode set to MINECRAFT!");
                            pf.getConfig().set("talk-mode", "mc");
                        }
                        break;


                    case "minecraft":
                    case "mc":
                    case "server":
                    default:
                        p.sendMessage("Talking mode set to MINECRAFT");
                        pf.getConfig().set("talk-mode", "mc");
                        break;
                }
            } else
                p.sendMessage("You are not registered with DiscordSRV! Please run /discord link to use this command!");
            pf.save();
        }
        return true;
    }
}
