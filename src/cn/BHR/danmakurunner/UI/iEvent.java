package cn.BHR.danmakurunner.UI;

public class iEvent
{
	private iEventFunction _function;
	public void register(iEventFunction function)
	{
		_function = function;
	}
	public void happen()
	{
		if (_function != null)
			_function.iDo();
	}
}
