package cn.BHR.danmakurunner.Projecting;

import java.io.*;
import java.util.TimerTask;

import android.os.Environment;
import cn.BHR.danmakurunner.*;
import cn.BHR.danmakurunner.Dialogs.*;
import cn.BHR.danmakurunner.Scripting.EngineMain;
import cn.BHR.danmakurunner.Scripting.OtherSTL;
import cn.BHR.danmakurunner.Scripting.MarkupClassDefs.GameObject;

public class ProjectingMain {
	public static String projectName = "";
	public static String fileName = "";
	public static String MiscPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DanmakuRunner/Misc/";
	public static void RunProject() {
		//StringBuilder codeGen = new StringBuilder();
		//codeGen.append("<html><body><script language=\"javascript\" crossorigin>").append(RunnerActivity.scriptSTL.toString()).append("</script>");
		try {
			//Thread.sleep(1000);
			String describerPath = EditorActivity.projectPath + "Describer";
			InputStream stream = new FileInputStream(describerPath);
			InputStreamReader treader = new InputStreamReader(stream, "utf-8");
			BufferedReader reader = new BufferedReader(treader);
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty() || line.startsWith("#")) {
					continue;
				}
				String[] cmd = line.split(" ");
				if (cmd[0].equals("Run")) {
					StringBuilder codeGen = new StringBuilder();
					BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(EditorActivity.projectPath + cmd[1]), "utf-8"));
					String line2 = null;
					while ((line2 = reader2.readLine()) != null) {
						codeGen.append(line2).append("\n");
					}
					reader2.close();
					try {
						EngineMain.v8.executeVoidScript(codeGen.toString(), cmd[1], 0);
					}
					catch (Exception e) {
						DRHelper.ScriptErr(e);
					}
				}
				else if (cmd[0].equals("RunP")) {
					StringBuilder codeGen = new StringBuilder();
					BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(EditorActivity.projectPath + cmd[1]), "utf-8"));
					String line2 = null;
					while ((line2 = reader2.readLine()) != null) {
						codeGen.append(line2).append("\n");
					}
					reader2.close();
					final String codeFinal = codeGen.toString();
					final String name = cmd[1];
					try {
						Runner.scriptThread.worker.schedule(new TimerTask() {
							@Override
							public void run() {
								OtherSTL.WillClear = true;
								EngineMain.v8.executeVoidScript(codeFinal, name, 0);
							}
						}, Integer.parseInt(cmd[2]));
						Runner.scriptThread.worker.schedule(new TimerTask() {
							@Override
							public void run() {
								for (com.badlogic.gdx.utils.Timer.Task task : OtherSTL.willClearTasks) {
									task.cancel();
								}
								OtherSTL.WillClear = false;
							}
						}, Integer.parseInt(cmd[3]));
					}
					catch (Exception e) {
						DRHelper.ScriptErr(e);
					}
				}
				else if (cmd[0].equals("Reg")) {
					StringBuilder codeGen = new StringBuilder();
					BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(EditorActivity.projectPath + cmd[1]), "utf-8"));
					String line2 = null;
					while ((line2 = reader2.readLine()) != null) {
						codeGen.append(line2).append("\n");
					}
					reader2.close();
					GameObject.markupIntepreter.Run(codeGen.toString());
				}
				else if (!cmd[0].isEmpty()) {
					reader.close();
					treader.close();
					stream.close();
					throw new Exception(cmd[0] + " is not a valid command");
				}
			}
			reader.close();
			treader.close();
			stream.close();
			//codeGen.append("<script>setInterval(function(){NewProjRandom();}, 500);</script></body></html>");
			//Log.i("Gen", codeGen.toString());
			//scriptkit.loadDataWithBaseURL("about:blank", codeGen.toString(), "text/html", "utf-8", null);
		} catch (Exception e) {
			new ErrorDialog("Build Project Failed: " + e.getMessage()).show(RunnerActivity.instance.getFragmentManager(), "Error");
		}
	}
}
