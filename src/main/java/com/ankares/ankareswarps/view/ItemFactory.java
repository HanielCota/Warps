package com.ankares.ankareswarps.view;

import com.ankares.ankareswarps.WarpPlugin;
import com.ankares.ankareswarps.utils.ItemBuilder;
import com.ankares.ankareswarps.view.enums.WarpViewLocations;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * This class provides methods to create various ItemStacks for a warp menu.
 */
public class ItemFactory {
    private final WarpPlugin plugin;

    public ItemFactory(WarpPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Create a back button ItemStack.
     */
    public ItemStack createBackButton() {
        return new ItemBuilder(Material.ARROW)
                .setName("§aFechar")
                .setLore("§7Clique para fechar o menu.")
                .build();
    }

    /**
     * Create a config button ItemStack.
     */
    public ItemStack createConfigButton() {
        return new ItemBuilder(Material.GRASS_BLOCK)
                .setName("§aSeleção de Warps")
                .setLore(
                        "§7Utilize para locomover-se mais rápido",
                        "§7e salve as favoritas.",
                        "",
                        "§7Assim, ganhando recompensas por isso.")
                .build();
    }

    /**
     * Create an information button ItemStack.
     */
    public ItemStack createInformationButton() {
        return new ItemBuilder(Material.WRITABLE_BOOK)
                .setName("§eInformações")
                .setLore(
                        "§7Caso ainda não tenha selecionado",
                        "§7Nenhuma warp favorita,",
                        "§7Três barreiras estarão disponíveis ao lado.",
                        "",
                        "§7No ícone de configurações,",
                        "§7Você pode selecionar até 3 warps.",
                        "",
                        "§7Como favoritas usando o §eShift + Clique esquerdo§7.",
                        "§7Ao teleportar para uma de suas warps",
                        "§7Favoritas através do menu,",
                        "§7Você receberá bônus exclusivos.",
                        "",
                        "§7Se não desejar adicionar favoritas,",
                        "§7Basta clicar normalmente",
                        "§7Na aba de configurações, sem usar o Shift.")
                .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .addItemFlag(ItemFlag.HIDE_ITEM_SPECIFICS)
                .addItemFlag(ItemFlag.HIDE_UNBREAKABLE)
                .build();
    }

    /**
     * Create a destruction button ItemStack.
     */
    public ItemStack createDestructionButton() {
        return new ItemBuilder(Material.DARK_OAK_BUTTON)
                .setName("§cRemover todas")
                .setLore("§7Remova todas as suas warps favoritas.")
                .build();
    }

    /**
     * Create a player head ItemStack.
     */
    public ItemStack createPlayerHead(Player player) {
        int playerFavoriteWarpsCount = plugin.getPlayerDataManager().getPlayerFavoriteWarpsCount(player.getName());

        return new ItemBuilder(Material.PLAYER_HEAD)
                .setSkullOwner(player.getName())
                .setName("§aSuas Vantagens")
                .setLore(
                        "§7Total de Favoritos: " + playerFavoriteWarpsCount + "/3",
                        "§7Moedas ao usar warps favoritas: §e30 §7coins.")
                .build();
    }

    /**
     * Create a barrier ItemStack.
     */
    public ItemStack createBarrierItem() {
        return new ItemBuilder(Material.BARRIER)
                .setName("§7Nenhuma Warp Favorita")
                .build();
    }

    /**
     * Create an ItemStack for a specific warp location.
     *
     * @param warp The warp location to create an item for.
     * @return The created ItemStack.
     */
    public ItemStack createWarpItem(WarpViewLocations warp) {
        String locationName = warp.getLocationName().substring(0, 1).toUpperCase()
                + warp.getLocationName().substring(1).toLowerCase();

        return new ItemBuilder(warp.getSkullIcon())
                .setName(ChatColor.GREEN + locationName)
                .setLore(warp.getLore())
                .build();
    }
}
