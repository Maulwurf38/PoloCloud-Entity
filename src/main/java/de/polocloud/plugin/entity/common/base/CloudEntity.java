package de.polocloud.plugin.entity.common.base;

import de.polocloud.api.CloudAPI;
import de.polocloud.api.groups.ServiceGroup;
import de.polocloud.plugin.entity.common.CloudEntityHandler;
import de.polocloud.plugin.entity.common.base.info.CloudEntityInfo;
import de.polocloud.plugin.entity.config.CloudEntityTitleConverter;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;

@Getter
public class CloudEntity {

    private final CloudEntityInfo cloudEntityInfo;
    private LivingEntity spawnedEntity;

    public CloudEntity(CloudEntityInfo cloudEntityInfo) {
        this.cloudEntityInfo = cloudEntityInfo;
        spawn();
        CloudEntityHandler.getInstance().getEntities().add(this);
    }

    public void destroy() {
        this.spawnedEntity.remove();
    }

    public void spawn() {
        if (!LivingEntity.class.isAssignableFrom(cloudEntityInfo.getEntityType().getEntityClass())) {
            return;
        }

        this.spawnedEntity = (LivingEntity) cloudEntityInfo.getLocation().getWorld().spawn(cloudEntityInfo.getLocation(), cloudEntityInfo.getEntityType().getEntityClass());
        this.spawnedEntity.setCustomNameVisible(true);
        ServiceGroup serviceGroup = CloudAPI.getInstance().getGroupManager().getServiceGroupByNameOrNull(this.cloudEntityInfo.getPossibleGroup());
        if (serviceGroup == null) {
            this.spawnedEntity.setCustomName("§cError §7(Group not found)");
        } else {
            this.spawnedEntity.setCustomName(CloudEntityTitleConverter.convertString(this.cloudEntityInfo, serviceGroup));
        }
        this.spawnedEntity.setSilent(true);
        this.spawnedEntity.setAI(false);

    }

    public void update() {
        if (!this.spawnedEntity.isDead()) {
            ServiceGroup serviceGroup = CloudAPI.getInstance().getGroupManager().getServiceGroupByNameOrNull(this.cloudEntityInfo.getPossibleGroup());
            if (serviceGroup == null) {
                return;
            }
            this.spawnedEntity.setCustomName(CloudEntityTitleConverter.convertString(this.cloudEntityInfo, serviceGroup));
        }
    }

}
