package de.polocloud.plugin.entity.listener;

import de.polocloud.api.CloudAPI;
import de.polocloud.plugin.entity.common.CloudEntityHandler;
import de.polocloud.plugin.entity.common.gui.GUIHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class InventoryClickListener implements Listener {

    public InventoryClickListener() {
        Bukkit.getPluginManager().registerEvents(this, CloudEntityHandler.getInstance().getPlugin());
    }

    @EventHandler
    public void handle(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (GUIHandler.currentInventories.containsKey(player.getUniqueId()) && GUIHandler.currentInventories.get(player.getUniqueId()).equals(event.getInventory())) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.AIR) && event.getCurrentItem().getItemMeta() != null) {
                CloudAPI.getInstance().getServiceManager().getService(Objects.requireNonNull(event.getCurrentItem()
                        .getItemMeta().getPersistentDataContainer()
                        .get(CloudEntityHandler.getInstance().getNamespacedKey(), PersistentDataType.STRING))).ifPresent(cloudService -> {
                    player.closeInventory();
                    CloudAPI.getInstance().getPlayerManager().getCloudPlayer(player.getUniqueId()).ifPresent(cloudPlayer -> {
                        player.sendMessage("§7Connecting...");
                        if (cloudPlayer.getServer().equals(cloudService)) {
                            player.sendMessage("§cYou are already on this service!");
                            return;
                        }
                        cloudPlayer.connect(cloudService);
                    });
                });
            }
        }
    }
}
