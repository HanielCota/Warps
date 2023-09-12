package com.ankares.ankareswarps.external.fastinv;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Esta classe gerencia a atualização de itens em um inventário.
 */
public class InventoryUpdater {

    private BukkitTask task = null;
    private long refreshInterval = 10; // Padrão: 10
    private final Map<Integer, Supplier<ItemStack>> suppliers = new HashMap<>();
    private final Map<Integer, Consumer<InventoryClickEvent>> handlers = new HashMap<>();

    /**
     * Define o intervalo de atualização.
     *
     * @param interval O intervalo de atualização em ticks.
     * @return Esta instância de InventoryUpdater.
     */
    public InventoryUpdater refreshInterval(long interval) {
        this.refreshInterval = interval;
        return this;
    }

    /**
     * Adiciona um item a ser atualizado.
     *
     * @param slot     O slot do inventário.
     * @param supplier O fornecedor do item.
     * @param handler  O manipulador de cliques (pode ser null).
     * @return Esta instância de InventoryUpdater.
     */
    public InventoryUpdater updateItem(Integer slot, Supplier<ItemStack> supplier, Consumer<InventoryClickEvent> handler) {
        this.suppliers.put(slot, supplier);
        this.handlers.put(slot, handler);
        return this;
    }

    /**
     * Adiciona um item a ser atualizado sem um manipulador de cliques.
     *
     * @param slot     O slot do inventário.
     * @param supplier O fornecedor do item.
     * @return Esta instância de InventoryUpdater.
     */
    public InventoryUpdater updateItem(Integer slot, Supplier<ItemStack> supplier) {
        return updateItem(slot, supplier, null);
    }

    /**
     * Adiciona uma faixa de itens a serem atualizados.
     *
     * @param from     O primeiro slot.
     * @param to       O último slot.
     * @param supplier O fornecedor do item.
     * @param handler  O manipulador de cliques (pode ser null).
     * @return Esta instância de InventoryUpdater.
     */
    public InventoryUpdater updateItems(Integer from, Integer to, Supplier<ItemStack> supplier, Consumer<InventoryClickEvent> handler) {
        for (int i = from; i <= to; i++) {
            updateItem(i, supplier, handler);
        }
        return this;
    }

    /**
     * Inicia a atualização do inventário.
     *
     * @param fastInv O inventário a ser atualizado.
     */
    public void startUpdating(FastInv fastInv) {
        stopUpdating();

        for (Map.Entry<Integer, Supplier<ItemStack>> entry : suppliers.entrySet()) {
            fastInv.setItem(entry.getKey(), entry.getValue().get(), handlers.getOrDefault(entry.getKey(), null));
        }

        this.task = Bukkit.getScheduler().runTaskTimer(
                JavaPlugin.getProvidingPlugin(InventoryUpdater.class),
                () -> {
                    for (Map.Entry<Integer, Supplier<ItemStack>> entry : suppliers.entrySet()) {
                        fastInv.getInventory().setItem(entry.getKey(), entry.getValue().get());
                    }
                },
                refreshInterval, // delay
                refreshInterval // period
        );
    }

    /**
     * Para a atualização do inventário.
     */
    public void stopUpdating() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
}
