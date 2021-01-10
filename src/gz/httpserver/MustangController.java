package gz.httpserver;

public abstract class MustangController {
    public final String path;
    public MustangController(String path) {
        this.path = path;
    }

    protected String callOnResponse(EmbeddHTTPServer.EmbeddHTTPParams embeddHTTPParams) throws Exception {
        return onResponse(new MustangHTTPParams(embeddHTTPParams));
    }

    public abstract String onResponse(MustangHTTPParams params) throws Exception;

}
