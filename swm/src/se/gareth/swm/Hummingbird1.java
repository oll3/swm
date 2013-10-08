package se.gareth.swm;

import java.lang.Math;
import java.util.ArrayList;
import java.util.LinkedList;

import android.graphics.BitmapFactory;
import android.graphics.Color;


public class Hummingbird1 extends Creature {

	private static ArrayList<Sprite> mFlyingSprite;
	private static ArrayList<LinkedList<Sprite>> mSplittedSpriteList;
	private MoveToBehavior mMoveToBehavior;
	private Animation mFlyingAnimation;
	
	private long mPauseTime;
	
	private float mSpeed;
	
	/* The number of levels supported for this creature */
	protected static final int numLevels = 5;
	
	private static int colorList[] = {
		Color.rgb(0x00, 0xae, 0x9c),
		Color.rgb(0x10, 0xbe, 0xac),
		Color.rgb(0x20, 0xce, 0xbc),
		Color.rgb(0x30, 0xde, 0xcc),
		Color.rgb(0x40, 0xfe, 0xdc)
	};
	
	public Hummingbird1(GameBase gameBase, int level) {
		/* Can take 100.0 damage, gives 10 points */
    	super(gameBase, 100, 20 + level * 10);
    	mSpeed = level * 200;
    	if (mFlyingSprite == null) {
    		mFlyingSprite = new ArrayList<Sprite>();
    		mSplittedSpriteList = new ArrayList<LinkedList<Sprite>>();
    		for (int i = 0; i < numLevels; i ++) {
    			Sprite sprite = new Sprite(Sprite.replaceColor(BitmapFactory.decodeResource(game.res,
    					R.drawable.hummingbird1_flying), Sprite.ColorChannel.GREEN, colorList[i], 2), 9);
    			mFlyingSprite.add(sprite);
    			mSplittedSpriteList.add(createSplittedSprite(sprite));
    		}
    	}
    	setSplittedSprite(mSplittedSpriteList.get(level));
    	mFlyingAnimation = new Animation(mFlyingSprite.get(level), 27, 0);
    	
    	setAnimation(mFlyingAnimation);
    	//mAcceleration = new Vector2D();
		//setVelocity(Vector2D.DIRECTION_LEFT, speed);
		//setMaxSpeed(speed);
		//applyForce(mAcceleration);
		//applyForce(game.forces.getWind());
		//applyForce(game.forces.getGravity());
		//mAcceleration.set(-100.0, -1000);
		
    	mPauseTime = System.currentTimeMillis();
    	
		/* How big is our hit area */
		setHitableRadius((double)(Math.min(getWidth(), getHeight()))/2.0);
		
		mMoveToBehavior = null;
		
		/* Set default start position */
		setPosition(game.getScreenWidth() + width / 2, 
				height / 2 + mRandom.nextInt((game.getPlayfieldHeight() - (int)height)));
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
	public void update(double frameTime) {
		super.update(frameTime);
		
		if (System.currentTimeMillis() > (mPauseTime)) {
			
			if (mMoveToBehavior != null) {
				/* delete a previous set move to behavior */
				removeBehavior(mMoveToBehavior);
				mMoveToBehavior = null;
			}
			
			mPauseTime = System.currentTimeMillis() + 1400 - (int)mSpeed/2;
			int hrange = game.getScreenWidth() / 10;
			int vrange = (int)(game.getPlayfieldHeight() * 1.0);
			double x = getX() - (mRandom.nextInt(hrange) + hrange);
			double y = getY() + (vrange - mRandom.nextInt(vrange * 2));
			if (y < getHeight() / 2) 
				y = getHeight() / 2;
			else if (y > game.getPlayfieldHeight() - getHeight() / 2)
				y = game.getPlayfieldHeight() - getHeight() / 2;
			
			mMoveToBehavior = new MoveToBehavior(game, x, y, 1200.0 + mSpeed);
			addBehavior(mMoveToBehavior);
		}
		
		if (getX() < 0) {

    		if ((getX() + getWidth()) < 0) {
    			deleteMe();
    		}
		}
	}
	

}
