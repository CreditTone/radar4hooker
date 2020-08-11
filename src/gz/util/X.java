package gz.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class X {

    public static Object newObject(String className) throws Exception {
        return newObject(className, null ,null);
    }

    public static Object newObject(String className, Class[] constructorParamsClzs, Object[] constructorParams) throws Exception {
        Class clz = Class.forName(className);
        if (constructorParams == null || constructorParams.length == 0) {
            Constructor<?> constructor = clz.getDeclaredConstructor();
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return clz.newInstance();
        }
        Constructor<?> constructor = clz.getDeclaredConstructor(constructorParamsClzs);
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        return constructor.newInstance(constructorParams);
    }

    private static Method findMehtod(Object obj, Class clz, String method, Class[] paramsClzs) throws Exception {
        Method foundedMethod = null;
        if (paramsClzs == null || paramsClzs.length == 0) {
            while (clz != null && !clz.getName().equals(Object.class.getName())) {
                try{
                    foundedMethod = clz.getMethod(method);
                }catch (NoSuchMethodException e){
                }
                if (foundedMethod != null) {
                    break;
                }
                try{
                    foundedMethod = clz.getDeclaredMethod(method);
                }catch (NoSuchMethodException e){
                }
                if (foundedMethod != null) {
                    break;
                }
                clz = clz.getSuperclass();
            }

        }else{
            while (clz != null && !clz.getName().equals(Object.class.getName())) {
                try{
                    foundedMethod = clz.getMethod(method, paramsClzs);
                }catch (NoSuchMethodException e){}
                if (foundedMethod != null) {
                    break;
                }
                try{
                    foundedMethod = clz.getDeclaredMethod(method, paramsClzs);
                }catch (NoSuchMethodException e){}
                if (foundedMethod != null) {
                    break;
                }
                clz = clz.getSuperclass();
            }
        }
        if (foundedMethod == null) {
            throw new Exception("The method "+method+" not found in the "+clz+".");
        }
        return foundedMethod;
    }

    public static Object invokeObject(Object obj, String methodname) throws Exception {
        return invokeObject(obj, methodname, null, null);
    }

    public static Object invokeObject(Object obj, String methodname, Class[] paramsClzs, Object[] params) throws Exception {
        Method method = findMehtod(obj, obj.getClass() ,methodname, paramsClzs);
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        if (paramsClzs == null || paramsClzs.length == 0) {
            return method.invoke(obj);
        }
        return method.invoke(obj, params);
    }

    private static Field findField(Object obj,Class clz, String fieldName) throws Exception {
        Field field = null;
        while (clz != null  && !clz.getName().equals(Object.class.getName())) {
            try{
                field = clz.getDeclaredField(fieldName);
            }catch (NoSuchFieldException e) {}
            if (field != null) {
                break;
            }
            try{
                field = clz.getField(fieldName);
            }catch (NoSuchFieldException e) {}
            if (field != null) {
                break;
            }
            clz = clz.getSuperclass();
        }
        return field;
    }

    public static <T> T getField(Object obj, String fieldName) throws Exception {
        Field field = findField(obj, obj.getClass() ,fieldName);
        if (field == null) {
            throw new NullPointerException("The field "+fieldName+" not found.");
        }
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        return (T) field.get(obj);
    }
   

}
