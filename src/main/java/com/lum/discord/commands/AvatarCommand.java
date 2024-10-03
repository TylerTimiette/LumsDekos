package com.lum.discord.commands;

import com.lum.utilities.PlayerFile;
import com.lum.utilities.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class AvatarCommand implements CommandExecutor {


    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (Util.checkPlayer(sender)) {
            return true;
        } else if (Util.checkArgs(sender, args, 2, false)) {
            sender.sendMessage("Usage: /avatar (name) (link) where Name is the character's name -- with no spaces -- and is what you use to type as them with /talk. \nLink is the Avatar URL -- please note that it must be an image link that is stored ON Discord. Post it in the bot channel, right click, and copy the link to it.");
            return true;
        } else {
            Player p = (Player) sender;
            PlayerFile pf = new PlayerFile(p.getUniqueId());

            if (pf.getConfig().isSet("characters." + args[0])) {
                if(args[1].startsWith("https://cdn.discordapp.com/attachments/")) {
                    try {
                        Image img = ImageIO.read(new URL(args[1]));

                        if (img != null) {
                            pf.getConfig().set("characters." + args[0] + ".avatar", args[1]);
                            p.sendMessage("Avatar set to " + args[1]);
                        } else {
                            p.sendMessage("This is not a valid avatar. Must be a PNG or JPG");
                            sender.sendMessage("Usage: /avatar (name) (link) where Name is the character's name -- with no spaces -- and is what you use to type as them with /talk. \nLink is the Avatar URL -- please note that it must be an image link that is stored ON Discord. Post it in the bot channel, right click, and copy the link to it.");
                        }



                    } catch(IOException e) {
                        p.sendMessage("This does not lead to a recognizable image.");
                    }
                } else {
                    p.sendMessage("This is not a valid Discord attachment.");
                    sender.sendMessage("Usage: /avatar (name) (link) where Name is the character's name -- with no spaces -- and is what you use to type as them with /talk. \nLink is the Avatar URL -- please note that it must be an image link that is stored ON Discord. Post it in the bot channel, right click, and copy the link to it.");
                }


            } else {
                p.sendMessage("That character does not exist in your playerfile!");
            }
            pf.save();

        }

        return true;
    }
}
