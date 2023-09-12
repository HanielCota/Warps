package com.ankares.ankareswarps.repository;

public interface PlayerDataRepository {
    boolean hasPlayerData(String playerName);

    void savePlayerData(String playerName, String data);

    String getPlayerData(String playerName);
}
