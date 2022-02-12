package com.timeist.minecraftcommands;

import com.timeist.TimeistsDecos;
import java.net.MalformedURLException;
import java.net.URL;
import javax.annotation.Nonnull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WebhookCommand implements CommandExecutor {
    public WebhookCommand() {
    }

    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length != 1) {
                sender.sendMessage("This command only requires one argument, and it is the Discord webhook URL you would like to change to.");
            } else {
                try {
                    new URL(args[0]);
                } catch (MalformedURLException var6) {
                    sender.sendMessage("This is not a valid URL.");
                    return true;
                }

                if (args[0].contains("discord.com/api/webhooks")) {
                    TimeistsDecos.getInstance().getConfig().set("webhook", args[0]);
                    TimeistsDecos.getInstance().saveConfig();
                    sender.sendMessage("Set webhook address to " + args[0]);
                    TimeistsDecos.getInstance();
                    TimeistsDecos.url = args[0];
                } else {
                    sender.sendMessage("This is not a valid Discord Webhook.");
                }
            }

            return true;
        } else {
            sender.sendMessage("Hello! You are trying to modify the webhook URL for Lum's Dekos. Unfortunately, due to security concerns, only CONSOLE can run this command. Thanks!");
            return true;
        }
    }
}
