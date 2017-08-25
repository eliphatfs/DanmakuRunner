package cn.BHR.danmakurunner.UI;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.webkit.WebView;
import cn.BHR.danmakurunner.EditorActivity;

public class WebViewInputRedirected extends WebView {
	public static boolean IsNewInput = false;
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
	
	@SuppressWarnings("deprecation")
	public WebViewInputRedirected(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
		super(context, attrs, defStyleAttr, privateBrowsing);
	}
	
	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		InputConnection inputConnection = new RedirectedInputConnection(super.onCreateInputConnection(outAttrs), true);
		try {
			inputConnection.getHandler();
			return inputConnection;
		} catch (Exception e) {
			IsNewInput = true;
			return super.onCreateInputConnection(outAttrs);
		}
	}
	
	static class RedirectedInputConnection extends InputConnectionWrapper {
		Handler handler;
        public RedirectedInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
            handler = new Handler();
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
