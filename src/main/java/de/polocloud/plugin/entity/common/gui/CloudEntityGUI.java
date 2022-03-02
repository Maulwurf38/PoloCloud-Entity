package de.polocloud.plugin.entity.common.gui;

import de.polocloud.api.CloudAPI;
import de.polocloud.api.groups.ServiceGroup;
import de.polocloud.api.service.CloudService;
import de.polocloud.api.service.ServiceState;
import de.polocloud.plugin.entity.common.CloudEntityHandler;
import de.polocloud.plugin.entity.common.base.CloudEntity;
import de.polocloud.plugin.entity.common.gui.type.CloudEntityGUIBlockType;
import de.polocloud.plugin.entity.config.ConfigPlaceholdersReplacer;
import de.polocloud.plugin.entity.event.CloudEntityInventoryOpenEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
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

        Inventory inventory = Bukkit.createInventory(null, size, ConfigPlaceholdersReplacer.convertStringOfServiceGroup(CloudEntityHandler.getInstance().getConfig().getInventoryTitle(), serviceGroup));
        List<CloudService> services = CloudAPI.getInstance().getServiceManager().getAllServicesByGroup(serviceGroup).stream().filter(server -> server.getState().equals(ServiceState.ONLINE)).toList();
        for (int i = 0; i < (Math.min(services.size(), size - 1)); i++) {
            inventory.addItem(buildItemStack(services.get(i)));
        }

        CloudEntityInventoryOpenEvent event = new CloudEntityInventoryOpenEvent(player, cloudEntity, serviceGroup, services, inventory);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCanceled()) {
            return;
        }
        inventory = event.getInventory();

        GUIHandler.currentInventories.put(player.getUniqueId(), inventory);
        player.openInventory(inventory);
    }

    public ItemStack buildItemStack(CloudService cloudService) {
        Material material;
        if (cloudService.getGroup().isMaintenance()) {
            material = CloudEntityHandler.getInstance().getConfig().getInventory_item_materials().getOrDefault(CloudEntityGUIBlockType.MAINTENANCE, Material.YELLOW_CONCRETE);
        } else if (cloudService.isFull()) {
            material = CloudEntityHandler.getInstance().getConfig().getInventory_item_materials().getOrDefault(CloudEntityGUIBlockType.FULL, Material.RED_CONCRETE);
        } else {
            material = (cloudService.getOnlineCount() > 0 ? CloudEntityHandler.getInstance().getConfig().getInventory_item_materials().getOrDefault(CloudEntityGUIBlockType.PLAYERS, Material.GREEN_CONCRETE) : CloudEntityHandler.getInstance().getConfig().getInventory_item_materials().getOrDefault(CloudEntityGUIBlockType.NO_PLAYER, Material.LIME_CONCRETE));
        }

        final var itemStack = new ItemStack(
                material,
                (cloudService.getOnlineCount() != 0 ? Math.min(64, cloudService.getOnlineCount()) : 1));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ConfigPlaceholdersReplacer.convertStringOfCloudService(CloudEntityHandler.getInstance().getConfig().getItemName(), cloudService));
        List<String> lore = new ArrayList<>();
        for (String s : CloudEntityHandler.getInstance().getConfig().getItemLore()) {
            lore.add(ConfigPlaceholdersReplacer.convertStringOfCloudService(s, cloudService));
        }
        itemMeta.setLore(lore);
        itemMeta.getPersistentDataContainer().set(CloudEntityHandler.getInstance().getNamespacedKey(), PersistentDataType.STRING, cloudService.getName());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
