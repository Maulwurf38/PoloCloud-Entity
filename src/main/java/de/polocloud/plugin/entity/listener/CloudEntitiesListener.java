package de.polocloud.plugin.entity.listener;

import de.polocloud.plugin.entity.common.CloudEntityHandler;
import de.polocloud.plugin.entity.common.base.CloudEntity;
import de.polocloud.plugin.entity.common.gui.CloudEntityGUI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class CloudEntitiesListener implements Listener {

    public CloudEntitiesListener() {
        Bukkit.getPluginManager().registerEvents(this, CloudEntityHandler.getInstance().getPlugin());
    }

    @EventHandler
    public void handle(EntityDamageEvent event) {
        if (CloudEntityHandler.getInstance().getEntities().stream().anyMatch(entity -> entity.getSpawnedEntity().getEntityId() == event.getEntity().getEntityId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(PlayerInteractEntityEvent event) {
        CloudEntity cloudEntity = CloudEntityHandler.getInstance().getEntities().stream().filter(entity -> entity.getSpawnedEntity().getEntityId() == event.getRightClicked().getEntityId()).findFirst().orElse(null);
        if (cloudEntity != null) {
            event.setCancelled(true);
            new CloudEntityGUI(cloudEntity, event.getPlayer());
        }
    }

}
