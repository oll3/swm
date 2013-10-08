package se.gareth.swm;

public class FadeDestroyEffect extends Effect {

	
	private double mAlpha;
	private double mOverTime;
	
	public FadeDestroyEffect(double overTime) {
		mOverTime = overTime;
		mAlpha = 1.0;
	}
	
	public void update(GraphicObject graphicObject, double frameTime) {
		super.update(graphicObject, frameTime);
		
		mAlpha -= frameTime / mOverTime;

		if (mAlpha < 0.0) {
			graphicObject.deleteMe();
		}
		else {		
			graphicObject.setAlpha((int)((double)255 * mAlpha));
		}
		
	}
	
}
