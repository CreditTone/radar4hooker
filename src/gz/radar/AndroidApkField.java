package gz.radar;

import org.json.JSONArray;
import org.json.JSONObject;


import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.Map;

public class  AndroidApkField {

    private String fieldName;
    private Class fieldClass;
    private boolean isView;
    private int viewId;
    private Object value;
    private String objectId;
    private boolean fromExtends;

    public AndroidApkField(String fieldName, Class fieldClass, boolean isView, int viewId, Object value, String objectId) {
        this.fieldName = fieldName;
        this.fieldClass = fieldClass;
        this.isView = isView;
        this.viewId = viewId;
        this.value = value;
        this.objectId = objectId;
    }

    public AndroidApkField fromExtends(boolean fromExtends) {
		this.fromExtends = fromExtends;
		return this;
	}

	public boolean isFromExtends() {
		return fromExtends;
	}

	public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
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
    	String fieldClassNameString = fieldClass.getName();
        if (!fieldClassNameString.startsWith("java.lang.")) {
            return fieldClassNameString;
        }
        if (fieldClassNameString.equals("java.lang.Long")) {
            return "long";
        }else if (fieldClassNameString.equals("java.lang.Integer")) {
            return "int";
        }else if (fieldClassNameString.equals("java.lang.Double")) {
            return "double";
        }else if (fieldClassNameString.equals("java.lang.Boolean")) {
            return "boolean";
        }else if (fieldClassNameString.equals("java.lang.Character")) {
            return "char";
        }else if (fieldClassNameString.equals("java.lang.Short")) {
            return "short";
        }else if (fieldClassNameString.equals("java.lang.Float")) {
            return "float";
        }else if (fieldClassNameString.equals("java.lang.Byte")) {
            return "byte";
        }
        TypeVariable[] typeVariables = fieldClass.getTypeParameters();
        if (typeVariables != null && typeVariables.length > 0) {
        	String resultString = fieldClassNameString +"<";
        	for (int i = 0; i < typeVariables.length; i++ ) {
        		TypeVariable typeVariable = typeVariables[i];
        		resultString += typeVariable.getName();
        		if (i < typeVariables.length - 1) {
        			resultString += ",";
        		}
        	}
        	resultString += ">";
        	return resultString;
        }
        return fieldClassNameString;
    }

    public String toLine() {
        return toString();
    }

    public String toString() {
        String toString = "name:" + fieldName;
        toString += "\tfromExtends:" + fromExtends;
        String type = type();
        toString += "\ttype:" + type;
        if (isView) {
            toString += "\tviewId:" + viewId;
        }
        if (objectId != null && value != null && type.contains(".")) {
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
                toString +=  "";//"hashcode(" + value.hashCode() + ")";
            }
        }else {
            toString += "null";
        }
        return toString;
    }
    
}
