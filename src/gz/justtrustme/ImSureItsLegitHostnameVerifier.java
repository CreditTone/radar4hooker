package gz.justtrustme;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class ImSureItsLegitHostnameVerifier implements HostnameVerifier {

	@Override
	public boolean verify(String hostname, SSLSession session) {
		return true;
	}

}
