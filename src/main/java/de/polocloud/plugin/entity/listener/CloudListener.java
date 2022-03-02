package de.polocloud.plugin.entity.listener;

import de.polocloud.api.CloudAPI;
import de.polocloud.api.event.player.CloudPlayerDisconnectEvent;
import de.polocloud.api.event.player.CloudPlayerLoginEvent;
import de.polocloud.api.event.player.CloudPlayerUpdateEvent;
import de.polocloud.plugin.entity.common.CloudEntityHandler;
import de.polocloud.plugin.entity.common.base.CloudEntity;

import java.util.List;

public class CloudListener {

    public CloudListener() {
        CloudAPI.getInstance().getEventHandler().registerEvent(CloudPlayerLoginEvent.class, cloudEvent -> {
            if (cloudEvent.getPlayer().getServer() != null) {
                List<CloudEntity> toUpdate = CloudEntityHandler.getInstance().getEntities().stream().filter(entity -> entity.getCloudEntityInfo().getPossibleGroup().equalsIgnoreCase(cloudEvent.getPlayer().getServer().getGroup().getName())).toList();
                for (CloudEntity cloudEntity : toUpdate) {
                    cloudEntity.update();
                }
            }
        });

        CloudAPI.getInstance().getEventHandler().registerEvent(CloudPlayerUpdateEvent.class, cloudEvent -> {
            if (cloudEvent.getUpdateReason().equals(CloudPlayerUpdateEvent.UpdateReason.SERVER_SWITCH)) {
                if (cloudEvent.getPlayer().getServer() != null) {
                    List<CloudEntity> toUpdate = CloudEntityHandler.getInstance().getEntities().stream().filter(entity -> entity.getCloudEntityInfo().getPossibleGroup().equalsIgnoreCase(cloudEvent.getPlayer().getServer().getGroup().getName())).toList();
                    for (CloudEntity cloudEntity : toUpdate) {
                        cloudEntity.update();
                    }
                }
            }
        });

        CloudAPI.getInstance().getEventHandler().registerEvent(CloudPlayerDisconnectEvent.class, cloudEvent -> {
            if (cloudEvent.getPlayer().getServer() != null) {
                List<CloudEntity> toUpdate = CloudEntityHandler.getInstance().getEntities().stream().filter(entity -> entity.getCloudEntityInfo().getPossibleGroup().equalsIgnoreCase(cloudEvent.getPlayer().getServer().getGroup().getName())).toList();
                for (CloudEntity cloudEntity : toUpdate) {
                    cloudEntity.update();
                }
            }
        });
    }
}
