package gz.httpserver.mustang;

import java.util.ArrayList;
import java.util.List;
import gz.httpserver.EmbeddHTTPServer;

public class MustangHttpServer extends EmbeddHTTPServer {

    protected final List<MustangController> mustangControllers = new ArrayList<>();

    public MustangHttpServer(int port) {
        super(port);
    }

    public void addController(MustangController mustangController) {
        mustangControllers.add(mustangController);
    }

    protected String checkPath(String originPath) {
        String urlPath = originPath.replaceAll("[/]{2,}", "/");
        if (urlPath.contains("?")) {
            urlPath = urlPath.split("\\?")[0];
        }
        return urlPath;
    }

    @Override
    public String onResponse(String path, EmbeddHTTPParams embeddHTTPParams) throws Exception {
        String urlPath = checkPath(path);
        for (MustangController mustangController : mustangControllers) {
            if (mustangController.path.equals(urlPath)) {
            	try {
            		return mustangController.callOnResponse(embeddHTTPParams);
				} catch (Exception e) {
					return "{exception:\""+e.toString()+"'.\"}";
				}
            }
        }
        return "{msg:\"Not found the path '"+path+"'.\"}";
    }
}
