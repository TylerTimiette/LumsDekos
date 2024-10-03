package com.lum;

import com.lum.discord.DiscordMessageReceived;
import com.lum.discord.DiscordReadyListener;
import com.lum.discord.commands.*;
import com.lum.minecraft.botrelated.ChannelListenCommand;
import com.lum.minecraft.botrelated.ChannelListenMode;
import com.lum.minecraft.commands.ArrowCommand;
import com.lum.minecraft.commands.ColorCommand;
import com.lum.minecraft.commands.EmoteCommand;
import com.lum.minecraft.commands.IgnoreCommand;
import com.lum.minecraft.commands.URLCommand;
import com.lum.minecraft.commands.MeCommand;
import com.lum.minecraft.commands.NickCommand;
import com.lum.minecraft.commands.PrefixCommand;
import com.lum.minecraft.commands.QuoteCommand;
import com.lum.minecraft.commands.SpecialChatCommand;
import com.lum.minecraft.botrelated.WebhookCommand;
import com.lum.database.Database;
import com.lum.database.SQLite;
import com.lum.minecraft.handlers.BrandHandler;

import java.util.Objects;

import com.lum.minecraft.listeners.*;
import com.lum.utilities.Util;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class LumsDekos extends JavaPlugin  {
    private static LumsDekos instance;
    private static Chat chat = null;
    private Database database;
    Permission permission = null;
    public static String url;
    private Database tupperdb;

    private DiscordReadyListener reg = new DiscordReadyListener();
    private DiscordLinkListener dlink = new DiscordLinkListener();
    private DiscordUnlinkListener dunlink = new DiscordUnlinkListener();
    private DiscordMessageReceived dmessr = new DiscordMessageReceived();

    private WhoAmICommand whois = new WhoAmICommand();

    public LumsDekos() {
        instance = this;
    }

    public static LumsDekos getInstance() {
        return instance;
    }

    public static Plugin getPlugin() {
        return getPlugin(LumsDekos.class);
    }

    public Database getDatabase() {
        return this.database;
    }


    public Database getTupperdb() { return this.tupperdb; }



   public void onEnable() {
        this.getLogger().info("Lum's Dekos project started on 1/27/18. It is developed & maintained by Timiette.");
        if((this.getServer().getPluginManager().getPlugin("PermissionsEx") == null) && (this.getServer().getPluginManager().getPlugin("LuckPerms") == null)) {
            this.getLogger().warning("There's no permission plugin installed! Disabling plugin now\n(You should be using LuckPerms or PEx. If either of these are present, do you have Vault installed?");
        }
        if (!this.setupVault()) {
            this.getLogger().warning("A certain dependency was not found. Disabling Plugin now.\nDo you have PEx/LuckPerms along with Vault installed?");
            this.getServer().getPluginManager().disablePlugin(this);
        } else {
            if (!this.getDataFolder().exists() && !this.getDataFolder().mkdir()) {
                this.getLogger().info("Failed to create the LumsDekos folder. Check your read/write settings.");
            }

            RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            if (provider != null) {
                LuckPerms api = provider.getProvider();

            }

            this.setupPermissions();
            this.getConfig().addDefault("maxLength", 25);
            this.getConfig().addDefault("prefix", "&a&lLumsDekos &f&l» &r");
            this.getConfig().options().copyDefaults(true);
            this.getConfig().addDefault("webhook", "CHANGEME");
            this.getConfig().addDefault("link-logging", "changeme");
            this.getConfig().addDefault("hostname", "changeme hostname");
            this.saveConfig();


            this.database = new SQLite(this);
            this.database.load();
            this.tupperdb = new SQLite(this);
            this.tupperdb.load();
            url = this.getConfig().getString("webhook");
            Util.init();
            ((PluginCommand)Objects.requireNonNull(this.getCommand("me"))).setExecutor(new MeCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("nick"))).setExecutor(new NickCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("link"))).setExecutor(new URLCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("arrow"))).setExecutor(new ArrowCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("emote"))).setExecutor(new EmoteCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("quote"))).setExecutor(new QuoteCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("prefix"))).setExecutor(new PrefixCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("ignore"))).setExecutor(new IgnoreCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("color"))).setExecutor(new ColorCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("webhook"))).setExecutor(new WebhookCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("specialchat"))).setExecutor(new SpecialChatCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("channel"))).setExecutor(new ChannelListenCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("listenmode"))).setExecutor(new ChannelListenMode());

            ((PluginCommand)Objects.requireNonNull(this.getCommand("regchar"))).setExecutor(new CharacterRegisterCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("remchar"))).setExecutor(new CharacterRemoveCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("avatar"))).setExecutor(new AvatarCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("talk"))).setExecutor(new TalkCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("tmode"))).setExecutor(new TalkModeCommand());
            ((PluginCommand)Objects.requireNonNull(this.getCommand("listchars"))).setExecutor(new ListCharsCommand());



            this.getServer().getMessenger().registerIncomingPluginChannel(this, "minecraft:brand", new BrandHandler());
            this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
            this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
            this.getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(this), this);
            this.getServer().getPluginManager().registerEvents(new ChannelTextListener(), this);
            this.getServer().getOnlinePlayers().forEach((player) -> {
                Util.addPlayerData(player.getUniqueId());
            });
            if(Bukkit.getPluginManager().isPluginEnabled("DiscordSRV")) {
                DiscordSRV.api.subscribe(reg);
                DiscordSRV.api.subscribe(dlink);
                DiscordSRV.api.subscribe(dunlink);
                //DiscordSRV.api.subscribe(dmessr);
                DiscordSRV.api.subscribe(whois);
            }
        }

    }

    public void onDisable() {
        DiscordSRV.api.unsubscribe(reg);
        DiscordSRV.api.unsubscribe(dlink);
        DiscordSRV.api.unsubscribe(dunlink);
      // DiscordSRV.api.unsubscribe(dmessr);
        DiscordSRV.api.unsubscribe(whois);
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
