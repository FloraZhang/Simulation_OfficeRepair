package smSimModel;

public class Output 
{
	/* SSOVs */
	public int numBasicSrv = 0;
	public int numPremiumSrv = 0;
	public int numType1and2Srv = 0;
	public int numType3and4Srv = 0;
	public int numBasicLW = 0;
	public int numPremiumLW = 0;
	public int numType1and2LW = 0;
	public int numType3and4LW = 0;
	public double overtmStaffA=0.0;
	public double overtmStaffB=0.0;
	public CallCenter model;
	
	// Constructor
	public Output(CallCenter m) {this.model=m;}
	
	/*Plus LW in the queue: 
	public int getNumBasicLW()
	{
		return(model.udp.GetnumBasicLW(model.qCallLine)+model.udp.GetnumBasicLW(model.qPriorityLine));
	}
	*/
	
	/*Plus LW in the queue: 
	public int getNumPremiumLW()
	{
		return(model.udp.GetnumPremiumLW(model.qCallLine)+model.udp.GetnumPremiumLW(model.qPriorityLine));
	}
	*/
	
	/*Plus LW in the queue: 
	public int getNumType1and2LW()
	{
		return(model.udp.GetnumType1and2LW(model.qCallLine)+model.udp.GetnumType1and2LW(model.qPriorityLine));
	}
	*/
	
	/*Plus LW in the queue:
	public int getNumType3and4LW()
	{
		return(model.udp.GetnumType3and4LW(model.qCallLine)+model.udp.GetnumType3and4LW(model.qPriorityLine));
	}
	*/

	public double getBasicSL()
	{
		if(numBasicSrv==0) return 0.0;
		else return (1.0-((double)numBasicLW/(double)numBasicSrv));		
	}
	
	public double getPremiumSL()
	{
		if(numPremiumSrv==0) return 0.0;
		else return (1.0-((double)numPremiumLW/(double)numPremiumSrv));		
	}
	
	public double getType1and2SL()
	{
		if(numType1and2Srv==0) return 0.0;
		else return (1.0- ((double)numType1and2LW/(double)numType1and2Srv));		
	}
	
	public double getType3and4SL()
	{
		if(numType3and4Srv==0) return 0.0;
		else return (1.0- ((double)numType3and4LW/(double)numType3and4Srv));		
	}
	
	public double getOverallSL()
	{
		if((numBasicSrv+numPremiumSrv)==0) return 0.0;
		else return (1.0-((double)(numBasicLW+numPremiumLW)/(double)(numBasicSrv+numPremiumSrv)));		
	}
	
	public double getCostEstimate(double period) 
	{
		double estimate;
		estimate=(Constants.STAFFA_COST*model.numStaffA+
				Constants.STAFFB_COST*model.numStaffB)*Constants.STD_BIZ_HOUR
				+Constants.OT_RATIO*(Constants.STAFFA_COST/Constants.ONE_HOUR*overtmStaffA
				+Constants.STAFFB_COST/Constants.ONE_HOUR*this.overtmStaffB)/period;
		return (estimate);
	}
	
	public double getOneDayCost()
	{
		double estimate;
		estimate=(Constants.STAFFA_COST*model.numStaffA
				+Constants.STAFFB_COST*model.numStaffB)*Constants.STD_BIZ_HOUR
				+Constants.OT_RATIO*(Constants.STAFFA_COST/Constants.ONE_HOUR*overtmStaffA
						+Constants.STAFFB_COST/Constants.ONE_HOUR*this.overtmStaffB);
		return (estimate);
	}
	
	public double getSevenDayCost()
	{
		double estimate;
		estimate=(Constants.STAFFA_COST*model.numStaffA
				+Constants.STAFFB_COST*model.numStaffB)*Constants.STD_BIZ_HOUR
				+Constants.OT_RATIO*(Constants.STAFFA_COST/Constants.ONE_HOUR*overtmStaffA
						+Constants.STAFFB_COST/Constants.ONE_HOUR*this.overtmStaffB)/7.0;
		return (estimate);
	}
	
	public void clearOutput()
	{
		numBasicSrv = 0;
		numPremiumSrv = 0;
		numType1and2Srv = 0;
		numType3and4Srv = 0;
		numBasicLW = 0;
		numPremiumLW = 0;
		numType1and2LW = 0;
		numType3and4LW = 0;
		overtmStaffA=0.0;
		overtmStaffB=0.0;
	}
	
}