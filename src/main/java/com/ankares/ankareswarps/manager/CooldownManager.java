package com.ankares.ankareswarps.manager;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownManager {
    private final Map<Player, Long> cooldowns = new ConcurrentHashMap<>();
    private final long cooldownTimeMillis;

    public CooldownManager(int seconds) {
        this.cooldownTimeMillis = seconds * 1000L;
    }

    public boolean tryUse(Player player) {
        long currentTimeMillis = System.currentTimeMillis();

        cooldowns.compute(player, (key, lastUsage) -> {
            if (lastUsage == null || currentTimeMillis - lastUsage >= cooldownTimeMillis) {
                return currentTimeMillis;
            }
            return lastUsage;
        });

        return cooldowns.get(player) != currentTimeMillis;
    }

    public void setCooldown(Player player) {
        cooldowns.put(player, System.currentTimeMillis());
    }

    public void removeCooldown(Player player) {
        cooldowns.remove(player);
    }
}
