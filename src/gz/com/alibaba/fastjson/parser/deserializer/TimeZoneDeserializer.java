package gz.com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.TimeZone;

import gz.com.alibaba.fastjson.parser.DefaultJSONParser;
import gz.com.alibaba.fastjson.parser.JSONToken;
import gz.com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

public class TimeZoneDeserializer implements ObjectDeserializer {
    public final static TimeZoneDeserializer instance = new TimeZoneDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        
        String id = (String) parser.parse();
        
        if (id == null) {
            return null;
        }
        
        return (T) TimeZone.getTimeZone(id);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
