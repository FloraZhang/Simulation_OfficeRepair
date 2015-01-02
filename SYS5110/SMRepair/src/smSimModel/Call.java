package smSimModel;

class Call 
{
	enum EquipType {TYPE1000,TYPE2000,TYPE3000,TYPE4000}; 
	enum ServiceType {BASIC,PREMIUM};
	EquipType equipType;  // Type of equipment
	ServiceType serviceType; //Type of service
	double startWaitTime;  // Time a call arrives
	double totalWaitTime;  //Time from a call arrives to it get serviced, excluding non-service period
	double startSrvTime;  //Time a call start to get serviced
	double srvTimeLeft;  //Service time left for a call
	
}
