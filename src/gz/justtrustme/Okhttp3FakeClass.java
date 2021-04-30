package gz.justtrustme;

import java.util.ArrayList;
import java.util.List;

public class Okhttp3FakeClass {

	public static final class FakeMethod {

		private final String originalMethod;

		private final String fakeMethod;

		public FakeMethod(String originalMethod, String fakeMethod) {
			this.originalMethod = originalMethod;
			this.fakeMethod = fakeMethod;
		}

		public String getOriginalMethod() {
			return originalMethod;
		}

		public String getFakeMethod() {
			return fakeMethod;
		}

	}
	

	private final String originalClassName;

	private final String fakeClassName;
	
	private final List<FakeMethod> fakeMethods = new ArrayList<Okhttp3FakeClass.FakeMethod>();

	public Okhttp3FakeClass(String originalClassName, String fakeClassName) {
		this.originalClassName = originalClassName;
		this.fakeClassName = fakeClassName;
	}
	
	public void addFakeMethod(FakeMethod fakeMethod) {
		fakeMethods.add(fakeMethod);
	}
	
	public FakeMethod getFakeMethod(int index) {
		return fakeMethods.get(index);
	}
	
	public int fakeMethodSize() {
		return fakeMethods.size();
	}
	
	public String getOriginalClassName() {
		return originalClassName;
	}

	public String getFakeClassName() {
		return fakeClassName;
	}

}
