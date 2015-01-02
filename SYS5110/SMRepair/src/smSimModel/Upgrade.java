package smSimModel;
import ABSmodJ.SequelActivity;


public class Upgrade extends SequelActivity {
	
	double startWT; 
	CallCenter model;

	
	//Constructor
	public Upgrade(double startWT, CallCenter m) {
		this.startWT=startWT;
		this.model=m;
	}
	
	@Override
	protected double duration() {
		// TODO Auto-generated method stub
		return 60;
	}
	@Override
	public void startingEvent() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void terminatingEvent() {
		// TODO Auto-generated method stub
		Call icCall=new Call();
		icCall=model.udp.RemoveCallFromLine(model.qCallLine, startWT);
		if(icCall!=null)
			model.qPriorityLine.add(icCall);  //If iC.Call exists in Q.CallLine, move it to Q.PrioirityLine.
	}
}