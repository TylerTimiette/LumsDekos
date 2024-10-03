package com.lum.discord;


import com.google.gson.JsonObject;
import com.lum.LumsDekos;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class DiscordWebhook {
    private String content;
    private String username;
    private String avatarUrl;
    private URL url;

    public DiscordWebhook(URL url) {
        this.url = url;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setDisplayname(String displayname) {
    }

    public void execute() throws IOException {
        if (this.content == null) {
            throw new IllegalArgumentException("I don't know how this managed to happen, but the message sent has no content.");
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("content", this.content);
            jsonObject.addProperty("username", this.username);
            jsonObject.addProperty("avatar_url", this.avatarUrl);
            if (LumsDekos.url.contains("discord.com/api/webhooks")) {
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.addRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("User-Agent", "Timmy can go fuck his user agent lol");
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                OutputStream stream = connection.getOutputStream();
                stream.write(jsonObject.toString().getBytes());
                stream.flush();
                stream.close();
                connection.getInputStream().close();
                connection.disconnect();
            } else {
                System.out.println("Hello! You have not set the Discord Webhook URL in the config! You can do this with /webhook (URL)! \nThis command must be run by CONSOLE.");
            }

        }
    }
}
