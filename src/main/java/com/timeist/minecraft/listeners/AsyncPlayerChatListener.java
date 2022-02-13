package com.timeist.minecraft.listeners;

import com.timeist.discord.DiscordWebhook;
import com.timeist.utilities.PlayerData;
import com.timeist.TimeistsDecos;
import com.timeist.utilities.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class AsyncPlayerChatListener implements Listener {
    private final TimeistsDecos plugin;
    private final List<UUID> partyChat = new ArrayList();
    private final List<UUID> sctChat = new ArrayList();
    private List<UUID> marryChat = new ArrayList();

    public AsyncPlayerChatListener(TimeistsDecos plugin) {
        this.plugin = plugin;
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (event.isCancelled()) {
            if (this.partyChat.contains(player.getUniqueId())) {
                this.plugin.getLogger().info("(PARTY) " + player.getName() + ": " + event.getMessage());
            } else if (this.sctChat.contains(player.getUniqueId())) {
                this.plugin.getLogger().info("(SCT) " + player.getName() + ": " + event.getMessage());
            }
        } else {
            event.setCancelled(true);
            PlayerData playerData = Util.getPlayerData(player.getUniqueId());
            Chat chat = TimeistsDecos.getChat();
            StringBuilder sb = new StringBuilder();
            sb.append(playerData.getPrefix());
            sb.append(chat.getPlayerPrefix((String)null, player));
            sb.append(playerData.getSuffix());
            sb.append("&r");
            if (sb.length() > 0) {
                sb.append(" ");
            }

            sb.append(player.getDisplayName());
            if (playerData.getMarker().contains("#")) {
                sb.append(ChatColor.translateAlternateColorCodes('&', ChatColor.of(playerData.getMarker()) + "&l >&r "));
            } else {
                sb.append(ChatColor.translateAlternateColorCodes('&', playerData.getMarker() + "&l >&r "));
            }

            String messagecheck = Util.removeFormatting(player, playerData.getColor() + event.getMessage());
            this.plugin.getLogger().info("[CHAT] " + player.getName() + " > " + event.getMessage());
            if (playerData.getColor().equalsIgnoreCase("special_rainbow")) {
                messagecheck = Util.rainbowifyText(messagecheck);
            }

            String playercolor = playerData.getColor();
            byte var8 = -1;
            switch(playercolor) {
                case "special_bi":
                    messagecheck = Util.segmentColoredMessage(messagecheck.replaceAll(playerData.getColor(), "").replaceAll("&[a-zA-Z0-9]", ""), "bi");
                        break;
                case "special_nb":
                    messagecheck = Util.segmentColoredMessage(messagecheck.replaceAll(playerData.getColor(), "").replaceAll("&[a-zA-Z0-9]", ""), "nb");
                    break;
                case "special_ace":
                    messagecheck = Util.segmentColoredMessage(messagecheck.replaceAll(playerData.getColor(), "").replaceAll("&[a-zA-Z0-9]", ""), "ace");
                    break;
                case "special_pan":
                    messagecheck = Util.segmentColoredMessage(messagecheck.replaceAll(playerData.getColor(), "").replaceAll("&[a-zA-Z0-9]", ""), "pan");
                    break;
                case "special_trans":
                    messagecheck = Util.segmentColoredMessage(messagecheck.replaceAll(playerData.getColor(), "").replaceAll("&[a-zA-Z0-9]", ""), "trans");
                    break;
                case "special_lesbian":
                    messagecheck = Util.segmentColoredMessage(messagecheck.replaceAll(playerData.getColor(), "").replaceAll("&[a-zA-Z0-9]", ""), "lesbian");
                    break;
                case "special_rainbow":
                    messagecheck = Util.rainbowifyText(messagecheck.replaceAll(playerData.getColor(), "").replaceAll("&[a-zA-Z0-9]", ""));
                    break;
            }

            if (messagecheck.startsWith(playerData.getColor()) && playerData.getColor().contains("special_")) {
                messagecheck = messagecheck.replace(playerData.getColor(), "");
            }

            DiscordWebhook hook = new DiscordWebhook();
            hook.setUsername(ChatColor.stripColor(ChatColor.stripColor(player.getDisplayName().replaceAll("&[a-zA-Z0-9]", "")) + " // " + player.getName()));
            hook.setDisplayname(ChatColor.stripColor(player.getDisplayName()).replaceAll("&[k-oK-O]", ""));
            hook.setContent(ChatColor.stripColor(event.getMessage().replaceAll("&[a-zA-Z0-9]", "").replaceAll("@", "#")));
            hook.setAvatarUrl("https://mc-heads.net/head/" + player.getUniqueId() + ".png");

            try {
                hook.execute();
            } catch (Exception var10) {
                var10.printStackTrace();
                System.out.println("You borked it!");
            }

            TextComponent precursor = new TextComponent(this.color(Util.translateHexColorCodes("#", sb.toString())));
            TextComponent message = new TextComponent(this.color(Util.translateHexColorCodes("#", messagecheck)));
            precursor.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, this.color("&4Name: &c" + player.getName() + "\n" + Util.translateHexColorCodes("#", playerData.getQuote()) + "\n&bClick to ignore this player!")));
            precursor.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/ignore " + player.getName()));
            Util.sendChatMessage(player.getUniqueId(), new BaseComponent[]{precursor, message});
        }

    }

    private BaseComponent[] color(String text) {
        return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', text));
    }

    @EventHandler(
            priority = EventPriority.LOWEST
    )
    public void onCommand(PlayerCommandPreprocessEvent event) {
        UUID player = event.getPlayer().getUniqueId();
        String message = event.getMessage();
        if (message.equalsIgnoreCase("/party chat")) {
            if (this.partyChat.contains(player)) {
                this.partyChat.remove(player);
            } else {
                this.partyChat.add(player);
            }
        } else {
            if (message.contains("/mpm name")) {
                event.setCancelled(true);
            }

            if (message.contains("/marry chat toggle")) {
                if (this.marryChat.contains(player)) {
                    this.marryChat.remove(player);
                } else {
                    this.marryChat.add(player);
                }
            }

            if (message.equalsIgnoreCase("/sct")) {
                if (this.sctChat.contains(player)) {
                    this.sctChat.remove(player);
                } else {
                    this.sctChat.add(player);
                }
            }
        }

    }
}
