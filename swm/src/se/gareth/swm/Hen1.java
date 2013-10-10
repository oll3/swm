package se.gareth.swm;

import java.lang.Math;
import java.util.ArrayList;
import java.util.LinkedList;

import android.graphics.BitmapFactory;
import android.graphics.Color;


public class Hen1 extends Creature {

	private static ArrayList<Sprite> mFlyingSprite, mFallingSprite;
	private Vector2D mAcceleration;
	
	private Animation mFlyingAnimation, mFallingAnimation;
	private static ArrayList<LinkedList<Sprite>> mSplittedSpriteList;
	
	private boolean mChangeForce;
	
	private double mSpeed;
	
	/* The number of levels supported for this creature */
	protected static final int numLevels = 5;
	
	private static final int baseColor = Color.rgb(0xe0, 0xe0, 0xd0);

	public Hen1(GameBase gameBase, int level) {
		/* Can take 100.0 damage, gives 10 points */
    	super(gameBase, 200 + 50 * level, 20 + 10 * level);
    	float speed = 150 + 50 * level;
    	
    	
    	if (mFlyingSprite == null) {
    		mFlyingSprite = new ArrayList<Sprite>();
    		mSplittedSpriteList = new ArrayList<LinkedList<Sprite>>();
    		for (int i = 0; i < numLevels; i ++) {
    			Sprite sprite = new Sprite(Sprite.replaceColor(BitmapFactory.decodeResource(game.res,
	        			R.drawable.hen1_flying), Sprite.ColorChannel.GREEN, Sprite.multiplyColor(baseColor, 6.0f / (i + 6))), 8);
    			mFlyingSprite.add(sprite);
    			mSplittedSpriteList.add(createSplittedSprite(sprite));
    		}    		
    	}
    	if (mFallingSprite == null) {
    		mFallingSprite = new ArrayList<Sprite>();
    		for (int i = 0; i < numLevels; i ++) {
	    		Sprite sprite = new Sprite(Sprite.replaceColor(BitmapFactory.decodeResource(game.res,
	        			R.drawable.hen1_falling), Sprite.ColorChannel.GREEN, Sprite.multiplyColor(baseColor, 6.0f / (i + 6))), 8);
	    		mFallingSprite.add(sprite);
    		}
    	}
    	
    	setSplittedSprite(mSplittedSpriteList.get(level));
    	mFlyingAnimation = new Animation(mFlyingSprite.get(level), 16, 0);
    	mFallingAnimation = new Animation(mFallingSprite.get(level), 16, 0);
    	
    	mSpeed = speed;
    	
    	setAnimation(mFlyingAnimation);
    	mAcceleration = new Vector2D();
		setVelocity(Vector2D.DIRECTION_LEFT, speed);
		setMaxSpeed(speed);
		mChangeForce = false;
		applyForce(mAcceleration);
		applyForce(game.forces.getGravity());
		mAcceleration.set(-100.0, -1250);
		
		setBloodStainOffset(0.5, 0.60);
		
		/* How big is our hit area */
		setHitableRadius((double)(Math.min(getWidth(), getHeight()))/2.0);
		
		/* Set default start position */
		setPosition(game.getScreenWidth() + getWidth() / 2, 
					getHeight() / 2 + mRandom.nextInt((game.getPlayfieldHeight() - (int)getHeight())));
	}

	@Override
	protected void wasDestroyed(int damage) {
		deleteMe();
	}
	
	@Override
	protected void wasHit(int damage) {
		super.wasHit(damage);
		setSpeed(0.0);
	}
	
	@Override
	public void update(final TimeStep timeStep) {
		super.update(timeStep);
		
		
		if (getY() > (game.getPlayfieldHeight() - getHeight())) {
			if (!mChangeForce) {
				setAnimation(mFlyingAnimation);
				mAcceleration.set(-100.0, -(1250 + mSpeed*2));
				mChangeForce = true;
			}
		}
		else if (getY() < getHeight()) {
			if (!mChangeForce) {
				setAnimation(mFallingAnimation);
				mAcceleration.set(-200.0, 0.0);
				mChangeForce = true;
			}
		}
		else {
			mChangeForce = false;
		}
		
		
		if (getX() < 0) {

    		if ((getX() + getWidth()) < 0) {
    			deleteMe();
    		}
		}
	}
	

}
