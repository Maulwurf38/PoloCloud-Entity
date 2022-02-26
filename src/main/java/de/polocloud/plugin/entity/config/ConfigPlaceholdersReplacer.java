package de.polocloud.plugin.entity.config;

import com.google.common.collect.Maps;
import de.polocloud.api.CloudAPI;
import de.polocloud.api.groups.ServiceGroup;
import de.polocloud.api.service.CloudService;
import de.polocloud.plugin.entity.common.base.info.CloudEntityInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

public class ConfigPlaceholdersReplacer {

    private static Map<String, ReplaceObject> replaceObjectMap = Maps.newConcurrentMap();

    static {
        replaceObjectMap.put("{GROUP}", new ReplaceObject("ServiceGroup", "getName"));
        replaceObjectMap.put("{SERVER}", new ReplaceObject("CloudService", "getName"));
        replaceObjectMap.put("{ONLINE}", new ReplaceObject("CloudService", "getOnlineCount"));
        replaceObjectMap.put("{MAX}", new ReplaceObject("CloudService", "getMaxPlayers"));
        replaceObjectMap.put("{MOTD}", new ReplaceObject("CloudService", "getMotd"));
    }

    public static String convertString(CloudEntityInfo cloudEntityInfo, ServiceGroup serviceGroup) {
        String converted = convertString(cloudEntityInfo.getEntityTitle(), serviceGroup);
        int players = 0;
        for (CloudService cloudService : CloudAPI.getInstance().getServiceManager().getAllServicesByGroup(serviceGroup)) {
            players += cloudService.getOnlineCount();
        }
        return converted.replace("{PLAYERS}", String.valueOf(players));
    }

    public static String convertString(String toConvert, Object obj) {
        String converted = toConvert;
        for (String s : replaceObjectMap.keySet()) {
            String invoked = replaceObjectMap.get(s).invokeMethod(obj);
            if (invoked != null) {
                converted = converted.replace(s, invoked);
            }
        }
        return converted;
    }

    public record ReplaceObject(String object, String method) {
        public String invokeMethod(Object obj) {
            if (!Arrays.stream(obj.getClass().getInterfaces()).anyMatch(inter -> inter.getSimpleName().equalsIgnoreCase(object))) {
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
