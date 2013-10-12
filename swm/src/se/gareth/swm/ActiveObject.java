package se.gareth.swm;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Canvas;
import android.util.Log;

public class ActiveObject extends GraphicObject {
	
	private static final String TAG = ActiveObject.class.getName();
	
	/* ID value for active object */
	public final int id;
	
	private static int mIdCnt;
	
	/* Random object shared for all active objects */
	protected static final Random mRandom = new Random();
	
	protected double mCollisionRadius; /* The calculated collision radius of the object */
	
	/* Velocity of object */
	protected Vector2D mVelocity;
	private double mSpeed, mDirection;
	
	private double mMaxSpeed;
	private boolean mSpeedLimit;
	private boolean mMovementDisabled;
	
	/* List of forces which operate on our velocity */
	private final ArrayList<Vector2D> mForces;

	private final Vector2D mTmpForce;

	private ArrayList<Behavior> mBehaviorList;
		
	public ActiveObject(GameBase gameBase) {
		super(gameBase);
		id = mIdCnt;
		mIdCnt ++;
		mCollisionRadius = 0.0;
		mVelocity = new Vector2D();
		mTmpForce = new Vector2D();
		mMaxSpeed = 0.0;
		mSpeedLimit = false;
		mSpeed = 0.0;
		mDirection = 0.0;
		mForces = new ArrayList<Vector2D>();
		mBehaviorList = new ArrayList<Behavior>();
		mMovementDisabled = false;
	}
	
	
	/*
	 * Apply a force to act upon the objects velocity
	 */
	public void applyForce(Vector2D vector) {
		mForces.add(vector);
	}
	
	public void removeForce(Vector2D vector) {
		mForces.remove(vector);
	}
	
	public void removeAllForces() {
		mForces.clear();
	}
	
	public void addBehavior(Behavior behavior) {
		mBehaviorList.add(behavior);
		behavior.wasAdded(this);
	}
	
	/*
	 * Called when a behavior has finished acting on our object
	 */
	protected void behaviorFinished(Behavior behavior) {
		
	}
	
	public void removeBehavior(Behavior behavior) {
		mBehaviorList.remove(behavior);
		behaviorFinished(behavior);
	}
	
	public void removeAllBehaviors() {
		mBehaviorList.clear();
	}
		
	public void setVelocity(double direction, double speed) {
		mSpeed = speed;
		mDirection = direction;
		mVelocity.setDirectionMagnitude(mDirection, mSpeed);
	}
	
	public void addVelocity(Vector2D velocityVector) {
		mVelocity.add(velocityVector);
	}
	
	/*
	 * Set speed (without changing the direction)
	 */
	public void setSpeed(double speed) {
		mSpeed = speed;
		mVelocity.setDirectionMagnitude(mDirection, mSpeed);
	}
	
	
	/*
	 * Disable object from moving
	 */
	public void disableMovement(boolean disable) {
		mMovementDisabled = disable;
	}
	
	/*
	 * Set direction (without changing the speed)
	 */
	public void setDirection(double direction) {
		mDirection = direction;
		mVelocity.setDirectionMagnitude(mDirection, mSpeed);
	}
	
	/* 
	 * make object look in the direction of given position
	 */
	public void lookAt(double x, double y) {
		mVelocity.set(x - getX(), y - getY());
		mDirection = mVelocity.getDirection();
		mVelocity.setDirectionMagnitude(mDirection, mSpeed);
	}
	
	/*
	 * Set the highest allowed speed of object.
	 */
	public void setMaxSpeed(double speed) {
		mMaxSpeed = speed;
		mSpeedLimit = true;
	}
	
	public void disableMaxSpeed() {
		mSpeedLimit = false;
	}

	public void setAnimation(Animation animation) {
		super.setAnimation(animation);
		mCollisionRadius = (double)(Math.min(getWidth(), getHeight()))/2.5;
	}

	
	protected static boolean circleIntersect(double x1, double y1, double r1, double x2, double y2, double r2) {
	    final double r = r1 + r2;
	    final double dx = x1 - x2;
	    final double dy = y1 - y2;
	    return (r*r) > (dx*dx) + (dy*dy);
	}
	
	public static boolean testCollision(ActiveObject object1, ActiveObject object2) {
		return circleIntersect(object1.getX(), object1.getY(), object1.mCollisionRadius, 
				object2.getX(), object2.getY(), object2.mCollisionRadius);
	}
	
	
	public void handleCollision(ActiveObject object) {
		/* Do nothing */
	}

	
	/* 
	 * Called when object is alive (added to list of objects to 
	 * be updated). Override to detect the event.
 	 */
	protected void isAlive() {
		
	}

	
	public void draw(Canvas canvas) {
		
		super.draw(canvas);
		
		/* Apply behaviors upon object */
		for (int i = 0; i < mBehaviorList.size(); i ++) {
			mBehaviorList.get(i).draw(this, canvas);
		}
		
		if (false) {
			canvas.drawCircle((float)getX(), (float)getY(), (float)mCollisionRadius, mPaint);
		}
	}
		
	public void update(final TimeStep timeStep) {
		
		if (!mMovementDisabled) {
			/* Move the object according to our velocity */
			final double speedScaler = timeStep.get() * game.gameView.getSpeedUp();
			addPosition(mVelocity.getX() * speedScaler, mVelocity.getY() * speedScaler);
		}

		/* Calculate the total force acting upon our object */
		if (mForces.size() > 0) {
			mTmpForce.set(0, 0);
			mTmpForce.add(mForces);
			mTmpForce.multiplyMagnitude(timeStep.get());
			mVelocity.add(mTmpForce);
			
			if (mSpeedLimit) {
				/* Limit speed */
				mVelocity.truncate(mMaxSpeed);
			}
		}
		
		mDirection = mVelocity.getDirection();
		mSpeed = mVelocity.getMagnitude();

		/* 
		 * Apply behaviors upon object 
		 */
		/* Only run if there are behaviors in list */
		for (int i = 0; i < mBehaviorList.size(); i ++) {
			Behavior behavior = mBehaviorList.get(i);
			behavior.update(this, timeStep);
			if (behavior.isDone()) {
				behaviorFinished(behavior);
				mBehaviorList.remove(i);
				i --;
			}
		}
		
		super.update(timeStep);
	}


	@Override
	public String toString() {
		return "" + getClass() + " (" + id + ")";
	}
}
