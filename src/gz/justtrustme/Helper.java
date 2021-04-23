package gz.justtrustme;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.HostNameResolver;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

import gz.util.X;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public final class Helper {

	// Create a SingleClientConnManager that trusts everyone!
	public static ClientConnectionManager getSCCM() {

		KeyStore trustStore;
		try {

			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new TrustAllSSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new SingleClientConnManager(null, registry);

			return ccm;

		} catch (Exception e) {
			return null;
		}
	}

	// This function determines what object we are dealing with.
	public static ClientConnectionManager getCCM(Object o, HttpParams params) {

		String className = o.getClass().getSimpleName();

		if (className.equals("SingleClientConnManager")) {
			return getSCCM();
		} else if (className.equals("ThreadSafeClientConnManager")) {
			return getTSCCM(params);
		}

		return null;
	}

	// This function creates a ThreadSafeClientConnManager that trusts everyone!
	public static ClientConnectionManager getTSCCM(HttpParams params) {

		KeyStore trustStore;
		try {

			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new TrustAllSSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

			return ccm;

		} catch (Exception e) {
			return null;
		}
	}

	private static KeyManager[] createKeyManagers(final KeyStore keystore, final String password)
			throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
		if (keystore == null) {
			throw new IllegalArgumentException("Keystore may not be null");
		}
		KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmfactory.init(keystore, password != null ? password.toCharArray() : null);
		return kmfactory.getKeyManagers();
	}

	public static boolean reInitSSLSocketFactory(javax.net.ssl.SSLSocketFactory sslFactory, String algorithm,
			final KeyStore keystore, final String keystorePassword, final KeyStore __truststore,
			final SecureRandom random, final HostNameResolver __nameResolver) throws Exception {
		KeyManager[] keymanagers = null;
		TrustManager[] trustmanagers = null;

		if (keystore != null) {
			keymanagers = createKeyManagers(keystore, keystorePassword);
		}

		trustmanagers = new TrustManager[] { new ImSureItsLegitTrustManager() };
		SSLContext sslcontext = SSLContext.getInstance(algorithm);
		X.setField(sslFactory, "sslcontext", sslcontext);
		sslcontext.init(keymanagers, trustmanagers, random);
		javax.net.ssl.SSLSocketFactory socketfactory = sslcontext.getSocketFactory();
		X.setField(sslFactory, "socketfactory", socketfactory);
		return true;
	}

	public static javax.net.ssl.SSLSocketFactory getEmptySSLFactory() {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[] { new ImSureItsLegitTrustManager() }, null);
			return sslContext.getSocketFactory();
		} catch (NoSuchAlgorithmException e) {
			return null;
		} catch (KeyManagementException e) {
			return null;
		}
	}

	public static javax.net.ssl.TrustManager[] replaceGetTrustManagers(
			javax.net.ssl.TrustManagerFactory trustManagerFactory, javax.net.ssl.TrustManager[] originResults)
			throws ClassNotFoundException {
		if (hasTrustManagerImpl()) {
			Class<?> cls = Class.forName("com.android.org.conscrypt.TrustManagerImpl");
			if (originResults.length > 0 && cls.isInstance(originResults[0]))
				return originResults;
		}
		return new TrustManager[] { new ImSureItsLegitTrustManager() };
	}

	public static javax.net.ssl.TrustManager[] getImSureTrustManagers() {
		return new TrustManager[] { new ImSureItsLegitTrustManager() };
	}

	public static boolean hasTrustManagerImpl() {
		try {
			Class.forName("com.android.org.conscrypt.TrustManagerImpl");
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}

}
