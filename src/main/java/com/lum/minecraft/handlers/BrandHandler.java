package com.lum.minecraft.handlers;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.lum.LumsDekos;
import javax.annotation.Nonnull;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BrandHandler implements PluginMessageListener {
    public BrandHandler() {
    }

    public void onPluginMessageReceived(String channel, @Nonnull Player player, @Nonnull byte[] bytes) {
        if (channel.equals("minecraft:brand")) {
            ByteArrayDataInput input = ByteStreams.newDataInput(bytes);
            LumsDekos.getInstance().getLogger().info(player.getName() + " logged in using " + input.readLine());
        }

    }
}
