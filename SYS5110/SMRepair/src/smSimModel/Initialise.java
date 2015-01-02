package smSimModel;
import java.util.ArrayList;

import smSimModel.CallCenter.BizState;
import smSimModel.Staff.StaffStatus;
import ABSmodJ.ScheduledAction;


class Initialise extends ScheduledAction
{
	CallCenter model;
	
	// Constructor
	public Initialise(CallCenter model) { this.model = model; }

	double [] ts = { 0.0, -1.0 }; // -1.0 ends scheduling
	int tsix = 0;  // set index to first entry.
	public double timeSequence() 
	{
		return ts[tsix++];  // only invoked at t=0
	}

	public void actionEvent() 
	{
		// System Initialisation
		model.output.overtmStaffA=0;
		model.output.overtmStaffB=0;
		model.qCallLine.clear();
		model.qPriorityLine.clear();
		model.qStaffIdle[Constants.A]=new ArrayList<Integer>();
		model.qStaffIdle[Constants.B]=new ArrayList<Integer>();
		model.bizState=BizState.BUSYAM;
		
		for(int staffid=0;staffid<model.numStaffA+model.numStaffB;staffid++)
		{
			model.rStaff[staffid]=new Staff();
			model.rStaff[staffid].staffStatus=StaffStatus.IDLE;
			model.rStaff[staffid].hadLunch=false;
			model.rStaff[staffid].hadJob=false;
			
			if(staffid<model.numStaffA) model.qStaffIdle[Constants.A].add(staffid);
			else model.qStaffIdle[Constants.B].add(staffid);
		}
		
		
	}
}
