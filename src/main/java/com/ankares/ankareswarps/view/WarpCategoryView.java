package com.ankares.ankareswarps.view;

import com.ankares.ankareswarps.WarpPlugin;
import com.ankares.ankareswarps.external.fastinv.FastInv;
import com.ankares.ankareswarps.view.enums.WarpViewLocations;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * This class represents a warp category view.
 */
public class WarpCategoryView extends FastInv {

    private final WarpPlugin plugin;

    public WarpCategoryView(Player player, WarpPlugin plugin) {
        super(54, "Escolha uma Opção");
        this.plugin = plugin;
        initializeItems(player);
    }

    /**
     * Initialize the items in the warp category view.
     *
     * @param player The player for whom the view is initialized.
     */
    public void initializeItems(Player player) {
        ItemFactory itemFactory = new ItemFactory(plugin);
        ItemClickHandler itemClickHandler = new ItemClickHandler(plugin, this);

        setItem(49, itemFactory.createBackButton(), click -> {
            player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_DOOR_OPEN, 10f, 1f);
            click.getInventory().close();
        });

        setItem(24, itemFactory.createConfigButton(), click -> {
            player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_DOOR_OPEN, 10f, 1f);
            new WarpView(plugin, player).open(player);
        });

        setItem(33, itemFactory.createInformationButton());

        setItem(42, itemFactory.createDestructionButton(), click -> {
            List<String> favoriteWarps = plugin.getPlayerDataManager().getPlayerFavoriteWarps(player.getName());

            if (favoriteWarps.isEmpty()) {
                player.sendMessage("§cVocê não tem warps favoritas para remover.");
                return;
            }

            plugin.getPlayerDataManager().removeAllPlayerFavoriteWarps(player.getName());

            initializeItems(player);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 10f, 1f);
            player.sendMessage("§aVocê removeu todas as suas warps favoritas.");
        });

        setItem(4, itemFactory.createPlayerHead(player));

        List<String> favoriteWarps = plugin.getPlayerDataManager().getPlayerFavoriteWarps(player.getName());

        int slot = 19;
        for (String warpName : favoriteWarps) {
            if (slot > 21) {
                break;
            }

            WarpViewLocations warpLocation = itemClickHandler.getWarpLocationByName(warpName);
            if (warpLocation != null) {
                itemClickHandler.updateFavoriteWarpIcon(player, slot, warpLocation);
            }

            slot++;
        }

        while (slot <= 21) {
            setItem(slot, itemFactory.createBarrierItem());
            slot++;
        }
    }

    /**
     * Capitalize the first letter of a string.
     *
     * @param input The input string.
     * @return The input string with the first letter capitalized.
     */
    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
