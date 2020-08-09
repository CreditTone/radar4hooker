package gz.radar;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import gz.util.X;
import gz.util.XLog;

public class ViewInfo extends ObjectInfo {

    private int viewId;

    private String viewIdName;

    private String viewText;

    private boolean isVisible;

    public ViewInfo(View view, String idName) throws IllegalAccessException {
        super(view);
        this.viewId = view.getId();
        this.viewIdName = idName;
        this.viewText = "";
        if (view instanceof TextView) {
            this.viewText = ((TextView) view).getText().toString();
        }
        this.isVisible = view.getVisibility() == View.VISIBLE;
        try {
            Object listenerInfo =  X.invokeObject(view, "getListenerInfo");
            if (listenerInfo != null) {
                String objectId = calculatObjectId(listenerInfo, "listenerInfo");
                String onClickListenerClass = listenerInfo.getClass().getName();
                //addAndroidApkField(new AndroidApkField("listenerInfo", onClickListenerClass, false, -1, listenerInfo, objectId));
            }
            View.OnClickListener onClickListener = X.getField(listenerInfo, "mOnClickListener");
            if (onClickListener != null) {
                String objectId = calculatObjectId(onClickListener, "mOnClickListener");
                String onClickListenerClass = onClickListener.getClass().getName();
                addAndroidApkField(new AndroidApkField("mOnClickListener", onClickListenerClass, false, -1, onClickListener, objectId));
            }
            View.OnLongClickListener onLongClickListener = X.getField(listenerInfo, "mOnLongClickListener");
            if (onLongClickListener != null) {
                String objectId = calculatObjectId(onLongClickListener, "mOnLongClickListener");
                String onLongClickListenerClass = onLongClickListener.getClass().getName();
                addAndroidApkField(new AndroidApkField("mOnLongClickListener", onLongClickListenerClass, false, -1, onLongClickListener, objectId));
            }
            View.OnTouchListener mOnTouchListener = X.getField(listenerInfo, "mOnTouchListener");
            if (mOnTouchListener != null) {
                String objectId = calculatObjectId(mOnTouchListener, "mOnTouchListener");
                String onTouchListenerClass = mOnTouchListener.getClass().getName();
                addAndroidApkField(new AndroidApkField("mOnTouchListener", onTouchListenerClass, false, -1, mOnTouchListener, objectId));
            }
            View.OnFocusChangeListener mOnFocusChangeListener = X.getField(listenerInfo, "mOnFocusChangeListener");
            if (mOnFocusChangeListener != null) {
                String objectId = calculatObjectId(mOnTouchListener, "mOnFocusChangeListener");
                String onFocusChangeListenerClass = mOnFocusChangeListener.getClass().getName();
                addAndroidApkField(new AndroidApkField("mOnFocusChangeListener", onFocusChangeListenerClass, false, -1, mOnFocusChangeListener, objectId));
            }
            if (view instanceof TextView) {
                try {
                    //android.widget.Editor
                    Object editor = X.getField(view, "mEditor");
                    if (editor != null) {
                        Object mInputContentType = X.getField(editor, "mInputContentType");
                        if (mInputContentType != null) {
                            TextView.OnEditorActionListener onEditorActionListener = X.getField(mInputContentType, "onEditorActionListener");
                            if (onEditorActionListener == null) {
                                onEditorActionListener = X.getField(mInputContentType, "mOnEditorActionListener");
                            }
                            if (onEditorActionListener != null) {
                                String onEditorActionListenerClass = onEditorActionListener.getClass().getName();
                                String objectId = calculatObjectId(onEditorActionListener, "mOnEditorActionListenerClass");
                                addAndroidApkField(new AndroidApkField("mOnEditorActionListener", onEditorActionListenerClass, false, -1, mOnFocusChangeListener, objectId));
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
            if (view.getClass().getName().endsWith("RecyclerView")) {
                try{
                    Object adapter = X.invokeObject(view, "getAdapter");
                    if (adapter != null) {
                        String objectId = calculatObjectId(adapter, "mAdapter");
                        addAndroidApkField(new AndroidApkField("mAdapter", adapter.getClass().getName(), false, -1, adapter, objectId));
                    }
                }catch (Exception e){}
            }
        }catch (Exception e){
            this.viewText = XLog.getException(e);
        }
    }

    public int getViewId() {
        return viewId;
    }

    public String getViewIdName() {
        return viewIdName;
    }

    public String getViewText() {
        return viewText;
    }

    public boolean isVisible() {
        return isVisible;
    }

}
