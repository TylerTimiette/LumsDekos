package com.timeist.minecraftcommands;

import com.timeist.PlayerFile;
import com.timeist.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class CharacterRegisterCommand implements CommandExecutor {

    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if(Util.checkPlayer(sender)) {
            return true;
        } else if (Util.checkArgs(sender, args, 1, false)) {
            sender.sendMessage("Usage: /regchar (name) (avatar) where Name is the character's name -- with no spaces -- and is what you will use to type as them later with /talk, and Avatar is an Imgur link to the desired portrait..");
            return true;
        } else {
            Player p = (Player) sender;
                    PlayerFile pf = new PlayerFile(p.getUniqueId());

                    if(args[1].startsWith("https://imgur.com/a/")) {

                        if (pf.getConfig().isSet(args[0])) {
                            sender.sendMessage("That character has already been registered.");
                            return true;
                        } else {
                           // pf.getConfig().set(args[0], );
                        }
                    } else {
                        sender.sendMessage("Not an imgur link.");
                    }
                }


        return true;
    }

}
