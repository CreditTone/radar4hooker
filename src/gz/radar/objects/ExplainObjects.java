package gz.radar.objects;

import java.util.ArrayList;
import java.util.List;

public class ExplainObjects {

	final List<String> keys;
	
	final List<String> objectIds;
	
	public ExplainObjects() {
		this.keys = new ArrayList<>();
		this.objectIds = new ArrayList<>();
	}
	
	public void put(String key, Object obj) {
		if (key != null && obj != null) {
			this.keys.add(key);
			this.objectIds.add(ObjectsStore.storeObject(obj));
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
	
	public String getObjectId(int index) {
		return objectIds.get(index);
	}
	
	public Object getObject(int index) {
		String objId = getObjectId(index);
		return ObjectsStore.getObject(objId);
	}
	
	public String getObjectClass(int index) {
		return getObject(index).getClass().getName();
	}
}
