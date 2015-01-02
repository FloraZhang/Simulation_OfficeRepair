package smSimModel;
import java.util.*;


import ABSmodJ.AOSimulationModel;
import ABSmodJ.Behaviour;
import ABSmodJ.SBNotice;

//The Simulation model Class
public class CallCenter extends AOSimulationModel
{
	/* Parameter */
	public int numStaffA,numStaffB;
	public enum DispMeth { EQUAL_PRIORITY, PREMIUM_PRIORITY };
	public DispMeth dispMeth;
	/*Input variables*/
	public enum BizState {BUSYAM,LUNCHTIME,BUSYPM,NOSERVICE,CLOSING,CLOSED};
	public BizState bizState;
	
	/* Entities */
	Staff[] rStaff;
	@SuppressWarnings("unchecked")
	ArrayList<Integer> [] qStaffIdle=new ArrayList[2];

	/* Group and Queue Entities */	
	protected ArrayList<Call> qCallLine = new ArrayList<Call>(); // FIFO queues
	protected ArrayList<Call> qPriorityLine= new ArrayList<Call>(); // FIFO queues

	// References to RVP and DVP objects
	protected RVPs rvp;  // Reference to rvp and dvp object
	protected DVPs dvp;
	UDPs udp = new UDPs(this); 
	
	// Output object
		public Output output = new Output(this);
		public double getBasicSL() { return output.getBasicSL(); }
		public double getOverallSL() { return output.getOverallSL(); }
		public double getPremiumSL() { return output.getPremiumSL(); }
		public double getType1and2SL() { return output.getType1and2SL(); }
		public double getType3and4SL() { return output.getType3and4SL(); }
		
	//Manage output
		public void clearAllOutput() 
		{
			output.clearOutput();
		}
		
	// Constructor - Also Initialises the model
		public CallCenter(double t0time, double tftime, DispMeth param, int numstaffA, int numstaffB, Seeds sd)
		{		
		    // all entity attributes initialised in constructors 
		    dispMeth = param;
		    numStaffA=numstaffA;
		    numStaffB=numstaffB;
		    rStaff=new Staff[numStaffA+numStaffB]; 
			// Setup Random Variate Procedures
		    rvp = new RVPs(this, sd);
		    dvp = new DVPs(this);
			// Initialise the simulation model
			initAOSimulModel(t0time,tftime);   
			
			// Schedule Initialise action
			Initialise init = new Initialise(this);
			scheduleAction(init);  // Should always be first one scheduled.

			
			// Schedule the first arrivals 
			// Create action actionName
			CallArrival arr = new CallArrival(this);
		    scheduleAction(arr);
		    
		    //Schedule BizStateChange
		    BizStateChange bsc=new BizStateChange(this);
		    scheduleAction(bsc);
		    
		  //Schedule StartBiz
		    StartBiz stb=new StartBiz(this);
		    scheduleAction(stb);
		    
		}
		
		// Another Constructor, without tftime
		public CallCenter(double t0time, DispMeth param, int numstaffA, int numstaffB, Seeds sd)
		{		
		    // all entity attributes initialised in constructors 
		    dispMeth = param;
		    numStaffA=numstaffA;
		    numStaffB=numstaffB;
		    rStaff=new Staff[numStaffA+numStaffB]; 
			// Setup Random Variate Procedures
		    rvp = new RVPs(this, sd);
		    dvp = new DVPs(this);
			// Initialise the simulation model
			initAOSimulModel(t0time);   
			
			// Schedule Initialise action
			Initialise init = new Initialise(this);
			scheduleAction(init);  // Should always be first one scheduled.

			
			// Schedule the first arrivals 
			// Create action actionName
			CallArrival arr = new CallArrival(this);
		    scheduleAction(arr);
		    
		    //Schedule BizStateChange
		    BizStateChange bsc=new BizStateChange(this);
		    scheduleAction(bsc);
		    
		  //Schedule StartBiz
		    StartBiz stb=new StartBiz(this);
		    scheduleAction(stb);
		    
		}
		
		
		/*
		 * Testing preconditions
		 */
		public void testPreconditions(Behaviour behObj)
		{
			while(scanPreconditions() == true) /* repeat */;
		}
	    // Single scan of all preconditions
		// Returns true if at least one precondition was true.
		private boolean scanPreconditions()
		{
			boolean statusChanged = false;
			
			//Conditional activity
			if(TravelToCustomer.precondition(this) == true)
			{
				TravelToCustomer act = new TravelToCustomer(this);
				act.startingEvent();
				scheduleActivity(act);
				statusChanged = true;
				//System.out.println("Started Travel to Customer. ");
			}
			
			if(TakingLunch.precondition(this) == true)
			{
				TakingLunch act = new TakingLunch(this);
				act.startingEvent();
				scheduleActivity(act);
				statusChanged = true;
				//System.out.println("Started Take Lunch. ");
			}
			
			// Do not change the status if already true
			if(statusChanged) scanInterruptPreconditions();
			else statusChanged = scanInterruptPreconditions();
			return(statusChanged);
		}
		
		private boolean scanInterruptPreconditions()
		{
			int num = esbl.size();
			int interruptionNum;
			SBNotice nt;
			Behaviour obj;
			boolean statusChanged = false;
			for(int i = 0; i < num ; i++)
			{
				nt = esbl.get(i);
				obj = (esbl.get(i)).behaviourInstance;			
				if(Serving.class.isInstance(obj))
				{
					Serving serving = (Serving) nt.behaviourInstance;
					interruptionNum = serving.interruptionPreCond();
					if(interruptionNum != 0)
					{
						serving.interruptionSCS(interruptionNum);
						statusChanged = true;
						unscheduleBehaviour(nt);
						i--; num--; // removed an entry, num decremented by 1 and need to look at same index for next loop
					}			
				}
				else System.out.println("Unrecognized behaviour object on ESBL: "+obj.getClass().getName()); 
			}
			return(statusChanged);
		}
		
	/*
	 * Starting Sequel Activities
	 */
	public void upStartUpgrade(double startWT)
	{
		Upgrade upgradeAct = new Upgrade(startWT, this);
		upgradeAct.startingEvent();
		scheduleActivity(upgradeAct);		
	}
	
	public void upStartServing(int staffid)
	{
		Serving serving=new Serving(staffid,this);
		serving.startingEvent();
		scheduleActivity(serving);
		//serving.terminatingEvent();
	}
	
	
//	public void eventOccured()
//	{
//		// Debugging Output
//		/* Debug code */
//		System.out.println("-------->Clock: "+getClock()+"<-----------------");
//		String dm;
//		if(dispMeth==DispMeth.EQUAL_PRIORITY) dm="EQUAL_PRIORITY"; 
//		else if(dispMeth==DispMeth.PREMIUM_PRIORITY) dm="PREMIUM_PRIORITY";
//		else dm="Error";
//		
//		System.out.println("Parameters: numStaffA="+numStaffA+", numStaffB="+numStaffB+", Dispatching Method="+dm);
//		System.out.println("Q.CallLine.n = "+qCallLine.size()+
//				   "\nQ.PriorityLine.n = "+qPriorityLine.size()+
//				   "\nQ.IdleStaffA.n = "+qStaffIdle[Constants.A].size()+
//				   "\nQ.IdleStaffB.n = "+qStaffIdle[Constants.B].size()+
//				   "\nBizState = "+bizState);
//		//Print out the contents of calls in the queue
//		System.out.println("======Status of calls in Q.CallLine======");
//		for(int ix=0;ix<qCallLine.size();ix++)
//		{
//			Call icCall=qCallLine.get(ix);
//			System.out.println("("+icCall.equipType+"-"+icCall.serviceType+"): StartWaitDate="+(int)(icCall.startWaitTime/Constants.ONE_DAY)+", StartWaitTime="+icCall.startWaitTime);
//		}
//		System.out.println("======Status of calls in Q.PriorityLine======");
//		for(int ix=0;ix<qPriorityLine.size();ix++)
//		{
//			Call icCall=qPriorityLine.get(ix);
//			System.out.println("("+icCall.equipType+"-"+icCall.serviceType+"): StartWaitDate="+(int)(icCall.startWaitTime/Constants.ONE_DAY)+", StartWaitTime="+icCall.startWaitTime);
//		}
//		// Print out the contents of each staff and the contents of related job
//		System.out.println("======Status of staff and their jobs======");
//		for(int ix = 0 ; ix < numStaffA+numStaffB ; ix++)
//		{
//			Staff staff = new Staff();
//			staff=rStaff[ix];
//			char staffclass;
//			if(ix<numStaffA) staffclass='A';
//			else staffclass='B';
//			System.out.print("("+ix+"-"+staffclass+"): staffStatus="+staff.staffStatus+", hadJob="+staff.hadJob+", hadLunch="+staff.hadLunch);
//			if(staff.hadJob==true)
//			{
//				Call icCall=staff.job;
//				System.out.println("; Job details: ("+icCall.equipType+"-"+icCall.serviceType+"): StartWaitDate="+(int)(icCall.startWaitTime/Constants.ONE_DAY)+", StartWaitTime="+icCall.startWaitTime+"; TotalWaitTime="+icCall.totalWaitTime+", StartServeTime="+icCall.startSrvTime+", ServeTimeLeft="+icCall.srvTimeLeft);
//			}
//			else System.out.println();
//		}
//		System.out.println("======Output======");
//		System.out.println("numBasicSrv="+output.numBasicSrv+", numBasicLW="+output.numBasicLW+", BasicSL="+output.getBasicSL()+
//				"; numPremiumSrv="+output.numPremiumSrv+", numPremiumLW="+output.numPremiumLW+", PremiumSL="+output.getPremiumSL()+
//				"; numType1and2Srv="+output.numType1and2Srv+", numType1and2LW="+output.numType1and2LW+", Type1and2SL="+output.getType1and2SL()+
//				"; numType3and4Srv="+output.numType3and4Srv+", numType3and4LW="+output.numType3and4LW+", Type3and4SL="+output.getType3and4SL()+
//				"; OverallSL="+output.getOverallSL()+
//				"; overtmStaffA="+output.overtmStaffA+", overtmStaffB="+output.overtmStaffB+
//				"; CostEstimate="+output.getOneDayCost()
//		);
//		
//		showSBL();
//		
//		//output.			
//	}
	
	protected double getClock() {return super.getClock();}
	protected double getTime0() {return time0;}
	protected double getTimef() {return timef;}
	

}