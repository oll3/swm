package se.gareth.swm;

import java.util.LinkedList;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;



public class HitableObject extends ActiveObject {
	
	private static final String TAG = HitableObject.class.getName();

	/* Split object */
	public class Split extends ActiveObject {

		private Animation mFlyingAnimation;
		private float mRotationSpeed;
		
		public Split(GameBase gameBase, Sprite sprite) {
	    	super(gameBase);
	    	
	    	mFlyingAnimation = new Animation(sprite, 0, 1);

	    	setAnimation(mFlyingAnimation);
	    	
			applyForce(game.forces.getGravity());
			
			mRotationSpeed = ((mRandom.nextInt(60) + 20) * 5) * (1 - (mRandom.nextInt(2) * 2));
	       	setDrawOrder(20);
		}
		
		@Override
		public void update(final TimeStep timeStep) {
			super.update(timeStep);
			
			addRotation(mRotationSpeed * (float)timeStep.get());
			
			if (getX() > game.getScreenWidth() || getX() < 0 || getY() > game.getScreenHeight()) {
				/* Delete when outside screen */
				deleteMe();
			}
		}
	}
	
	
	private double mHitableRadius;
	private int mHitPoints;
	private int mHitScore;
	private int mHitsLeft;
	private int mHitCount;
	private boolean mFreeStanding;
	private boolean mWasDestroyed;
	private boolean mMustBeKilled;
	private boolean mDrawHitArea;
	private boolean mIsKillable;
	private final ArrayList<Split> mSplitList;
	
	public HitableObject(GameBase gameBase) {
		super(gameBase);
		mHitableRadius = 0.0;
		setHealthAttributes(0, 1);
		setHitScore(0);
		setDrawOrder(10);
		mWasDestroyed = false;
		mDrawHitArea = false;
		mMustBeKilled = false;
		mFreeStanding = true;
		mIsKillable = true;
		mSplitList = new ArrayList<Split>();
	}


	/*
	 * Set to false if this hitable should be included in the level 
	 * counting.
	 */
	public void setFreeStanding(boolean flag) {
		mFreeStanding = flag;
	}

	public void setKillable(boolean killable) {
		mIsKillable = killable;
	}
	
	public boolean isFreeStanding() {
		return mFreeStanding;
	}
	
	public void setMustBeKilled(boolean flag) {
		mMustBeKilled= flag;
	}
	
	public boolean mustBeKilled() {
		return mMustBeKilled;
	}
	
	/*
	 * Split up a sprite into pieces which will be inserted to playfield when object 
	 * is destroyed.
	 */
	protected static LinkedList<Sprite> createSplittedSprite(Sprite sprite) {
		LinkedList<Sprite> spriteList = new LinkedList<Sprite>();
		Bitmap bitmap = sprite.getFrame(0);
		int x, y;
		int numSplints = 4; //mRandom.nextInt(3)+4;
		int width = bitmap.getWidth() / numSplints;
		int height = bitmap.getHeight() / numSplints;
		for (y = 0; y < numSplints; y ++) {
			for (x = 0; x < numSplints; x ++) {
				//Log.d(TAG, "bitmap=" + bitmap + ", width=" + width + ", height=" + height + ", x=" + x + ", y=" + y);
				spriteList.add(new Sprite(Bitmap.createBitmap(bitmap, x*width, y*height, width , height), 1)); 
				//new Split(game, 
				//		Bitmap.createBitmap(bitmap, x*width, y*height, width , height)));
			}
		}
		return spriteList;
	}
	
	protected void setSplittedSprite(LinkedList<Sprite> spriteList) {
		for (Sprite sprite: spriteList) {
			mSplitList.add(new Split(game, sprite));
		}
	}
	
	
	/*
	 * HitPoints is the how much damage this object can take. 
	 * If the object should only be sensitive to a specific number of hits 
	 * then numHits should be set to that number.
	 */
	protected void setHealthAttributes(int hitPoints, int numHits) {
		mHitPoints = hitPoints;
		mHitsLeft = numHits;
		mHitCount = 0;
	}
	
	protected void setHitScore(int hitScore) {
		mHitScore = hitScore;
	}
	
	public int getHitScore() {
		return mHitScore;
	}

	protected void setHitableRadius(double radius) {
		mHitableRadius = radius;
	}
	
	
	/*
	 * How many times has object been hit
	 */
	public int getHitCount() {
		return mHitCount;
	}
	
	/*
	 * How is the health of object
	 */
	protected int getHitPoints() {
		return mHitPoints;
	}
	
		
	/*
	 * Returns a value from <0 - 1.0, which says if and how good the hit was. 
	 * <= 0 is miss, 1.0 is full hit.
	 */
	public double isHit(ActiveObject object) {
		if (mHitPoints > 0 || mHitsLeft > 0 || mIsKillable == false) {
			double r = mHitableRadius + object.mCollisionRadius;
			double dx = getX() - object.getX();
			double dy = getY() - object.getY();
			double rSquared = r * r;
			double distanceSquared = dx * dx + dy * dy;
			return (rSquared - distanceSquared) / rSquared;
		}
		return 0;
	}
	
	
	/*
	 * Test if we have been shot till death
	 */
	public boolean isDead() {
		return (mHitPoints <= 0.0 && mHitsLeft <= 0);
	}
	
	
	/* 
	 * Can be overridden to get called when object has been shot 
	 * and destroyed.
	 */
	protected void wasDestroyed(int damage) {
		
	}
	
	/*
	 * Can be overridden to get called when object has been shot.
	 */
	protected void wasHit(int damage) {
		
	}
	
	public boolean hasBeenDestroyed() {
		return mWasDestroyed;
	}
	
	protected void destroyMe(int damage) {
		mHitScore = 0;
		mWasDestroyed = true;
		while (mSplitList.size() > 0) {
			final Split split = mSplitList.remove(mSplitList.size() - 1);
			double direction = mRandom.nextDouble() * 2 * Math.PI;
			double speed = mRandom.nextDouble() * 300 + 300.0 + damage/100.0;
			split.setPosition(getX(), getY());
			split.setVelocity(direction, speed);
			split.addVelocity(mVelocity);
			split.setDrawOrder(getDrawOrder());
			game.gameStage.addActiveObject(split);
		}
		wasDestroyed(damage);
	}
	
	/* 
	 * Should handle the hit appropriate, and return the hit score
	 * of object. Must limit how many hits it can receive, eg if only
	 * one hit is allowed it should return 0 the second time it's hit. 
	 */
	public int handleHit(int damage) {

		int hitScore = 0;
		
		if (damage > 0) {
		
			int willSurviveHits = 0;
			
			if (damage > 0) {
				/* Calculate the number of hits we will survive 
				 * before we're destroyed. */
				willSurviveHits = ((mHitPoints - 1) / damage) + 1;
			}
					
			if (mHitsLeft > 0) {
				willSurviveHits = mHitsLeft;
				mHitsLeft --;
			}
			
			wasHit(damage);
			
			mHitPoints -= damage;
			mHitCount ++;
			
			
			Log.d(TAG, "mHitPoints=" + mHitPoints + " after damage (" + damage + ")");
			
			if (mHitPoints <= 0 && mHitsLeft <= 0) {
				/* Was destroyed */
				hitScore = mHitScore;
				destroyMe(damage);
			}
			else {
				hitScore = mHitScore / willSurviveHits;
				mHitScore -= hitScore;
			}
			
		}		
		
		return hitScore;
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		
		if (mDrawHitArea) {
			final Paint circlePaint = new Paint();
			circlePaint.setARGB(0x80, 0xff, 0, 0);
			canvas.drawCircle((float)getX(), (float)getY(), (float)mHitableRadius, circlePaint);
		}
	}
	
}
