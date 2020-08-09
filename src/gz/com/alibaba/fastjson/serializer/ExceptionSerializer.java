package gz.com.alibaba.fastjson.serializer;

import gz.com.alibaba.fastjson.serializer.JSONSerializer;
import gz.com.alibaba.fastjson.serializer.JavaBeanSerializer;

import java.lang.reflect.Type;

public class ExceptionSerializer extends JavaBeanSerializer {

    public ExceptionSerializer(Class<?> clazz){
        super(clazz);
    }

    protected boolean isWriteClassName(JSONSerializer serializer, Object obj, Type fieldType, Object fieldName) {
        return true;
    }
}
