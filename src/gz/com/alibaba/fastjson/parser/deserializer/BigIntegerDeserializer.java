package gz.com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.math.BigInteger;

import gz.com.alibaba.fastjson.parser.DefaultJSONParser;
import gz.com.alibaba.fastjson.parser.JSONLexer;
import gz.com.alibaba.fastjson.parser.JSONToken;
import gz.com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import gz.com.alibaba.fastjson.util.TypeUtils;

public class BigIntegerDeserializer implements ObjectDeserializer {

    public final static BigIntegerDeserializer instance = new BigIntegerDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        return (T) deserialze(parser);
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialze(DefaultJSONParser parser) {
        final JSONLexer lexer = parser.getLexer();
        if (lexer.token() == JSONToken.LITERAL_INT) {
            String val = lexer.numberString();
            lexer.nextToken(JSONToken.COMMA);
            return (T) new BigInteger(val);
        }

        Object value = parser.parse();

        if (value == null) {
            return null;
        }

        return (T) TypeUtils.castToBigInteger(value);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
