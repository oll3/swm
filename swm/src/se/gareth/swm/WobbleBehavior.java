package se.gareth.swm;

import android.util.Log;

public class WobbleBehavior extends Behavior {

	private static final String TAG = WobbleBehavior.class.getName();

	private long mRange;
	private long mFrameCnt;
	private final Vector2D mForce;
	private long mChangeDirectionTime;

	public WobbleBehavior(GameBase gameBase, double direction, double speed, long range) {
		super(gameBase);
		mRange = range;
		mForce = new Vector2D();
		mForce.setDirectionMagnitude(direction, speed);
	}
	
	@Override
	public void wasAdded(ActiveObject activeObject) {
		mChangeDirectionTime = System.currentTimeMillis() + (mRange / 2);
		Log.d(TAG, "Added wobble behavior to " + activeObject);
	}
	
	@Override
	public void update(ActiveObject activeObject, double frameTime) {
		activeObject.addPosition(mForce.getX() * 0.016,
				mForce.getY() * 0.016);
		mFrameCnt ++;
		if (mFrameCnt > mRange) {
			mFrameCnt = 0;
			mForce.invert();
			mChangeDirectionTime = System.currentTimeMillis() + mRange;
			Log.d(TAG, "Force now " + mForce);
		}
	}
}
