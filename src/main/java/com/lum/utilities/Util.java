package com.lum.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lum.LumsDekos;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Util {
    private static String prefix = "";
    private static final Map<UUID, PlayerData> playerData = new HashMap();
    public static final char COLOR_CHAR = '§';

    public Util() {
    }

    public static void init() {
        prefix = color(LumsDekos.getInstance().getConfig().getString("prefix"));
    }

    private static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void sendMessage(CommandSender sender, String string) {
        sender.sendMessage(prefix + color(string));
    }

    public static boolean hasPlayerData(UUID uuid) {
        return playerData.containsKey(uuid);
    }

    public static PlayerData getPlayerData(UUID uuid) {
        return (PlayerData)playerData.get(uuid);
    }

    public static void addPlayerData(UUID uuid) {
        playerData.put(uuid, LumsDekos.getInstance().getDatabase().getPlayerData(uuid));
    }

    public static void removePlayerData(UUID uuid) {
        playerData.remove(uuid);
    }

    public static boolean checkPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sendMessage(sender, "This command can only be executed as a player.");
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkArgs(CommandSender sender, String[] args, int length, boolean minimum) {
        if (minimum) {
            if (args.length < length) {
                sendMessage(sender, "You used an invalid amount of arguments. This command has to have at least " + length + ".");
                return true;
            } else {
                return false;
            }
        } else if (args.length != length) {
            sendMessage(sender, "You used an invalid amount of arguments. This command accepts only " + length + ".");
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPerms(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            sendMessage(sender, "&cI'm sorry, but you are missing the following permission: &6" + permission);
            return false;
        } else {
            return true;
        }
    }

    public static void sendChatMessage(UUID player, BaseComponent... baseComponents) {
        Bukkit.getScheduler().runTaskAsynchronously(LumsDekos.getInstance(), () -> {
            Bukkit.getOnlinePlayers().forEach((target) -> {
                if (!getPlayerData(target.getUniqueId()).getIgnoredPlayers().contains(player.toString())) {
                    target.sendMessage(baseComponents);
                }

            });
        });
    }

    static void logException(Exception exception, UUID uuid, String playerMessage, String consoleMessage) {
        LumsDekos plugin = LumsDekos.getInstance();
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            sendMessage(player, playerMessage);
        }

        plugin.getLogger().severe(consoleMessage);
        exception.printStackTrace();
    }

    public static String removeFormatting(Player player, String string) {
        return player.hasPermission("chat.formatting") ? string : string.replaceAll("&+[k-oK-O]+", "");
    }

    public static String translateHexColorCodes(String startTag, String message) {
        Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})");
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 32);

        while(matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, "§x§" + group.charAt(0) + '§' + group.charAt(1) + '§' + group.charAt(2) + '§' + group.charAt(3) + '§' + group.charAt(4) + '§' + group.charAt(5));
        }

        return matcher.appendTail(buffer).toString();
    }

    public static boolean validateHexCode(String toValidate) {
        String regex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
        Pattern p = Pattern.compile(regex);
        if (toValidate == null) {
            return false;
        } else {
            Matcher m = p.matcher(toValidate);
            return m.matches();
        }
    }

    public static String rainbowifyText(String input) {
        StringBuilder output = new StringBuilder();
        input = input.replace("special_rainbow", "");
        int currentChar = 1;

        for(int i = 0; i < input.length(); ++i) {
            switch(currentChar) {
                case 1:
                    output.append("&4");
                    break;
                case 2:
                    output.append("&c");
                    break;
                case 3:
                    output.append("&6");
                    break;
                case 4:
                    output.append("&e");
                    break;
                case 5:
                    output.append("&a");
                    break;
                case 6:
                    output.append("&2");
                    break;
                case 7:
                    output.append("&b");
                    break;
                case 8:
                    output.append("&3");
                    break;
                case 9:
                    output.append("&5");
                    break;
                case 10:
                    output.append("&d");
                    break;
                case 11:
                    currentChar = 1;
            }

            output.append(input.charAt(i));
            if (input.charAt(i) != ' ') {
                ++currentChar;
            }
        }

        return output.toString();
    }

    public static String segmentColoredMessage(String input, String type) {
        StringBuilder output = new StringBuilder(input);

        //I've decompiled this so many times that this is why it looks like this.
        byte var6 = -1;
        switch(type) {
            case "bi":
                var6 = 0;
                break;
            case "nb":
                var6 = 2;
                break;
            case "ace":
                var6 = 4;
                break;
            case "pan":
                var6 = 5;
                break;
            case "lesbian":
                var6 = 1;
                break;
            case "trans":
                var6 = 3;
                break;
        }

        byte segments;
        int increment;
        switch(var6) {
            case 0:
                segments = 3;
                Math.round((float)(increment = input.length() / segments));
                output.insert(0, "#D8097E");
                output.insert(increment + 7, "#8C579C");
                output.insert(increment * 2 + 14, "#24468E");
                break;
            case 1:
                segments = 5;
                Math.round((float)(increment = input.length() / segments));
                output.insert(0, "#D62900");
                output.insert(increment + 7, "#FF9B55");
                output.insert(increment * 2 + 14, "#FFFFFF");
                output.insert(increment * 3 + 21, "#D461A6");
                output.insert(increment * 4 + 28, "#A50062");
                break;
            case 2:
                segments = 4;
                Math.round((float)(increment = input.length() / segments));
                output.insert(0, "#FFF430");
                output.insert(increment + 7, "#FFFFFF");
                output.insert(increment * 2 + 14, "#9C59D1");
                output.insert(increment * 3 + 21, "#000000");
                break;
            case 3:
                segments = 5;
                Math.round((float)(increment = input.length() / segments));
                output.insert(0, "#55CDFC");
                output.insert(increment + 7, "#F7A8B8");
                output.insert(increment * 2 + 14, "#FFFFFF");
                output.insert(increment * 3 + 21, "#F7A8B8");
                output.insert(increment * 4 + 28, "#55CDFC");
                break;
            case 4:
                segments = 4;
                Math.round((float)(increment = input.length() / segments));
                output.insert(0, "#000000");
                output.insert(increment + 7, "#A3A3A3");
                output.insert(increment * 2 + 14, "#FFFFFF");
                output.insert(increment * 3 + 21, "#800080");
                break;
            case 5:
                segments = 3;
                Math.round((float)(increment = input.length() / segments));
                output.insert(0, "#FF1B8D");
                output.insert(increment + 7, "#FFDA00");
                output.insert(increment * 2 + 14, "#1BB3FF");
        }

        return output.toString();
    }
}
