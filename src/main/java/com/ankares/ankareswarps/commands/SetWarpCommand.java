package com.ankares.ankareswarps.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import com.ankares.ankareswarps.WarpPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("setWarp")
public class SetWarpCommand extends BaseCommand {

    @Dependency
    private WarpPlugin plugin;

    @Default
    public void setWarp(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cUso correto: /setwarp <nome>");
            return;
        }

        String warpName = args[0];

        Location currentLocation = player.getLocation();

        if (plugin.getLocationManager().getLocationByName(warpName) != null) {
            player.sendMessage("§cJá existe uma warp com o nome " + warpName + ".");
            return;
        }

        plugin.getLocationManager().createLocation(warpName, currentLocation);
        player.sendMessage("§aWarp " + warpName + " definida com sucesso.");
    }
}