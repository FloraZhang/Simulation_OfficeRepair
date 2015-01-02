package smSimModel;
public class Constants 
{
	/* Constants */
	final static double LUNCH_TIME = 60.0;
	final static double ONE_HOUR=60.0;
	final static double OVERNIGHT_TIME=900.0; //from 5PM to 8AM next day
	final static double THREE_HOUR = 180.0;
	final static double ONE_DAY = 1440.0;
	final static double STAFFA_COST = 26.0;
	final static double STAFFB_COST = 41.0;
	final static double STD_BIZ_HOUR = 9.0;  //From 8AM to 5PM
	final static double OT_RATIO=1.75; //overtime wage is 75% additional cost
	final static int A=0; //Identifier used in queue set StaffIdle to indicate the queue of idle class A staff.
	final static int B=1;  //Identifier used in queue set StaffIdle to indicate the queue of idle class B staff.

}
