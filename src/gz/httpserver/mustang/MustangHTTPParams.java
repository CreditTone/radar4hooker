package gz.httpserver.mustang;

import gz.httpserver.EmbeddHTTPServer;

public class MustangHTTPParams {

    public final EmbeddHTTPServer.EmbeddHTTPParams embeddHTTPParams;
    public MustangHTTPParams(EmbeddHTTPServer.EmbeddHTTPParams embeddHTTPParams) {
        this.embeddHTTPParams = embeddHTTPParams;
    }

    public String getParamter(String name) throws Exception {
        String value = embeddHTTPParams.getParameter(name);
        if (value == null || value.trim().isEmpty()) {
            throw new Exception("Not Found Param "+name);
        }
        return value;
    }

    public String getParamter(String name, String defultValue) {
        String value = embeddHTTPParams.getParameter(name);
        if (value == null || value.trim().isEmpty()) {
            return defultValue;
        }
        return value;
    }
}
