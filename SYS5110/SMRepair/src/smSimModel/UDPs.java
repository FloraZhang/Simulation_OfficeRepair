package smSimModel;

import java.util.ArrayList;

import smSimModel.Call.EquipType;
import smSimModel.Call.ServiceType;
import smSimModel.CallCenter.BizState;
import smSimModel.Staff.StaffStatus;

class UDPs
{
	CallCenter model;
	
	//Constructor
	public UDPs(CallCenter m) {this.model=m;}
	
	// User Defined Procedures
	
	//Returns TRUE if a call in either Q.PriorityLine or Q.CallLine can be serviced and FALSE otherwise.
	public boolean CanServiceCall(ArrayList<Call> queRef)  
	{
		if(queRef.isEmpty()) return false;
		else if((ExistAvailableStaff(model.qStaffIdle[Constants.A]) ||ExistAvailableStaff(model.qStaffIdle[Constants.B]))
				&& (queRef.get(0).equipType==Call.EquipType.TYPE1000 || queRef.get(0).equipType==Call.EquipType.TYPE2000))  return true;
		else if (ExistAvailableStaff(model.qStaffIdle[Constants.B])
				&&(queRef.get(0).equipType==Call.EquipType.TYPE3000 || queRef.get(0).equipType==Call.EquipType.TYPE4000))  return true;
		else if ((ExistAvailableStaff(model.qStaffIdle[Constants.A]) &&!ExistAvailableStaff(model.qStaffIdle[Constants.B]))  
				&&ExistType1or2(queRef))  return true;
		else return false;
	}
	
	//This sub-UDP serves for the UDP.CanServiceCall
	private boolean ExistType1or2 (ArrayList<Call> queRef)  //If there exist type1000 or type2000 in a queue
	{
		boolean exist=false;
		for (int i=0;i<queRef.size();i++)
		{
			if(queRef.get(i).equipType==Call.EquipType.TYPE1000||queRef.get(i).equipType==Call.EquipType.TYPE2000) exist=true;
		}
		return exist;		
	}
	
	//This sub-UDP serves for the UDP.CanServiceCall
	private boolean ExistAvailableStaff(ArrayList<Integer> queRef)
	{
		if(model.bizState!=BizState.LUNCHTIME
				&&queRef.size()>0) return true;
		else if (model.bizState==BizState.LUNCHTIME)
		{
			for(int i=0;i<queRef.size();i++)
				if(model.rStaff[queRef.get(i)].hadLunch) return true;
			
			return false;
		}
		else return false;
	}
	
	//Return the first iC.Call that can be served from the queRef.
	public Call FindServiceCall(ArrayList<Call> queRef)  
	{
		if((model.qStaffIdle[Constants.A].size()>0 ||model.qStaffIdle[Constants.B].size()>0)
				&& (queRef.get(0).equipType==Call.EquipType.TYPE1000 || queRef.get(0).equipType==Call.EquipType.TYPE2000))  return queRef.remove(0);
		else if (model.qStaffIdle[Constants.B].size()>0
				&&(queRef.get(0).equipType==Call.EquipType.TYPE3000 || queRef.get(0).equipType==Call.EquipType.TYPE4000))  return queRef.remove(0);
		else if ((model.qStaffIdle[Constants.A].size()>0 &&model.qStaffIdle[Constants.B].size()==0)  
				&&ExistType1or2(queRef))  return FindType1or2(queRef); 
		else return null;
	}
	
	//This sub-UDP serves for the UDP.FindServiceCall
	private Call FindType1or2 (ArrayList<Call> queRef)  //If there exist type1000 or type2000 in a queue
	{
		Call icCall=new Call();
		for (int i=0;i<queRef.size();i++)
		{
			if(queRef.get(i).equipType==Call.EquipType.TYPE1000||queRef.get(i).equipType==Call.EquipType.TYPE2000) 
			{
				icCall=queRef.remove(i);
				break;
			}
		}
		return icCall;		
	}
	
	//Removes the iC.Call with specific startWaitTime from the queRef.
	public Call RemoveCallFromLine(ArrayList<Call> queRef,double startWT)
	{
		for (int i=0;i<queRef.size();i++)
		{
			if(queRef.get(i).startWaitTime==startWT)
			{
				return queRef.remove(i);
			}
		}
		return null;
	}
	
		
	//Returns a staffId which represents ready for lunch staff
	public int ReadyForLunch()
	{
		int index=-1;
		if(model.bizState==BizState.LUNCHTIME)
		{
			for(int i=0;i<model.qStaffIdle[Constants.A].size();i++)
			{
				if (!model.rStaff[model.qStaffIdle[Constants.A].get(i)].hadLunch) 
				{
					index=model.qStaffIdle[Constants.A].get(i);
					break;
				}
			}
			if(index==-1)
			{
				for(int i=0;i<model.qStaffIdle[Constants.B].size();i++)
				{
					if (!model.rStaff[model.qStaffIdle[Constants.B].get(i)].hadLunch) 
					{
						index=model.qStaffIdle[Constants.B].get(i);
						break;
					}
				}
			}
		}
		return index;
	}
	
	//Remove a staffid from idle queue
	public void RemoveStaffId(ArrayList<Integer> queRef,int staffid)
	{
		for(int i=0;i<queRef.size();i++)
			if(queRef.get(i)==staffid) queRef.remove(i);
	}
	
	public void CalculateOT(int staffid)
	{
		if((model.getClock()+480)%Constants.ONE_DAY-1020>=0)  //finish before midnight
		{
			if(staffid<model.numStaffA)  model.output.overtmStaffA+=(model.getClock()+480)%Constants.ONE_DAY-1020;
			else model.output.overtmStaffB+=(model.getClock()+480)%Constants.ONE_DAY-1020;
		}
		else  //work ot pass midnight, only occur when service type is premium
		{
			if(staffid<model.numStaffA)  model.output.overtmStaffA+=420+(model.getClock()+480)%Constants.ONE_DAY;
			else model.output.overtmStaffB+=420+(model.getClock()+480)%Constants.ONE_DAY;
		}
		model.rStaff[staffid].staffStatus=StaffStatus.OFF;
	}
	
	/*Get the number of basic type Long-waiting customers in the queue
	public int GetnumBasicLW(ArrayList<Call> queRef)
	{
		int num=model.output.numBasicLW;
		for(int i=0;i<queRef.size();i++)
			if(queRef.get(i).serviceType==ServiceType.BASIC && (model.getClock()-queRef.get(i).startWaitTime)>Constants.ONE_DAY) 
				num++;
		
		return (num);
	}
	*/
	
	/*Get the number of premium type Long-waiting customers in the queue
	public int GetnumPremiumLW(ArrayList<Call> queRef)
	{
		int num=model.output.numPremiumLW;
		for(int i=0;i<queRef.size();i++)
			if(queRef.get(i).serviceType==ServiceType.PREMIUM && (model.getClock()-queRef.get(i).startWaitTime)>Constants.THREE_HOUR) num++;
		
		return (num);
	}
	*/
	
	/*Get the number of type1K&2K Long-waiting customers in the queue
	public int GetnumType1and2LW(ArrayList<Call> queRef)
	{
		int num=model.output.numType1and2LW;
		for(int i=0;i<queRef.size();i++)
		{
			if(queRef.get(i).equipType==EquipType.TYPE1000 || queRef.get(i).equipType==EquipType.TYPE2000) 
			{
				if(queRef.get(i).serviceType==ServiceType.BASIC && (model.getClock()-queRef.get(i).startWaitTime)>Constants.ONE_DAY)
					num++;
				else if(queRef.get(i).serviceType==ServiceType.PREMIUM && (model.getClock()-queRef.get(i).startWaitTime)>Constants.THREE_HOUR) num++;
			}
		}
		return (num);
	}
	*/
	
	/*Get the number of type3K&4K Long-waiting customers in the queue
	public int GetnumType3and4LW(ArrayList<Call> queRef)
	{
		int num=model.output.numType3and4LW;
		for(int i=0;i<queRef.size();i++)
		{
			if(queRef.get(i).equipType==EquipType.TYPE3000 || queRef.get(i).equipType==EquipType.TYPE4000) 
			{
				if(queRef.get(i).serviceType==ServiceType.BASIC && (model.getClock()-queRef.get(i).startWaitTime)>Constants.ONE_DAY)
					num++;
				else if(queRef.get(i).serviceType==ServiceType.PREMIUM && (model.getClock()-queRef.get(i).startWaitTime)>Constants.THREE_HOUR) num++;
			}
		}
		return (num);
	}
	*/
}
