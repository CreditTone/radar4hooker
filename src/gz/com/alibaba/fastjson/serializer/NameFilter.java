package gz.com.alibaba.fastjson.serializer;

import gz.com.alibaba.fastjson.serializer.SerializeFilter;

public interface NameFilter extends SerializeFilter {
    String process(Object source, String name, Object value);
}
