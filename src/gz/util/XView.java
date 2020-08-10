package gz.util;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class XView {

	private final View view;

	public XView(View view) {
		this.view = view;
	}
	
	public Object getListenerInfo() {
		try {
			return X.invokeObject(view, "getListenerInfo");
		}catch(Exception e) {
			XLog.appendText(e);
		}
		return null;
	}
	
	public View.OnClickListener getOnClickListener() {
		try {
			Object listenerInfo = getListenerInfo();
			return X.getField(listenerInfo, "mOnClickListener");
		}catch(Exception e) {
			XLog.appendText(e);
		}
		return null;
	}
	
	public View.OnLongClickListener getOnLongClickListener() {
		try {
			Object listenerInfo = getListenerInfo();
			return X.getField(listenerInfo, "mOnLongClickListener");
		}catch(Exception e) {
			XLog.appendText(e);
		}
		return null;
	}
	
	public View.OnTouchListener getOnTouchListener() {
		try {
			Object listenerInfo = getListenerInfo();
			return X.getField(listenerInfo, "mOnTouchListener");
		}catch(Exception e) {
			XLog.appendText(e);
		}
		return null;
	}
	
	public View.OnFocusChangeListener getOnFocusChangeListener() {
		try {
			Object listenerInfo = getListenerInfo();
			return X.getField(listenerInfo, "mOnFocusChangeListener");
		}catch(Exception e) {
			XLog.appendText(e);
		}
		return null;
	}
	
	//View.OnScrollChangeListener
	public Object getOnScrollChangeListener() {
		try {
			Object listenerInfo = getListenerInfo();
			return X.getField(listenerInfo, "mOnScrollChangeListener");
		}catch(Exception e) {
			XLog.appendText(e);
		}
		return null;
	}
	
	public AbsListView.OnScrollListener getOnScrollListener() {
		try {
			return X.getField(view, "mOnScrollListener");
		}catch(Exception e) {
			XLog.appendText(e);
		}
		return null;
	}
	
	public View.OnHoverListener getOnHoverListener() {
		try {
			Object listenerInfo = getListenerInfo();
			return X.getField(listenerInfo, "mOnHoverListener");
		}catch(Exception e) {
			XLog.appendText(e);
		}
		return null;
	}
	
	public View.OnDragListener getOnDragListener() {
		try {
			Object listenerInfo = getListenerInfo();
			return X.getField(listenerInfo, "mOnDragListener");
		}catch(Exception e) {
			XLog.appendText(e);
		}
		return null;
	}
	
	public TextView.OnEditorActionListener getOnEditorActionListener() {
		if (view instanceof TextView) {
			try {
				//android.widget.Editor
                Object editor = X.getField(view, "mEditor");
                if (editor != null) {
                    Object mInputContentType = X.getField(editor, "mInputContentType");
                    if (mInputContentType != null) {
                        TextView.OnEditorActionListener onEditorActionListener = X.getField(mInputContentType, "onEditorActionListener");
                        return onEditorActionListener;
                    }
                }
			}catch(Exception e) {
				XLog.appendText(e);
			}
		}
		return null;
	}
	
	public Object getAdapter() {
		try{
            return X.invokeObject(view, "getAdapter");
        }catch (Exception e){}
		return null;
	}
	
	public OnItemClickListener getOnItemClickListener() {
		try {
			return X.getField(view, "mOnItemClickListener");
		}catch(Exception e) {
		}
		return null;
	}
	
	public OnItemLongClickListener getOnItemLongClickListener() {
		try {
			return X.getField(view, "mOnItemLongClickListener");
		}catch(Exception e) {
		}
		return null;
	}
	
	public OnItemSelectedListener getOnItemSelectedListener() {
		try {
			return X.getField(view, "mOnItemSelectedListener");
		}catch(Exception e) {
		}
		return null;
	}
	
	public boolean isVisible() {
		return view.getVisibility() == View.VISIBLE;
	}
	
	public String getViewText() {
		if (view instanceof TextView) {
            return ((TextView) view).getText().toString();
        }
		return "";
	}
}
