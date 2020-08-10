package gz.radar;

import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import gz.radar.ClassRadar.RadarClassResult;

public class ObjectInfo {

    protected Object obj;

    protected String name;

    protected RadarClassResult classResult;

    protected AndroidApkField[] androidApkFields;

    public ObjectInfo(Object obj) throws IllegalAccessException {
        this.obj = obj;
        Class<?> objClass = obj.getClass();
        name = objClass.getName();
        classResult = ClassRadar.discoverClass(name);
        initAndroidApkFields();
    }
    
    public AndroidApkField makeAndroidApkField(Field field, boolean fromExtends) throws IllegalArgumentException, IllegalAccessException {
    	field.setAccessible(true);
    	Object fieldObject = field.get(obj);
        String fieldName = field.getName();
        boolean isView = fieldObject != null && (fieldObject instanceof View);
        int viewId = 0;
        if (isView) {
            viewId = ((View)fieldObject).getId();
        }
        String objectId = calculatObjectId(field, fieldName);
        Class fieldType = fieldObject!= null ? fieldObject.getClass():field.getType();
        return new AndroidApkField(fieldName, fieldType, isView, viewId, fieldObject, objectId).fromExtends(fromExtends);
    }

    public String calculatObjectId(Object field, String fieldName) {
    	if (field == null 
    			|| field instanceof String 
    			|| field instanceof Integer 
    			|| field instanceof java.lang.Long 
    			|| field instanceof java.lang.Double 
    			|| field instanceof java.lang.Boolean
    			|| field instanceof java.lang.Character
    			|| field instanceof java.lang.Short
    			|| field instanceof java.lang.Float
    			|| field instanceof java.lang.Byte) {
    		return null;
    	}
        Object value = null;
        String objectId = null;
        if (field instanceof Collection || field instanceof Map) {
            value = field;
        }else if (field instanceof TextView) {
            value = ((TextView)field).getText().toString();
        } else {
            value = field;
        }
        objectId = String.valueOf((Math.abs(field.hashCode() + fieldName.hashCode()) % 65535 + fieldName.hashCode() % 100));
        RadarProperties.cacheObject(obj.hashCode(), objectId, value);
        return objectId;
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RadarClassResult getClassResult() {
        return classResult;
    }

    public void setClassResult(RadarClassResult classResult) {
        this.classResult = classResult;
    }

    public String getClazz() {
        return getName();
    }

    public String getSuperClazz() {
        return this.classResult.superClassName;
    }

    public String getImplementInterfaces() {
        String implementToString = "";
        for (int i = 0; i < this.classResult.implementsInterfaces.length; i++) {
            implementToString += this.classResult.implementsInterfaces[i];
            if (i < (this.classResult.implementsInterfaces.length - 1)) {
                implementToString += ",";
            }
        }
        return implementToString;
    }

    public AndroidApkField[] getAndroidApkFields() {
        return androidApkFields;
    }

    public void setAndroidApkFields(AndroidApkField[] androidApkFields) {
        this.androidApkFields = androidApkFields;
    }

    public void addAndroidApkField(AndroidApkField androidApkField) {
        if (androidApkField != null) {
            AndroidApkField[] fields = new AndroidApkField[androidApkFields.length +1];
            for (int i = 0; i < androidApkFields.length; i ++) {
                fields[i] = androidApkFields[i];
            }
            fields[androidApkFields.length - 1] = androidApkField;
            this.androidApkFields = fields;
        }
    }

    public String[] methods() {
        List<String> methods = new ArrayList<>();
        String replaceClassName = name + ".";
        for (ClassRadar.RadarConstructorMethod radarMethod : classResult.constructorMethods) {
            if (radarMethod.isLocal) {
                methods.add(radarMethod.describe.replace(replaceClassName, ""));
            }
        }
        for (ClassRadar.RadarMethod radarMethod : classResult.methods) {
            if (radarMethod.isLocal) {
                methods.add(radarMethod.describe.replace(replaceClassName, ""));
            }
        }
        String[] returnArray = new String[methods.size()];
        methods.toArray(returnArray);
        return returnArray;
    }
    
    private void initAndroidApkFields() throws IllegalArgumentException, IllegalAccessException {
    	List<AndroidApkField> androidApkFields = new ArrayList<AndroidApkField>();
    	Class<?> objClass = obj.getClass();
    	Field[] declaredFields = objClass.getDeclaredFields();
    	for (int i = 0;declaredFields != null && i < declaredFields.length; i++) {
    		Field field = declaredFields[i];
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			androidApkFields.add(makeAndroidApkField(field, false));
		}
    	Class<?> objSuperClass = objClass.getSuperclass();
    	if (!objClass.equals(Object.class) && !objSuperClass.equals(Object.class)) {
    		Field[] superClassFields = objSuperClass.getFields();
    		for (int i = 0; superClassFields != null && i < superClassFields.length; i++) {
    			Field field = superClassFields[i];
    			if (Modifier.isStatic(field.getModifiers())) {
    				continue;
    			}
    			androidApkFields.add(makeAndroidApkField(field, true));
			}
    		Field[] superClassDeclaredFields = objSuperClass.getDeclaredFields();
    		for (int i = 0;superClassDeclaredFields != null && i < superClassDeclaredFields.length; i++) {
    			Field field = superClassDeclaredFields[i];
    			if (Modifier.isStatic(field.getModifiers())) {
    				continue;
    			}
    			if (Modifier.isProtected(field.getModifiers())) {
    				androidApkFields.add(makeAndroidApkField(field, true));
    			}
    		}
    	}
    	AndroidApkField[] androidApkFields2 = new AndroidApkField[androidApkFields.size()];
    	this.androidApkFields = androidApkFields.toArray(androidApkFields2);
    }
}
