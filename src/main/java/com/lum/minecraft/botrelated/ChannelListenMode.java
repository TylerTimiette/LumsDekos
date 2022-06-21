package com.lum.minecraft.botrelated;

import com.lum.utilities.PlayerFile;
import com.lum.utilities.Util;
import github.scarsz.discordsrv.DiscordSRV;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class ChannelListenMode implements CommandExecutor {


    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (Util.checkPlayer(sender)) {
            return true;
        } else if (Util.checkArgs(sender, args, 1, false)) {
            return true;
        } else {
            Player player = (Player) sender;
            PlayerFile pf = new PlayerFile(player.getUniqueId());

            if (DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId()) != null) {


                switch (args[0]) {

                    case "bot":
                    case "tupper":
                    case "robot":
                    case "rp":
                    case "rp-only":
                    case "rponly":
                        pf.getConfig().set("listen-mode", "bot-only");
                        player.sendMessage("Listening mode for selected channel set to BOTS ONLY");
                        break;

                    case "user":
                    case "users":
                    case "human":
                    case "person":
                        pf.getConfig().set("listen-mode", "users");
                        player.sendMessage("Listening mode for selected channel set to USERS ONLY");
                        break;

                    case "all":
                    case "ALL":
                    case "everything":

                    default:
                        pf.getConfig().set("listen-mode", "all");
                        player.sendMessage("Listening mode for selected channel set to ALL");
                        break;
                }

                pf.save();
            } else {
                player.sendMessage("You are not linked to a Discord account! Please run /discord link and follow the instructions provided.");
            }
        }
        return true;
    }
}
