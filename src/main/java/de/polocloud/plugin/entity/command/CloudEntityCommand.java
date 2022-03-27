package de.polocloud.plugin.entity.command;

import de.polocloud.api.CloudAPI;
import de.polocloud.api.groups.ServiceGroup;
import de.polocloud.api.groups.utils.ServiceType;
import de.polocloud.plugin.entity.common.CloudEntityHandler;
import de.polocloud.plugin.entity.common.base.CloudEntity;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class CloudEntityCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command is only usable as a player.");
            return false;
        }

        if (!player.hasPermission(CloudEntityHandler.getInstance().getConfig().getEntityCloudCommandPermission())) {
            player.sendMessage("§cYou don't have the permission for that.");
            return false;
        }
        if (args.length == 1) {
            if (args[0].equals("remove")) {
                player.sendMessage("§7Removing...");
                List<Entity> entities = player.getNearbyEntities(0.5, 0.5, 0.5);
                if (entities.isEmpty()) {
                    player.sendMessage("§cThere is no CloudEntity on your location to remove!");
                    return false;
                }
                CloudEntity cloudEntity = null;
                for (Entity entity : entities) {
                    CloudEntity cloudEntityTemp = CloudEntityHandler.getInstance().getCloudEntityOfEntity(entity);
                    if (cloudEntityTemp != null) {
                        cloudEntity = cloudEntityTemp;
                        break;
                    }
                }

                if (cloudEntity == null) {
                    player.sendMessage("§cThere is no CloudEntity on your location to remove!");
                    return false;
                }
                CloudEntityHandler.getInstance().removeEntity(cloudEntity);
                player.sendMessage("§cRemoved!");
                return false;
            }
        } else if (args.length == 3) {
            if (args[0].equals("create")) {
                player.sendMessage("§7Creating...");
                if (CloudEntityHandler.getInstance().getCloudEntityOfLocation(player.getLocation()) != null) {
                    player.sendMessage("§cThere is already a CloudEntity on your location!");
                    return false;
                }
                String entityType_RAW = args[1];
                EntityType entityType = entityTypeOfName(entityType_RAW);
                if (entityType == null) {
                    player.sendMessage("§cThis entity type doesn't exist!");
                    return false;
                }
                String group_RAW = args[2];
                ServiceGroup serviceGroup = CloudAPI.getInstance().getGroupManager().getServiceGroupByNameOrNull(group_RAW);
                if (serviceGroup == null) {
                    player.sendMessage("§cThis cloudgroup doesn't exist!");
                    return false;
                }
                if (!serviceGroup.getGameServerVersion().isProxy()) {
                    player.sendMessage("§cThis cloudgroup is a proxy group!");
                    return false;
                }

                CloudEntityHandler.getInstance().createCloudEntity(player.getLocation(), entityType, serviceGroup, null, null, null);
                player.sendMessage("§aCreated!");
                return false;
            }
        } else if (args.length >= 4) {
            if (args[0].equals("create")) {
                String entityType_RAW = args[1];
                EntityType entityType = entityTypeOfName(entityType_RAW);
                if (entityType == null) {
                    player.sendMessage("§cThis entity type doesn't exist!");
                    return false;
                }
                String group_RAW = args[2];
                ServiceGroup serviceGroup = CloudAPI.getInstance().getGroupManager().getServiceGroupByNameOrNull(group_RAW);
                if (serviceGroup == null) {
                    player.sendMessage("§cThis cloudgroup doesn't exist!");
                    return false;
                }
                if (!serviceGroup.getGameServerVersion().isProxy()) {
                    player.sendMessage("§cThis cloudgroup is a proxy group!");
                    return false;
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    if (i < 3) {
                        continue;
                    }
                    stringBuilder.append(ChatColor.translateAlternateColorCodes('&', args[i])).append(" ");
                }

                CloudEntityHandler.getInstance().createCloudEntity(player.getLocation(), entityType, serviceGroup, stringBuilder.toString(), null, null);
                return false;
            }
        }

        player.sendMessage("""
                §7Help §bCloudEntities
                  §7-> §bcreate <EntityType> <Group>§7, creates a CloudEntity with a specific Type and a CloudGroup (on your Location)
                  §7-> §bcreate <EntityType> <Group> <Name>§7, creates a CloudEntity with a specific Type, CloudGroup and a CustomName (on your Location)
                  §7-> §bremove§7, removes the CloudEntity near your Location""");

        return false;
    }

    public EntityType entityTypeOfName(String name) {
        return Arrays.stream(EntityType.values()).filter(type -> type.getName() != null && type.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
