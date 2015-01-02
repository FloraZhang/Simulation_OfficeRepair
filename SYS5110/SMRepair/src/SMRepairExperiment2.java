
import smSimModel.CallCenter;
import smSimModel.CallCenter.DispMeth;
import smSimModel.Seeds;
import cern.jet.random.engine.RandomSeedGenerator;


public class SMRepairExperiment2 
{
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		int i, NUMRUNS = 30;
		double startTime=0.0, endTime=7*14*24*60; // run for 14 weeks
		Seeds[] sds = new Seeds[NUMRUNS];
		CallCenter cc;  // Simulation object

		// Lets get a set of uncorrelated seeds
		RandomSeedGenerator rsg = new RandomSeedGenerator();
		for(i=0 ; i<NUMRUNS ; i++)
			sds[i] = new Seeds(rsg);

		// Loop for NUMRUN simulation runs for each case
		int numStaffA=9;
		int numStaffB=9;
		double meanOverallSL;
		double meanType1and2SL;
		double meanType3and4SL;
		DispMeth dm;
		// Base Case
		System.out.println("Base Case");
		printSSOVsHeader();
		dm=DispMeth.EQUAL_PRIORITY;
		for (numStaffA=9;numStaffA<=16;numStaffA++)
		{
			while(numStaffA<=16 && numStaffB<=30)
			{
				meanOverallSL=0.0;
				meanType1and2SL=0.0;
				meanType3and4SL=0.0;
				
				for(i=0 ; i < NUMRUNS ; i++)
				{
					cc = new CallCenter(startTime,dm,numStaffA,numStaffB,sds[i]);
					cc.setTimef(endTime);
					cc.runSimulation();
					printSSOVs(i+1,endTime/(24.0*60.0), cc);
					meanOverallSL+=cc.output.getOverallSL();
					meanType1and2SL+=cc.output.getType1and2SL();
					meanType3and4SL+=cc.output.getType3and4SL();
				}
				meanOverallSL/=NUMRUNS;
				meanType1and2SL/=NUMRUNS;
				meanType3and4SL/=NUMRUNS;
				if(meanOverallSL>=0.95) break;
				else if(meanType3and4SL<=meanType1and2SL) numStaffB++;
				else numStaffA++;
			}//while
			numStaffB=9;
		}
		// Alternate Case
		System.out.println("Alternate Case");
		printSSOVsHeader();
		dm=DispMeth.PREMIUM_PRIORITY;
		numStaffA=9;
		numStaffB=9;
		for (numStaffA=9;numStaffA<=16;numStaffA++)
		{
			while(numStaffA<=16 && numStaffB<=30)
			{
				meanOverallSL=0.0;
				meanType1and2SL=0.0;
				meanType3and4SL=0.0;
				
				for(i=0 ; i < NUMRUNS ; i++)
				{
					cc = new CallCenter(startTime,dm,numStaffA,numStaffB,sds[i]);
					cc.setTimef(endTime);
					cc.runSimulation();
					printSSOVs(i+1,endTime/(24.0*60.0), cc);
					meanOverallSL+=cc.output.getOverallSL();
					meanType1and2SL+=cc.output.getType1and2SL();
					meanType3and4SL+=cc.output.getType3and4SL();
				}
				meanOverallSL/=NUMRUNS;
				meanType1and2SL/=NUMRUNS;
				meanType3and4SL/=NUMRUNS;
				if(meanOverallSL>=0.95) break;
				else if(meanType3and4SL<=meanType1and2SL) numStaffB++;
				else numStaffA++;
			}
			numStaffB=9;
		}
	}

	private static void printSSOVsHeader()
	{
		System.out.println("RunNumber, numStaffA, numStaffB, dispMeth"+
				": numBasicSrv, numBasicLW, BasicSL; "+
				"numPremiumSrv, numPremiumLW, PremiumSL; "+
				"numType1and2Srv, numType1and2LW, Type1and2SL; "+
				"numType3and4Srv, numType3and4LW, Type3and4SL; "+
				"OverallSL; "+
				"overtmStaffA, overtmStaffB; "+
				"CostEstimate"
		);
	}

	private static void printSSOVs(int num, double period, CallCenter cc)
	{
		System.out.println(num+
				", "+cc.numStaffA+
				", "+cc.numStaffB+
				", "+cc.dispMeth+
				// Basic type output
				": "+cc.output.numBasicSrv+
				", "+cc.output.numBasicLW+
				", "+cc.output.getBasicSL()+
				
				// Premium type output
				"; "+cc.output.numPremiumSrv+
				", "+cc.output.numPremiumLW+
				", "+cc.output.getPremiumSL()+
				
				// Type1000 and Type2000 output
				"; "+cc.output.numType1and2Srv+
				", "+cc.output.numType1and2LW+
				", "+cc.output.getType1and2SL()+
				
				// Type3000 and Type4000 output
				"; "+cc.output.numType3and4Srv+
				", "+cc.output.numType3and4LW+
				", "+cc.output.getType3and4SL()+
				
				// Overall satisfaction level
				"; "+cc.output.getOverallSL()+
				
				// Overtime
				"; "+cc.output.overtmStaffA+
				", "+cc.output.overtmStaffB+
				
				// Cost per day
				"; "+cc.output.getCostEstimate(period));
		
	}	

}
