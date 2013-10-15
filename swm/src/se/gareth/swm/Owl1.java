package se.gareth.swm;

import java.lang.Math;
import java.util.ArrayList;
import java.util.LinkedList;

import android.graphics.BitmapFactory;
import android.graphics.Color;


public class Owl1 extends Creature {
	
	private static ArrayList<Sprite> mFlyingSprite;
	private static ArrayList<LinkedList<Sprite>> mSplittedSpriteList;
	private Vector2D mAcceleration;
	private double mSpeed;
	enum State {
		ShowUp,
		GoBack,
		Go,
	};
	
	private State mState;
	
	/* The number of levels supported for this creature */
	protected static final int numLevels = 5;
	
	private static final int baseColor = Color.rgb(0xc8, 0x71, 0x37);
	
	public Owl1(GameBase gameBase, int level) {
		/* Can take 100.0 damage, gives 10 points */
    	super(gameBase, 100 + 100 * level, 25 + level * 5);
		mSpeed = game.calcHorizonalSpeed(500 + level * 100);
    	
    	if (mFlyingSprite == null) {
    		mFlyingSprite = new ArrayList<Sprite>();
    		mSplittedSpriteList = new ArrayList<LinkedList<Sprite>>();
    		for (int i = 0; i < numLevels; i ++) {
    			Sprite sprite = new Sprite(Sprite.replaceColor(BitmapFactory.decodeResource(game.res,
	        			R.drawable.owl1_flying), Sprite.ColorChannel.GREEN, Sprite.multiplyColor(baseColor, 6.0f / (i + 10))), 6);
	    		mFlyingSprite.add(sprite);
	    		LinkedList<Sprite> splittedSpriteList = createSplittedSprite(sprite);
	    		mSplittedSpriteList.add(splittedSpriteList);
    		}
    	}
    	
    	setSplittedSprite(mSplittedSpriteList.get(level));
    	setAnimation(new Animation(mFlyingSprite.get(level), 16, 0));
		
		setMaxSpeed(mSpeed);
		
		mAcceleration = new Vector2D(game.calcHorizonalSpeed(-400.0), 0);
		applyForce(mAcceleration);
		
		mState = State.ShowUp;
		
		/* How big is our hit area */
		setHitableRadius((double)(Math.min(getWidth(), getHeight()))/2.0);
		
		/* Set default start position */
		setPosition(game.getScreenWidth() + getWidth() / 2, 
					getHeight() / 2 + mRandom.nextInt((game.getPlayfieldHeight()/2 - (int)getHeight())));
	}

	@Override
	protected void wasDestroyed(int damage) {
		deleteMe();
	}
	
	@Override
	public double isHit(ActiveObject object) {
		/* Ignore being hit if not in Go state */
		if (mState == State.Go) {
			return super.isHit(object);
		}
		return 0.0;
	}
	
	@Override
	protected void wasHit(int damage) {
		super.wasHit(damage);
		setSpeed(0.0);
	}
	
	@Override
	public void update(final TimeStep timeStep) {
		super.update(timeStep);
		
		if (mState == State.ShowUp && (getX() - getWidth()/4) < game.getScreenWidth()) {
			mAcceleration.set(game.calcHorizonalSpeed(400.0), 0);
			mState = State.GoBack;
		}
		else if (mState == State.GoBack && (getX() - getWidth() / 2) > game.getScreenWidth()) {
			mState = State.Go;
			lookAt(0, getHeight() / 2 + mRandom.nextInt((game.getPlayfieldHeight()/2 - (int)getHeight())) + game.getPlayfieldHeight()/2);
			mAcceleration.set(-mSpeed, 0);
			mAcceleration.setDirection(this.mVelocity.getDirection());
		}
		
		if (getX() < 0) {

    		if ((getX() + getWidth()) < 0) {
    			deleteMe();
    		}
		}
	}
	

}
