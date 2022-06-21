package com.lum.discord.commands;

import com.lum.utilities.PlayerFile;
import com.lum.utilities.Util;
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
            sender.sendMessage("Usage: /regchar (name) where Name is the character's name -- with no spaces -- and is what you will use to type as them later with /talk.");
            return true;
        } else {
            Player p = (Player) sender;
                    PlayerFile pf = new PlayerFile(p.getUniqueId());



                    if(!(args[0].length() + (" // " + p.getName()).length() >= 80)) {
                        if (pf.getConfig().isSet("characters." + args[0])) {
                            sender.sendMessage("That character has already been registered.");
                            return true;
                        } else {

                            pf.getConfig().set("characters." + args[0] + ".name", args[0]);
                            p.sendMessage("Character added to player file. You may want to add an avatar using /avatar (charactername) -- please be aware that we only accept images that are stored through the Discord CDN and they must be a PNG or JPG.");
                        }

                    } else
                        sender.sendMessage("Character name is too large! When registering a character, your name can be no longer than " + (80 - (" // " + p.getName()).length()) + " characters, as Discord will not accept the request if the total is over 80 characters. Sorry for any inconvenience.");
                    pf.save();
                }


        return true;
    }

}
