package com.timeist.minecraft.listeners;

import com.timeist.utilities.PlayerFile;
import com.timeist.TimeistsDecos;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.dependencies.jda.api.events.message.MessageReceivedEvent;
import github.scarsz.discordsrv.dependencies.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

public class DiscordChannelListener extends ListenerAdapter {



    @Subscribe
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        Bukkit.getScheduler().runTaskAsynchronously(TimeistsDecos.getInstance(), () -> {
            Bukkit.getOnlinePlayers().forEach((target) -> {

                PlayerFile pf = new PlayerFile(target.getUniqueId());
                if (pf.getConfig().getString("connectedchannel").equalsIgnoreCase(event.getChannel().getId())) {


                    if(pf.getConfig().get("listen-mode").equals("bot-only"))

                    target.sendMessage(event.getMessage().getAuthor().getName() + " >> " + event.getMessage().getContentRaw());
                }

            });
        });


        }

}
