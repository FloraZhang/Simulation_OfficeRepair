package smSimModel;

class Staff 
{
	enum StaffStatus {IDLE,LUNCH,BUSY,OFF};
	StaffStatus staffStatus;
	Call job;
	boolean hadJob;
	boolean hadLunch;
	
}
