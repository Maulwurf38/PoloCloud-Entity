package de.polocloud.plugin.entity.config;

import de.polocloud.api.CloudAPI;
import de.polocloud.api.groups.ServiceGroup;
import de.polocloud.api.service.CloudService;
import de.polocloud.plugin.entity.common.base.info.CloudEntityInfo;

public class CloudEntityTitleConverter {

    public static String convertString(CloudEntityInfo cloudEntityInfo, ServiceGroup serviceGroup) {
        return convertString(cloudEntityInfo.getEntityTitle(), serviceGroup);
    }

    public static String convertString(String toConvert, ServiceGroup serviceGroup) {
        int players = 0;
        for (CloudService cloudService : CloudAPI.getInstance().getServiceManager().getAllServicesByGroup(serviceGroup)) {
            players += cloudService.getOnlineCount();
        }
        return toConvert.replace("{GROUP}", serviceGroup.getName()).replace("{PLAYERS}", String.valueOf(players));
    }

}
