package de.polocloud.plugin.entity.command.completer;

import com.google.common.collect.Lists;
import de.polocloud.api.CloudAPI;
import de.polocloud.plugin.entity.common.CloudEntityHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class CloudEntityCommandTabCompleter implements org.bukkit.command.TabCompleter {


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> answers = Lists.newArrayList();

        if (!(sender instanceof Player) || !sender.hasPermission(CloudEntityHandler.getInstance().getConfig().getEntityCloudCommandPermission())) {
            return answers;
        }
        if (args.length == 1) {
            answers.addAll(Arrays.asList("create", "remove"));
            return answers;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                for (EntityType value : EntityType.values()) {
                    if (value.getEntityClass() != null && LivingEntity.class.isAssignableFrom(value.getEntityClass())) {

                        answers.add(value.getName());
                    }
                }
                return answers;
            }
        } else if (args.length == 3) {
            CloudAPI.getInstance().getGroupManager().getAllCachedServiceGroups().stream().filter(serviceGroup -> !serviceGroup.getGameServerVersion().isProxy()).forEach(group -> answers.add(group.getName()));
            return answers;
        }

        return answers;
    }
}
