package gz.com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

import gz.com.alibaba.fastjson.parser.DefaultJSONParser;
import gz.com.alibaba.fastjson.parser.JSONToken;
import gz.com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

public class CharsetDeserializer implements ObjectDeserializer {
    public final static CharsetDeserializer instance = new CharsetDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        Object value = parser.parse();

        if (value == null) {
            return null;
        }
        
        String charset = (String) value;
        
        return (T) Charset.forName(charset);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
