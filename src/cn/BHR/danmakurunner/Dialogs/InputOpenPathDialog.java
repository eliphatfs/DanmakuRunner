package cn.BHR.danmakurunner.Dialogs;

import java.io.*;
import java.util.ArrayList;
import android.app.*;
import android.os.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.*;
import android.view.View.OnCreateContextMenuListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import cn.BHR.danmakurunner.*;
import cn.BHR.danmakurunner.Projecting.ProjectingMain;
import cn.BHR.danmakurunner.UI.DRSEditText;

public class InputOpenPathDialog extends DialogFragment {
	public static ListView listView;
	static ListAdapter adapter;
	public static ArrayList<String> items = new ArrayList<String>();
	public static ArrayList<String> displayItems = new ArrayList<String>();
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		listView = new ListView(EditorActivity.instance);
		File dir = new File(EditorActivity.projectPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		final File[] files = dir.listFiles();
		items.clear();
		displayItems.clear();
		if (EditorActivity.projecting) {
			displayItems.add("退出当前工程");
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				items.add(files[i].getName());
				displayItems.add(files[i].getName());
			}
			else if (files[i].isDirectory() && !EditorActivity.projecting) {
				String name = files[i].getName();
				if (name.equals("Misc") || name.equals("ExtraTextures")) {
					continue;
				}
				items.add(name);
				displayItems.add(name + " [工程]");
			}
		}
		adapter = new ArrayAdapter<String>(EditorActivity.instance, android.R.layout.simple_list_item_1, displayItems);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				if (id == -1) {
					return;
				}
				if (EditorActivity.projecting && id == 0) {
					EditorActivity.projecting = false;
					EditorActivity.projectPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DanmakuRunner/";
					ProjectingMain.fileName = ProjectingMain.projectName = "";
					EditorActivity.codeInside = "";
					EditorActivity.updateCodeHandler.sendEmptyMessage(0);
					EditorActivity.updateTitleHandler.sendEmptyMessage(0);
					InputOpenPathDialog.this.dismiss();
					return;
				}
				if (EditorActivity.projecting) {
					id--;
				}
				DRSEditText editText = EditorActivity.editorMain;
				File pFile = new File(EditorActivity.projectPath + items.get((int)id));
				if (pFile.isFile()) {
					try {
						InputStream stream = new FileInputStream(pFile);
						InputStreamReader treader = new InputStreamReader(stream, "utf-8");
						BufferedReader reader = new BufferedReader(treader);
						String line = null;
						StringBuilder builder = new StringBuilder();
						while ((line = reader.readLine()) != null) {
							builder.append(line);
							builder.append("\n");
						}
						editText.setText(builder.toString());
						if (EditorActivity.projecting) {
							ProjectingMain.fileName = pFile.getName();
							Message message = new Message();
							Bundle data = new Bundle();
							data.putCharSequence("title", "Danmaku Runner - " + ProjectingMain.projectName + " - " + pFile.getName());
							message.setData(data);
							EditorActivity.updateTitleHandler.sendMessage(message);
						}
						stream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if (pFile.isDirectory()) {
					Message message = new Message();
					Bundle data = new Bundle();
					data.putCharSequence("title", "Danmaku Runner - " + pFile.getName() + " - Unsaved File");
					EditorActivity.projecting = true;
					EditorActivity.projectPath += pFile.getName() + "/";
					EditorActivity.codeInside = "";
					EditorActivity.updateCodeHandler.sendEmptyMessage(0);
					ProjectingMain.projectName = pFile.getName();
					message.setData(data);
					EditorActivity.updateTitleHandler.sendMessage(message);
				}
				InputOpenPathDialog.this.dismiss();
			}
			
		});
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				menu.add(0, 0, 0, "删除");
				menu.getItem(0).setOnMenuItemClickListener(new OnMenuItemClickListener() {
					
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
						switch (item.getItemId()) {
						case 0:
							final int id = (int) info.id;
							new ConfirmDialog("确认删除？", "即将删除 " + InputOpenPathDialog.items.get(EditorActivity.projecting?id-1:id) + " ，此操作不可逆，请仔细确认") {
								
								@SuppressWarnings("unchecked")
								@Override
								public void OnOKPressed() {
									try {
										File file = new File(EditorActivity.projectPath + InputOpenPathDialog.items.get(EditorActivity.projecting?id-1:id));
										DRHelper.delete(file);
										File dir = new File(EditorActivity.projectPath);
										final File[] files = dir.listFiles();
										items.clear();
										displayItems.clear();
										if (EditorActivity.projecting) {
											displayItems.add("退出当前工程");
										}
										for (int i = 0; i < files.length; i++) {
											if (files[i].isFile()) {
												items.add(files[i].getName());
												displayItems.add(files[i].getName());
											}
											else if (files[i].isDirectory() && !EditorActivity.projecting) {
												String name = files[i].getName();
												if (name.equals("Misc") || name.equals("ExtraTextures")) {
													continue;
												}
												items.add(name);
												displayItems.add(name + " [工程]");
											}
										}
										((ArrayAdapter<String>)InputOpenPathDialog.listView.getAdapter()).notifyDataSetChanged();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}.show();
							break;

						default:
							break;
						}
						return false;
					}
				});
			}
		});
	    AlertDialog.Builder builder = 
	        new AlertDialog.Builder(EditorActivity.instance);
	    builder.setTitle("选择要打开的代码（长按可以进行删除操作）")
	    	   .setView(listView);
	    /*builder.setItems(items.toArray(new String[items.size()]), new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            // which 下标从0开始
				EditText editText = EditorActivity.editorMain;
				try {
					InputStream stream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/DanmakuRunner/" + items.get(which));
					InputStreamReader treader = new InputStreamReader(stream, "utf-8");
					BufferedReader reader = new BufferedReader(treader);
					String line = null;
					editText.setText("");
					while ((line = reader.readLine()) != null) {
						editText.append(line);
						editText.append("\n");
					}
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	    });*/
		return builder.create();
	}
}
