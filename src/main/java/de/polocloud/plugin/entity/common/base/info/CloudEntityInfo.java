package de.polocloud.plugin.entity.common.base.info;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

@Getter
public class CloudEntityInfo {

    private Location location;
    private String possibleGroup;
    private EntityType entityType;
    private String entityTitle = "§b{GROUP} §7| §e{PLAYERS} §7Players §aonline";
    private String secondLine = "";
    private Location secondLineLocation;

    public CloudEntityInfo(Location location, String possibleGroup, EntityType entityType, @Nullable String entityTitle, @Nullable String secondLine, @Nullable Location secondLineLocation) {
        this.location = location;
        this.possibleGroup = possibleGroup;
        this.entityType = entityType;
        if (entityTitle != null) {
            this.entityTitle = entityTitle;
        }
        if (secondLine != null) {
            this.secondLine = secondLine;
        }

        if (secondLineLocation == null) {
            this.secondLineLocation = location.clone().add(0, 1, 0);
        } else {
            this.secondLineLocation = secondLineLocation;
        }
    }

}
