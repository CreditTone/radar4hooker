package gz.httpserver;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public abstract class EmbeddHTTPServer extends NanoHTTPD {

    public EmbeddHTTPServer(int port) {
        super("0.0.0.0",port);
    }

    public static class EmbeddHTTPParams {

        private final  Map<String, List<String>> parameter;

        public EmbeddHTTPParams(Map<String, List<String>> parameter) {
            this.parameter = parameter;
        }

        public String getParameter(String pName) {
            List<String> list = parameter.get(pName);
            return list != null && list.size() > 0? list.get(0):null;
        }
    }

    private void checkoutParameter(IHTTPSession session) {
        if (session.getMethod().equals(Method.POST)){
            try {
                session.parseBody(new HashMap<String, String>());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public abstract String onResponse(String path, EmbeddHTTPParams embeddHTTPParams) throws Exception;


    @Override
    public Response serve(IHTTPSession session) {
        checkoutParameter(session);
        String path = session.getUri();
        if (path == null) {
            path = "/";
        }
        try{
            Map<String, List<String>> parameter = session.getParameters();
            EmbeddHTTPParams embeddHTTPParams = new EmbeddHTTPParams(parameter);
            String reponse = onResponse(path, embeddHTTPParams);
            return newFixedLengthResponse(reponse);
        }catch (Exception e){
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            return newFixedLengthResponse(stringWriter.toString());
        }
    }
}
