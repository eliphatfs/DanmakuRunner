package cn.BHR.danmakurunner;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.Context;
import android.os.*;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.*;
import cn.BHR.danmakurunner.Dialogs.ErrorDialog;

public class NetActivity extends Activity {
	WebView webView;
	public static NetActivity instance;
	@SuppressLint("SetJavaScriptEnabled")
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		instance = this;
		webView = new WebView(this);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setAllowFileAccessFromFileURLs(true);
		webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
		webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.addJavascriptInterface(new NetInterface(), "bridge");
		webView.setWebViewClient(new WebViewClient() {
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
		webView.setWebChromeClient(new WebChromeClient(){
			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result)
			{
				new ErrorDialog(message).show(getFragmentManager(), "WebErr");
				result.cancel();
				return true;
			}
		});
		String dir = getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webView.getSettings().setDatabasePath(dir);
		webView.loadUrl("file:///android_asset/Docs/NetP/index.html");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(webView);
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

class NetInterface
{
	@JavascriptInterface
	public String getList()
	{
		File dir = new File(EditorActivity.projectPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		final File[] files = dir.listFiles();
		ArrayList<String> items = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				items.add(files[i].getName());
			}
			else if (files[i].isDirectory() && !EditorActivity.projecting) {
				String name = files[i].getName();
				if (name.equals("Misc") || name.equals("ExtraTextures")) {
					continue;
				}
				items.add(name + " [工程]");
			}
		}
		StringBuilder builder = new StringBuilder();
		for (String string : items) {
			builder.append(string).append('\n');
		}
		return builder.toString();
	}
	@JavascriptInterface
	public String getDanmaku(String name)
	{
		return DRHelper.encodeBase64File(mainPath + name);
	}
	@JavascriptInterface
	public void write(String base64, String path)
	{
		DRHelper.decoderBase64File(base64, mainPath + path);
	}
	String mainPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DanmakuRunner/";
	@JavascriptInterface
	public String getProjectDataList(String name)
	{
		StringBuilder builder = new StringBuilder();
		File[] files = new File(mainPath + name).listFiles();
		for (File file : files) {
			builder.append(file.getName()).append('\n');
		}
		return builder.toString();
	}
}