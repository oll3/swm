package se.gareth.swm;

public class MoveToBehavior extends Behavior {

	private double mDestX, mDestY; 
	private double mSpeed;
	private double mDiffX, mDiffY;
	
	public MoveToBehavior(GameBase gameBase, double x, double y, double speed) {
		super(gameBase);
		mDestX = x;
		mDestY = y;
		mSpeed = speed;
	}
	
	@Override
	public void wasAdded(ActiveObject activeObject) {
		activeObject.lookAt(mDestX, mDestY);
		activeObject.setSpeed(mSpeed);
		mDiffX = activeObject.getX() - mDestX;
		mDiffY = activeObject.getY() - mDestY;
	}
	
	@Override
	public void update(ActiveObject activeObject, final TimeStep timeStep) {
		/* Test if object has moved into position */
		boolean reachedX = false;
		boolean reachedY = false;

		if (mDiffX > 0.0 && activeObject.getX() < mDestX) {
			reachedX = true;
		}
		else if (mDiffX <= 0.0 && activeObject.getX() >= mDestX) {
			reachedX = true;
		}

		if (mDiffY > 0.0 && activeObject.getY() < mDestY) {
			reachedY = true;
		}
		else if (mDiffY <= 0.0 && activeObject.getY() >= mDestY) {
			reachedY = true;
		}
		
		if (reachedX && reachedY) {
			activeObject.setPosition(mDestX, mDestY);
			activeObject.setSpeed(0);
			setDone();
		}
	}
}
