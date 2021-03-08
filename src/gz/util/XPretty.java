package gz.util;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import com.alibaba.fastjson.JSON;

public class XPretty {

	public static String getPrettyString(Object obj) {
		if (obj == null) {
			return "null";
		}
		if (obj instanceof android.os.Parcelable) {
			return "Cannot serialization android.os.Parcelable object!";
		}
		if (obj instanceof CharSequence) {
			return obj.toString();
		}
		String classNameString = obj.getClass().getName();
		if (obj instanceof Collection || obj instanceof Map || classNameString.startsWith("[")) {
			return JSON.toJSONString(obj);
		}
		try {
			Method toStringMethod = obj.getClass().getDeclaredMethod("toString");
			return obj.toString();
		} catch (Exception e) {
			XLog.appendText(e);
		}
		return "Serialization error!";
	}
}
