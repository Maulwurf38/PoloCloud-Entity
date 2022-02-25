package de.polocloud.plugin.entity.common.gui;

import com.google.common.collect.Maps;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.UUID;

public class GUIHandler {

    public static Map<UUID, Inventory> currentInventories = Maps.newConcurrentMap();

}
