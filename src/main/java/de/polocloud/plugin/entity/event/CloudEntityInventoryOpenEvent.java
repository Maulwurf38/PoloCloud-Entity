package de.polocloud.plugin.entity.event;

import de.polocloud.api.groups.ServiceGroup;
import de.polocloud.api.service.CloudService;
import de.polocloud.plugin.entity.common.base.CloudEntity;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@Setter
public class CloudEntityInventoryOpenEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final Player player;
    private final CloudEntity cloudEntity;
    private final ServiceGroup serviceGroup;
    private final List<CloudService> cloudServices;
    private boolean canceled = false;
    private Inventory inventory;

    public CloudEntityInventoryOpenEvent(Player player, CloudEntity cloudEntity, ServiceGroup serviceGroup, List<CloudService> cloudServices, Inventory inventory) {
        this.player = player;
        this.cloudEntity = cloudEntity;
        this.serviceGroup = serviceGroup;
        this.cloudServices = cloudServices;
        this.inventory = inventory;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    @Override
    public boolean isCancelled() {
        return this.canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.canceled = cancel;
    }
}
