package gz.justtrustme;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

public class OkHttp3FakeFinder {

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
