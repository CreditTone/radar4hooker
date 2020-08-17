package gz.radar;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gz.com.alibaba.fastjson.JSONObject;
import gz.util.XLog;

public class ClassRadar {

    public static final int ANDROID_SDK_VERSION = android.os.Build.VERSION.SDK_INT;

    public static class RadarConstructorMethod {
        public String accessType;
        public int paramsNum;
        public String[] paramsClasses;
        public int throwsNum;
        public String[] throwsExceptions;
        public boolean isLocal;
        public boolean isOverWrite;
        public String describe;

        public RadarConstructorMethod(int modifier, String describe) {
            this.describe = describe;
            this.accessType = makeAccessType(modifier);
            String[] params = getMethodParams(describe);
            this.paramsNum = params.length;
            this.paramsClasses = params;
            String[] throwsExceptions = getThrowsExceptions(describe);
            this.throwsNum = throwsExceptions.length;
            this.throwsExceptions = throwsExceptions;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RadarConstructorMethod that = (RadarConstructorMethod) o;

            if (paramsNum != that.paramsNum) return false;
            // Probably incorrect - comparing Object[] arrays with Arrays.equals
            return Arrays.equals(paramsClasses, that.paramsClasses);
        }

        @Override
        public int hashCode() {
            int result = paramsNum;
            result = 31 * result + Arrays.hashCode(paramsClasses);
            return result;
        }
    }

    public static class RadarMethod extends RadarConstructorMethod {
        public String methodName;
        public String returnClass;
        public boolean isNative;
        public boolean isStatic;
        public boolean isFinally;
        public boolean isAbstrat;

        public RadarMethod(int modifier, String describe) {
            super(modifier, describe);
            this.isNative = java.lang.reflect.Modifier.isNative(modifier);
            this.isStatic = java.lang.reflect.Modifier.isStatic(modifier);
            this.isFinally = java.lang.reflect.Modifier.isFinal(modifier);
            this.isAbstrat = java.lang.reflect.Modifier.isAbstract(modifier);
            this.methodName = getMethodName(describe);
            this.returnClass = getType(describe);
        }

        public boolean matchName(String testName) {
            if (testName.equals(methodName) || testName.equals("?")){
                return true;
            }
            Matcher matcher = Pattern.compile(testName).matcher(methodName);
            while (matcher.find()){
                if (methodName.equals(matcher.group())){
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            RadarMethod that = (RadarMethod) o;

            return methodName != null ? methodName.equals(that.methodName) : that.methodName == null;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
            return result;
        }
    }

    public static class RadarField {
        public java.lang.reflect.Field field;
        public String accessType;
        public String fieldClass;
        public String fieldName;
        public boolean isLocal;
        public boolean isStatic;
        public boolean isFinally;
        public String describe;

        public RadarField(java.lang.reflect.Field field) {
            this.field = field;
            this.describe = field.toString();
            this.accessType = makeAccessType(field.getModifiers());
            this.isStatic = java.lang.reflect.Modifier.isStatic(field.getModifiers());
            this.isFinally =java.lang.reflect.Modifier.isFinal(field.getModifiers());
            this.fieldName = field.getName();
            this.fieldClass = getType(describe);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RadarField that = (RadarField) o;

            if (fieldClass != null ? !fieldClass.equals(that.fieldClass) : that.fieldClass != null)
                return false;
            return fieldName != null ? fieldName.equals(that.fieldName) : that.fieldName == null;
        }

        @Override
        public int hashCode() {
            int result = fieldClass != null ? fieldClass.hashCode() : 0;
            result = 31 * result + (fieldName != null ? fieldName.hashCode() : 0);
            return result;
        }

    }

    public static class RadarClassResult {
        public String superClassName;
        public String[] implementsInterfaces;
        public String className;
        public RadarField[] fields;
        public RadarConstructorMethod[] constructorMethods;
        public RadarMethod[] methods;
        public boolean isStatic;
        public boolean isFinally;
        public boolean isEnum;
        public boolean isAbstract;
        public boolean isInterface;
        public String  accessType;

        public String describ() {
            String describ = accessType;
            if (!describ.isEmpty()) {
                describ += " ";
            }
            if (isInterface) {
                describ += "interface ";
            }else if (isEnum) {
                describ += "enum ";
            }else{
                describ += "class ";
            }
            describ += className;
            if (superClassName != null && !superClassName.isEmpty() && !superClassName.equals("java.lang.Object")) {
                describ += " extends " + superClassName;
            }
            if (isInterface && implementsInterfaces.length > 0) {
                describ += " extends " + implementsInterfaces[0];
            }else if (implementsInterfaces.length > 0){
                describ += " implements ";
                for (int i = 0; i < implementsInterfaces.length; i++) {
                    describ += implementsInterfaces[i];
                    if (i < implementsInterfaces.length - 1) {
                        describ += ",";
                    }
                }
            }
            return describ;
        }

        private void setFields(Collection<RadarField> fields) {
            this.fields = new RadarField[fields.size()];
            Iterator<RadarField> fiter =  fields.iterator();
            int i = 0;
            while (fiter.hasNext()) {
                this.fields[i] = fiter.next();
                i++;
            }
        }

        private void setRadarConstructorMethods(Collection<RadarConstructorMethod> radarConstructorMethods) {
            this.constructorMethods = new RadarConstructorMethod[radarConstructorMethods.size()];
            Iterator<RadarConstructorMethod> fiter =  radarConstructorMethods.iterator();
            int i = 0;
            while (fiter.hasNext()) {
                this.constructorMethods[i] = fiter.next();
                i++;
            }
        }

        private void setRadarMethods(Collection<RadarMethod> radarMethods) {
            this.methods = new RadarMethod[radarMethods.size()];
            Iterator<RadarMethod> fiter = radarMethods.iterator();
            int i = 0;
            while (fiter.hasNext()) {
                this.methods[i] = fiter.next();
                i++;
            }
        }

        public boolean hasLocalNativeMethod() {
            for (RadarMethod radarMethod : methods) {
                if (radarMethod.isNative && radarMethod.isLocal) {
                    return true;
                }
            }
            return false;
        }

        public boolean containsMethod(String methodNameReg) {
            if (methodNameReg.isEmpty() || methodNameReg.equals("?") || methodNameReg.equals("_") || methodNameReg.equals(".*")) {
                return true;
            }
            Pattern regex = Pattern.compile(methodNameReg);
            for (RadarMethod radarMethod : methods) {
                if (regex.matcher(radarMethod.methodName).find()) {
                    return true;
                }
            }
            return false;
        }

    }
    public static RadarClassResult discoverObject(Object obj) {
        if (obj == null)
            return null;
        return discoverClass(obj.getClass().getName());
    }

    private static boolean lookingSuperClass(Class clz, String superClassName) {
        if (clz == null || clz.getName().equals("java.lang.Object")) {
            return false;
        }
        if (clz.getName().equals(superClassName)) {
            return true;
        }
        if (!clz.isInterface()) {
            Class<?> superClz = clz.getSuperclass();
            if (lookingSuperClass(superClz, superClassName)) {
                return true;
            }
        }

        Class<?>[] interfaces = clz.getInterfaces();
        for (int  i = 0; interfaces != null && i < interfaces.length;  i++) {
            if (lookingSuperClass(interfaces[i], superClassName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSubClass(String className, String superClassName) {
        if (!className.contains(".") || className.equals(superClassName)) {
            return false;
        }
        if ("java.lang.Object".equals(superClassName)) {
            return true;
        }
        Class<?> superClz = null;
        Class<?> clz = null;
        try{
            clz = Class.forName(className);
            superClz = Class.forName(superClassName);
            return clz.isInterface() == false && superClz.isAssignableFrom(clz);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        //return lookingSuperClass(clz, superClassName);
    }
    
    public static RadarClassResult discoverClass(String className) {
        try {
            if (className.startsWith("[")){
                return null;
            }
            Class<?> clz = Class.forName(className);
            RadarClassResult result = new RadarClassResult();
            result.accessType = makeAccessType(clz.getModifiers());
            result.isAbstract = java.lang.reflect.Modifier.isAbstract(clz.getModifiers());
            result.isStatic = java.lang.reflect.Modifier.isStatic(clz.getModifiers());
            result.isFinally = java.lang.reflect.Modifier.isFinal(clz.getModifiers());
            result.isEnum = clz.isEnum();
            result.isInterface = clz.isInterface();
            result.className = clz.getName();
            boolean isClass = !result.isEnum && !result.isInterface;
            if (isClass) {
                result.superClassName = clz.getSuperclass() != null?clz.getSuperclass().getName():"unkown";
            }else{
                result.superClassName = "";
            }
            List<String> implementsI = new ArrayList<>();
            if (!result.isEnum) {
                try {
                    Class<?>[] interfaces = clz.getInterfaces();
                    for (int i = 0; interfaces != null && i < interfaces.length; i++) {
                        implementsI.add(interfaces[i].getName());
                    }
                }catch (Exception e){}

            }
            result.implementsInterfaces = implementsI.toArray(new String[implementsI.size()]);

            Set<RadarField> radarFields = new HashSet<>();
            if (isClass) {
                java.lang.reflect.Field[] declaredFields = clz.getDeclaredFields();
                if (declaredFields != null){
                    for (int i = 0; i < declaredFields.length; i++) {
                        RadarField radarField = new RadarField(declaredFields[i]);
                        radarField.isLocal = true;
                        radarFields.add(radarField);
                    }
                }
                java.lang.reflect.Field[] fields = clz.getFields();
                if (fields != null){
                    for (int i = 0; i < fields.length; i++) {
                        RadarField radarField = new RadarField(fields[i]);
                        radarField.isLocal = false;
                        radarFields.add(radarField);
                    }
                }
            }
            result.setFields(radarFields);
            //构造方法
            Set<RadarConstructorMethod> radarConstructorMethods = new HashSet<RadarConstructorMethod>();
            if (isClass) {
                Constructor<?>[] constructors = clz.getDeclaredConstructors();
                if (constructors != null) {
                    for (int i = 0; i < constructors.length; i++) {
                        RadarConstructorMethod raderConstructorMethod = new RadarConstructorMethod(constructors[i].getModifiers(), constructors[i].toString());
                        raderConstructorMethod.isLocal = true;
                        radarConstructorMethods.add(raderConstructorMethod);
                    }
                }
                constructors = clz.getConstructors();
                if (constructors != null){
                    for (int i = 0; i < constructors.length; i++) {
                        RadarConstructorMethod raderConstructorMethod = new RadarConstructorMethod(constructors[i].getModifiers(), constructors[i].toString());
                        boolean needToSkip = false;
                        for (RadarConstructorMethod raderConstructorMethodAdded : radarConstructorMethods) {
                            if (raderConstructorMethodAdded.isLocal && raderConstructorMethodAdded.equals(raderConstructorMethod)) {
                                raderConstructorMethod.isOverWrite = true;
                                needToSkip = true;
                                break;
                            }
                        }
                        if (needToSkip) {
                            continue;
                        }
                        radarConstructorMethods.add(raderConstructorMethod);
                    }
                }
            }
            result.setRadarConstructorMethods(radarConstructorMethods);
            //方法
            Set<RadarMethod> radarMethods = new HashSet<RadarMethod>();
            if (isClass) {
                Method[] methods = clz.getDeclaredMethods();
                if (methods != null){
                    for (int i = 0; i < methods.length; i++) {
                        RadarMethod radarMethod = new RadarMethod(methods[i].getModifiers(), methods[i].toString());
                        radarMethod.isLocal = true;
                        radarMethods.add(radarMethod);
                    }
                }
                methods = clz.getMethods();
                if (methods != null){
                    for (int i = 0; i < methods.length; i++) {
                        RadarMethod radarMethod = new RadarMethod(methods[i].getModifiers(), methods[i].toString());
                        boolean needToSkip = false;
                        for (RadarMethod radarMethodAdded : radarMethods) {
                            if (radarMethodAdded.isLocal && !radarMethodAdded.isNative && radarMethodAdded.equals(radarMethod)) {
                                radarMethodAdded.isOverWrite = true;
                                needToSkip = true;
                                break;
                            }
                        }
                        if (needToSkip) {
                            continue;
                        }
                        radarMethods.add(radarMethod);
                    }
                }
            }
            result.setRadarMethods(radarMethods);
            return result;
        } catch (Exception e) {
            XLog.appendText(e);
            //e.printStackTrace();
        }
        return null;
    }

    private static String makeAccessType(int modifiers) {
        if (java.lang.reflect.Modifier.isPublic(modifiers)) {
            return "public";
        }else if (java.lang.reflect.Modifier.isPrivate(modifiers)) {
            return "private";
        }else if (java.lang.reflect.Modifier.isProtected(modifiers)) {
            return "protected";
        }
        return "";
    }

    private static String getType(String describe) {
        String[] splits = describe.split(" ");
        int skipTimes = -1;
        for (int i = 0; i < splits.length ; i++) {
            String item = splits[i];
            skipTimes ++;
            if ("public".equals(item) || "protected".equals(item) || "private".equals(item)) {
                continue;
            }else if ("static".equals(item)) {
                continue;
            }else if ("final".equals(item)) {
                continue;
            }else if ("native".equals(item)) {
                continue;
            }
            if (item.contains(".") || item.equals("int") || item.equals("float") || item.equals("boolean") || item.equals("double") || item.equals("long") || item.equals("void") || item.equals("short") || item.equals("char")) {
                return item;
            }else if (item.endsWith("[]")) {
                return item;
            }
        }
        return "unkown";
    }

    private static String getMethodName(String describe) {
        Matcher matcher = Pattern.compile("([^.]+)\\(").matcher(describe);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "unkown";
    }

    private static String convertToFridaType(String type){
        if (!type.endsWith("[]")) {
            return type;
        }
        String fridaType = "";
        while (type.endsWith("[]")) {
            fridaType += "[";
            type = type.substring(0, type.length()-2);
        }
        switch (type){
            case "int":
                fridaType += "I";
                break;
            case "boolean":
                fridaType += "Z";
                break;
            case "byte":
                fridaType += "B";
                break;
            case "char":
                fridaType += "C";
                break;
            case "double":
                fridaType += "D";
                break;
            case "float":
                fridaType += "F";
                break;
            case "long":
                fridaType += "J";
                break;
            case "short":
                fridaType += "S";
                break;
            default:
                fridaType += "L"+type + ";";
                break;
        }
        return fridaType;
    }

    private static String[] getMethodParams(String describe) {
        Matcher matcher = Pattern.compile("[^.]+\\(([^\\)]*)\\)").matcher(describe);
        if (matcher.find()){
            String paramsLine = matcher.group(1).trim();
            if (paramsLine.isEmpty()) {
                return new String[0];
            }
            if (paramsLine.contains(",")){
                String[] params = paramsLine.split(",");
                for (int j = 0; j < params.length; j++){
                    params[j] = convertToFridaType(params[j].trim());
                }
                return params;
            }else{
                return new String[]{convertToFridaType(paramsLine)};
            }
        }
        return new String[0];
    }

    private static String[] getThrowsExceptions(String describe) {
        Matcher matcher = Pattern.compile("\\)[\\s]*throws[\\s]+(.*)").matcher(describe);
        if (matcher.find()){
            String throwsExceptionsLine = matcher.group(1).trim();
            if (throwsExceptionsLine.contains(",")){
                String[] throwsExceptions = throwsExceptionsLine.split(",");
                for (int j = 0; j < throwsExceptions.length; j++){
                    throwsExceptions[j] = throwsExceptions[j].trim();
                }
                return throwsExceptions;
            }else{
                return new String[]{throwsExceptionsLine};
            }
        }
        return new String[0];
    }

    public static boolean exists(String className){
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
        }
        return false;
    }
    
    
    public static JSONObject getObjectProfileWithFilter(Object obj, Set<Object> filter) {
    	JSONObject profile = null;
    	if (filter == null) {
    		filter = new HashSet<Object>();
    	}
    	if (obj != null) {
    		profile = new JSONObject();
    		String className = obj.getClass().getName();
    		profile.put("className", className);
    		profile.put("objectHash" , obj.hashCode());
    		if (className.startsWith("java.") || className.startsWith("javax.") || className.startsWith("android.") || className.startsWith("androidx.")) {
    			return profile;
    		}
    		if (filter.contains(obj)) {
    			return profile;
    		}
    		filter.add(obj);
    		Field[] fields = obj.getClass().getDeclaredFields();
    		for (int i = 0; fields != null && i < fields.length; i++) {
    			Field field = fields[i];
    			field.setAccessible(true);
    			try {
    				Object fieldValue = field.get(obj);
        			if (fieldValue != null) {
        				profile.put("field_" + field.getName(), getObjectProfileWithFilter(fieldValue, filter));
        			}
    			}catch (Exception e) {
    				e.printStackTrace();
				}
			}
    	}
    	return profile;
    }
    
    public static JSONObject getObjectProfile(Object obj) {
    	return getObjectProfileWithFilter(obj, null);
    }

}
