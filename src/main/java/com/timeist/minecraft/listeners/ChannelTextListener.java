package com.timeist.minecraft.listeners;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.timeist.TimeistsDecos;
import com.timeist.utilities.PlayerData;
import com.timeist.utilities.PlayerFile;
import com.timeist.utilities.Util;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChannelTextListener implements Listener {
    @EventHandler(
            priority = EventPriority.HIGH
    )
    public void onChat(AsyncPlayerChatEvent event) {
        //They want to chat on Discord ONLY
        PlayerFile pf = new PlayerFile(event.getPlayer().getUniqueId());
        if(pf.getConfig().getString("talk-mode").equalsIgnoreCase("discord")) {


            if (DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(event.getPlayer().getUniqueId()) != null) { /**&& pf.getConfig().getString("chat-mode").equalsIgnoreCase("discord")**/
                //Let's check if they're muted or not.
                User u = DiscordUtil.getUserById(DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(event.getPlayer().getUniqueId()));
                if (!DiscordSRV.getPlugin().getMainGuild().getRolesByName("muted", true).contains(u)) {
                    /** Webhook webhook; // some webhook instance
                     WebhookClientBuilder builder = webhook.newClient();
                     WebhookClient client = builder.build(); //remember to close this client when you are done
                     **/


                    Player player = event.getPlayer();
                    PlayerData playerData = Util.getPlayerData(player.getUniqueId());
                    Chat chat = TimeistsDecos.getChat();
                    StringBuilder sb = new StringBuilder();
                    sb.append(playerData.getPrefix());
                    sb.append(chat.getPlayerPrefix((String) null, player));
                    sb.append(playerData.getSuffix());
                    sb.append("&r");
                    if (sb.length() > 0) {
                        sb.append(" ");
                    }

                    sb.append(player.getDisplayName() + " #5663F7--> #5865F2" + DiscordUtil.getJda().getTextChannelById(pf.getConfig().getString("connectedchannel")).getName());
                    if (playerData.getMarker().contains("#")) {
                        sb.append(ChatColor.translateAlternateColorCodes('&', ChatColor.of(playerData.getMarker()) + "&l >&r "));
                    } else {
                        sb.append(ChatColor.translateAlternateColorCodes('&', playerData.getMarker() + "&l >&r "));
                    }

                    String messagecheck = Util.removeFormatting(player, playerData.getColor() + event.getMessage());
                    TimeistsDecos.getPlugin().getLogger().info("[LINKCHAT] " + player.getName() + " > " + event.getMessage());
                    if (playerData.getColor().equalsIgnoreCase("special_rainbow")) {
                        messagecheck = Util.rainbowifyText(messagecheck);
                    }

                    String playercolor = playerData.getColor();
                    byte var8 = -1;
                    switch (playercolor) {
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


                    TextComponent precursor = new TextComponent(this.color(Util.translateHexColorCodes("#", sb.toString())));
                    TextComponent message = new TextComponent(this.color(Util.translateHexColorCodes("#", messagecheck)));
                    precursor.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, this.color("&4Name: &c" + player.getName() + "\n" + Util.translateHexColorCodes("#", playerData.getQuote()) + "\n&bClick to ignore this player!")));
                    precursor.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/ignore " + player.getName()));
                    event.getPlayer().sendMessage(new BaseComponent[]{precursor, message});


//This event is already asynchronous, so we don't need to run this code asynchronously ourselves.
                    WebhookClient client = WebhookClient.withUrl(DiscordUtil.getJda().getTextChannelById(pf.getConfig().getString("connectedchannel")).retrieveWebhooks().complete().get(0).getUrl());
                    WebhookMessageBuilder builder = new WebhookMessageBuilder();
                    builder.setUsername(event.getPlayer().getName() + " // unity.exousia.online");
                    builder.setContent(event.getMessage());
                    builder.setAvatarUrl("https://mc-heads.net/head/" + event.getPlayer().getUniqueId() + ".png");
                    client.send(builder.build());

                    event.setCancelled(true);


                } else
                    System.out.println("muted player tried to speak");
            }

        }
        }

    private BaseComponent[] color(String text) {
        return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', text));
    }
}
