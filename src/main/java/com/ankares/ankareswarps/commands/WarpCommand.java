package com.ankares.ankareswarps.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import com.ankares.ankareswarps.WarpPlugin;
import com.ankares.ankareswarps.view.WarpCategoryView;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("warp")
public class WarpCommand extends BaseCommand {

    @Dependency
    private WarpPlugin plugin;

    @Default
    public void warp(Player player, String[] args) {
        if (args.length < 1) {
            new WarpCategoryView(player, plugin).open(player);
            return;
        }

        String locationName = args[0];
        Location location = plugin.getLocationManager().getLocationByName(locationName);

        if (location == null) {
            player.sendMessage("§cA localização especificada não foi encontrada.");
            return;
        }

        player.teleportAsync(location);
        player.sendMessage("§aVocê foi teleportado para " + locationName + ".");
    }
}
