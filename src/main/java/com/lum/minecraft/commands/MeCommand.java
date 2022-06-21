package com.lum.minecraft.commands;

import com.lum.discord.DiscordWebhook;
import com.lum.LumsDekos;
import com.lum.utilities.Util;
import javax.annotation.Nonnull;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.net.URL;

public class MeCommand implements CommandExecutor {
    public MeCommand() {
    }

    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (Util.checkArgs(sender, args, 1, true)) {
            return true;
        } else {

            String message = String.join(" ", args);
            if(!(sender instanceof Player)) {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&0(&d&lCONSOLE&0) &d&l> &r" + message));

            } else {

                message = Util.removeFormatting((Player)sender, message);
                final Player player = (Player)sender;
                final String discordActionMessage = "**(**_ACTION_**)** " + message;
                Bukkit.getScheduler().runTaskAsynchronously(LumsDekos.getPlugin(), new Runnable() {
                    public void run() {


                        try {
                        DiscordWebhook hook = new DiscordWebhook(new URL(LumsDekos.url));
                        hook.setUsername(player.getName());
                        hook.setContent(net.md_5.bungee.api.ChatColor.stripColor(discordActionMessage.replaceAll("&[a-zA-Z0-9]", "").replaceAll("@", "#")));
                        hook.setAvatarUrl("https://mc-heads.net/head/" + player.getUniqueId() + ".png");

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
