package com.ankares.ankareswarps.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import com.ankares.ankareswarps.WarpPlugin;
import org.bukkit.entity.Player;

@CommandAlias("delwarp")
public class DeleteWarpCommand extends BaseCommand {

    @Dependency
    private WarpPlugin plugin;

    @Default
    public void deleteWarp(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cUso correto: /delwarp <nome>");
            return;
        }

        String warpName = args[0];

        if (plugin.getLocationManager().getLocationByName(warpName) == null) {
            player.sendMessage("§cA warp com o nome " + warpName + " não foi encontrada.");
            return;
        }

        plugin.getLocationManager().deleteLocation(warpName); // Implemente este método em sua LocationManager para excluir a warp.
        player.sendMessage("§aWarp " + warpName + " deletada com sucesso.");
    }
}
