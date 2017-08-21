package cn.BHR.danmakurunner;

import android.annotation.SuppressLint;
import android.app.*;
import android.webkit.*;
import android.os.*;
import android.view.*;

public class DocumentActivity extends Activity {
	private WebView webView;
	public static DocumentActivity instance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		webView = new WebView(this);
		instance = this;
		//setContentView(R.layout.main);
		setContentView(webView);
		init();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void init(){
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		webView.loadUrl("file:///android_asset/Docs/index.html");
		//webView.loadUrl("javascript:test.toast()");
		//webView.addJavascriptInterface(new test(), "test");
		//webView.loadData("<html><body>138<script language=\"javascript\">var a=0;document.write(100);test.showtime();for(var j=0;j<10000;j++)a=test.getValue();test.showtime();</script></body></html>", "text/html", "utf-8");
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(webView.canGoBack())
            {
                webView.goBack();
                return true;
            }
            else
            {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
