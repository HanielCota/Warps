package com.ankares.ankareswarps.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import com.ankares.ankareswarps.WarpPlugin;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("warpList")
public class WarpListCommand extends BaseCommand {
    @Dependency
    private WarpPlugin plugin;

    @Default
    public void listWarps(Player player) {
        List<String> availableWarps = plugin.getLocationManager().getAvailableWarps();

        if (availableWarps.isEmpty()) {
            player.sendMessage("§cNão há warps disponíveis.");
            return;
        }

        player.sendMessage("§eWarps disponíveis:");
        for (String warp : availableWarps) {
            player.sendMessage("- " + warp);
        }
    }
}

