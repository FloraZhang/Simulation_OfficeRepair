package smSimModel;
import smSimModel.CallCenter.BizState;
import ABSmodJ.ScheduledAction;
//Problems: how to let timeSequence extends automatically? Solved
class BizStateChange extends ScheduledAction 
{
	CallCenter model;  // reference to model object
	public BizStateChange(CallCenter model) { this.model = model; }

	
	public double timeSequence() 
	{ 
		return(model.dvp.bizStateChangeTS()); 
	}

	// Sched Event
	protected void actionEvent()
	{
	    if(model.getClock()%Constants.ONE_DAY == 0.0) model.bizState = BizState.BUSYAM;
	    else if(model.getClock()%Constants.ONE_DAY == 210.0) model.bizState = BizState.LUNCHTIME;
	    else if(model.getClock()%Constants.ONE_DAY == 330.0) model.bizState = BizState.BUSYPM;
	    else if(model.getClock()%Constants.ONE_DAY == 510.0) model.bizState = BizState.NOSERVICE;
	    else if(model.getClock()%Constants.ONE_DAY == 540.0) model.bizState = BizState.CLOSING;
	    else if(model.getClock()%Constants.ONE_DAY == 570.0) model.bizState = BizState.CLOSED;
	    else System.out.println("Invalid time to schedule business state change:"+model.getClock());
	}

}
