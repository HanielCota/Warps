package com.ankares.ankareswarps.cache;

import com.ankares.ankareswarps.repository.PlayerDataCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

public class PlayerDataCacheProvider implements PlayerDataCache {
    private final Cache<String, String> playerDataCache;

    public PlayerDataCacheProvider() {
        this.playerDataCache = Caffeine.newBuilder()
                .expireAfterWrite(10L, TimeUnit.MINUTES)
                .maximumSize(100L)
                .build();
    }

    @Override
    public void put(String key, String value) {
        playerDataCache.put(key, value);
    }

    @Override
    public String getIfPresent(String key) {
        return playerDataCache.getIfPresent(key);
    }
}