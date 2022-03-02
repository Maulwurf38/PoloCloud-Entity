package de.polocloud.plugin.entity.config;

import com.google.common.collect.Maps;
import de.polocloud.api.CloudAPI;
import de.polocloud.api.groups.ServiceGroup;
import de.polocloud.api.service.CloudService;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

public class ConfigPlaceholdersReplacer {

    private static final Map<String, ReplaceObject> replaceObjectMap = Maps.newConcurrentMap();

    static {
        replaceObjectMap.put("{GROUP}", new ReplaceObject("ServiceGroup", "getName"));
        replaceObjectMap.put("{SERVER}", new ReplaceObject("CloudService", "getName"));
        replaceObjectMap.put("{ONLINE}", new ReplaceObject("CloudService", "getOnlineCount"));
        replaceObjectMap.put("{MAX}", new ReplaceObject("CloudService", "getMaxPlayers"));
        replaceObjectMap.put("{MOTD}", new ReplaceObject("CloudService", "getMotd"));

        replaceObjectMap.put("{HIGHEST_SERVICE_NAME}", new ReplaceObject("CloudService", "getName"));
        replaceObjectMap.put("{HIGHEST_SERVICE_ONLINE}", new ReplaceObject("CloudService", "getOnlineCount"));
        replaceObjectMap.put("{HIGHEST_SERVICE_MAX}", new ReplaceObject("CloudService", "getMaxPlayers"));
        replaceObjectMap.put("{HIGHEST_SERVICE_MOTD}", new ReplaceObject("CloudService", "getMotd"));
    }

    public static String convertStringOfServiceGroup(String toConvert, ServiceGroup serviceGroup) {
        String converted = convertString(toConvert, serviceGroup);
        int players = 0;
        for (CloudService cloudService : CloudAPI.getInstance().getServiceManager().getAllServicesByGroup(serviceGroup)) {
            players += cloudService.getOnlineCount();
        }
        converted = converted.replace("{PLAYERS}", String.valueOf(players));
        CloudService cloudService = getServiceWithHighestPlayers(serviceGroup);
        if (cloudService != null) {
            converted = convertString(converted, cloudService);
        }
        return converted;
    }

    public static String convertStringOfCloudService(String toConvert, CloudService cloudService) {
        String converted = convertString(toConvert, cloudService);
        return convertStringOfServiceGroup(converted, cloudService.getGroup());
    }

    public static String convertString(String toConvert, Object obj) {
        String converted = toConvert;
        for (String s : replaceObjectMap.keySet()) {
            String invoked = replaceObjectMap.get(s).invokeMethod(obj);
            converted = converted.replace(s, invoked == null ? "none" : invoked);
        }
        return converted;
    }

    @Nullable
    public static CloudService getServiceWithHighestPlayers(ServiceGroup serviceGroup) {
        return CloudAPI.getInstance().getServiceManager().getAllServicesByGroup(serviceGroup).stream().max(Comparator.comparing(CloudService::getMaxPlayers)).orElse(null);
    }

    public record ReplaceObject(String object, String method) {
        public String invokeMethod(Object obj) {
            if (Arrays.stream(obj.getClass().getInterfaces()).noneMatch(inter -> inter.getSimpleName().equalsIgnoreCase(object))) {
                return null;
            }

            try {
                return String.valueOf(obj.getClass().getMethod(method).invoke(obj));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
