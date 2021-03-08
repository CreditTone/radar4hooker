package gz.radar.objects;

import android.view.View;
import gz.radar.AndroidApkField;
import gz.radar.ClassRadar;
import gz.radar.ClassRadar.RadarClassResult;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

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
    
    public AndroidApkField makeAndroidApkField(Field field, boolean fromExtends, boolean isStatic) throws IllegalArgumentException, IllegalAccessException {
    	field.setAccessible(true);
    	Object fieldObject = field.get(obj);
        String fieldName = field.getName();
        boolean isView = fieldObject != null && (fieldObject instanceof View);
        int viewId = 0;
        if (isView) {
            viewId = ((View)fieldObject).getId();
        }
        String objectId = ObjectsStore.storeObject(fieldObject);
        Class fieldType = fieldObject!= null ? fieldObject.getClass():field.getType();
        return new AndroidApkField(fieldName, fieldType, isView, viewId, fieldObject, objectId, isStatic).fromExtends(fromExtends);
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
            fields[fields.length - 1] = androidApkField;
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
			boolean isStatic = Modifier.isStatic(field.getModifiers());
			androidApkFields.add(makeAndroidApkField(field, false, isStatic));
		}
    	Class<?> objSuperClass = objClass.getSuperclass();
    	if (!objClass.equals(Object.class) && !objSuperClass.equals(Object.class)) {
    		Field[] superClassFields = objSuperClass.getFields();
    		for (int i = 0; superClassFields != null && i < superClassFields.length; i++) {
    			Field field = superClassFields[i];
    			boolean isStatic = Modifier.isStatic(field.getModifiers());
    			androidApkFields.add(makeAndroidApkField(field, true, isStatic));
			}
    		Field[] superClassDeclaredFields = objSuperClass.getDeclaredFields();
    		for (int i = 0;superClassDeclaredFields != null && i < superClassDeclaredFields.length; i++) {
    			Field field = superClassDeclaredFields[i];
    			boolean isStatic = Modifier.isStatic(field.getModifiers());
    			if (Modifier.isProtected(field.getModifiers())) {
    				androidApkFields.add(makeAndroidApkField(field, true, isStatic));
    			}
    		}
    	}
    	AndroidApkField[] androidApkFields2 = new AndroidApkField[androidApkFields.size()];
    	this.androidApkFields = androidApkFields.toArray(androidApkFields2);
    }
}
