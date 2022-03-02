package de.polocloud.plugin.entity.config;

import com.google.common.collect.Lists;
import de.polocloud.plugin.entity.common.base.info.CloudEntityInfo;
import de.polocloud.plugin.entity.common.gui.type.CloudEntityGUIBlockType;
import lombok.Getter;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class EntitiesConfig {

    private List<CloudEntityInfo> cloudEntities = Lists.newArrayList();
    private int inventorySize = 54;
    private String entityCloudCommandPermission = "cloud.plugin.entity.command";
    private String inventoryTitle = "§b{GROUP}";
    private String itemName = "§8» §a{SERVER}";
    private String[] itemLore = new String[]{"", "§7Players §8» §b{ONLINE}§7/§b{MAX}", "§7Motd §8» §b{MOTD}", ""};
    private Map<CloudEntityGUIBlockType, Material> inventory_item_materials = new HashMap<>() {{
        put(CloudEntityGUIBlockType.FULL, Material.RED_CONCRETE);
        put(CloudEntityGUIBlockType.NO_PLAYER, Material.LIME_CONCRETE);
        put(CloudEntityGUIBlockType.PLAYERS, Material.GREEN_CONCRETE);
        put(CloudEntityGUIBlockType.MAINTENANCE, Material.YELLOW_CONCRETE);
    }};

}
