package gz.util;

import java.util.List;

public final class XStringHelper {
	
	public static final String subStringWithKeyword(String str, String keyword) {
		return str;
	}

	public static final boolean contains(String str, List<String> keywords) {
		if (keywords == null || keywords.isEmpty()) {
			return false;
		}
		for (String keyword : keywords) {
			if (str.contains(keyword)) {
				return true;
			}
		}
		return false;
	}
	
}
