package smSimModel;
public class DVPs 
{
	CallCenter model;  // for accessing the clock
	// Implementation of timeSequence of biz state change
	private double[] changeTimeInterval = {0, 210, 120, 180, 30 , 30 , 870 };
	private double nxtTime=0;
	private int intervalIdx = 0;
	// Implementation of timeSequence of start biz
	private double changeTimeIntervalSB = 1440;
	private double nxtTimeSB=-1439.999;  //to let startBiz always executed after bizStateChange

		
	// Constructor
	public DVPs(CallCenter model) { this.model = model; }

	protected double bizStateChangeTS()  // getting a series of biz state change time points
	{
		double nxtTimeInterval = changeTimeInterval[intervalIdx];
		nxtTime=model.getClock()+nxtTimeInterval;
		if (intervalIdx<6) intervalIdx++;
		else intervalIdx=7-intervalIdx;
		
		return(nxtTime); 
	}
	
	protected double startBizTS()
	{
		nxtTimeSB+=changeTimeIntervalSB;
		return(nxtTimeSB); 
	}
}