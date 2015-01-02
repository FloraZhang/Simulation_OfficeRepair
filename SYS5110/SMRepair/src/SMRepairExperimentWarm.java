
import smSimModel.CallCenter;
import smSimModel.CallCenter.DispMeth;
import smSimModel.Seeds;
import cern.jet.random.engine.RandomSeedGenerator;


public class SMRepairExperimentWarm 
{
	private static CallCenter cc; //Simulation Object
	private static double[][] overallSLResults;
	private static double[][] costEstimateResults;
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		int i, NUMRUNS = 30;
		double intervalStart;
		double intervalLength=7*24.0*60; //7 days
		int numIntervals=20;  //140 days
		overallSLResults= new double[NUMRUNS][numIntervals];
		costEstimateResults= new double[NUMRUNS][numIntervals];
		
		Seeds[] sds = new Seeds[NUMRUNS];
		
		// Lets get a set of uncorrelated seeds
		RandomSeedGenerator rsg = new RandomSeedGenerator();
		for(i=0 ; i<NUMRUNS ; i++)
			sds[i] = new Seeds(rsg);

		// Loop for NUMRUN simulation runs for each case
		int numStaff[][]=new int[][] {{10,10},{10,15},{10,20},{10,30}};  //Test 4 combinations of numStaffA and numStaffB
		DispMeth dm;
		
		// Base Case
		System.out.println("Base Case");
		dm=DispMeth.EQUAL_PRIORITY;
		for (int j=0;j<4;j++)
		{
			System.out.println("Number of StaffA: "+numStaff[j][0]+". Number of StaffB: "+numStaff[j][1]);
			for(i=0 ; i < NUMRUNS ; i++)
			{
				//For computing warmup, compute average over intervalLength for numIntervals
	            //Setup the simulation object
				cc = new CallCenter(0,dm,numStaff[j][0],numStaff[j][1],sds[i]); // Initialise object
				// Loop for the all intervals
	            for( int interval=0 ; interval<numIntervals ; interval++) 
	            {
	            	// Run the simulation for an interval
	            	intervalStart = interval*intervalLength;
	            	cc.setTimef(intervalStart+intervalLength);
	            	cc.runSimulation();
	            	// compute scalar output
				    overallSLResults[i][interval]=cc.output.getOverallSL(); // save in matrix
					costEstimateResults[i][interval]=cc.output.getSevenDayCost(); // save in matrix
					cc.clearAllOutput();
				}
			}
			System.out.println("Average OverallSL");
			printMatrix(overallSLResults,NUMRUNS,numIntervals);
			System.out.println("Average Cost Estimate");
			printMatrix(costEstimateResults,NUMRUNS,numIntervals);
			}//for
			
		
		// Alternate Case
		System.out.println("Alternate Case");
		dm=DispMeth.PREMIUM_PRIORITY;
		for (int j=0;j<4;j++)
		{
			System.out.println("Number of StaffA: "+numStaff[j][0]+". Number of StaffB: "+numStaff[j][1]);
			for(i=0 ; i < NUMRUNS ; i++)
			{
				//For computing warmup, compute average over intervalLength for numIntervals
	            //Setup the simulation object
				cc = new CallCenter(0,dm,numStaff[j][0],numStaff[j][1],sds[i]); // Initialise object
				// Loop for the all intervals
	            for( int interval=0 ; interval<numIntervals ; interval++) 
	            {
	            	// Run the simulation for an interval
	            	intervalStart = interval*intervalLength;
	            	cc.setTimef(intervalStart+intervalLength);
	            	cc.runSimulation();
	            	// compute scalar output
				    overallSLResults[i][interval]=cc.output.getOverallSL(); // save in matrix
					costEstimateResults[i][interval]=cc.output.getSevenDayCost(); // save in matrix
					cc.clearAllOutput();
				}
			}
			System.out.println("Average OverallSL");
			printMatrix(overallSLResults,NUMRUNS,numIntervals);
			System.out.println("Average Cost Estimate");
			printMatrix(costEstimateResults,NUMRUNS,numIntervals);
		}//for
	
	}//main
		private static void  printMatrix(double[][] results,int n, int ni)
		{
			  int i,interval;
		
			  // Let's output the data
			  for(interval=0 ; interval <ni ; interval++)
			  {
			      for(i=0 ; i<n-1 ; i++)
				  System.out.print(results[i][interval]+",");
			      System.out.println(results[i][interval]); // last one - no comma
			  }
		}	
}//class

