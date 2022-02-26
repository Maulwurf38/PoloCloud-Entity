package de.polocloud.plugin.entity.common.base.info;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

@Getter
public class CloudEntityInfo {

    private Location location;
    private String possibleGroup;
    private EntityType entityType;
    private String entityTitle = "§b{GROUP} §7| §e{PLAYERS} §7Players §aonline";

    public CloudEntityInfo(Location location, String possibleGroup, EntityType entityType) {
        this.location = location;
        this.possibleGroup = possibleGroup;
        this.entityType = entityType;
    }

    public CloudEntityInfo(Location location, String possibleGroup, EntityType entityType, String entityTitle) {
        this.location = location;
        this.possibleGroup = possibleGroup;
        this.entityType = entityType;
        this.entityTitle = entityTitle;
    }
}
