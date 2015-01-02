package smSimModel;
import smSimModel.Staff.StaffStatus;
import ABSmodJ.ScheduledAction;
//Problems: how to let timeSequence extends automatically? Solved.
class StartBiz extends ScheduledAction 
{
	CallCenter model;  // reference to model object
	public StartBiz(CallCenter model) { this.model = model; }

	public double timeSequence() 
	{ 
		return model.dvp.startBizTS();
	}

	// Sched Event
	protected void actionEvent()
	{
	    if(/*model.getClock()%1440 == 0.001 &&*/ model.getClock()>0.0) 
	    {
	    	for(int staffid=0;staffid<model.numStaffA+model.numStaffB;staffid++)
	    	{
	    		model.rStaff[staffid].hadLunch=false;
	    		
	    		if(model.rStaff[staffid].staffStatus==StaffStatus.OFF && model.rStaff[staffid].hadJob==false)
	    		{
	    			model.rStaff[staffid].staffStatus=StaffStatus.IDLE;
	    			if(staffid<model.numStaffA) model.qStaffIdle[Constants.A].add(staffid);
	    			else model.qStaffIdle[Constants.B].add(staffid);
	    		}
	    		else if (model.rStaff[staffid].staffStatus==StaffStatus.OFF && model.rStaff[staffid].hadJob==true)
	    			model.upStartServing(staffid);
	    	}
	    }

	    else System.out.println("Invalid time to schedule start of business:"+model.getClock()); //Sanity check.
	}

}
