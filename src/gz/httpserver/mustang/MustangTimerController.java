package gz.httpserver.mustang;

import gz.httpserver.EmbeddHTTPServer;

public abstract class MustangTimerController extends MustangController {

    private final long timeInterval;
    private volatile long lastResponseTime;

    protected MustangTimerController(String path, long timeInterval) {
        super(path);
        this.timeInterval = timeInterval;
    }

    protected String callOnResponse(EmbeddHTTPServer.EmbeddHTTPParams embeddHTTPParams) throws Exception {
        synchronized (this){
            if (System.currentTimeMillis() - lastResponseTime < timeInterval) {
                return "{code: 101, msg:\"Call too often.\",chinaeseMsg:\"调用太频繁\",lastResponseTime: "+lastResponseTime+"}";
            }
            lastResponseTime = System.currentTimeMillis();
        }
        return onResponse(new MustangHTTPParams(embeddHTTPParams));
    }
}
