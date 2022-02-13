package com.timeist.minecraft.commands;

import com.timeist.utilities.PlayerData;
import com.timeist.utilities.Util;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpecialChatCommand implements CommandExecutor {
    public SpecialChatCommand() {
    }

    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!Util.checkPlayer(sender)) {
            Player player = (Player)sender;
            PlayerData playerData = Util.getPlayerData(player.getUniqueId());
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("rainbow") || args[0].equalsIgnoreCase("gay") || args[0].equalsIgnoreCase("lgbt") || args[0].equalsIgnoreCase("pride")) {
                    playerData.setColor("special_rainbow");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Util.rainbowifyText("Gotcha. Text set to rainbowify.")));
                }


                String var7 = args[0];

                switch(var7) {
                    case "transgender":
                    case "transgener":
                    case "transrights":
                    case "#transrights":
                    case "trans":
                    case "transmasc":
                    case "transfem":
                        playerData.setColor("special_trans");
                        player.sendMessage(Util.translateHexColorCodes("#", Util.segmentColoredMessage("Gotcha. Text set to transgender pride. Trans rights!", "trans")));
                        break;


                    case "girllovinggirl":
                    case "gorls":
                    case "lesbian":
                    case "lesbo":
                    case "youbetterbe!":
                    case "glg":
                        playerData.setColor("special_lesbian");
                        player.sendMessage(Util.translateHexColorCodes("#", Util.segmentColoredMessage("Gotcha. Text set to lesbian pride.", "lesbian")));
                        break;

                    case "ace":
                    case "asexual":
                    case "spades":
                    case "hearts":
                    case "clubs":
                    case "diamonds":
                    case "jack":
                    case "king":
                    case "queen":
                        playerData.setColor("special_ace");
                        player.sendMessage(Util.translateHexColorCodes("#", Util.segmentColoredMessage("Gotcha. Text set to asexual pride.", "ace")));
                        break;

                    case "pan":
                    case "pansexual":
                    case "pots":
                    case "pans":
                    case "pan-sexual":
                    case "lilbitofeverything":
                        playerData.setColor("special_pan");
                        player.sendMessage(Util.translateHexColorCodes("#", Util.segmentColoredMessage("Gotcha. Text set to pansexual pride.", "pan")));
                        break;

                    case "bi":
                    case "bye":
                    case "bisexual":
                        playerData.setColor("special_bi");
                        player.sendMessage(Util.translateHexColorCodes("#", Util.segmentColoredMessage("Gotcha. Text set to bisexual pride.", "bi")));
                        break;

                    case "nb":
                    case "nonbinary":
                    case "nobinary":
                    case "anticomputer":
                        playerData.setColor("special_nb");
                        player.sendMessage(Util.translateHexColorCodes("#", Util.segmentColoredMessage("Gotcha. Text set to non-binary pride.", "nb")));
                        break;

                    case "awesome":
                    case "lum":
                    case "tyler":
                    case "dev":
                        playerData.setColor("#F6F89F");
                        sender.sendMessage(Util.translateHexColorCodes("#", "#F6F89FFunny story, the entire rebrand happened because I offered to make these new features in exchange. Your color code has been changed."));
                        break;

                }
                playerData.save();
            } else {
                sender.sendMessage("You have used this command incorrectly. Please specify a special keyword in order to use a special chat color.");
            }
        }

        return true;
    }
}