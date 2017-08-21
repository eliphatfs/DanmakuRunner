package cn.BHR.danmakurunner.Dialogs;

import java.io.*;

import android.app.*;
import android.content.*;
import android.widget.EditText;
import cn.BHR.danmakurunner.*;

public class OtherDialogs {
	public static void ChooseCodeParaDialog() {
	    final String[] items = { "每隔一段时间执行一次的代码" };
	    AlertDialog.Builder listDialog = 
	        new AlertDialog.Builder(EditorActivity.instance);
	    listDialog.setTitle("选择要插入的代码片段");
	    listDialog.setItems(items, new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            // which 下标从0开始
				EditText editText = EditorActivity.editorMain;
				try {
					InputStream stream = EditorActivity.instance.getAssets().open("CodePara/"+which+".js");
					InputStreamReader treader = new InputStreamReader(stream, "utf-8");
					BufferedReader reader = new BufferedReader(treader);
					String line = null;
					while ((line = reader.readLine()) != null) {
						editText.getEditableText().insert(editText.getSelectionStart(), line);
						editText.getEditableText().insert(editText.getSelectionStart(), "\n");
					}
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	    });
	    listDialog.show();
	}
	public static void ChooseCodeFuncDialog() {
	    final String[] items = { "NewProj生成一颗子弹" };
	    AlertDialog.Builder listDialog = 
	        new AlertDialog.Builder(EditorActivity.instance);
	    listDialog.setTitle("选择要插入的函数");
	    listDialog.setItems(items, new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            // which 下标从0开始
	        	EditText editText = EditorActivity.editorMain;
				try {
					InputStream stream = EditorActivity.instance.getAssets().open("CodeFunc/"+which+".js");
					InputStreamReader treader = new InputStreamReader(stream, "utf-8");
					BufferedReader reader = new BufferedReader(treader);
					String line = null;
					while ((line = reader.readLine()) != null) {
						editText.getEditableText().insert(editText.getSelectionStart(), line);
						editText.getEditableText().insert(editText.getSelectionStart(), "\n");
					}
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	    });
	    listDialog.show();
	}
	
	public static Dialog showWaitingDialog(final Thread thread) {
	    /* 等待Dialog具有屏蔽其他控件的交互能力
	     * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
	     * 下载等事件完成后，主动调用函数关闭该Dialog
	     */
	    ProgressDialog waitingDialog= 
	        new ProgressDialog(EditorActivity.instance);
	    waitingDialog.setTitle("上传中");
	    waitingDialog.setMessage("请稍等……");
	    waitingDialog.setIndeterminate(true);
	    waitingDialog.setCancelable(false);
	    thread.start();
	    waitingDialog.show();
	    return waitingDialog;
	}
	
	public static Dialog showWaitingDialog(String title) {
	    /* 等待Dialog具有屏蔽其他控件的交互能力
	     * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
	     * 下载等事件完成后，主动调用函数关闭该Dialog
	     */
	    ProgressDialog waitingDialog= 
	        new ProgressDialog(EditorActivity.instance);
	    waitingDialog.setTitle(title);
	    waitingDialog.setMessage("请稍等……");
	    waitingDialog.setIndeterminate(true);
	    waitingDialog.setCancelable(false);
	    waitingDialog.show();
	    return waitingDialog;
	}
	
	public static Dialog showWaitingDialog(String title, Activity block) {
	    /* 等待Dialog具有屏蔽其他控件的交互能力
	     * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
	     * 下载等事件完成后，主动调用函数关闭该Dialog
	     */
	    ProgressDialog waitingDialog= 
	        new ProgressDialog(block);
	    waitingDialog.setTitle(title);
	    waitingDialog.setMessage("请稍等……");
	    waitingDialog.setIndeterminate(true);
	    waitingDialog.setCancelable(false);
	    waitingDialog.show();
	    return waitingDialog;
	}
	
	public static Dialog showWaitingDialog(String title, final Thread thread) {
	    /* 等待Dialog具有屏蔽其他控件的交互能力
	     * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
	     * 下载等事件完成后，主动调用函数关闭该Dialog
	     */
	    ProgressDialog waitingDialog= 
	        new ProgressDialog(EditorActivity.instance);
	    waitingDialog.setTitle(title);
	    waitingDialog.setMessage("请稍等……");
	    waitingDialog.setIndeterminate(true);
	    waitingDialog.setCancelable(false);
	    thread.start();
	    waitingDialog.show();
	    return waitingDialog;
	}
	
	public static Dialog showWaitingDialog(String title, final Thread thread, Activity block) {
	    /* 等待Dialog具有屏蔽其他控件的交互能力
	     * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
	     * 下载等事件完成后，主动调用函数关闭该Dialog
	     */
	    ProgressDialog waitingDialog= 
	        new ProgressDialog(block);
	    waitingDialog.setTitle(title);
	    waitingDialog.setMessage("请稍等……");
	    waitingDialog.setIndeterminate(true);
	    waitingDialog.setCancelable(false);
	    thread.start();
	    waitingDialog.show();
	    return waitingDialog;
	}
}
