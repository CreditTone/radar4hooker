package gz.httpserver.mustang;

public class MustangTimerHttpServer extends MustangHttpServer {

    private final long timeInterval;
    private volatile long lastResponseTime;

    public MustangTimerHttpServer(int port, long timeInterval) {
        super(port);
        this.timeInterval = timeInterval;
    }

    @Override
    public String onResponse(String path, EmbeddHTTPParams embeddHTTPParams) throws Exception {
        String urlPath = checkPath(path);
        for (MustangController mustangController : mustangControllers) {
            if (mustangController.path.equals(urlPath)) {
                synchronized (this){
                    if (System.currentTimeMillis() - lastResponseTime < timeInterval) {
                        return "{code: 101, msg:\"Call too often.\",chinaeseMsg:\"调用太频繁\"lastResponseTime: "+lastResponseTime+"}";
                    }
                    lastResponseTime = System.currentTimeMillis();
                }
                return mustangController.callOnResponse(embeddHTTPParams);
            }
        }
        return "{msg:\"Not found the path '"+path+"'.\"}";
    }

}
