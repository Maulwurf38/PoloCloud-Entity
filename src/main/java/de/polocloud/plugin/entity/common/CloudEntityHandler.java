package de.polocloud.plugin.entity.common;

import com.google.common.collect.Lists;
import de.polocloud.api.groups.ServiceGroup;
import de.polocloud.plugin.entity.common.base.CloudEntity;
import de.polocloud.plugin.entity.common.base.info.CloudEntityInfo;
import de.polocloud.plugin.entity.config.EntitiesConfig;
import de.polocloud.plugin.entity.config.gson.Document;
import de.polocloud.plugin.entity.listener.CloudEntitiesListener;
import de.polocloud.plugin.entity.listener.CloudListener;
import de.polocloud.plugin.entity.listener.InventoryClickListener;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

@Getter
public class CloudEntityHandler {

    @Getter
    private static CloudEntityHandler instance;

    private List<CloudEntity> entities;

    private EntitiesConfig config;
    private NamespacedKey namespacedKey;
    private Plugin plugin;

    public CloudEntityHandler(Plugin plugin) {
        instance = this;
        this.plugin = plugin;
        this.namespacedKey = new NamespacedKey(plugin, "server");

        this.entities = Lists.newArrayList();

        File file = new File("plugins/cloudentities/", "config.json");
        if (file.exists()) {
            this.config = new Document(file).get(EntitiesConfig.class);
        } else {
            file.getParentFile().mkdirs();
            new Document(this.config = new EntitiesConfig()).write(file);
        }
        spawnEntities();

        new CloudEntitiesListener();
        new InventoryClickListener();
        new CloudListener();
    }

    public void callShutdown() {
        for (CloudEntity entity : this.entities) {
            entity.destroy();
        }
        this.entities.clear();
    }

    public void spawnEntities() {
        this.plugin.getLogger().log(Level.INFO, "Checking world difficulties...");
        for (World world : Bukkit.getWorlds()) {
            if (world.getDifficulty().equals(Difficulty.PEACEFUL)) {
                this.plugin.getLogger().log(Level.WARNING, "Difficulty in world (" + world.getName() + ") is §cPEACEFUL §7some CloudEntities may not spawn!");
            }
        }

        for (CloudEntityInfo cloudEntityInfo : this.config.getCloudEntities()) {
            new CloudEntity(cloudEntityInfo);
        }
    }

    public CloudEntity createCloudEntity(Location location, EntityType entityType, ServiceGroup serviceGroup, @Nullable String title) {
        CloudEntityInfo cloudEntityInfo;
        if (title == null) {
            cloudEntityInfo = new CloudEntityInfo(location, serviceGroup.getName(), entityType);
        } else {
            cloudEntityInfo = new CloudEntityInfo(location, serviceGroup.getName(), entityType, title);
        }

        this.config.getCloudEntities().add(cloudEntityInfo);
        CloudEntity cloudEntity = new CloudEntity(cloudEntityInfo);
        saveConfig();
        return cloudEntity;
    }


    public void removeEntity(CloudEntity cloudEntity) {
        cloudEntity.destroy();
        this.entities.remove(cloudEntity);
        this.config.getCloudEntities().remove(cloudEntity.getCloudEntityInfo());
        saveConfig();
    }

    @Nullable
    public CloudEntity getCloudEntityOfLocation(Location location) {
        return this.entities.stream().filter(entities -> entities.getSpawnedEntity().getLocation().equals(location)).findFirst().orElse(null);
    }

    @Nullable
    public CloudEntity getCloudEntityOfLocation0(Location location) {
        return this.entities.stream().filter(entities -> entities.getCloudEntityInfo().getLocation().equals(location)).findFirst().orElse(null);
    }

    @Nullable
    public CloudEntity getCloudEntityOfEntity(Entity entity) {
        return this.entities.stream().filter(entities -> entities.getSpawnedEntity().getEntityId() == entity.getEntityId()).findFirst().orElse(null);
    }

    public void saveConfig() {
        File file = new File("plugins/cloudentities/", "config.json");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        new Document(this.config).write(file);
    }
}
