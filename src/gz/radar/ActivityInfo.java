package gz.radar;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.List;

public class ActivityInfo extends ObjectInfo {

    private String title;
    private boolean onTop;
    private boolean paused;
    private boolean stopped;

    public ActivityInfo(Activity activity) throws IllegalAccessException {
        super(activity);
        title = activity.getTitle().toString();
        if (activity instanceof FragmentActivity) {
        	FragmentActivity fragmentActivity = (FragmentActivity) activity;
        	FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        	List<Fragment> fragments = fm.getFragments();
        	if (fragments != null) {
        		int i = 0;
        		for (Fragment fragment : fragments) {
        			String virtualVarName = "mFragment_"+i;
        			String objectId = calculatObjectId(fragment, virtualVarName);
        			addAndroidApkField(new AndroidApkField(virtualVarName, fragment.getClass(), false, -1, fragment, objectId));
        			i++;
        		}
        	}
        }
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
