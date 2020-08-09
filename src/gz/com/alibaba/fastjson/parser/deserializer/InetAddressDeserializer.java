package gz.com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;

import gz.com.alibaba.fastjson.JSONException;
import gz.com.alibaba.fastjson.parser.DefaultJSONParser;
import gz.com.alibaba.fastjson.parser.JSONToken;
import gz.com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

public class InetAddressDeserializer implements ObjectDeserializer {

    public final static InetAddressDeserializer instance = new InetAddressDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {

        String host = (String) parser.parse();

        if (host == null) {
            return null;
        }
        
        if (host.length() == 0) {
            return null;
        }

        try {
            return (T) InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            throw new JSONException("deserialize error", e);
        }
    }
    
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }

}
