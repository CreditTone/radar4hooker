/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gz.com.alibaba.fastjson.serializer;

import gz.com.alibaba.fastjson.serializer.JSONSerializer;
import gz.com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.regex.Pattern;

/**
 * @author wenshao<szujobs@hotmail.com>
 */
public class PatternSerializer implements ObjectSerializer {

    public final static PatternSerializer instance = new PatternSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        if (object == null) {
            serializer.writeNull();
            return;
        }
        
        Pattern p = (Pattern) object;
        serializer.write(p.pattern());
    }

}
