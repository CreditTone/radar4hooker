package gz.com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.regex.Pattern;

import gz.com.alibaba.fastjson.parser.DefaultJSONParser;
import gz.com.alibaba.fastjson.parser.JSONToken;
import gz.com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

public class PatternDeserializer implements ObjectDeserializer {
    public final static PatternDeserializer instance = new PatternDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        Object value = parser.parse();

        if (value == null) {
            return null;
        }
        
        String pattern = (String) value;
        
        return (T) Pattern.compile(pattern);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
