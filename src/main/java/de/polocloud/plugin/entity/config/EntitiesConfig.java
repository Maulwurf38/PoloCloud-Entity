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

}
