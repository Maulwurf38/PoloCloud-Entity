package de.polocloud.plugin.entity.common.gui;

import de.polocloud.api.CloudAPI;
import de.polocloud.api.groups.ServiceGroup;
import de.polocloud.api.service.CloudService;
import de.polocloud.api.service.ServiceState;
import de.polocloud.plugin.entity.common.CloudEntityHandler;
import de.polocloud.plugin.entity.common.base.CloudEntity;
import de.polocloud.plugin.entity.config.CloudEntityTitleConverter;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

@Getter
public class CloudEntityGUI {

    public CloudEntityGUI(CloudEntity cloudEntity, Player player) {
        int size;
        if (CloudEntityHandler.getInstance().getConfig().getInventorySize() % 9 != 0) {
            size = 54;
        } else {
            size = Math.min(CloudEntityHandler.getInstance().getConfig().getInventorySize(), 54);
        }
        ServiceGroup serviceGroup = CloudAPI.getInstance().getGroupManager().getServiceGroupByNameOrNull(cloudEntity.getCloudEntityInfo().getPossibleGroup());
        if (serviceGroup == null) {
            return;
        }

        Inventory inventory = Bukkit.createInventory(null, size, CloudEntityTitleConverter.convertString(CloudEntityHandler.getInstance().getConfig().getInventoryTitle(), serviceGroup));
        List<CloudService> services = CloudAPI.getInstance().getServiceManager().getAllServicesByGroup(serviceGroup).stream().filter(server -> server.getState().equals(ServiceState.ONLINE)).toList();
        for (int i = 0; i < (Math.min(services.size(), 53)); i++) {
            inventory.addItem(buildItemStack(services.get(i)));
        }


        GUIHandler.currentInventories.put(player.getUniqueId(), inventory);
        player.openInventory(inventory);
    }

    public ItemStack buildItemStack(CloudService cloudService) {
        final var itemStack = new ItemStack(
                (cloudService.getOnlineCount() > 0 ? Material.GREEN_CONCRETE : Material.LIME_CONCRETE),
                (cloudService.getOnlineCount() != 0 ? cloudService.getOnlineCount() : 1));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§8» §a" + cloudService.getName());
        itemMeta.getPersistentDataContainer().set(CloudEntityHandler.getInstance().getNamespacedKey(), PersistentDataType.STRING, cloudService.getName());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
