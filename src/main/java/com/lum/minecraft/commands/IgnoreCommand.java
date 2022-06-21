package com.lum.minecraft.commands;

import com.lum.utilities.PlayerData;
import com.lum.LumsDekos;
import com.lum.utilities.Util;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IgnoreCommand implements CommandExecutor {
    public IgnoreCommand() {
    }

    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (Util.checkPlayer(sender)) {
            return true;
        } else {
            Player player = (Player)sender;
            PlayerData playerData = Util.getPlayerData(player.getUniqueId());
            List<String> ignoredPlayers = playerData.getIgnoredPlayers();
            if (args.length == 0) {
                if (ignoredPlayers.isEmpty()) {
                    Util.sendMessage(sender, "You have not ignored any players yet. You must have the patience of an angel.");
                    return true;
                } else {
                    Bukkit.getScheduler().runTaskAsynchronously(LumsDekos.getInstance(), () -> {
                        Util.sendMessage(sender, "&eYou're currently ignoring &b" + ignoredPlayers.size() + " &eplayers.");
                        ignoredPlayers.forEach((uuid) -> {
                            String targetName = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
                            TextComponent component = new TextComponent("&3- &b" + targetName);
                            component.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, (new ComponentBuilder("Click to un-ignore this player.")).color(ChatColor.AQUA).create()));
                            component.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/ignore " + targetName));
                            sender.sendMessage(component);
                            System.out.println(String.join(", ", ignoredPlayers));
                        });
                    });
                    return true;
                }
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    Util.sendMessage(sender, "&cThe player &f" + args[0] + " &ccould not be found.");
                    return true;
                } else if (target.hasPermission("chat.unignorable")) {
                    Util.sendMessage(sender, "&cThis player can not be ignored.");
                    return true;
                } else if (player.getUniqueId().equals(target.getUniqueId())) {
                    Util.sendMessage(sender, "&cWhy are you ignoring yourself? Are you okay? Do you need a hug? Have some hearts.");
                    player.getWorld().spawnParticle(Particle.HEART, player.getLocation(), 100, 1.0D, 1.0D, 1.0D, 0.0D);
                    return true;
                } else {
                    String targetUUID = target.getUniqueId().toString();
                    if (ignoredPlayers.contains(targetUUID)) {
                        ignoredPlayers.remove(targetUUID);
                    } else {
                        ignoredPlayers.add(targetUUID);
                    }

                    playerData.setIgnoredPlayers(ignoredPlayers);
                    playerData.save();
                    Util.sendMessage(sender, ignoredPlayers.contains(targetUUID) ? "&aThe player has been added to your ignore list." : "&aThe player has been removed from your ignore list.");
                    return true;
                }
            }
        }
    }
}
