package smSimModel;
import smSimModel.Call.EquipType;
import smSimModel.Call.ServiceType;
import smSimModel.CallCenter.BizState;
import smSimModel.Staff.StaffStatus;
import ABSmodJ.ExtSequelActivity;


public class Serving extends ExtSequelActivity {
	protected int staffid; 
	protected CallCenter model;
	public Call icCall=new Call();
	
	//Constructor
	public Serving(int staffid, CallCenter m) {
		this.staffid=staffid;
		this.model=m;
	}
	
	@Override
	public void startingEvent() {
		
		Output output = model.output; // Create local reference to output object
		
		icCall=model.rStaff[staffid].job;
		if(icCall.srvTimeLeft==0.0) icCall.startSrvTime=model.getClock();
		if(icCall.srvTimeLeft==0.0)
		{
			if((int)(model.getClock()/Constants.ONE_DAY)==(int)(icCall.startWaitTime/Constants.ONE_DAY))
				icCall.totalWaitTime+=model.getClock()-icCall.startWaitTime;
			else icCall.totalWaitTime+=model.getClock()-icCall.startWaitTime-Constants.OVERNIGHT_TIME;
			
			if(icCall.serviceType==ServiceType.BASIC)
			{
				output.numBasicSrv++;
				if(icCall.equipType==EquipType.TYPE1000 ||icCall.equipType==EquipType.TYPE2000)  output.numType1and2Srv++;
				else output.numType3and4Srv++;
				if(icCall.totalWaitTime>Constants.ONE_DAY)
				{
					output.numBasicLW++;
					if(icCall.equipType==EquipType.TYPE1000 ||icCall.equipType==EquipType.TYPE2000)  output.numType1and2LW++;
					else output.numType3and4LW++;
				}
			}
			else 
			{
				output.numPremiumSrv++;
				if(icCall.equipType==EquipType.TYPE1000 ||icCall.equipType==EquipType.TYPE2000)  output.numType1and2Srv++;
				else output.numType3and4Srv++;
				if(icCall.totalWaitTime>Constants.THREE_HOUR)
				{
					output.numPremiumLW++;
					if(icCall.equipType==EquipType.TYPE1000 ||icCall.equipType==EquipType.TYPE2000)  output.numType1and2LW++;
					else output.numType3and4LW++;
				}
			}
			
			icCall.srvTimeLeft=model.rvp.uSrvTm(icCall.equipType);
		}
		
		else model.rStaff[staffid].staffStatus=StaffStatus.BUSY;
	}
	
	@Override
	public int interruptionPreCond() {
		
		int retval=0;
		if(model.bizState==BizState.CLOSED
				&& icCall.serviceType==ServiceType.BASIC) retval=1;
		return retval;
	}

	@Override
	public void interruptionSCS(int arg0) {
		
		icCall.srvTimeLeft-=model.getClock()-icCall.startSrvTime;
		if(staffid<model.numStaffA) model.output.overtmStaffA+=30;
		else model.output.overtmStaffB+=30;
		model.rStaff[staffid].staffStatus=StaffStatus.OFF;
	}

	@Override
	protected double duration() {
		
		return icCall.srvTimeLeft;
	}



	@Override
	protected void terminatingEvent() {
		
		if(model.bizState==BizState.BUSYAM || model.bizState==BizState.BUSYPM || model.bizState==BizState.LUNCHTIME)
		{
			model.rStaff[staffid].staffStatus=StaffStatus.IDLE;
			if(staffid<model.numStaffA) model.qStaffIdle[Constants.A].add(staffid);
			else model.qStaffIdle[Constants.B].add(staffid);
		}
		else if(model.bizState==BizState.NOSERVICE)
			model.rStaff[staffid].staffStatus=StaffStatus.OFF;
		else if(model.bizState==BizState.CLOSING ||model.bizState==BizState.CLOSED)
		{
			model.udp.CalculateOT(staffid);
			
		}
		
		model.rStaff[staffid].job=null;
		model.rStaff[staffid].hadJob=false;
	}
	
}