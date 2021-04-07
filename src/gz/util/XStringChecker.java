package gz.util;

import java.util.List;

public final class XStringChecker {

	public final boolean contains(String str, List<String> keywords) {
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
