package gz.com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

import gz.com.alibaba.fastjson.JSONException;
import gz.com.alibaba.fastjson.parser.DefaultJSONParser;
import gz.com.alibaba.fastjson.parser.JSONToken;
import gz.com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

public class URLDeserializer implements ObjectDeserializer {
    public final static URLDeserializer instance = new URLDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        
        String url = (String) parser.parse();
        
        if (url == null) {
            return null;
        }
        
        try {
            return (T) new URL(url);
        } catch (MalformedURLException e) {
            throw new JSONException("create url error", e);
        }
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
