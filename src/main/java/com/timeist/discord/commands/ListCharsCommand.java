package com.timeist.discord.commands;

import com.timeist.utilities.PlayerFile;
import com.timeist.utilities.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

//This class is poorly named. It's for a player to see what is in their personal playerfile.
public class ListCharsCommand implements CommandExecutor {


    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (Util.checkPlayer(sender)) {
            return true;
        } else if (Util.checkArgs(sender, args, 0, false)) {
            sender.sendMessage("Usage: /dumpconfig");
            return true;
        } else {
            Player p = (Player) sender;

            PlayerFile pf = new PlayerFile(p.getUniqueId());


            p.sendMessage("Here are your characters according to your playerfile:");
            for(String idfk : pf.getConfig().getConfigurationSection("characters").getKeys(false)) {
                p.sendMessage(idfk);
            }

            if(pf.getConfig().getConfigurationSection("characters").getKeys(false).size() == 0)
                p.sendMessage("You have no characters registered!");
        }
        return true;
    }
}
