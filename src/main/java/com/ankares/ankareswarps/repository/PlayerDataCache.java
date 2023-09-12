package com.ankares.ankareswarps.repository;

public interface PlayerDataCache {
    void put(String key, String value);

    String getIfPresent(String key);
}