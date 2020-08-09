package gz.radar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RadarProperties {

    public static final String ObjectCacheKey = "RadarCacheObjectKey";

    public static final long ObjectCacheExpiredTime = 1000 * 60 * 60; //1小时

    //Object 的 hashCode 到 fields的映射
    private static Map<Integer,Map<String,Object>> getObjectCache() {
        if (!System.getProperties().containsKey(ObjectCacheKey)) {
            System.getProperties().put(ObjectCacheKey, new HashMap<Integer,Map<String,Object>>());
        }
        Map<Integer,Map<String,Object>> objectCache =
                (Map<Integer,Map<String,Object>>) System.getProperties().get(ObjectCacheKey);
        return objectCache;
    }

    public static void cacheObject(int hostHashcode,String objectId, Object obj) {
        Map<Integer,Map<String,Object>> objectCache = getObjectCache();
        Map<String,Object> currentObjectCacheLayer = objectCache.get(hostHashcode);
        if (currentObjectCacheLayer == null) {
            currentObjectCacheLayer = new HashMap<>();
            objectCache.put(hostHashcode, currentObjectCacheLayer);
        }
        currentObjectCacheLayer.put("opTime", System.currentTimeMillis());
        if (objectId != null && obj != null) {
            currentObjectCacheLayer.put(objectId, obj);
        }
        clearExpiredCache();
    }


    public static Object getCachedObject(String objectId) {
        Object obj = null;
        Map<Integer,Map<String,Object>> objectCache = getObjectCache();
        for (Map<String,Object> objectCacheLayer : objectCache.values()) {
            obj = objectCacheLayer.get(objectId);
            if (obj != null) {
                objectCacheLayer.put("opTime", System.currentTimeMillis());
                break;
            }
        }
        clearExpiredCache();
        return obj;
    }

    private static void clearExpiredCache() {
        Map<Integer,Map<String,Object>> objectCache = getObjectCache();
        Set<Integer> clearKey = new HashSet<>();
        for (Integer hostHashcode : objectCache.keySet()) {
            Map<String,Object> objectCacheLayer = objectCache.get(hostHashcode);
            long opTimeMillis = (long) objectCacheLayer.get("opTime");
            if ((System.currentTimeMillis() - opTimeMillis) > ObjectCacheExpiredTime) {
                clearKey.add(hostHashcode);
            }
        }
        for (Integer clearHostHashcode : clearKey) {
            objectCache.remove(clearHostHashcode);
        }
    }


}
