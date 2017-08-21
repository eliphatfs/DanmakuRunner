package cn.BHR.danmakurunner.UI;

public class iEvent
{
	private iFunction _function;
	public void register(iFunction function)
	{
		_function = function;
	}
	public void happen()
	{
		if (_function != null)
			_function.iDo();
	}
}
