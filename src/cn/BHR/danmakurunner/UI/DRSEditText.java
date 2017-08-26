package cn.BHR.danmakurunner.UI;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import cn.BHR.danmakurunner.EditorActivity;

@SuppressLint("SetJavaScriptEnabled")
public class DRSEditText {
	public WebView DWebView;
	public static Timer Worker = new Timer();
	public DRSEditText(WebView context) {
		if (Worker == null) {
			Worker = new Timer();
		}
		DWebView = context;
		Init();
	}
	
	public DRSEditInterface DRSI;
	public EditOps Eops;
	public void Init()
	{
		Worker.schedule(new TimerTask() {
			@Override
			public void run() {
				DRSI = new DRSEditInterface();
				Eops = new EditOps(DRSEditText.this);
				DRSI.ready = false;
				EditorActivity.updateCodeHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						DWebView.getSettings().setJavaScriptEnabled(true);
						DWebView.addJavascriptInterface(DRSI, "DRSI");
						DWebView.loadUrl("file:///android_asset/CodeMirror/index.html");
					}
				}, 1);
				WaitReady();
			}
		}, 0);
	}
	public int getSelectionStart()
	{
		return DRSI.cursor;
	}
	public int getSelectionEnd()
	{
		return getSelectionStart();
	}
	public void setSelection(final int start, final int end)
	{
		Worker.schedule(new TimerTask() {
			@Override
			public void run() {
				DRSI.ready = false;
				EditorActivity.updateCodeHandler.post(new Runnable() {
					@Override
					public void run() {
						DWebView.loadUrl("javascript:setCursorPos("+ start +")");
					}
				});
				WaitReady();
			}
		}, 0);
		
	}
	public String getText()
	{
		return DRSI.content;
	}
	public void setText(final String string)
	{
		Worker.schedule(new TimerTask() {
			@Override
			public void run() {
				DRSI.ready = false;
				EditorActivity.updateCodeHandler.post(new Runnable() {
					@Override
					public void run() {
						DWebView.loadUrl("javascript:setContent('"+string.replace("\n", "\\n").replace("'", "\\'")+"')");
					}
				});
				WaitReady();
			}
		}, 0);
		
	}
	public void append(final String string)
	{
		Worker.schedule(new TimerTask() {
			@Override
			public void run() {
				DRSI.ready = false;
				EditorActivity.updateCodeHandler.post(new Runnable() {
					@Override
					public void run() {
						DWebView.loadUrl("javascript:appendContent('"+string.replace("\n", "\\n").replace("'", "\\'")+"')");
					}
				});
				WaitReady();
			}
		}, 0);
	}
	public void Backspace()
	{
		Worker.schedule(new TimerTask() {
			@Override
			public void run() {
				DRSI.needDelete++;
			}
		}, 0);
	}
	public EditOps getEditableText()
	{
		return Eops;
	}
	public void WaitReady()
	{
		while(!DRSI.ready);
	}
	public static class EditOps
	{
		public DRSEditText Parent;
		public EditOps(DRSEditText parent)
		{
			Parent = parent;
		}
		public void insert(final int pos, final CharSequence cs)
		{
			Worker.schedule(new TimerTask() {
				@Override
				public void run() {
					Parent.DRSI.ready = false;
					EditorActivity.updateCodeHandler.post(new Runnable() {
						@Override
						public void run() {
							String string = cs.toString();
							Parent.DWebView.loadUrl("javascript:insertContent("+pos+",'"+string.replace("\n", "\\n").replace("'", "\\'")+"')");
						}
					});
					Parent.WaitReady();
				}
			}, 0);
		}
		@Override
		public String toString() {
			return Parent.getText();
		}
	}
	public static class DRSEditInterface
	{
		public int needDelete = 0;
		public boolean ready = false;
		public int cursor = 0;
		public String content = "";
		@JavascriptInterface
		public void notifyReady()
		{
			ready = true;
		}
		@JavascriptInterface
		public void sendCursorPos(String pos)
		{
			cursor = Integer.parseInt(pos);
		}
		@JavascriptInterface
		public void sendContent(String code)
		{
			content = code;
		}
		@JavascriptInterface
		public String getNeedDeleteCount()
		{
			String toret = String.valueOf(needDelete);
			needDelete = 0;
			return toret;
		}
	}
}
