package smSimModel;

import smSimModel.CallCenter.BizState;
import cern.jet.random.Exponential;
import cern.jet.random.Normal;
import ABSmodJ.TriangularVariate;
import cern.jet.random.engine.MersenneTwister;
//Problem: uCallArr is not generated when not in biz hour.
class RVPs 
{
	CallCenter model;  // reference to the complete model
	
	// Constructor
		RVPs(CallCenter model, Seeds sd) 
		{ 
			this.model = model; 
			// Initialise Internal modules, user modules and input variables
			// Set up random variate generators
		    dmInterArrivalTime = new Exponential(1/MEAN1_INTER_ARRIVAL, new MersenneTwister(sd.ia));
		    dmServiceTime1 = new Normal(MEAN1, StDev1,new MersenneTwister(sd.stm1));
		    dmServiceTime2 = new Normal(MEAN2, StDev2,new MersenneTwister(sd.stm2));
		    dmServiceTime3 = new Normal(MEAN3, StDev3,new MersenneTwister(sd.stm3));
		    dmServiceTime4 = new Normal(MEAN4, StDev4,new MersenneTwister(sd.stm4));
		    dmTravelTime=new TriangularVariate(MIN_TRAVELTIME,MEAN_TRAVELTIME,MAX_TRAVELTIME,new MersenneTwister(sd.ttm));
		    et = new MersenneTwister(sd.et);  // random number generator for equipment type
		    st = new MersenneTwister(sd.st); // random number generator for service type
		}
		/*-----------------------------------Random Variate Procedures--------------------------*/
		//--------------------------------------------------------------------
		// Random Variate Procedure: duCallArr
		//--------------------------------------------------------------------
		//17.8;        //Mean interarrival times for patients.
		protected final static double MEAN1_INTER_ARRIVAL = 2.727;
		protected final static double MEAN2_INTER_ARRIVAL = 1.875;
		protected final static double MEAN3_INTER_ARRIVAL = 2.222;
		protected final static double MEAN4_INTER_ARRIVAL = 2.609;
		protected final static double MEAN5_INTER_ARRIVAL = 3.75;
		protected final static double MEAN6_INTER_ARRIVAL = 5;
		protected final static double MEAN7_INTER_ARRIVAL = 5.455;
		protected final static double MEAN8_INTER_ARRIVAL = 6.0;
		protected final static double MEAN9_INTER_ARRIVAL = 8.571;
		private Exponential dmInterArrivalTime;
	    protected double duCallArr() 
	    { 
	    	double nxtIntArr;
	    	double mean;
	    	if(model.bizState!=BizState.CLOSING && model.bizState!=BizState.CLOSED)
	    	{
	    		if(model.getClock()%Constants.ONE_DAY<60) mean=MEAN1_INTER_ARRIVAL;
	    		else if(model.getClock()%Constants.ONE_DAY<120) mean=MEAN2_INTER_ARRIVAL;
	    		else if(model.getClock()%Constants.ONE_DAY<180) mean=MEAN3_INTER_ARRIVAL;
	    		else if(model.getClock()%Constants.ONE_DAY<240) mean=MEAN4_INTER_ARRIVAL;
	    		else if(model.getClock()%Constants.ONE_DAY<300) mean=MEAN5_INTER_ARRIVAL;
	    		else if(model.getClock()%Constants.ONE_DAY<360) mean=MEAN6_INTER_ARRIVAL;
	    		else if(model.getClock()%Constants.ONE_DAY<420) mean=MEAN7_INTER_ARRIVAL;
	    		else if(model.getClock()%Constants.ONE_DAY<480) mean=MEAN8_INTER_ARRIVAL;
	    		else mean=MEAN9_INTER_ARRIVAL;
	    		nxtIntArr = dmInterArrivalTime.nextDouble(1/mean);
	    	}
	    	else nxtIntArr=dmInterArrivalTime.nextDouble()+Constants.OVERNIGHT_TIME;
	    	if (model.getClock()%Constants.ONE_DAY<540 &&
	    			540<model.getClock()%Constants.ONE_DAY+nxtIntArr)
	    		nxtIntArr=dmInterArrivalTime.nextDouble()+(540-model.getClock()%Constants.ONE_DAY)+Constants.OVERNIGHT_TIME; //if current time is close to the CLOSING time, to prevent next arrival occur during the CLOSING time, we schedule the next arrival nextday
	    	return (model.getClock()+nxtIntArr); 
	    }
	    
		
	    
		//--------------------------------------------------------------------
		// Random Variate Procedure: uEquipType
		//--------------------------------------------------------------------
	    private final double PERCENT_TYPE_1 = 36.0;         //Percentage of arriving calls of type1000.
	    private final double PERCENT_TYPE_2 = 34.0;         //Percentage of arriving calls of type2000.
	    private final double PERCENT_TYPE_3 = 19.0;         //Percentage of arriving calls of type3000.
	    //private final double PERCENT_TYPE_4 = 11.0;         //Percentage of arriving calls of type4000.
	    private MersenneTwister et;
	    protected Call.EquipType uEquipType() 
	    {  
	    	Call.EquipType cet;
	    	if(et.nextDouble()<PERCENT_TYPE_1/100.0) cet=Call.EquipType.TYPE1000;
	    	else if (!(et.nextDouble()<PERCENT_TYPE_1/100.0) && et.nextDouble()<(PERCENT_TYPE_1+PERCENT_TYPE_2)/100.0) cet=Call.EquipType.TYPE2000;
	    	else if (!(et.nextDouble()<(PERCENT_TYPE_1+PERCENT_TYPE_2)/100.0) && et.nextDouble()<(PERCENT_TYPE_1+PERCENT_TYPE_2+PERCENT_TYPE_3)/100.0) cet=Call.EquipType.TYPE3000;
	    	else cet=Call.EquipType.TYPE4000;
	    	return (cet);
	    }	

		//--------------------------------------------------------------------
		// Random Variate Procedure: uSrvType
		//--------------------------------------------------------------------
	    private final double PROPB1 = 35.0;	      //Percentage of basic service type for TYPE1000.
	    private final double PROPB2 = 40.0;	      //Percentage of basic service type for TYPE2000.
	    private final double PROPB3 = 25.0;	      //Percentage of basic service type for TYPE3000.
	    private final double PROPB4 = 15.0;	      //Percentage of basic service type for TYPE4000.
	    private MersenneTwister st;  
	    protected Call.ServiceType uSrvType(Call.EquipType cet)
	    {
	    	Call.ServiceType cst;
	    	
	    	switch(cet)
	    	{
	    	case TYPE1000:
	    		if(st.nextDouble() < PROPB1/100.0) cst = Call.ServiceType.BASIC;
	    		else cst = Call.ServiceType.PREMIUM;
	    		break;
	    	case TYPE2000:
	    		if(st.nextDouble() < PROPB2/100.0) cst = Call.ServiceType.BASIC;
	    		else cst = Call.ServiceType.PREMIUM;
	    		break;
	    	case TYPE3000:
	    		if(st.nextDouble() < PROPB3/100.0) cst = Call.ServiceType.BASIC;
	    		else cst = Call.ServiceType.PREMIUM;
	    		break;
	    	case TYPE4000:
	    		if(st.nextDouble() < PROPB4/100.0) cst = Call.ServiceType.BASIC;
	    		else cst = Call.ServiceType.PREMIUM;
	    		break;
	    	default: cst=null;
	    	}
	    	return (cst);
	    }
	    
		//--------------------------------------------------------------------
		// Random Variate Procedure: uSrvTm
		//--------------------------------------------------------------------
	    private final double MEAN1 = 14.8;        //Mean time for serving TYPE1000.
	    private final double StDev1 = 5.8;        //StDev for serving TYPE1000.
	    private final double MEAN2 = 44.4;        //Mean time for serving TYPE2000.
	    private final double StDev2 = 15.1;        //StDev for serving TYPE2000.
	    private final double MEAN3 = 60.3;        //Mean time for serving TYPE3000.
	    private final double StDev3 = 27.4;        //StDev for serving TYPE3000.
	    private final double MEAN4 = 130.2;        //Mean time for serving TYPE2000.
	    private final double StDev4 = 29.8;        //StDev for serving TYPE2000.
	    private Normal dmServiceTime1;
	    private Normal dmServiceTime2;
	    private Normal dmServiceTime3;
	    private Normal dmServiceTime4;
		protected double uSrvTm(Call.EquipType cet) 
		{ 
			double srvTm;
			switch(cet)
	    	{
	    	case TYPE1000:
	    		srvTm=dmServiceTime1.nextDouble();
	    		break;
	    	case TYPE2000:
	    		srvTm=dmServiceTime2.nextDouble();
	    		break;
	    	case TYPE3000:
	    		srvTm=dmServiceTime3.nextDouble();
	    		break;
	    	case TYPE4000:
	    		srvTm=dmServiceTime4.nextDouble();
	    		break;
	    	default: srvTm=-1;
	    	}
	    	return (srvTm);
		}	

		//--------------------------------------------------------------------
		// Random Variate Procedure: uTravelTm
		//--------------------------------------------------------------------
		private final double MIN_TRAVELTIME = 10.0;      
		private final double MAX_TRAVELTIME = 45.0;
		private final double MEAN_TRAVELTIME = 35.0;
		private TriangularVariate dmTravelTime;
		protected double uTravelTm() { return dmTravelTime.next(); }	
		
		
}