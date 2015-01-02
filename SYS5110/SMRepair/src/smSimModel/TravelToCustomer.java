package smSimModel;
import smSimModel.Call.EquipType;
import smSimModel.CallCenter.BizState;
import smSimModel.Staff.StaffStatus;
import ABSmodJ.ConditionalActivity;

public class TravelToCustomer extends ConditionalActivity 
{
	CallCenter model;  // reference to model object
	int staffid;
	
	public TravelToCustomer(CallCenter model) { this.model = model; }

	protected static boolean precondition(CallCenter simModel)
	{
		boolean returnValue = false;
				
	    if( (simModel.bizState==BizState.BUSYAM || simModel.bizState==BizState.BUSYPM ||simModel.bizState==BizState.LUNCHTIME)
	    		&& (simModel.udp.CanServiceCall(simModel.qCallLine)||simModel.udp.CanServiceCall(simModel.qPriorityLine)))
	    	 returnValue = true;
	    
		return(returnValue);
	}
	
	@Override
	public void startingEvent() {
		// TODO Auto-generated method stub
		Call icCall=new Call();
		if(model.udp.CanServiceCall(model.qPriorityLine))
			icCall=model.udp.FindServiceCall(model.qPriorityLine);
		else icCall=model.udp.FindServiceCall(model.qCallLine);
		
		if ((icCall.equipType==EquipType.TYPE1000 ||icCall.equipType==EquipType.TYPE2000)
				&& model.qStaffIdle[Constants.A].size()>0)
		{
			staffid=model.qStaffIdle[Constants.A].remove(0);
			model.rStaff[staffid].staffStatus=StaffStatus.BUSY;
			model.rStaff[staffid].hadJob=true;
			model.rStaff[staffid].job=icCall;
		}
		
		else
		{
			staffid=model.qStaffIdle[Constants.B].remove(0);
			model.rStaff[staffid].staffStatus=StaffStatus.BUSY;
			model.rStaff[staffid].hadJob=true;
			model.rStaff[staffid].job=icCall;
		}
	}
	
	@Override
	protected double duration() {
		// TODO Auto-generated method stub
		return model.rvp.uTravelTm();
	}



	@Override
	protected void terminatingEvent() {
		// TODO Auto-generated method stub
		model.upStartServing(staffid);
	}
	
}