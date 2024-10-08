package com.lum.database;

import com.lum.utilities.PlayerData;
import com.lum.LumsDekos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public abstract class Database {
    final LumsDekos instance;
    Connection connection;

    Database(LumsDekos instance) {
        this.instance = instance;
    }

    protected abstract Connection getSQLConnection();

    public abstract void load();

    void initialize() {
        this.connection = this.getSQLConnection();

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT * FROM `players` LIMIT 1");
            ResultSet resultSet = preparedStatement.executeQuery();
            this.close(preparedStatement, resultSet);
        } catch (SQLException var3) {
            this.instance.getLogger().log(Level.SEVERE, "The plugin was not able to make a connection to the database.", var3);
        }

    }

    public PlayerData getPlayerData(UUID uuid) {
        PlayerData playerData = new PlayerData(uuid, "", "", "", "&7", "", "", new ArrayList<>());
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            this.connection = this.getSQLConnection();
            preparedStatement = this.connection.prepareStatement("SELECT * FROM `players` WHERE `uuid`=?;");
            preparedStatement.setString(1, uuid.toString());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                playerData.setPrefix(resultSet.getString("prefix"));
                playerData.setNickname(resultSet.getString("nickname"));
                playerData.setSuffix(resultSet.getString("suffix"));
                playerData.setMarker(resultSet.getString("marker"));
                playerData.setQuote(resultSet.getString("quote"));
                playerData.setColor(resultSet.getString("color"));
                List<String> ignoredPlayers = new ArrayList<>(Arrays.asList(resultSet.getString("ignored").split(",")));
                ignoredPlayers.removeIf(String::isEmpty);
                playerData.setIgnoredPlayers(ignoredPlayers);
            }

            return playerData;
        } catch (SQLException var10) {
            this.instance.getLogger().log(Level.SEVERE, "There has been an error executing the statement.", var10);
        } finally {
            this.close(preparedStatement, resultSet);
        }

        return playerData;
    }

    public void setPlayerData(PlayerData playerData) {
        PreparedStatement preparedStatement = null;

        try {
            this.connection = this.getSQLConnection();
            preparedStatement = this.connection.prepareStatement("INSERT OR REPLACE INTO `players` (uuid, prefix, nickname, suffix, marker, quote, ignored, color) VALUES (?,?,?,?,?,?,?,?);");
            preparedStatement.setString(1, playerData.getUuid().toString());
            preparedStatement.setString(2, playerData.getPrefix());
            preparedStatement.setString(3, playerData.getNickname());
            preparedStatement.setString(4, playerData.getSuffix());
            preparedStatement.setString(5, playerData.getMarker());
            preparedStatement.setString(6, playerData.getQuote());
            preparedStatement.setString(7, String.join(",", playerData.getIgnoredPlayers()));
            preparedStatement.setString(8, playerData.getColor());
            preparedStatement.executeUpdate();
        } catch (SQLException var7) {
            this.instance.getLogger().log(Level.SEVERE, "There has been an error executing the statement.", var7);
        } finally {
            this.close(preparedStatement, (ResultSet)null);
        }

    }

    private void close(PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException var4) {
            this.instance.getLogger().log(Level.SEVERE, "A SQL Statement or ResultSet could not be closed.", var4);
        }

    }
}
