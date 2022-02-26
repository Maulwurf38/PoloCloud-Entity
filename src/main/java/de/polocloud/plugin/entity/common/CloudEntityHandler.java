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
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

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
        for (CloudEntityInfo cloudEntityInfo : this.config.getCloudEntities()) {
            new CloudEntity(cloudEntityInfo);
        }
    }

    public void createCloudEntity(Location location, EntityType entityType, ServiceGroup serviceGroup, @Nullable String title) {
        CloudEntityInfo cloudEntityInfo;
        if (title == null) {
            cloudEntityInfo = new CloudEntityInfo(location, serviceGroup.getName(), entityType);
        } else {
            cloudEntityInfo = new CloudEntityInfo(location, serviceGroup.getName(), entityType, title);
        }

        this.config.getCloudEntities().add(cloudEntityInfo);
        new CloudEntity(cloudEntityInfo);
        saveConfig();
    }


    public void removeEntity(CloudEntity cloudEntity) {
        cloudEntity.destroy();
        this.entities.remove(cloudEntity);
        this.config.getCloudEntities().remove(cloudEntity.getCloudEntityInfo());
        saveConfig();
    }

    public void saveConfig() {
        File file = new File("plugins/cloudentities/", "config.json");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        new Document(this.config).write(file);
    }
}
