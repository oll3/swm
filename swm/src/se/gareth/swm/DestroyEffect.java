package se.gareth.swm;

public class DestroyEffect extends Effect {

	private double mOverTime;
	
	public DestroyEffect(double overTime) {
		mOverTime = overTime;
	}
	
	public void update(ActiveObject activeObject, double frameTime) {
		super.update(activeObject, frameTime);
		
		mOverTime -= frameTime;
		
		if (mOverTime < 0.0) {
			activeObject.deleteMe();
		}		
	}
	
}
