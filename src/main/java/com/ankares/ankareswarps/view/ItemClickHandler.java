package com.ankares.ankareswarps.view;

import com.ankares.ankareswarps.WarpPlugin;
import com.ankares.ankareswarps.utils.ItemBuilder;
import com.ankares.ankareswarps.view.enums.WarpViewLocations;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemClickHandler {
    private final WarpPlugin plugin;
    private final WarpCategoryView warpCategoryView;

    public ItemClickHandler(WarpPlugin plugin, WarpCategoryView warpCategoryView) {
        this.plugin = plugin;
        this.warpCategoryView = warpCategoryView;
    }

    public WarpViewLocations getWarpLocationByName(String warpName) {
        for (WarpViewLocations warpLocation : WarpViewLocations.values()) {
            if (warpLocation.getLocationName().equalsIgnoreCase(warpName)) {
                return warpLocation;
            }
        }
        return null;
    }

    public void updateFavoriteWarpIcon(Player player, int slot, WarpViewLocations warpLocation) {
        ItemStack itemStack = new ItemBuilder(Material.PLAYER_HEAD)
                .setSkull(warpLocation.getSkullIcon())
                .setName("§a" + WarpCategoryView.capitalizeFirstLetter(warpLocation.getLocationName()))
                .setLore(
                        "§7Você selecionou esta warp como favorita.",
                        "",
                        "§7Salvo em: "
                                + plugin.getPlayerDataManager()
                                        .getPlayerFavoriteWarpSaveDate(
                                                player.getName(), warpLocation.getLocationName()),
                        "§fClique com o §eSHIFT + CLICK ESQUERDO §fpara remover.")
                .build();

        warpCategoryView.setItem(slot, itemStack, click -> {
            if (click.isShiftClick()) {
                handleShiftClick(player, warpLocation);
                return;
            }

            if (!plugin.getCooldownManager().tryUse(player)) {
                player.sendMessage("§cEspere um momento antes de usar esta warp novamente.");
                return;
            }

            handleWarpItemClick(player, warpLocation.getLocationName());
        });
    }

    private void handleWarpItemClick(Player player, String warpName) {
        Location location = plugin.getLocationManager().getLocationByName(warpName);

        if (location == null) {
            player.sendMessage("§cA localização especificada não foi encontrada.");
            return;
        }

        player.teleportAsync(location);
        player.sendMessage("§aVocê foi teleportado para " + warpName + ".");
        plugin.getCooldownManager().setCooldown(player);
    }

    private void handleShiftClick(Player player, WarpViewLocations warpLocation) {
        plugin.getPlayerDataManager().removePlayerFavoriteWarp(player.getName(), warpLocation.getLocationName());

        player.sendMessage("§aWarp favorita removida: " + warpLocation.getLocationName());
        warpCategoryView.initializeItems(player);
        new WarpCategoryView(player, plugin).open(player);
    }
}
