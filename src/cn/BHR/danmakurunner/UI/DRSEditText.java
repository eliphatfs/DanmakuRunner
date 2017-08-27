package cn.BHR.danmakurunner.UI;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
		DWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				//new Msgbox(message).show(EditorActivity.instance.getFragmentManager(), "JsAlert");
				result.confirm();
				return true;
			}
		});
		DWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= 24) {
                    view.loadUrl(request.getUrl().toString());
                } else {
                    view.loadUrl(request.toString());
                }
                return true;
            }
		});
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
						_adaptedRunJavascript("setCursorPos("+ start +")");
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
				//new Msgbox("SETCONTENT SCHEDULE ACTIATED").show(EditorActivity.instance.getFragmentManager(), "Msgbox");
				DRSI.ready = false;
				EditorActivity.instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//new Msgbox("SETCONTENT CALL").show(EditorActivity.instance.getFragmentManager(), "Msgbox");
						_adaptedRunJavascript("setContent('"+string.replace("\n", "\\n").replace("'", "\\'")+"')");
					}
				});
				WaitReady();
			}
		}, 0);
		//new Msgbox("SETCONTENT SCHEDULED").show(EditorActivity.instance.getFragmentManager(), "Msgbox");
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
						_adaptedRunJavascript("appendContent('"+string.replace("\n", "\\n").replace("'", "\\'")+"')");
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
		EditorActivity.updateCodeHandler.post(new Runnable() {
			@Override
			public void run() {
				DWebView.setFocusable(true);
				DWebView.setFocusableInTouchMode(true);
				DWebView.requestFocus();
				DWebView.requestFocusFromTouch();
				InputMethodManager imm = (InputMethodManager) EditorActivity.instance.getSystemService(EditorActivity.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
			}
		});
		int time = 0;
		while(!DRSI.ready)
		{
			System.out.println("waited for notify: "+ time * 20 + "ms");
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			time++;
		}
	}
	/**
	 * Please Run On UI Thread
	 */
	private void _adaptedRunJavascript(String code)
	{
		if (Build.VERSION.SDK_INT >= 24) {
			DWebView.evaluateJavascript(code, null);
			//Toast.makeText(EditorActivity.instance, "NEWAPI", Toast.LENGTH_SHORT).show();
		} else {
			DWebView.loadUrl("javascript:" + code);
			//Toast.makeText(EditorActivity.instance, "OLDAPI", Toast.LENGTH_SHORT).show();
		}
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
							Parent._adaptedRunJavascript("insertContent("+pos+",'"+string.replace("\n", "\\n").replace("'", "\\'")+"')");
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
