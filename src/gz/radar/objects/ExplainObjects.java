package gz.radar.objects;

import java.util.ArrayList;
import java.util.List;

public class ExplainObjects {

	final List<String> keys;
	
	final List<Object> objects;
	
	public ExplainObjects() {
		this.keys = new ArrayList<>();
		this.objects = new ArrayList<>();
	}
	
	public void put(String key, Object obj) {
		if (key != null && obj != null) {
			this.keys.add(key);
			this.objects.add(obj);
		}
	}
	
	public int size() {
		return keys.size();
	}
	
	public boolean isEmpty() {
		return keys.isEmpty();
	}
	
	public String getKey(int index) {
		return keys.get(index);
	}
	
	public Object getObject(int index) {
		return objects.get(index);
	}
}
