package gz.httpserver.mustang;

import java.util.concurrent.locks.ReentrantLock;

import gz.httpserver.EmbeddHTTPServer;
import gz.util.XLog;

public abstract class MustangAtomController extends MustangController {

    private final ReentrantLock reentrantLock = new ReentrantLock(true);


    protected MustangAtomController(String path) {
        super(path);
    }

    protected String callOnResponse(EmbeddHTTPServer.EmbeddHTTPParams embeddHTTPParams) throws Exception {
        if (reentrantLock.isLocked()) {
            return "{msg:\"Device is busy now.\",chinaeseMsg:\"当前有任务在执行\"}";
        }
        Exception happendEx = null;
        String response = null;
        try{
            reentrantLock.lock();
            response = this.onResponse(new MustangHTTPParams(embeddHTTPParams));
        }catch (Exception e) {
            XLog.appendText(e);
            happendEx = e;;
        }finally {
            reentrantLock.unlock();
        }
        if (happendEx != null) {
            throw happendEx;
        }
        return response;
    }
}
