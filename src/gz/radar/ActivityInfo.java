package gz.radar;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityInfo extends ObjectInfo {

    private String title;
    private boolean onTop;
    private boolean paused;
    private boolean stopped;

    public ActivityInfo(Activity activity) throws IllegalAccessException {
        super(activity);
        List<AndroidApkField> fields = new ArrayList<>();
        title = activity.getTitle().toString();
    }

    public boolean isOnTop() {
        return onTop;
    }

    public void setOnTop(boolean onTop) {
        this.onTop = onTop;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

}
