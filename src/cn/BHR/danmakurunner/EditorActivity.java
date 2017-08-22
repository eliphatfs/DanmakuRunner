package cn.BHR.danmakurunner;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import cn.BHR.danmakurunner.Dialogs.*;
import cn.BHR.danmakurunner.Projecting.ProjectingMain;
import android.content.*;
import java.io.*;

public class EditorActivity extends Activity {
	public static EditorActivity instance;
	public static EditText editorMain;
	public static String codeInside="";
	public static boolean projecting = false;
	public static String projectPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DanmakuRunner/";
	public static UpdateCodeHandler updateCodeHandler;
	public static UpdateTitleHandler updateTitleHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		updateCodeHandler = new UpdateCodeHandler();
		updateTitleHandler = new UpdateTitleHandler();
		setContentView(R.layout.editor);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		editorMain = (EditText)(findViewById(R.id.editText));
		editorMain.setHorizontallyScrolling(true);
		//Toast.makeText(this, Environment.getDataDirectory().getAbsolutePath().toString(), Toast.LENGTH_SHORT).show();
	}
	public void OnButtonClick_Save(View view)
	{
		if (!projecting) {
			InputSavePathDialog dialog = new InputSavePathDialog();
			dialog.show(getFragmentManager(), "saveFile");
		}
		else if (ProjectingMain.fileName.isEmpty()) {
			InputPathDialog dialog2 = new InputPathDialog();
			dialog2.show(getFragmentManager(), "inputNewFilename");
		}
		else {
			FileOutputStream stream;
			try {
				stream = new FileOutputStream(projectPath + ProjectingMain.fileName);
				stream.write(editorMain.getText().toString().getBytes());
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void OnButtonClick_Open(View view)
	{
		InputOpenPathDialog dialog = new InputOpenPathDialog();
		dialog.show(getFragmentManager(), "openFile");
		if (projecting && (!ProjectingMain.fileName.isEmpty())) {
			FileOutputStream stream;
			try {
				stream = new FileOutputStream(projectPath + ProjectingMain.fileName);
				stream.write(editorMain.getText().toString().getBytes());
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Toast.makeText(this, ProjectingMain.fileName + " 已经自动保存", Toast.LENGTH_LONG).show();
		}
	}
	public void OnButtonClick_Run(View view)
	{
		codeInside = editorMain.getText().toString();
		if (projecting && (!ProjectingMain.fileName.isEmpty())) {
			FileOutputStream stream;
			try {
				stream = new FileOutputStream(projectPath + ProjectingMain.fileName);
				stream.write(editorMain.getText().toString().getBytes());
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Toast.makeText(this, ProjectingMain.fileName + " 已经自动保存", Toast.LENGTH_LONG).show();
		}
		Intent runner = new Intent(this, RunnerActivity.class);
		startActivity(runner);
		//RunnerActivity.start();
	}
	public void OnButtonClick_New(View view)
	{
		new ConfirmDialog("确认新建New", "注意：所有未保存的数据将会丢失。") {
			@Override
			public void OnOKPressed() {
				codeInside = "";
				editorMain.setText("");
				InputPathDialog.workingPath = "";
				if (projecting) {
					ProjectingMain.fileName = "";
					Message message = new Message();
					Bundle data = new Bundle();
					data.putCharSequence("title", "Danmaku Runner - " + ProjectingMain.projectName + " - Unsaved File");
					message.setData(data);
					EditorActivity.updateTitleHandler.sendMessage(message);
				}
			}
		}.show();
	}
	
	public void OnButtonClick_Inserts(View view)
	{
		switch (view.getId()) {
		case R.id.buttonInsertTab:
			editorMain.getEditableText().insert(_findLineStart(editorMain.getSelectionStart()), "\t");
			break;
		default:
			editorMain.getEditableText().insert(editorMain.getSelectionStart(), ((Button)view).getText());
			break;
		}
	}
	
	private int _findLineStart(int position)
	{
		String string = editorMain.getText().toString();
		System.out.println(string);
		int i;
		for (i=position; i>0; i--)
		{
			if (string.charAt(i - 1) == '\n')
				return i;
		}
		return 0;
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		codeInside = editorMain.getText().toString();
		try{
			FileOutputStream stream = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DanmakuRunner/Misc/codeState");
			stream.write(codeInside.getBytes());
			stream.close();
			stream = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DanmakuRunner/Misc/pointerState");
			DataOutputStream output = new DataOutputStream(stream);
			output.writeInt(editorMain.getSelectionStart());
			output.writeInt(editorMain.getSelectionEnd());
			output.writeBoolean(projecting);
			output.writeUTF(projectPath);
			output.writeUTF(ProjectingMain.projectName);
			output.writeUTF(ProjectingMain.fileName);
			output.close();
			stream.close();
		}
		catch(Exception e){}
	}
	@Override
	public void onResume()
	{
		super.onResume();
		try{
			FileInputStream stream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DanmakuRunner/Misc/codeState");
			InputStreamReader treader = new InputStreamReader(stream, "utf-8");
			BufferedReader reader = new BufferedReader(treader);
			StringBuilder builder = new StringBuilder(stream.available() / 2);
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append('\n');
			}
			codeInside = builder.toString();
			editorMain = (EditText)(findViewById(R.id.editText));
			editorMain.setText(codeInside);
			stream.close();
			
			stream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DanmakuRunner/Misc/pointerState");
			DataInputStream input = new DataInputStream(stream);
			editorMain.setSelection(input.readInt(), input.readInt());
			projecting = input.readBoolean();
			projectPath = input.readUTF();
			if (projecting) {
				ProjectingMain.projectName = input.readUTF();
				ProjectingMain.fileName = input.readUTF();
				Message message = new Message();
				Bundle data = new Bundle();
				data.putCharSequence("title", "Danmaku Runner - " + ProjectingMain.projectName + " - " + (ProjectingMain.fileName.isEmpty() ? "Unsaved File" : ProjectingMain.fileName));
				message.setData(data);
				EditorActivity.updateTitleHandler.sendMessage(message);
			}
			
			input.close();
			stream.close();
		}
		catch(Exception e){}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate main_menu.xml 
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.editor_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.insertCodePara:
				OtherDialogs.ChooseCodeParaDialog();
				return true;
			case R.id.insertFunction:
				OtherDialogs.ChooseCodeFuncDialog();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public static class UpdateCodeHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			editorMain.setText(codeInside);
		}
	}
	
	public static class UpdateTitleHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			EditorActivity.instance.setTitle(msg.getData().getCharSequence("title", "Danmaku Runner"));
		}
	}
}
