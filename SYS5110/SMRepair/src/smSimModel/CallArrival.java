package smSimModel;
import smSimModel.CallCenter.DispMeth;
import ABSmodJ.ScheduledAction;
//Problems: how to schedule call arrival on a repetitive daily base?
/**
 * WArrivals Scheduled Action
 *
 */
class CallArrival extends ScheduledAction 
{
	CallCenter model;  // reference to model object
	public CallArrival(CallCenter model) { this.model = model; }

	public double timeSequence() 
	{
		return model.rvp.duCallArr();
	}

	public void actionEvent() 
	{
		// WArrival Action Sequence SCS
	     Call icCall = new Call();
	     icCall.equipType = model.rvp.uEquipType();
	     icCall.serviceType = model.rvp.uSrvType(icCall.equipType);
	     icCall.startWaitTime= model.getClock(); 
	     icCall.totalWaitTime=0;
	     model.qCallLine.add(icCall);
	     
	     if(icCall.serviceType==Call.ServiceType.PREMIUM && model.dispMeth==DispMeth.PREMIUM_PRIORITY) model.upStartUpgrade(icCall.startWaitTime);
	}

}

