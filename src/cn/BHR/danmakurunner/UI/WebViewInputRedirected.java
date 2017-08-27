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
	public static boolean IsNewInput;
	public WebViewInputRedirected(Context context) {
		super(context);
		Init(context);
	}
	
	public WebViewInputRedirected(Context context, AttributeSet attrs) {
		super(context, attrs);
		Init(context);
	}
	
	public WebViewInputRedirected(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		Init(context);
	}
	
	public WebViewInputRedirected(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		Init(context);
	}
	
	@SuppressWarnings("deprecation")
	public WebViewInputRedirected(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
		super(context, attrs, defStyleAttr, privateBrowsing);
		Init(context);
	}
	
	private void Init(Context context)
	{
	}
	
	@Override
	public boolean onCheckIsTextEditor() {
		return true;
	}
		
	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		InputConnection inputConnection = new RedirectedInputConnection(super.onCreateInputConnection(outAttrs), true);

		try {
			inputConnection.getTextBeforeCursor(1, 0);
			return inputConnection;
		} catch (Exception e) {
			IsNewInput = true;
			return super.onCreateInputConnection(outAttrs);
		}
		//return new RedirectedInputConnection(new EditText(getContext()).onCreateInputConnection(outAttrs), true);
	}
	
	class RedirectedInputConnection extends InputConnectionWrapper
	{
		public RedirectedInputConnection(InputConnection target, boolean mutable) {
			super(target, mutable);
		}
		@Override
		public boolean sendKeyEvent(KeyEvent event) {
			if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
				EditorActivity.editorMain.Backspace();
			}
			return super.sendKeyEvent(event);
		}
	}
	
}
