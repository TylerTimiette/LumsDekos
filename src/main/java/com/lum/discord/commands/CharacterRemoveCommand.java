package com.lum.discord.commands;

import com.lum.utilities.PlayerFile;
import com.lum.utilities.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class CharacterRemoveCommand implements CommandExecutor {


    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (Util.checkPlayer(sender)) {
            return true;
        } else if (Util.checkArgs(sender, args, 1, false)) {
            sender.sendMessage("Usage: /delchar (name) where Name is the character's name, and what you use to control them with /talk.");
            return true;
        } else {
            Player p = (Player) sender;
            PlayerFile pf = new PlayerFile(p.getUniqueId());

            if (pf.getConfig().isSet("characters." + args[0])) {
                pf.getConfig().set("characters." + args[0] + ".name", null);
                pf.getConfig().set("characters." + args[0], null);
                sender.sendMessage("Character has been deleted.");
                pf.save();
                return true;
            } else {
                p.sendMessage("That character does not exist in your playerfile!");
            }

            pf.save();
        }
        return true;
    }
}
