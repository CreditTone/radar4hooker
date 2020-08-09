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
        List<AndroidApkField> fields = new ArrayList<>();
        Class<?> objClass = obj.getClass();
        name = objClass.getName();
        classResult = ClassRadar.discoverClass(name);
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        if (declaredFields == null) {
            androidApkFields = new AndroidApkField[0];
            return;
        }
        for (Field declaredField : declaredFields) {
            if (Modifier.isStatic(declaredField.getModifiers())) {
                continue;
            }
            declaredField.setAccessible(true);

            Object field = declaredField.get(obj);
            String fieldName = declaredField.getName();
            String fieldClass = declaredField.getType().getName();
            boolean isView = field != null && (field instanceof View);
            int viewId = 0;
            if (isView) {
                viewId = ((View)field).getId();
            }
            String objectId = calculatObjectId(field, fieldName);
            fields.add(new AndroidApkField(fieldName, fieldClass, isView, viewId, field, objectId));
        }
        androidApkFields = new AndroidApkField[fields.size()];
        androidApkFields = fields.toArray(androidApkFields);
    }

    public String calculatObjectId(Object field, String fieldName) {
        Object value = null;
        String objectId = null;
        if (field != null) {
            if (field instanceof Collection || field instanceof Map) {
                value = field;
            }else if (field instanceof TextView) {
                value = ((TextView)field).getText().toString();
            } else {
                value = field;
            }
            objectId = String.valueOf((Math.abs(field.hashCode() + fieldName.hashCode()) % 65535 + fieldName.hashCode() % 100));
            RadarProperties.cacheObject(obj.hashCode(), objectId, value);
        }
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
}
