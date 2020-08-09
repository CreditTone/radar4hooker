package gz.com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;

import gz.com.alibaba.fastjson.parser.DefaultJSONParser;

public interface ObjectDeserializer {
    <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName);
    
    int getFastMatchToken();
}
