package com.timeist.minecraft.commands;

import com.timeist.discord.DiscordWebhook;
import com.timeist.TimeistsDecos;
import com.timeist.utilities.Util;
import javax.annotation.Nonnull;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MeCommand implements CommandExecutor {
    public MeCommand() {
    }

    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (Util.checkArgs(sender, args, 1, true)) {
            return true;
        } else {
            String message = String.join(" ", args);
            message = Util.removeFormatting((Player)sender, message);
            if(!(sender instanceof Player)) {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&0(&d&lCONSOLE&0) &d&l> &r" + message));

            } else {
                final Player player = (Player)sender;
                final String discordActionMessage = "**(**_ACTION_**)** " + message;
                Bukkit.getScheduler().runTaskAsynchronously(TimeistsDecos.getPlugin(), new Runnable() {
                    public void run() {
                        DiscordWebhook hook = new DiscordWebhook();
                        hook.setUsername(player.getName());
                        hook.setContent(net.md_5.bungee.api.ChatColor.stripColor(discordActionMessage.replaceAll("&[a-zA-Z0-9]", "").replaceAll("@", "#")));
                        hook.setAvatarUrl("https://mc-heads.net/head/" + player.getUniqueId() + ".png");

                        try {
                            hook.execute();
                        } catch (Exception var3) {
                            var3.printStackTrace();
                            System.out.println("You borked it!");
                        }

                    }
                });
                message = ChatColor.translateAlternateColorCodes('&', "&d&l(&5ACTION&d&l) " + player.getDisplayName() + "&0: &d" + message);
                Util.sendChatMessage(player.getUniqueId(), new BaseComponent[]{new TextComponent(this.color(Util.translateHexColorCodes("#", message)))});
                return true;
            }
        }
        return true;
    }

    private BaseComponent[] color(String text) {
        return TextComponent.fromLegacyText(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', text));
    }
}
