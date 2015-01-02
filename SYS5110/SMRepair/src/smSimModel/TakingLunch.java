package smSimModel;
import smSimModel.CallCenter.BizState;
import smSimModel.Staff.StaffStatus;
import ABSmodJ.ConditionalActivity;

public class TakingLunch extends ConditionalActivity {
	CallCenter model;  // reference to model object
	int staffid;
	
	public TakingLunch(CallCenter model) { this.model = model; }

	protected static boolean precondition(CallCenter simModel)
	{
		boolean returnValue = false;
		
	    if( simModel.bizState==BizState.LUNCHTIME 
	    		&& simModel.udp.ReadyForLunch()!=-1)
	    	 returnValue = true;
	    
		return(returnValue);
	}
	
	@Override
	protected double duration() {
		// TODO Auto-generated method stub
		return Constants.LUNCH_TIME;
	}

	@Override
	public void startingEvent() {
		// TODO Auto-generated method stub
		staffid=model.udp.ReadyForLunch();
		model.rStaff[staffid].staffStatus=StaffStatus.LUNCH;
		if(staffid<model.numStaffA) model.udp.RemoveStaffId(model.qStaffIdle[Constants.A], staffid);
		else model.udp.RemoveStaffId(model.qStaffIdle[Constants.B], staffid);
	}

	@Override
	protected void terminatingEvent() {
		// TODO Auto-generated method stub
		model.rStaff[staffid].staffStatus=StaffStatus.IDLE;
		model.rStaff[staffid].hadLunch=true;
		if(staffid<model.numStaffA) model.qStaffIdle[Constants.A].add(staffid);
		else model.qStaffIdle[Constants.B].add(staffid);
	}
	
}