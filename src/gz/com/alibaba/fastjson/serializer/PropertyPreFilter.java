package gz.com.alibaba.fastjson.serializer;

import gz.com.alibaba.fastjson.serializer.JSONSerializer;
import gz.com.alibaba.fastjson.serializer.SerializeFilter;

public interface PropertyPreFilter extends SerializeFilter {

    boolean apply(JSONSerializer serializer, Object source, String name);
}
