package de.polocloud.plugin.entity.config;

import com.google.common.collect.Lists;
import de.polocloud.plugin.entity.common.base.info.CloudEntityInfo;
import lombok.Getter;

import java.util.List;

@Getter
public class EntitiesConfig {

    private List<CloudEntityInfo> cloudEntities = Lists.newArrayList();
    private int inventorySize = 54;
    private String entityCloudCommandPermission = "cloud.plugin.entity.command";
    private String inventoryTitle = "§b{GROUP}";

    private String itemName = "§8» §a{SERVER}";
    private String[] itemLore = new String[]{"", "§7Players §8» §b{ONLINE}§7/§b{MAX}", "§7Motd §8» §b{MOTD}", ""};

}
