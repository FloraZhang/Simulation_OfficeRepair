package smSimModel;
import cern.jet.random.engine.RandomSeedGenerator;


public class Seeds 
{
		public static final int Num = 11;
		int ia;   // call arrivals
		int et;   // equipment type of the arriving calls
		int st;   // service type of the arriving calls
		int stm1;  // service time of the arriving calls TYPE1000
		int stm2;  // service time of the arriving calls TYPE2000
		int stm3;  // service time of the arriving calls TYPE3000
		int stm4;  // service time of the arriving calls TYPE4000
		int ttm;  //travel time

		public Seeds(RandomSeedGenerator rsg)
		{
		    ia=rsg.nextSeed();
		    et=rsg.nextSeed();
		    st=rsg.nextSeed();
		    stm1=rsg.nextSeed();
		    stm2=rsg.nextSeed();
		    stm3=rsg.nextSeed();
		    stm4=rsg.nextSeed();
		    ttm=rsg.nextSeed();

		}

}
