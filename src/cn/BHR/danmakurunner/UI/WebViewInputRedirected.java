package cn.BHR.danmakurunner.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.webkit.WebView;
import cn.BHR.danmakurunner.EditorActivity;

public class WebViewInputRedirected extends WebView {
	public WebViewInputRedirected(Context context) {
		super(context);
	}
	
	public WebViewInputRedirected(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public WebViewInputRedirected(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	public WebViewInputRedirected(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}
	
	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		return new RedirectedInputConnection(super.onCreateInputConnection(outAttrs), true);
	}
	
	static class RedirectedInputConnection extends InputConnectionWrapper {
        public RedirectedInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                EditorActivity.editorMain.Backspace();
                return true;
            }
            return super.sendKeyEvent(event);
        }

    }
}
