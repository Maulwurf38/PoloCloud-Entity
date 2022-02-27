package de.polocloud.plugin.entity.bootstrap;

import de.polocloud.plugin.entity.command.CloudEntityCommand;
import de.polocloud.plugin.entity.command.completer.CloudEntityCommandTabCompleter;
import de.polocloud.plugin.entity.common.CloudEntityHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class CloudEntityBootstrap extends JavaPlugin {

    private CloudEntityHandler cloudEntityHandler;

    @Override
    public void onEnable() {
        this.cloudEntityHandler = new CloudEntityHandler(this);

        getCommand("cloudentity").setExecutor(new CloudEntityCommand());
        getCommand("cloudentity").setAliases(Arrays.asList("cloudentites", "entities", "entity"));
        getCommand("cloudentity").setTabCompleter(new CloudEntityCommandTabCompleter());
    }

    @Override
    public void onDisable() {
        cloudEntityHandler.callShutdown();
    }
}
