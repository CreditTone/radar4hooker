package gz.com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.Map;

import gz.com.alibaba.fastjson.parser.DefaultJSONParser;
import gz.com.alibaba.fastjson.parser.JSONLexer;
import gz.com.alibaba.fastjson.parser.JSONToken;
import gz.com.alibaba.fastjson.parser.ParserConfig;
import gz.com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import gz.com.alibaba.fastjson.util.FieldInfo;
import gz.com.alibaba.fastjson.util.TypeUtils;

public class IntegerFieldDeserializer extends FieldDeserializer {

    public IntegerFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo){
        super(clazz, fieldInfo);
    }

    @Override
    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        Integer value;

        final JSONLexer lexer = parser.getLexer();
        if (lexer.token() == JSONToken.LITERAL_INT) {
            int val = lexer.intValue();
            lexer.nextToken(JSONToken.COMMA);
            if (object == null) {
                fieldValues.put(fieldInfo.getName(), val);
            } else {
                setValue(object, val);
            }
            return;
        } else if (lexer.token() == JSONToken.NULL) {
            value = null;
            lexer.nextToken(JSONToken.COMMA);
        } else {
            Object obj = parser.parse();

            value = TypeUtils.castToInt(obj);
        }

        if (value == null && getFieldClass() == int.class) {
            // skip
            return;
        }

        if (object == null) {
            fieldValues.put(fieldInfo.getName(), value);
        } else {
            setValue(object, value);
        }
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
