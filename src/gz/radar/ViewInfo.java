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
import gz.util.XView;

public class ViewInfo extends ObjectInfo {

	private int viewId;

	private String viewIdName;

	private String viewText;

	private boolean isVisible;

	public ViewInfo(View view, String idName) throws IllegalAccessException {
		super(view);
		this.viewId = view.getId();
		this.viewIdName = idName;
		XView xView = new XView(view);
		this.viewText = xView.getViewText();
		this.isVisible = xView.isVisible();
		View.OnClickListener onClickListener = xView.getOnClickListener();
		if (onClickListener != null) {
			String objectId = calculatObjectId(onClickListener, "mOnClickListener");
			String onClickListenerClass = onClickListener.getClass().getName();
			addAndroidApkField(new AndroidApkField("mOnClickListener", onClickListenerClass, false, -1, onClickListener,
					objectId));
		}
		View.OnLongClickListener onLongClickListener = xView.getOnLongClickListener();
		if (onLongClickListener != null) {
			String objectId = calculatObjectId(onLongClickListener, "mOnLongClickListener");
			String onLongClickListenerClass = onLongClickListener.getClass().getName();
			addAndroidApkField(new AndroidApkField("mOnLongClickListener", onLongClickListenerClass, false, -1,
					onLongClickListener, objectId));
		}
		View.OnTouchListener mOnTouchListener = xView.getOnTouchListener();
		if (mOnTouchListener != null) {
			String objectId = calculatObjectId(mOnTouchListener, "mOnTouchListener");
			String onTouchListenerClass = mOnTouchListener.getClass().getName();
			addAndroidApkField(new AndroidApkField("mOnTouchListener", onTouchListenerClass, false, -1,
					mOnTouchListener, objectId));
		}
		View.OnFocusChangeListener mOnFocusChangeListener = xView.getOnFocusChangeListener();
		if (mOnFocusChangeListener != null) {
			String objectId = calculatObjectId(mOnTouchListener, "mOnFocusChangeListener");
			String onFocusChangeListenerClass = mOnFocusChangeListener.getClass().getName();
			addAndroidApkField(new AndroidApkField("mOnFocusChangeListener", onFocusChangeListenerClass, false, -1,
					mOnFocusChangeListener, objectId));
		}
		TextView.OnEditorActionListener mOnEditorActionListener = xView.getOnEditorActionListener();
		if (mOnEditorActionListener != null) {
			String onEditorActionListenerClass = mOnEditorActionListener.getClass().getName();
			String objectId = calculatObjectId(mOnEditorActionListener, "mOnEditorActionListenerClass");
			addAndroidApkField(new AndroidApkField("mOnEditorActionListener", onEditorActionListenerClass, false, -1,
					mOnFocusChangeListener, objectId));
		}
		Object adapter = xView.getAdapter();
		if (adapter != null) {
			String adapterClass = adapter.getClass().getName();
			String objectId = calculatObjectId(adapter, "mOnEditorActionListenerClass");
			addAndroidApkField(new AndroidApkField("mAdapter", adapterClass, false, -1, adapter, objectId));
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
