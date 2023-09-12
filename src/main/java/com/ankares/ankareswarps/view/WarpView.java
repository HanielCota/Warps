package com.ankares.ankareswarps.view;

import com.ankares.ankareswarps.WarpPlugin;
import com.ankares.ankareswarps.external.fastinv.FastInv;
import com.ankares.ankareswarps.view.enums.WarpViewLocations;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WarpView extends FastInv {

    private final WarpPlugin plugin;

    public WarpView(WarpPlugin plugin, Player player) {
        super(54, "Para onde deseja ir?");
        this.plugin = plugin;
        initializeItems(player);
    }

    private void initializeItems(Player player) {
        ItemFactory itemFactory = new ItemFactory(plugin);

        ItemStack backButton = itemFactory.createBackButton();

        setItem(49, backButton, click -> {
            new WarpCategoryView(player, plugin).open(player);
            player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_DOOR_CLOSE, 10f, 1f);
        });

        for (WarpViewLocations warp : WarpViewLocations.values()) {
            ItemStack warpItem = itemFactory.createWarpItem(warp);
            Location location = plugin.getLocationManager().getLocationByName(warp.getLocationName());

            if (location == null) {
                continue;
            }

            int slot = getSlotForLocation(warp);

            setItem(slot, warpItem, click -> {
                if (!click.isShiftClick()) {
                    if (plugin.getCooldownManager().tryUse(player)) {
                        player.sendMessage("§cEspere um momento antes de usar esta warp novamente.");
                        return;
                    }

                    player.sendMessage("§aVocê foi teleportado para " + warp.getLocationName() + ".");
                    handleWarpItemClick(player, location);
                    return;
                }

                if (isFavoriteWarp(player.getName(), warp.getLocationName())) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10f, 1f);
                    player.sendMessage("§cVocê já adicionou a warp " + warp.getLocationName() + " aos favoritos!");
                    return;
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = dateFormat.format(new Date());

                plugin.getPlayerDataManager().savePlayerFavoriteWarpWithDate(player.getName(), warp.getLocationName(), dateString);
                player.playSound(player.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 10f, 1f);
                player.sendMessage("§aWarp " + warp.getLocationName() + " adicionada aos favoritos com sucesso!");
            });
        }
    }

    private boolean isFavoriteWarp(String playerName, String warpName) {
        List<String> favoriteWarps = plugin.getPlayerDataManager().getPlayerFavoriteWarps(playerName);
        return favoriteWarps.contains(warpName);
    }

    private int getSlotForLocation(WarpViewLocations location) {
        return switch (location) {
            case MINA -> 12;
            case CEU -> 13;
            case ARENA -> 28;
            case POCO -> 29;
            case SHOP -> 30;
            case PESCA -> 31;
            case PORTAIS -> 32;
            case FLORESTA -> 33;
            case FARM -> 34;
        };
    }

    private void handleWarpItemClick(Player player, Location location) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10f, 1f);
        player.teleportAsync(location);
        plugin.getCooldownManager().setCooldown(player);
    }
}
