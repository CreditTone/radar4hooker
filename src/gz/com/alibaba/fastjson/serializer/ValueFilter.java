package gz.com.alibaba.fastjson.serializer;

import gz.com.alibaba.fastjson.serializer.SerializeFilter;

public interface ValueFilter extends SerializeFilter {

    Object process(Object source, String name, Object value);
}
