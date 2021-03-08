package gz.util;

import java.lang.reflect.Method;

public class XPretty {

	public static String getPrettyString(Object obj) {
		if (obj == null) {
			return "null";
		}
		String classNameString = obj.getClass().getName();
		if (obj instanceof android.os.Parcelable) {
			return classNameString + "@" + obj.hashCode();
		}
		if (obj instanceof CharSequence) {
			return obj.toString();
		}
		try {
			Method toStringMethod = obj.getClass().getDeclaredMethod("toString");
			return obj.toString();
		} catch (Exception e) {
			XLog.appendText(e);
		}
		return classNameString + "@" + obj.hashCode();
	}
}
