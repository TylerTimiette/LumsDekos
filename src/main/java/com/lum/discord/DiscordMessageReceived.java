package com.lum.discord;

import com.lum.utilities.Util;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePostProcessEvent;
import github.scarsz.discordsrv.dependencies.kyori.adventure.text.Component;
import github.scarsz.discordsrv.dependencies.kyori.adventure.text.format.NamedTextColor;
import github.scarsz.discordsrv.dependencies.kyori.adventure.text.format.TextDecoration;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

public class DiscordMessageReceived {


    /**
     * Not going to lie to you, this class is FUCKED. I actually DO want to use DiscordSRV's Adventure implementation
     * to make the presentation of the code a little nicer, there's probably another way to do this though.
     *
     * I also have never used Adventure before, so this is a first. This probably didn't go very well....
     *
     * Eventually, I would like to use Adventure for more of this plugin, but the documentation is a little hard for me
     * to comprehend for whatever reason.
     **/

    @Subscribe
    public void discordMessageReceivedEvent(DiscordGuildMessagePostProcessEvent e) {
        //Getting the message which we've just processed
        String[] messageArray = e.getProcessedMessage().split("\\s+");
        //Here we start building our Component.
        final Component text = Component.text("");

        for (int i = 0; i < messageArray.length; i++) {
            //We now check if any given word in the string is a URL. We do this by just trying everything.
            try {
                new URL(messageArray[i]).toURI();
                messageArray[i] = "(LINK)";
                text.append(Component.text("(LINK)", NamedTextColor.AQUA).decoration(TextDecoration.BOLD, true));
            } catch (MalformedURLException | URISyntaxException ignored) {
                //If there's an exception, it's not a URL! Therefore, we don't do anything special.
            }
        }
        //Setting our (maybe) modded message to be sent off
        e.setMinecraftMessage(text);

    }
}
