package gz.radar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Map;

public class  AndroidApkField {

    private String fieldName;
    private String fieldClass;
    private boolean isView;
    private int viewId;
    private Object value;
    private String objectId;

    public AndroidApkField(String fieldName, String fieldClass, boolean isView, int viewId, Object value, String objectId) {
        this.fieldName = fieldName;
        this.fieldClass = fieldClass;
        this.isView = isView;
        this.viewId = viewId;
        this.value = value;
        this.objectId = objectId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldClass() {
        return fieldClass;
    }

    public void setFieldClass(String fieldClass) {
        this.fieldClass = fieldClass;
    }

    public boolean isView() {
        return isView;
    }

    public void setView(boolean view) {
        isView = view;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    private String type() {
        if (!fieldClass.startsWith("java.lang.")) {
            return fieldClass;
        }
        if (fieldClass.equals("java.lang.Long")) {
            return "long";
        }else if (fieldClass.equals("java.lang.Integer")) {
            return "int";
        }else if (fieldClass.equals("java.lang.Double")) {
            return "double";
        }else if (fieldClass.equals("java.lang.Boolean")) {
            return "boolean";
        }else if (fieldClass.equals("java.lang.Character")) {
            return "char";
        }else if (fieldClass.equals("java.lang.Short")) {
            return "short";
        }else if (fieldClass.equals("java.lang.Float")) {
            return "float";
        }else if (fieldClass.equals("java.lang.Byte")) {
            return "byte";
        }
        return fieldClass;
    }

    public String toLine() {
        return toString();
    }

    public String toString() {
        String toString = "name:" + fieldName;
        String type = type();
        toString += "\ttype:" + type;
        if (isView) {
            toString += "\tviewId:" + viewId;
        }
        if (value != null && type.contains(".")) {
            toString += "\tobjectId:" + objectId;
        }
        if (value instanceof Collection) {
            Collection collection = (Collection) value;
            toString += "\tsize:" + collection.size();
        }else if (value instanceof Map) {
            Map map = (Map) value;
            toString += "\tsize:" + map.size();
        }
        toString += "\tvalue:" ;
        if (value != null) {
            if (value instanceof JSONObject || value instanceof JSONArray || value instanceof String || value instanceof Integer || value instanceof Long || value instanceof Float || value instanceof Double || value instanceof Boolean || value instanceof Short || value instanceof Byte || value instanceof Character) {
                toString += value;
            }else{
                toString +=  "hashcode(" + value.hashCode() + ")";
            }
        }else {
            toString += "null";
        }
        return toString;
    }
}
