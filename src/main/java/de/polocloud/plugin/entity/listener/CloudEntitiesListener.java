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
        if (CloudEntityHandler.getInstance().getCloudEntityOfEntity(event.getEntity()) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handle(PlayerInteractEntityEvent event) {
        CloudEntity cloudEntity = CloudEntityHandler.getInstance().getCloudEntityOfEntity(event.getRightClicked());
        if (cloudEntity == null) {
            cloudEntity = CloudEntityHandler.getInstance().getCloudEntityOfLocation(event.getRightClicked().getLocation());
        }
        
        if (cloudEntity != null) {
            event.setCancelled(true);
            new CloudEntityGUI(cloudEntity, event.getPlayer());
        }
    }

}
