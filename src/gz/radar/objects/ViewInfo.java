package gz.radar.objects;

import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnHoverListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import gz.radar.AndroidApkField;
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
			String objectId = ObjectsStore.storeObject(onClickListener);
			addAndroidApkField(new AndroidApkField("mOnClickListener", onClickListener.getClass(), false, -1, onClickListener,
					objectId));
		}
		View.OnLongClickListener onLongClickListener = xView.getOnLongClickListener();
		if (onLongClickListener != null) {
			String objectId = ObjectsStore.storeObject(onLongClickListener);
			addAndroidApkField(new AndroidApkField("mOnLongClickListener", onLongClickListener.getClass(), false, -1,
					onLongClickListener, objectId));
		}
		View.OnTouchListener mOnTouchListener = xView.getOnTouchListener();
		if (mOnTouchListener != null) {
			String objectId = ObjectsStore.storeObject(mOnTouchListener);
			addAndroidApkField(new AndroidApkField("mOnTouchListener", mOnTouchListener.getClass(), false, -1,
					mOnTouchListener, objectId));
		}
		View.OnFocusChangeListener mOnFocusChangeListener = xView.getOnFocusChangeListener();
		if (mOnFocusChangeListener != null) {
			String objectId = ObjectsStore.storeObject(mOnFocusChangeListener);
			addAndroidApkField(new AndroidApkField("mOnFocusChangeListener", mOnFocusChangeListener.getClass(), false, -1,
					mOnFocusChangeListener, objectId));
		}
		TextView.OnEditorActionListener mOnEditorActionListener = xView.getOnEditorActionListener();
		if (mOnEditorActionListener != null) {
			String objectId = ObjectsStore.storeObject(mOnEditorActionListener);
			addAndroidApkField(new AndroidApkField("mOnEditorActionListener", mOnEditorActionListener.getClass(), false, -1,
					mOnFocusChangeListener, objectId));
		}
		
		Object mOnScrollChangeListener = xView.getOnScrollChangeListener();
		if (mOnScrollChangeListener != null) {
			String objectId = ObjectsStore.storeObject(mOnScrollChangeListener);
			addAndroidApkField(new AndroidApkField("mOnScrollChangeListener", mOnScrollChangeListener.getClass(), false, -1,
					mOnScrollChangeListener, objectId));
		}
		
		Object adapter = xView.getAdapter();
		if (adapter != null) {
			String objectId = ObjectsStore.storeObject(adapter);
			addAndroidApkField(new AndroidApkField("mAdapter", adapter.getClass(), false, -1, adapter, objectId));
		}
		
		OnScrollListener onScrollListener = xView.getOnScrollListener();
		if (onScrollListener != null) {
			String objectId = ObjectsStore.storeObject(onScrollListener);
			addAndroidApkField(new AndroidApkField("mOnScrollListener", onScrollListener.getClass(), false, -1, onScrollListener, objectId));
		}
		
		OnHoverListener mOnHoverListener = xView.getOnHoverListener();
		if (mOnHoverListener != null) {
			String objectId = ObjectsStore.storeObject(mOnHoverListener);
			addAndroidApkField(new AndroidApkField("mOnHoverListener", mOnHoverListener.getClass(), false, -1, mOnHoverListener, objectId));
		}
		
		OnDragListener mOnDragListener = xView.getOnDragListener();
		if (mOnDragListener != null) {
			String objectId = ObjectsStore.storeObject(mOnDragListener);
			addAndroidApkField(new AndroidApkField("mOnDragListener", mOnDragListener.getClass(), false, -1, mOnDragListener, objectId));
		}
		
		OnItemClickListener mOnItemClickListener = xView.getOnItemClickListener();
		if (mOnItemClickListener != null) {
			String objectId = ObjectsStore.storeObject(mOnItemClickListener);
			addAndroidApkField(new AndroidApkField("mOnItemClickListener", mOnItemClickListener.getClass(), false, -1, mOnItemClickListener, objectId));
		}
		
		OnItemLongClickListener mOnItemLongClickListener = xView.getOnItemLongClickListener();
		if (mOnItemLongClickListener != null) {
			String objectId = ObjectsStore.storeObject(mOnItemLongClickListener);
			addAndroidApkField(new AndroidApkField("mOnItemLongClickListener", mOnItemLongClickListener.getClass(), false, -1, mOnItemLongClickListener, objectId));
		}
		
		OnItemSelectedListener mOnItemSelectedListener = xView.getOnItemSelectedListener();
		if (mOnItemSelectedListener != null) {
			String objectId = ObjectsStore.storeObject(mOnItemSelectedListener);
			addAndroidApkField(new AndroidApkField("mOnItemSelectedListener", mOnItemSelectedListener.getClass(), false, -1, mOnItemSelectedListener, objectId));
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
