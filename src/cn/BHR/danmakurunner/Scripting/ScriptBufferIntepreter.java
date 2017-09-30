package cn.BHR.danmakurunner.Scripting;

public class ScriptBufferIntepreter {
	public static final int NEWPROJ = 1;
	public static final int NEWPROJL = 2;
	public static final int SHOOTPLR = 3;
	public static final int SHOOTPLRL = 4;
	public static final int SET = 16;
	public static final int SETO = 17;
	
	public static final int AI = 1;
	public static final int LIFE = 2;
	public static final int SHADER = 3;
	public static void ProcessCommand(int[] poolI, double[] poolD)
	{
		int nowptI = 0;
		int nowptD = 0;
		while (nowptI < poolI.length) {
			switch (poolI[nowptI++]) {
			case NEWPROJ:
				DanmakuInterface.Caller.NewProj(poolI[nowptI++], poolI[nowptI++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++]);
				break;
			case NEWPROJL:
				DanmakuInterface.Caller.NewProjL(poolI[nowptI++], poolI[nowptI++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++]);
				break;
			case SHOOTPLR:
				DanmakuInterface.Caller.ShootPlayer(poolI[nowptI++], poolI[nowptI++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++]);
				break;
			case SHOOTPLRL:
				DanmakuInterface.Caller.ShootPlayerL(poolI[nowptI++], poolI[nowptI++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++]);
				break;
			case SET:
				DanmakuInterface.Caller.SetProj(poolI[nowptI++], poolI[nowptI++], poolD[nowptD++], poolD[nowptD++]);
				break;
			case SETO:
				switch (poolI[nowptI++]) {
				case AI:
					DanmakuInterface.Caller.SetProjAI(poolI[nowptI++], poolI[nowptI++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++]);
					break;
				case LIFE:
					DanmakuInterface.Caller.SetProjLifeTime(poolI[nowptI++], poolI[nowptI++]);
					break;
				case SHADER:
					DanmakuInterface.Caller.SetProjShader(poolI[nowptI++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++], poolD[nowptD++]);
					break;

				default:
					break;
				}
				break;

			default:
				break;
			}
		}
	}
}
