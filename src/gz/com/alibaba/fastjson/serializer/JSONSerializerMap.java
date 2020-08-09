package gz.com.alibaba.fastjson.serializer;


import gz.com.alibaba.fastjson.serializer.ObjectSerializer;
import gz.com.alibaba.fastjson.serializer.SerializeConfig;

@Deprecated
public class JSONSerializerMap extends SerializeConfig {
    public final boolean put(Class<?> clazz, ObjectSerializer serializer) {
        return super.put(clazz, serializer);
    }
}
