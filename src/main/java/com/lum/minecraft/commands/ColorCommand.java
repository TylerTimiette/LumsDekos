package com.lum.minecraft.commands;

import com.lum.utilities.PlayerData;
import com.lum.utilities.Util;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ColorCommand implements CommandExecutor {
    public ColorCommand() {
    }

    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!Util.checkPlayer(sender)) {
            if (args.length == 1) {
                if (!args[0].startsWith("&") && !args[0].startsWith("#")) {
                    sender.sendMessage("Hello! You have used this command incorrectly. To use /color, please run the command with one of the following options. \n1. A valid chat code (starting with \"&\", ex. &6 for gold.) \n2. A valid hex code (Starting with a # followed by 6 letters (A-F/0-9) or numbers, ex. #ffffff for white.)");
                    return true;
                }

                Player player;
                PlayerData playerData;
                if (args[0].startsWith("#")) {
                    if (args[0].length() == 7 && Util.validateHexCode(args[0])) {
                        player = (Player)sender;
                        playerData = Util.getPlayerData(player.getUniqueId());
                        playerData.setColor(args[0]);
                        playerData.save();
                        sender.sendMessage(Util.translateHexColorCodes("#", "Your chat color has been set to " + args[0] + "this color! #ffffffIsn't it lovely?"));
                    } else {
                        sender.sendMessage("You are only able to use a chat color or hex code. Your hex color cannot be longer than 6 characters and must be a valid code.");
                    }
                }

                if (args[0].startsWith("&")) {
                    if (args[0].length() != 2) {
                        sender.sendMessage("You are using a chat code like &6. You may only have a length of 2 characters when using this type of color.");
                    } else {
                        player = (Player)sender;
                        playerData = Util.getPlayerData(player.getUniqueId());
                        if (args[0].matches(".*[g-zG-Z].*") && !Character.isDigit(args[0].charAt(1))) {
                            sender.sendMessage("Your input of " + args[0] + " is invalid.");
                        } else {
                            playerData.setColor(args[0]);
                            playerData.save();
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "Your chat color has been set to " + args[0] + "this color! &rIsn't it lovely?"));
                        }
                    }
                }
            } else {
                sender.sendMessage("Invalid number of arguments. You need one argument to use this command. ie /chat &6 or /chat #ffffff");
            }
        } else {
            sender.sendMessage("You're not a player. Sorry!");
        }

        return true;
    }
}
