package gz.radar;

import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnHoverListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

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
			addAndroidApkField(new AndroidApkField("mOnClickListener", onClickListener.getClass(), false, -1, onClickListener,
					objectId));
		}
		View.OnLongClickListener onLongClickListener = xView.getOnLongClickListener();
		if (onLongClickListener != null) {
			String objectId = calculatObjectId(onLongClickListener, "mOnLongClickListener");
			addAndroidApkField(new AndroidApkField("mOnLongClickListener", onLongClickListener.getClass(), false, -1,
					onLongClickListener, objectId));
		}
		View.OnTouchListener mOnTouchListener = xView.getOnTouchListener();
		if (mOnTouchListener != null) {
			String objectId = calculatObjectId(mOnTouchListener, "mOnTouchListener");
			addAndroidApkField(new AndroidApkField("mOnTouchListener", mOnTouchListener.getClass(), false, -1,
					mOnTouchListener, objectId));
		}
		View.OnFocusChangeListener mOnFocusChangeListener = xView.getOnFocusChangeListener();
		if (mOnFocusChangeListener != null) {
			String objectId = calculatObjectId(mOnTouchListener, "mOnFocusChangeListener");
			addAndroidApkField(new AndroidApkField("mOnFocusChangeListener", mOnFocusChangeListener.getClass(), false, -1,
					mOnFocusChangeListener, objectId));
		}
		TextView.OnEditorActionListener mOnEditorActionListener = xView.getOnEditorActionListener();
		if (mOnEditorActionListener != null) {
			String objectId = calculatObjectId(mOnEditorActionListener, "mOnEditorActionListener");
			addAndroidApkField(new AndroidApkField("mOnEditorActionListener", mOnEditorActionListener.getClass(), false, -1,
					mOnFocusChangeListener, objectId));
		}
		
		Object mOnScrollChangeListener = xView.getOnScrollChangeListener();
		if (mOnScrollChangeListener != null) {
			String objectId = calculatObjectId(mOnScrollChangeListener, "mOnScrollChangeListener");
			addAndroidApkField(new AndroidApkField("mOnScrollChangeListener", mOnScrollChangeListener.getClass(), false, -1,
					mOnScrollChangeListener, objectId));
		}
		
		Object adapter = xView.getAdapter();
		if (adapter != null) {
			String objectId = calculatObjectId(adapter, "mAdapter");
			addAndroidApkField(new AndroidApkField("mAdapter", adapter.getClass(), false, -1, adapter, objectId));
		}
		
		OnScrollListener onScrollListener = xView.getOnScrollListener();
		if (onScrollListener != null) {
			String objectId = calculatObjectId(adapter, "mOnScrollListener");
			addAndroidApkField(new AndroidApkField("mOnScrollListener", onScrollListener.getClass(), false, -1, onScrollListener, objectId));
		}
		
		OnHoverListener mOnHoverListener = xView.getOnHoverListener();
		if (onScrollListener != null) {
			String objectId = calculatObjectId(mOnHoverListener, "mOnHoverListener");
			addAndroidApkField(new AndroidApkField("mOnHoverListener", mOnHoverListener.getClass(), false, -1, mOnHoverListener, objectId));
		}
		
		OnDragListener mOnDragListener = xView.getOnDragListener();
		if (mOnDragListener != null) {
			String objectId = calculatObjectId(mOnDragListener, "mOnDragListener");
			addAndroidApkField(new AndroidApkField("mOnDragListener", mOnDragListener.getClass(), false, -1, mOnDragListener, objectId));
		}
		
		OnItemClickListener mOnItemClickListener = xView.getOnItemClickListener();
		if (mOnItemClickListener != null) {
			String objectId = calculatObjectId(mOnItemClickListener, "mOnItemClickListener");
			addAndroidApkField(new AndroidApkField("mOnItemClickListener", mOnItemClickListener.getClass(), false, -1, mOnItemClickListener, objectId));
		}
		
		OnItemLongClickListener mOnItemLongClickListener = xView.getOnItemLongClickListener();
		if (mOnItemLongClickListener != null) {
			String objectId = calculatObjectId(mOnItemLongClickListener, "mOnItemLongClickListener");
			addAndroidApkField(new AndroidApkField("mOnItemLongClickListener", mOnItemLongClickListener.getClass(), false, -1, mOnItemLongClickListener, objectId));
		}
		
		OnItemSelectedListener mOnItemSelectedListener = xView.getOnItemSelectedListener();
		if (mOnItemSelectedListener != null) {
			String objectId = calculatObjectId(mOnItemSelectedListener, "mOnItemSelectedListener");
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
