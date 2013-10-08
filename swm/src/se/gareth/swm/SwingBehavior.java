package se.gareth.swm;

public class SwingBehavior extends Behavior {

	private float mRotation;
	private float mRotationSpeed;
	private float mRotationLimit;
	private boolean mDirectionLeft;
	private boolean mLimitRotation;
	
	public SwingBehavior(GameBase gameBase, float rotationLimit) {
		super(gameBase);
		mRotationLimit = rotationLimit;
		mLimitRotation = true;
	}
	
	public void limitRotation(boolean doLimit) {
		mLimitRotation = doLimit;
	}
	
	@Override
	public void wasAdded(ActiveObject activeObject) {
		mRotation = 0;
		mRotationSpeed = 0;
		mDirectionLeft = true;
	}
	
	@Override
	public void update(ActiveObject activeObject, double frameTime) {

		
		if (mLimitRotation) {
			mRotationSpeed = 7 * (mRotationLimit - Math.abs(mRotation)) + 20;
			
			if (mDirectionLeft) {
				mRotation += mRotationSpeed * frameTime;
				
				if (mRotation > mRotationLimit) {
					mDirectionLeft = false;
					mRotation = mRotationLimit;
				}
			}
			else {
				mRotation -= mRotationSpeed * frameTime;
				
				if (mRotation < -mRotationLimit) {
					mDirectionLeft = true;
					mRotation = -mRotationLimit;
				}
			}
		}
		else {
			if (mDirectionLeft) {
				mRotation += mRotationSpeed * frameTime;
			}
			else {
				mRotation -= mRotationSpeed * frameTime;
			}
		}
		
		activeObject.setRotation(mRotation);
	}
}
