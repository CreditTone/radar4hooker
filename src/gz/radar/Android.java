package gz.radar;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Android {


    public static <T extends Activity> T getTopActivity() throws Exception {
        Class activityThreadClass = Class.forName("android.app.ActivityThread");
        Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
        Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
        activitiesField.setAccessible(true);
        Map activities = (Map) activitiesField.get(activityThread);
        for (Object activityClientRecord : activities.values()) {
            Class activityClietnRecordClass = activityClientRecord.getClass();
            Field pausedField = activityClietnRecordClass.getDeclaredField("paused");
            pausedField.setAccessible(true);
            if (!pausedField.getBoolean(activityClientRecord)) {
                Field activityField = activityClietnRecordClass.getDeclaredField("activity");
                activityField.setAccessible(true);
                return (T) activityField.get(activityClientRecord);
            }
        }
        throw new Exception("请确认app界面已经打开，并且手机没有熄屏。");
    }

    public static <T extends Application> T getApplication() throws Exception {
        Class<?> activityThread = Class.forName("android.app.ActivityThread");
        Method currentApplication = activityThread.getDeclaredMethod("currentApplication");
        Method currentActivityThread = activityThread.getDeclaredMethod("currentActivityThread");
        Object current = currentActivityThread.invoke((Object)null);
        Object app = currentApplication.invoke(current);
        return (T) app;
    }

    public static String getVersionName() throws Exception {
        Application context = getApplication();
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    public static String getMainActivity() throws Exception {
        Application context = getApplication();
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            Intent intent = pm.getLaunchIntentForPackage(context.getPackageName());
            return intent.getComponent().getClassName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    public static ObjectInfo getObjectInfo(String objId) throws Exception {
        Object obj = RadarProperties.getCachedObject(objId);
        if (obj == null) {
            return null;
        }
        return new ObjectInfo(obj);
    }

    public static ViewInfo getViewInfo(String id) throws Exception {
        if (Pattern.compile("^\\d{10}$").matcher(id).find()) {
            int viewId = Integer.parseInt(id.toString());
            View view = AndroidUI.findViewById(viewId);
            if (view != null) {
                String idName = Android.getApplication().getResources().getResourceEntryName(viewId);
                return new ViewInfo(view, idName);
            }
        }
        if (Pattern.compile("[a-z]").matcher(id).find()) {
            String idName = id.toString();
            View view = AndroidUI.findViewByIdName(idName);
            if (view != null) {
                return new ViewInfo(view, idName);
            }
        }
        View view = (View) RadarProperties.getCachedObject(id);
        String idName = "null";
        if (view != null && view.getId() != -1) {
            idName = Android.getApplication().getResources().getResourceEntryName(view.getId());
        }
        if (view != null) {
            return new ViewInfo(view, idName);
        }
        return null;
    }

    public static ServiceInfo[] getServiceInfos() throws Exception {
        ServiceInfo[] serviceInfos = null;
        List<ServiceInfo> results = new ArrayList<>();
        Class activityThreadClass = Class.forName("android.app.ActivityThread");
        Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
        Field mServicesField = activityThreadClass.getDeclaredField("mServices");
        mServicesField.setAccessible(true);
        Map mServices = (Map) mServicesField.get(activityThread);
        for (Object service : mServices.values()) {
            ServiceInfo serviceInfo = new ServiceInfo((Service) service);
            results.add(serviceInfo);
        }
        serviceInfos = new ServiceInfo[results.size()];
        for (int i = 0; i < results.size(); i++) {
            serviceInfos[i] = results.get(i);
        }
        return serviceInfos;
    }

    public static ActivityInfo[] getActivityInfos() throws Exception {
        ActivityInfo[] activityInfos = null;
        List<ActivityInfo> results = new ArrayList<>();
        ActivityInfo topActivity = null;
        Class activityThreadClass = Class.forName("android.app.ActivityThread");
        Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
        Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
        activitiesField.setAccessible(true);
        Map activities = (Map) activitiesField.get(activityThread);
        for (Object activityClientRecord : activities.values()) {
            Class activityClietnRecordClass = activityClientRecord.getClass();
            Field pausedField = activityClietnRecordClass.getDeclaredField("paused");
            pausedField.setAccessible(true);
            Field activityField = activityClietnRecordClass.getDeclaredField("activity");
            activityField.setAccessible(true);
            Field stoppedField = activityClietnRecordClass.getDeclaredField("stopped");
            stoppedField.setAccessible(true);
            Activity activity = (Activity) activityField.get(activityClientRecord);
            ActivityInfo activityInfo = new ActivityInfo(activity);
            activityInfo.setName(activity.getClass().getName());
            activityInfo.setPaused(pausedField.getBoolean(activityClientRecord));
            activityInfo.setOnTop(!activityInfo.isPaused());
            activityInfo.setTitle(activity.getTitle().toString());
            activityInfo.setStopped(stoppedField.getBoolean(activityClientRecord));
            if (activityInfo.isOnTop()) {
                topActivity = activityInfo;
            }else{
                results.add(activityInfo);
            }
        }
        int length = results.size() + (topActivity != null? 1 : 0);
        activityInfos = new ActivityInfo[length];
        if (length == 0) {
            return activityInfos;
        }
        int index = 0;
        if (topActivity != null) {
            activityInfos[index] = topActivity;
            index ++;
        }
        for (ActivityInfo item : results) {
            activityInfos[index] = item;
            index ++;
        }
        return activityInfos;
    }


}
