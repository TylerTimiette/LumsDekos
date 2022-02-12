package com.timeist;

import com.timeist.discordcommands.RegisterCommand;
import com.timeist.listeners.*;
import com.timeist.minecraftcommands.ArrowCommand;
import com.timeist.minecraftcommands.ColorCommand;
import com.timeist.minecraftcommands.EmoteCommand;
import com.timeist.minecraftcommands.IgnoreCommand;
import com.timeist.minecraftcommands.LinkCommand;
import com.timeist.minecraftcommands.MeCommand;
import com.timeist.minecraftcommands.NickCommand;
import com.timeist.minecraftcommands.PrefixCommand;
import com.timeist.minecraftcommands.QuoteCommand;
import com.timeist.minecraftcommands.SpecialChatCommand;
import com.timeist.minecraftcommands.WebhookCommand;
import com.timeist.database.Database;
import com.timeist.database.SQLite;
import com.timeist.handlers.BrandHandler;

import java.util.Objects;

import github.scarsz.discordsrv.DiscordSRV;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class TimeistsDecos extends JavaPlugin  {
    private static TimeistsDecos instance;
    private static Chat chat = null;
    private Database database;
    Permission permission = null;
    public static String url;
    private Database tupperdb;

    private DiscordReadyListener reg = new DiscordReadyListener();
    private RegisterCommand rc = new RegisterCommand();
    private DiscordLinkListener dlink = new DiscordLinkListener();
    private DiscordUnlinkListener dunlink = new DiscordUnlinkListener();

    public TimeistsDecos() {
        instance = this;
    }

    public static TimeistsDecos getInstance() {
        return instance;
    }

    public static Plugin getPlugin() {
        return getPlugin(TimeistsDecos.class);
    }

    public Database getDatabase() {
        return this.database;
    }


    public Database getTupperdb() { return this.tupperdb; }

    public void onEnable() {
        this.getLogger().info("Lum's Dekos project started on 1/27/18. It is developed & maintained by Timiette.");
        if (!this.setupVault()) {
            this.getLogger().warning("Vault dependency not found. Disabling Plugin now.");
            this.getServer().getPluginManager().disablePlugin(this);
        } else {
            if (!this.getDataFolder().exists() && !this.getDataFolder().mkdir()) {
                this.getLogger().info("Failed to create the MultiNick folder. Check your read/write settings.");
            }

            this.setupPermissions();
            this.getConfig().addDefault("maxLength", 25);
            this.getConfig().addDefault("prefix", "&a&lTimeistsDekos &f&l» &r");
            this.getConfig().options().copyDefaults(true);
            this.getConfig().addDefault("webhook", "CHANGEME");
            this.getConfig().addDefault("link-logging", "changeme");
            this.saveConfig();


            this.database = new SQLite(this);
            this.database.load();
            this.tupperdb = new SQLite(this);
            this.tupperdb.load();
            url = this.getConfig().getString("webhook");
            Util.init();
            ((PluginCommand)Objects.requireNonNull(this.getCommand("me"))).setExecutor(new MeCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("nick"))).setExecutor(new NickCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("link"))).setExecutor(new LinkCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("arrow"))).setExecutor(new ArrowCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("emote"))).setExecutor(new EmoteCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("quote"))).setExecutor(new QuoteCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("prefix"))).setExecutor(new PrefixCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("ignore"))).setExecutor(new IgnoreCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("color"))).setExecutor(new ColorCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("webhook"))).setExecutor(new WebhookCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("specialchat"))).setExecutor(new SpecialChatCommand());
            this.getServer().getMessenger().registerIncomingPluginChannel(this, "minecraft:brand", new BrandHandler());
            this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
            this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
            this.getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(this), this);
            this.getServer().getOnlinePlayers().forEach((player) -> {
                Util.addPlayerData(player.getUniqueId());
            });
            this.getServer().getPluginManager().registerEvents((new DiscordChannelListener()), this);
            DiscordSRV.api.subscribe(reg);
            System.out.println("Did this work?");
            DiscordSRV.api.subscribe(rc);
            System.out.println("How about this?");

            DiscordSRV.api.subscribe(dlink);
            DiscordSRV.api.subscribe(dunlink);
        }

    }

    public void onDisable() {
        DiscordSRV.api.unsubscribe(reg);
        DiscordSRV.api.unsubscribe(rc);
        DiscordSRV.api.unsubscribe(dlink);
        DiscordSRV.api.unsubscribe(dunlink);
    }

    private boolean setupVault() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        } else {
            RegisteredServiceProvider<Chat> rsp = this.getServer().getServicesManager().getRegistration(Chat.class);
            chat = rsp == null ? null : (Chat)rsp.getProvider();
            return chat != null;
        }
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = this.getServer().getServicesManager().getRegistration(Permission.class);
        if (permissionProvider != null) {
            this.permission = (Permission)permissionProvider.getProvider();
        }

        return this.permission != null;
    }

    public void addPerm(String perm, Player player) {
        this.permission.playerAdd((String)null, player, perm);
    }

    public static Chat getChat() {
        return chat;
    }
}
