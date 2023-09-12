package com.ankares.ankareswarps.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Subcommand;
import com.ankares.ankareswarps.WarpPlugin;
import com.ankares.ankareswarps.manager.PlayerDataManager;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("setPrefer")
public class WarpPreferCommand extends BaseCommand {
    @Dependency
    WarpPlugin plugin;
    private final PlayerDataManager playerDataManager;

    public WarpPreferCommand(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    @Default
    public void onCommand(Player player) {
        List<String> favoriteWarps = playerDataManager.getPlayerFavoriteWarps(player.getName());

        if (favoriteWarps.isEmpty()) {
            player.sendMessage("§eVocê não tem warps favoritas.");
            return;
        }

        player.sendMessage("§eSuas warps favoritas:");

        for (String warpName : favoriteWarps) {
            player.sendMessage("§a- " + warpName);
        }

    }

    @Subcommand("setfavorita")
    public void setFavoriteWarp(Player player, String warpName) {
        if (warpName == null || warpName.isBlank()) {
            player.sendMessage("§cVocê deve fornecer um nome de warp para adicionar aos favoritos.");
            return;
        }

        playerDataManager.addPlayerFavoriteWarp(player.getName(), warpName);
        player.sendMessage("§aWarp " + warpName + " adicionada aos favoritos com sucesso!");
    }

    @Subcommand("removefavorite")
    public void removeFavoriteWarp(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("§cVocê deve fornecer um nome de warp para remover dos favoritos.");
            return;
        }
        String warpName = args[0];

        List<String> favoriteWarps = playerDataManager.getPlayerFavoriteWarps(player.getName());

        if (!favoriteWarps.contains(warpName)) {
            player.sendMessage("§cVocê não tem a warp '" + warpName + "' nos favoritos.");
            return;
        }

        playerDataManager.removePlayerFavoriteWarp(player.getName(), warpName);
        player.sendMessage("§aWarp " + warpName + " removida dos favoritos com sucesso!");
    }

}