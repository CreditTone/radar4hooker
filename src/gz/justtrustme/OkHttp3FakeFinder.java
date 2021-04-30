package gz.justtrustme;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import gz.justtrustme.Okhttp3FakeClass.FakeMethod;
import gz.util.XLog;

public class OkHttp3FakeFinder {

	// public static final class Builder
	public static Okhttp3FakeClass okHttpClientBuilderCheck(Class clz) {
		// java.net.Proxy proxy;
		boolean hasProxyField = false;

		// java.net.ProxySelector proxySelector;
		boolean hasProxySelectorField = false;

		// javax.net.SocketFactory socketFactory;
		boolean hasSocketFactoryField = false;

		// javax.net.ssl.SSLSocketFactory sslSocketFactory;
		boolean hasSSLSocketFactoryField = false;

		// javax.net.ssl.HostnameVerifier
		boolean hasHostnameVerifierField = false;

		// public Builder sslSocketFactory(javax.net.ssl.SSLSocketFactory
		// sSLSocketFactory)
		boolean hasSslSocketFactoryMethod = false;
		String sslSocketFactoryMethodName = null;

		// public Builder sslSocketFactory(javax.net.ssl.SSLSocketFactory
		// sSLSocketFactory, javax.net.ssl.X509TrustManager x509TrustManager)
		boolean hasSslSocketFactory2Method = false;
		String sslSocketFactoryMethod2Name = null;

		try {
			String className = clz.getName();
			if (clz.isInterface() || clz.isArray() || clz.isAnnotation() || clz.isEnum()
					|| className.startsWith("java.") || className.startsWith("android") || className.startsWith("com")
					|| !className.contains("$")) {
				// XLog.appendText("okHttpClientBuilderCheck 111");
				return null;
			}
			// 校验 public static final class Builder 类声明
			if (!Modifier.isFinal(clz.getModifiers()) || !Modifier.isPublic(clz.getModifiers())
					|| !Modifier.isStatic(clz.getModifiers())) {
				// XLog.appendText("okHttpClientBuilderCheck 222");
				return null;
			}
			Field[] fields = clz.getDeclaredFields();
			for (Field field : fields) {
				String fieldClassName = field.getType().getName();
				if ("java.net.Proxy".equals(fieldClassName)) {
					hasProxyField = true;
				} else if ("java.net.ProxySelector".equals(fieldClassName)) {
					hasProxySelectorField = true;
				} else if ("javax.net.SocketFactory".equals(fieldClassName)) {
					hasSocketFactoryField = true;
				} else if ("javax.net.ssl.SSLSocketFactory".equals(fieldClassName)) {
					hasSSLSocketFactoryField = true;
				} else if ("javax.net.ssl.HostnameVerifier".equals(fieldClassName)) {
					hasHostnameVerifierField = true;
				}
			}
			Method[] methods = clz.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				// 如果方法非public或static方法直接continue
				if (!Modifier.isPublic(method.getModifiers()) || Modifier.isStatic(method.getModifiers())) {
					continue;
				}
				// 校验方法返回值为Builder自己
				if (!clz.equals(method.getReturnType())) {
					continue;
				}
				Class<?>[] parameterTypes = method.getParameterTypes();
				int parameterCount = parameterTypes.length;
				if (parameterCount == 1) {
					if ("javax.net.ssl.SSLSocketFactory".equals(parameterTypes[0].getName())) {
						hasSslSocketFactoryMethod = true;
						sslSocketFactoryMethodName = method.toString();
					}
				} else if (parameterCount == 2) {
					Class parameter0Clz = parameterTypes[0];
					Class parameter1Clz = parameterTypes[1];
					if ("javax.net.ssl.SSLSocketFactory".equals(parameter0Clz.getName())
							&& "javax.net.ssl.X509TrustManager".equals(parameter1Clz.getName())) {
						hasSslSocketFactory2Method = true;
						sslSocketFactoryMethod2Name = method.toString();
					}
				}
			}
		} catch (Throwable e) {
			// XLog.appendText(new Exception(e));
		}
		// 7项核心指标全部达标，我们可以判断这个类就是okhttp3.OkHttpClient$Builder的混淆类了
		if (hasProxyField && hasProxySelectorField && hasSocketFactoryField && hasSSLSocketFactoryField
				&& hasHostnameVerifierField) {
			// 返回混淆的类名和方法名
			Okhttp3FakeClass okhttp3FakeClass = new Okhttp3FakeClass("okhttp3.OkHttpClient$Builder", clz.getName());
			if (hasSslSocketFactoryMethod && hasSslSocketFactory2Method) {
				okhttp3FakeClass.addFakeMethod(new FakeMethod(
						"public okhttp3.OkHttpClient$Builder okhttp3.OkHttpClient$Builder.sslSocketFactory(javax.net.ssl.SSLSocketFactory)",
						sslSocketFactoryMethodName));
				okhttp3FakeClass.addFakeMethod(new FakeMethod(
						"public okhttp3.OkHttpClient$Builder okhttp3.OkHttpClient$Builder.sslSocketFactory(javax.net.ssl.SSLSocketFactory,javax.net.ssl.X509TrustManager)",
						sslSocketFactoryMethod2Name));
			}
			return okhttp3FakeClass;
		}
		return null;
	}

	// public final class okhttp3.CertificatePinner
	public static Okhttp3FakeClass okHttpCertificatePinnerCheck(Class clz) {
		// public void check(String str, List<Certificate> list)
		boolean hasCheckMethod = false;
		// public static String pin(Certificate certificate)
		boolean hasPinMethod = false;
		// public static ByteString sha1(X509Certificate x509Certificate)
		boolean hasSha1Method = false;
		String checkMethodName = null;
		try {
			if (isInvalidOkHttpClass(clz)) {
				return null;
			}
			Class superClz = clz.getSuperclass();
			if (!superClz.equals(Object.class) || !Modifier.isPublic(clz.getModifiers())
					|| !Modifier.isFinal(clz.getModifiers())) {
				return null;
			}
			if (!Modifier.isFinal(clz.getModifiers())) {
				return null;
			}
			Method[] methods = clz.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				Class<?>[] parameterTypes = method.getParameterTypes();
				int parameterCount = parameterTypes.length;
				int modifier = method.getModifiers();
				if (parameterCount == 1) {
					Class parameter0Clz = parameterTypes[0];
					if ("java.security.cert.Certificate".equals(parameter0Clz.getName()) && Modifier.isStatic(modifier)
							&& method.getReturnType().equals(String.class)) {
						hasPinMethod = true;
					}
					if ("java.security.cert.X509Certificate".equals(parameter0Clz.getName())
							&& Modifier.isStatic(modifier) && !method.getReturnType().equals(Void.TYPE)) {
						hasSha1Method = true;
					}
				} else if (parameterCount == 2) {
					Class parameter0Clz = parameterTypes[0];
					Class parameter1Clz = parameterTypes[1];
					if ("java.lang.String".equals(parameter0Clz.getName())
							&& "java.util.List".equals(parameter1Clz.getName())
							&& method.getReturnType().equals(Void.TYPE)) {
						hasCheckMethod = true;
						checkMethodName = method.toString();
					}
				}
			}
		} catch (Throwable e) {
		}
		if (hasCheckMethod && hasPinMethod && hasSha1Method) {
			Okhttp3FakeClass okhttp3FakeClass = new Okhttp3FakeClass("okhttp3.CertificatePinner", clz.getName());
			okhttp3FakeClass.addFakeMethod(new FakeMethod(
					"public void okhttp3.CertificatePinner.check(java.lang.String,java.util.List)", checkMethodName));
			return okhttp3FakeClass;
		}
		return null;
	}

	private static boolean isInvalidOkHttpClass(Class clz) {
		String className = clz.getName();
		return clz.isInterface() || clz.isArray() || clz.isAnnotation() || clz.isEnum() || className.startsWith("java.")
				|| className.startsWith("android") || className.startsWith("com") || className.contains("$");
	}

	// public final class OkHostnameVerifier implements HostnameVerifier
	public static Okhttp3FakeClass okHttpOkHostnameVerifierVerify(Class clz) {
		// public boolean verify(String str, SSLSession sSLSession)
		boolean hasVerify_Str_SSLSession_Method = false;
		// public boolean verify(String str, X509Certificate x509Certificate)
		boolean hasVerify_Str_X509Certificate_Method = false;
		// public static List<String> allSubjectAltNames(X509Certificate
		// x509Certificate)
		boolean has_AllSubjectAltNames_Method = false;

		String hasVerify_Str_SSLSession_MethodName = null;

		String hasVerify_Str_X509Certificate_MethodName = null;
		try {
			if (isInvalidOkHttpClass(clz)) {
				return null;
			}
			Class[] implInterfaces = clz.getInterfaces();
			if (implInterfaces == null || implInterfaces.length != 1
					|| !implInterfaces[0].equals(javax.net.ssl.HostnameVerifier.class)) {
				return null;
			}
			if (!Modifier.isFinal(clz.getModifiers())) {
				return null;
			}
			Method[] methods = clz.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				Class<?>[] parameterTypes = method.getParameterTypes();
				int parameterCount = parameterTypes.length;
				int modifier = method.getModifiers();
				if (parameterCount == 1) {
					Class parameterClz = parameterTypes[0];
					if ("java.security.cert.X509Certificate".equals(parameterClz.getName())
							&& Modifier.isStatic(modifier)
							&& method.getReturnType().getName().equals("java.util.List")) {
						has_AllSubjectAltNames_Method = true;
					}
				} else if (parameterCount == 2) {
					Class parameter0Clz = parameterTypes[0];
					Class parameter1Clz = parameterTypes[1];
					if ("java.lang.String".equals(parameter0Clz.getName())
							&& "javax.net.ssl.SSLSession".equals(parameter1Clz.getName())
							&& method.getReturnType().equals(boolean.class)) {
						hasVerify_Str_SSLSession_Method = true;
						hasVerify_Str_SSLSession_MethodName = method.toString();
					}
					if ("java.lang.String".equals(parameter0Clz.getName())
							&& "java.security.cert.X509Certificate".equals(parameter1Clz.getName())
							&& method.getReturnType().equals(boolean.class)) {
						hasVerify_Str_X509Certificate_Method = true;
						hasVerify_Str_X509Certificate_MethodName = method.toString();
					}
				}
			}
		} catch (Throwable e) {
		}
		if (hasVerify_Str_SSLSession_Method && hasVerify_Str_X509Certificate_Method && has_AllSubjectAltNames_Method) {
			Okhttp3FakeClass okhttp3FakeClass = new Okhttp3FakeClass("okhttp3.internal.tls.OkHostnameVerifier",
					clz.getName());
			okhttp3FakeClass.addFakeMethod(new FakeMethod(
					"public boolean okhttp3.internal.tls.OkHostnameVerifier.verify(java.lang.String,javax.net.ssl.SSLSession)",
					hasVerify_Str_SSLSession_MethodName));
			okhttp3FakeClass.addFakeMethod(new FakeMethod(
					"public boolean okhttp3.internal.tls.OkHostnameVerifier.verify(java.lang.String,java.security.cert.X509Certificate)",
					hasVerify_Str_X509Certificate_MethodName));
			return okhttp3FakeClass;
		}
		return null;
	}

}
