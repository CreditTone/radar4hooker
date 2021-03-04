package gz.radar;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.system.Os;
import android.view.View;
import gz.util.X;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Android {
	
	public static String getBundleProfile(Bundle bundle) throws Exception {
		if (bundle != null) {
			Object mMap = X.getField(bundle, "mMap");
			return JSON.toJSONString(mMap);
		}
		return "";
	}
	
	public static String getIntentProfile(Intent intent) throws Exception {
		JSONObject profile = new JSONObject();
		profile.put("action", intent.getAction());
		if (X.hasField(intent, "mData")) {
			Object mData = X.getField(intent, "mData");
			if (mData != null) {
				profile.put("data", mData.toString());
			}
		}
		profile.put("type", intent.getType());
		if (X.hasMehtod(intent, "getIdentifier")) {
			profile.put("identifier", X.invokeObject(intent, "getIdentifier"));
		}
		profile.put("package", intent.getPackage());
		profile.put("flags", intent.getFlags());
		profile.put("categories", intent.getCategories());
		profile.put("type", intent.getType());
		ComponentName componentName = intent.getComponent();
		if (componentName != null) {
			profile.put("component", componentName.getClassName());
		}
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			Object mMap = X.getField(bundle, "mMap");
			profile.put("bundle", mMap);
		}
		return profile.toJSONString();
	}

	public static void checkSelfPermission(String permission) throws Exception {
		Activity context = Android.getTopActivity();
		if (context.checkPermission(permission, android.os.Process.myPid(), Os.getuid()) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(context, new String[] {permission}, 19021);
		}
	}
	
	public static void requestPermissions(final Activity activity,
            final String[] permissions, final int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            activity.requestPermissions(permissions, requestCode);
        }
    }

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
    
    public static String getSignatureInfo() throws Exception {
    	JSONArray result = new JSONArray();
    	Application app = getApplication();
    	PackageManager packageManager = app.getPackageManager();
    	PackageInfo packageInfo = packageManager.getPackageInfo(app.getPackageName(), PackageManager.GET_SIGNATURES);
        for (Signature sig : packageInfo.signatures) {
        	JSONObject item = new JSONObject();
        	item.put("charsString", sig.toCharsString());
        	result.add(item);
        }
        return result.toJSONString();
    }
}
