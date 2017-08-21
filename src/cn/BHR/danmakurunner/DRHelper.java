package cn.BHR.danmakurunner;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;

import com.badlogic.gdx.math.Vector2;

import android.util.Base64;
import cn.BHR.danmakurunner.Dialogs.*;

public class DRHelper {
	public static int NewProj(int nowpt, int type, Vector2 position, Vector2 velocity, float scale)
	{
		/*while (Runner.proj[nowpt].active) {
			nowpt++;
			nowpt = nowpt < Runner.MAXPROJ ? nowpt : 0;
			count++;
			if (count == Runner.MAXPROJ) return -1;
		}*/
			//if (!Runner.proj[nowpt].active) {
		Runner.proj[nowpt].Init(type);
		Runner.proj[nowpt].whoAmI = nowpt;
		Runner.proj[nowpt].velocity=(velocity);
		Runner.proj[nowpt].scale = scale;
		Runner.proj[nowpt].setCenter(position);
		Runner.proj[nowpt].active = true;
		return nowpt;
	}
	static float rotation;
	static Vector2 radi = new Vector2();
	public static int NewProjL(int i, int type, Vector2 position, Vector2 velocity, float scale, float scaleL)
	{
		Runner.proj[i].Init(type);
		Runner.proj[i].whoAmI = i;
		Runner.proj[i].velocity = velocity;
		Runner.proj[i].scale = scale;
		Runner.proj[i].laserlike = true;
		Runner.proj[i].laserlikescale = scaleL;
		rotation = velocity.angle();
		radi.set(0, Runner.projTextures.get(type).getHeight());
		radi.rotate(rotation).scl(0.5f * scaleL);
		position.sub(radi);
		Runner.proj[i].position = position;
		Runner.proj[i].active = true;
		return i;
	}
	/*public static Float getInputFloat_notCalculated(String input)
	{
		if (parseFloat(input) > -1e37f)
			return parseFloat(input);
		else
			return Runner.runtimeFloats.get(input);
	}*/
	public static int parseInt(String toParse)
	{
		int len = toParse.length();
		int buf = 0;
		int i=0;
		boolean flagNeg = false;
		if (toParse.charAt(0)=='-')
		{
			flagNeg = true;
			i++;
		}
		for (; i<len; i++)
		{
			int flag = toParse.charAt(i) - 48;
			if (flag >= 0 && flag <= 9)
			{
				buf *= 10;
				buf += flag;
			}
			else throw new NumberFormatException();
		}
		if (flagNeg) return -buf;
		return buf;
	}
	private final static int decimalDot = '.' - 48;
	public static float parseFloat(String toParse)
	{
		int len = toParse.length();
		float buf = 0;
		int i=0;
		boolean flagNeg = false;
		boolean decimal = false;
		int decimalBase = 10;
		if (toParse.charAt(0)=='-')
		{
			flagNeg = true;
			i++;
		}
		for (; i<len; i++)
		{
			int flag = toParse.charAt(i) - 48;
			if (flag == decimalDot)
			{
				if (decimal) throw new NumberFormatException();
				decimal = true;
				continue;
			}
			else if (flag >= 0 && flag <= 9)
			{
				if (!decimal)
				{
					buf *= 10;
					buf += flag;
				}
				else if (decimalBase < 1000000)
				{
					buf += (float)flag / decimalBase;
					decimalBase *= 10;
				}
			}
			else return -1e38f;
		}
		if (flagNeg) return -buf;
		return buf;
	}
	public static String getMD5(String str) {
	    try {
	        // 生成一个MD5加密计算摘要
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        // 计算md5函数
	        md.update(str.getBytes());
	        // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
	        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
	        return new BigInteger(1, md.digest()).toString(16);
	    } catch (Exception e) {
	    	ErrorDialog dialog = new ErrorDialog("加密出现错误");
	    	dialog.show(EditorActivity.instance.getFragmentManager(), "MD5Error");
	    	return "";
	    }
	}
	public static String getBase64(String str)
	{
		return Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
	}
	public static String decodeBase64(String str)
	{
		return new String(Base64.decode(str, Base64.DEFAULT));
	}
	public static String encodeBase64File(String path)
	{
		try {
			File file = new File(path);
			FileInputStream inputFile = new FileInputStream(file);
			byte[] buffer = new byte[(int)file.length()];
			inputFile.read(buffer);
	    	inputFile.close();
	    	return Base64.encodeToString(buffer, Base64.DEFAULT);
		} catch (Exception e) {
			new ErrorDialog("读取文件出错："+e.getMessage()).show(EditorActivity.instance.getFragmentManager(), "Error");
			return "";
		}
	}
	public static void ScriptErr(Exception e)
	{
		/*PrintStream printStream = new PrintStream(new OutputStream() {
			ArrayList<Byte> buffer = new ArrayList<Byte>(256);
			@Override
			public void write(int arg0) throws IOException {
				buffer.add((byte)arg0);
			}
			@Override
			public String toString()
			{
				Byte[] bytes = buffer.toArray(new Byte[0]);
				byte[] data = new byte[bytes.length];
				for (int i = 0; i < bytes.length; i++) {
					data[i] = bytes[i];
				}
				return new String(data);
			}
		}){
			@Override
			public String toString()
			{
				return out.toString();
			}
		};
		e.printStackTrace(printStream);*/
		new ErrorDialog("Danmaku Runner Script 运行时错误 >_<\n" + e.toString()).show(RunnerActivity.instance.getFragmentManager(), "ScriptError");
	}
	public static void decoderBase64File(String base64Code,String savePath)
	{
		try {
			byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
			String path = savePath;
			int thispt = 0;
			while (new File(path).exists()) {
				thispt++;
				path = savePath + thispt;
			}
			new File(path).getParentFile().mkdirs();
			FileOutputStream out = new FileOutputStream(path);
			out.write(buffer);
			out.close();
		} catch (Exception e) {
			try {
				new ErrorDialog("写出文件出错："+e.getMessage()).show(EditorActivity.instance.getFragmentManager(), "Error");
			} catch (Exception e2) {
				new ErrorDialog("写出文件出错："+e.getMessage()).show(NetActivity.instance.getFragmentManager(), "Error");
			}
		}
	}
	public static void delete(File file) {  
        if (file.isFile()) {  
            file.delete();  
            return;  
        }
  
        if(file.isDirectory()){  
            File[] childFiles = file.listFiles();  
            if (childFiles == null || childFiles.length == 0) {  
                file.delete();  
                return;  
            }  
      
            for (int i = 0; i < childFiles.length; i++) {  
                delete(childFiles[i]);  
            }  
            file.delete();  
        }  
    }
	/*private static String[] operators = new String[]{"<",">","==","<=",">=","!="};
	public static boolean CalculateContidion(String Condition)
	{
		int currentOp = -1;
		for (int i=0; i<operators.length; i++)
		{
			if (Condition.indexOf(operators[i]) > 0)
			{
				currentOp = i;
				break;
			}
		}
		if (currentOp == -1) {
			ErrorDialog errorDialog = new ErrorDialog("条件中无比较操作符\r\n请确认是否删除条件中的空格以及是否将==误写为=");
			errorDialog.show(RunnerActivity.instance.getFragmentManager(), "errorMsg");
			return false;
		}
		
		String[] toCompare = Condition.split(operators[currentOp]);
		float value1 = Calculator.calculate(toCompare[0]);
		float value2 = Calculator.calculate(toCompare[1]);
		
		switch (currentOp) {
		case 0:
			return value1 < value2;
		case 1:
			return value1 > value2;
		case 2:
			return value1 == value2;
		case 3:
			return value1 <= value2;
		case 4:
			return value1 >= value2;
		case 5:
			return value1 != value2;

		default:
			ErrorDialog errorDialog = new ErrorDialog("未知错误：不正确的比较运算符");
			errorDialog.show(RunnerActivity.instance.getFragmentManager(), "errorMsg");
			return false;
		}
	}*/
}
