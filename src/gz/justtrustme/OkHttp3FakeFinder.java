package gz.justtrustme;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

public class OkHttp3FakeFinder {
	
	// public static final class Builder
	public static String[] matchOkHttpClientBuilderCheck(String className) {
		if (className.startsWith("java.") || className.startsWith("android") || className.startsWith("com")
				|| className.startsWith("[") || !className.contains("$")) {
			return null;
		}
		//java.net.Proxy proxy;
		boolean hasProxyField = false;
		
		//java.net.ProxySelector proxySelector;
		boolean hasProxySelectorField = false;
		
		//javax.net.SocketFactory socketFactory;
		boolean hasSocketFactoryField = false;
		
        //javax.net.ssl.SSLSocketFactory sslSocketFactory;
		boolean hasSSLSocketFactoryField = false;
		
		//javax.net.ssl.HostnameVerifier
		boolean hasHostnameVerifierField = false;
		
		//public Builder sslSocketFactory(javax.net.ssl.SSLSocketFactory sSLSocketFactory)
		boolean hasSslSocketFactoryMethod = false;
		String sslSocketFactoryMethodName = null;
		
		//public Builder sslSocketFactory(javax.net.ssl.SSLSocketFactory sSLSocketFactory, javax.net.ssl.X509TrustManager x509TrustManager)
		boolean hasSslSocketFactory2Method = false;
		String sslSocketFactoryMethod2Name = null;
		
		try {
			Class<?> clz = Class.forName(className);
			//校验 public static final class Builder 类声明
			if (!Modifier.isFinal(clz.getModifiers()) || !Modifier.isPublic(clz.getModifiers()) || !Modifier.isStatic(clz.getModifiers()) ) {
				return null;
			}
			Field[] fields = clz.getDeclaredFields();
			for (Field field : fields) {
				String fieldClassName = field.getType().getName();
				if ("java.net.Proxy".equals(fieldClassName)) {
					hasProxyField = true;
				}else if ("java.net.ProxySelector".equals(fieldClassName)) {
					hasProxySelectorField = true;
				}else if ("javax.net.SocketFactory".equals(fieldClassName)) {
					hasSocketFactoryField = true;
				}else if ("javax.net.ssl.SSLSocketFactory".equals(fieldClassName)) {
					hasSocketFactoryField = true;
				}else if ("javax.net.ssl.HostnameVerifier".equals(fieldClassName)) {
					hasHostnameVerifierField = true;
				}
			}
			
			Method[] methods = clz.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				//如果方法非public或static方法直接continue
				if (!Modifier.isPublic(method.getModifiers()) || Modifier.isStatic(method.getModifiers())) {
					continue;
				}
				//校验方法返回值为Builder自己
				if (!clz.equals(method.getReturnType())) {
					continue;
				}
				
				if (method.getParameterCount() == 1) {
					Parameter parameter = method.getParameters()[0];
					if ("javax.net.ssl.SSLSocketFactory".equals(parameter.getType().getName())) {
						hasSslSocketFactoryMethod = true;
						sslSocketFactoryMethodName = method.getName();
					}
				} else if (method.getParameterCount() == 2) {
					Parameter parameter0 = method.getParameters()[0];
					Parameter parameter1 = method.getParameters()[1];
					if ("javax.net.ssl.SSLSocketFactory".equals(parameter0.getType().getName())
							&& "javax.net.ssl.X509TrustManager".equals(parameter1.getType().getName())) {
						hasSslSocketFactory2Method = true;
						sslSocketFactoryMethod2Name = method.getName();
					}
				}
			}
			
			//7项核心指标全部达标，我们可以判断这个类就是okhttp3.OkHttpClient$Builder的混淆类了
			if (hasProxyField && hasProxySelectorField && hasSocketFactoryField 
					&& hasSSLSocketFactoryField && hasHostnameVerifierField && hasSslSocketFactoryMethod 
					&& hasSslSocketFactory2Method) {
				//返回混淆的类名和方法名
				return new String[] {className, sslSocketFactoryMethodName, sslSocketFactoryMethod2Name};
			}
		} catch (ClassNotFoundException e) {
		}
		
		return null;
	}

	// public final class okhttp3.CertificatePinner
	public static String[] matchOk3CertificatePinnerCheck(String className) {
		if (isInvalidOkHttpClass(className)) {
			return null;
		}
		// public void check(String str, List<Certificate> list)
		boolean sameCheckMethod = false;
		// public static String pin(Certificate certificate)
		boolean samePinMethod = false;
		// public static ByteString sha1(X509Certificate x509Certificate)
		boolean sameSha1Method = false;
		String checkMethodName = null;
		try {
			Class<?> clz = Class.forName(className);
			if (!Modifier.isFinal(clz.getModifiers())) {
				return null;
			}
			Method[] methods = clz.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				int modifier = method.getModifiers();
				if (method.getParameterCount() == 1) {
					Parameter parameter = method.getParameters()[0];
					if ("java.security.cert.Certificate".equals(parameter.getType().getName())
							&& Modifier.isStatic(modifier) && method.getReturnType().equals(String.class)) {
						samePinMethod = true;
					}
					if ("java.security.cert.X509Certificate".equals(parameter.getType().getName())
							&& Modifier.isStatic(modifier) 
							&& !method.getReturnType().equals(Void.TYPE)) {
						sameSha1Method = true;
					}
				} else if (method.getParameterCount() == 2) {
					Parameter parameter0 = method.getParameters()[0];
					Parameter parameter1 = method.getParameters()[1];
					if ("java.lang.String".equals(parameter0.getType().getName())
							&& "java.util.List".equals(parameter1.getType().getName())
							&& method.getReturnType().equals(Void.TYPE)) {
						sameCheckMethod = true;
						checkMethodName = method.getName();
					}
				}
			}
		} catch (ClassNotFoundException e) {
		}
		if (sameCheckMethod && samePinMethod && sameSha1Method) {
			return new String[] { className, checkMethodName };
		}
		return null;
	}

	private static boolean isInvalidOkHttpClass(String className) {
		return className.startsWith("java.") || className.startsWith("android") || className.startsWith("com")
				|| className.startsWith("[") || className.contains("$");
	}

	// public final class OkHostnameVerifier implements HostnameVerifier
	public static String[] matchOk3OkHostnameVerifierVerify(String className) {
		if (isInvalidOkHttpClass(className)) {
			return null;
		}
		// public boolean verify(String str, SSLSession sSLSession)
		boolean sameVerify_Str_SSLSession_Method = false;
		// public boolean verify(String str, X509Certificate x509Certificate)
		boolean sameVerify_Str_X509Certificate_Method = false;
		// public static List<String> allSubjectAltNames(X509Certificate
		// x509Certificate)
		boolean same_AllSubjectAltNames_Method = false;

		String sameVerify_Str_SSLSession_MethodName = null;

		String sameVerify_Str_X509Certificate_MethodName = null;
		try {
			Class<?> clz = Class.forName(className);
			if (!Modifier.isFinal(clz.getModifiers())) {
				return null;
			}
			Method[] methods = clz.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				int modifier = method.getModifiers();
				method.getReturnType().getName();
				if (method.getParameterCount() == 1) {
					Parameter parameter = method.getParameters()[0];
					if ("java.security.cert.X509Certificate".equals(parameter.getType().getName())
							&& Modifier.isStatic(modifier)
							&& method.getReturnType().getName().equals("java.util.List")) {
						same_AllSubjectAltNames_Method = true;
					}
				} else if (method.getParameterCount() == 2) {
					Parameter parameter0 = method.getParameters()[0];
					Parameter parameter1 = method.getParameters()[1];
					if ("java.lang.String".equals(parameter0.getType().getName())
							&& "javax.net.ssl.SSLSession".equals(parameter1.getType().getName())
							&& method.getReturnType().equals(boolean.class)) {
						sameVerify_Str_SSLSession_Method = true;
						sameVerify_Str_SSLSession_MethodName = method.getName();
					}
					if ("java.lang.String".equals(parameter0.getType().getName())
							&& "java.security.cert.X509Certificate".equals(parameter1.getType().getName())
							&& method.getReturnType().equals(boolean.class)) {
						sameVerify_Str_X509Certificate_Method = true;
						sameVerify_Str_X509Certificate_MethodName = method.getName();
					}
				}
			}
		} catch (ClassNotFoundException e) {
		}
		if (sameVerify_Str_SSLSession_Method && sameVerify_Str_X509Certificate_Method
				&& same_AllSubjectAltNames_Method) {
			return new String[] { className, sameVerify_Str_SSLSession_MethodName,
					sameVerify_Str_X509Certificate_MethodName };
		}
		return null;
	}

}
