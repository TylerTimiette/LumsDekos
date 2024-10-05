package com.lum.minecraft.commands;

import com.lum.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;


public class SizeCommand implements CommandExecutor {

    public SizeCommand() {
    }

    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {

        //The user may be trying to change the size of someone else, so we need to take that into account.
        if(args.length >= 1) {
            if (args.length <= 2) {
                if (args.length == 2) {
                    if (sender.hasPermission("lums.size.others")) {
                        if (Bukkit.getServer().getPlayerExact(args[0]) != null) {
                            try {
                                if (Double.parseDouble(args[1]) >= 0.0625 && Double.parseDouble(args[1]) <= 4) {
                                    changePlayerSize(Bukkit.getPlayerExact(args[0]), Double.parseDouble(args[1]));
                                    Util.sendMessage(sender, "Changing the size of " + args[0] + " to " + args[1]);
                                } else {
                                    displayUsage(sender);
                                }
                            } catch (NumberFormatException e) {
                                displayUsage(sender);
                            }
                        } else
                            displayUsage(sender);
                    } else
                        displayUsage(sender);
                }
                if(args.length == 1) {
                    if (sender.hasPermission("lums.size.self")) {
                        if (!Util.checkPlayer(sender)) {
                            Player player = (Player) sender;
                            try {
                                if (Double.parseDouble(args[0]) >= 0.0625 && Double.parseDouble(args[0]) <= 4) {
                                    changePlayerSize(player, Double.parseDouble(args[0]));
                                } else
                                    displayUsage(sender);
                            } catch (NumberFormatException e) {
                                displayUsage(sender);
                            }
                            //Console can't change its own size!
                        } else
                            Util.sendMessage(sender, "As console, you may only change another player's size. You may do this with /scale (playername) (num from 0.0625 -> 4).");
                    } else
                        displayUsage(sender);
                }
            } else
                displayUsage(sender);

        } else {
            displayUsage(sender);
        }
        return true;
    }

    public void displayUsage(CommandSender sender) {
        //staff msg
        if(sender.hasPermission("lums.size.self")) {
            if (sender.hasPermission("lums.size.others")) {
                Util.sendMessage(sender, "You have used an invalid number of arguments. This command allows you to change the size of others and yourself.");
                Util.sendMessage(sender, "To change your size, use /scale (number ranging from 0.0625 -> 4).");
                Util.sendMessage(sender, "To change another player's size, use /scale (playername) (num from 0.0625 -> 4).");
            } else //Display player message
                Util.sendMessage(sender, "You have used an invalid number of arguments. This command only allows you to use /scale (num from 0.0625 -> 4).");
        } else
            Util.sendMessage(sender, "No permission! Missing lums.size.self.");
    }


    public void changePlayerSize(Player player, double size) {
        player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(size);
        Util.sendMessage(player, "Your size has been adjusted to " + size);
    }
}