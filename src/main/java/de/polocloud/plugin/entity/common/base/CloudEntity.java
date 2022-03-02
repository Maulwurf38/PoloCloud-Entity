package de.polocloud.plugin.entity.common.base;

import de.polocloud.api.CloudAPI;
import de.polocloud.api.groups.ServiceGroup;
import de.polocloud.plugin.entity.common.CloudEntityHandler;
import de.polocloud.plugin.entity.common.base.info.CloudEntityInfo;
import de.polocloud.plugin.entity.config.ConfigPlaceholdersReplacer;
import lombok.Getter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;

@Getter
public class CloudEntity {

    private final CloudEntityInfo cloudEntityInfo;
    private LivingEntity spawnedEntity;
    private ArmorStand secondLine;

    public CloudEntity(CloudEntityInfo cloudEntityInfo) {
        this.cloudEntityInfo = cloudEntityInfo;
        spawn();
        CloudEntityHandler.getInstance().getEntities().add(this);
    }

    public void destroy() {
        this.spawnedEntity.remove();
        this.secondLine.remove();
    }

    public void spawn() {
        if (!LivingEntity.class.isAssignableFrom(cloudEntityInfo.getEntityType().getEntityClass())) {
            return;
        }

        this.secondLine = cloudEntityInfo.getSecondLineLocation().getWorld().spawn(this.cloudEntityInfo.getSecondLineLocation(), ArmorStand.class);
        this.secondLine.setSilent(true);
        this.secondLine.setFireTicks(0);
        this.secondLine.setAI(false);
        this.secondLine.setGravity(false);
        this.secondLine.setCustomNameVisible(!cloudEntityInfo.getSecondLine().equals(""));
        this.secondLine.setVisible(false);
        this.secondLine.setMarker(true);

        this.spawnedEntity = (LivingEntity) cloudEntityInfo.getLocation().getWorld().spawn(cloudEntityInfo.getLocation(), cloudEntityInfo.getEntityType().getEntityClass());
        this.spawnedEntity.setCustomNameVisible(true);
        ServiceGroup serviceGroup = CloudAPI.getInstance().getGroupManager().getServiceGroupByNameOrNull(this.cloudEntityInfo.getPossibleGroup());
        if (serviceGroup == null) {
            this.spawnedEntity.setCustomName("§cError §7(Group not found)");
            this.secondLine.setCustomName("§cError §7(Group not found)");
        } else {
            this.spawnedEntity.setCustomName(ConfigPlaceholdersReplacer.convertStringOfServiceGroup(this.cloudEntityInfo.getEntityTitle(), serviceGroup));
            this.secondLine.setCustomName(ConfigPlaceholdersReplacer.convertStringOfServiceGroup(this.cloudEntityInfo.getSecondLine(), serviceGroup));
        }
        this.spawnedEntity.setSilent(true);
        this.spawnedEntity.setAI(false);
        this.spawnedEntity.setFireTicks(0);

        if (this.spawnedEntity.getType().equals(EntityType.VILLAGER)) {
            Villager villager = getCastedEntity();
            if (villager != null) {
                villager.setProfession(Villager.Profession.CLERIC);
                villager.setVillagerType(Villager.Type.TAIGA);
            }
        }
    }

    public void update() {
        if (!this.spawnedEntity.isDead()) {
            ServiceGroup serviceGroup = CloudAPI.getInstance().getGroupManager().getServiceGroupByNameOrNull(this.cloudEntityInfo.getPossibleGroup());
            if (serviceGroup == null) {
                return;
            }
            this.spawnedEntity.setCustomName(ConfigPlaceholdersReplacer.convertStringOfServiceGroup(this.cloudEntityInfo.getEntityTitle(), serviceGroup));
            this.secondLine.setCustomName(ConfigPlaceholdersReplacer.convertStringOfServiceGroup(this.cloudEntityInfo.getSecondLine(), serviceGroup));
        }
    }

    public <T> T getCastedEntity() {
        try {
            return (T) this.spawnedEntity;
        } catch (ClassCastException exception) {
            return null;
        }
    }

}
