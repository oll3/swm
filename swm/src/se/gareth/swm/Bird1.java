package se.gareth.swm;

import java.lang.Math;
import java.util.ArrayList;
import java.util.LinkedList;

import android.graphics.BitmapFactory;
import android.graphics.Color;


public class Bird1 extends Creature {
	
	private static ArrayList<Sprite> mFlyingSprite;
	private static ArrayList<LinkedList<Sprite>> mSplittedSpriteList;
	private final Vector2D mAcceleration;
	
	/* The number of levels supported for this creature */
	protected static final int numLevels = 5;
	
	private static final int baseColor = Color.rgb(0xfa, 0x90, 0xa0);
	
	public Bird1(GameBase gameBase, int level) {
		/* Can take 100.0 damage, gives 10 points */
    	super(gameBase, 50 + 10 * level, 5 + level * 5);
    	double speed = game.calcHorizonalSpeed(200 + level * 100);
    	
    	if (mFlyingSprite == null) {
    		mFlyingSprite = new ArrayList<Sprite>();
    		mSplittedSpriteList = new ArrayList<LinkedList<Sprite>>();
    		for (int i = 0; i < numLevels; i ++) {
    			Sprite sprite = new Sprite(Sprite.replaceColor(BitmapFactory.decodeResource(game.res,
	        			R.drawable.bird1_flying), Sprite.ColorChannel.GREEN, Sprite.multiplyColor(baseColor, 6.0f / (i + 6))), 8);
	    		mFlyingSprite.add(sprite);
	    		LinkedList<Sprite> splittedSpriteList = createSplittedSprite(sprite);
	    		mSplittedSpriteList.add(splittedSpriteList);
    		}
    	}
    	
    	setSplittedSprite(mSplittedSpriteList.get(level));
    	setAnimation(new Animation(mFlyingSprite.get(level), 16, 0));
		
		setVelocity(Vector2D.DIRECTION_LEFT, speed);
		
		mAcceleration = new Vector2D(-speed, -1500.0);
		applyForce(mAcceleration);
		applyForce(game.forces.getGravity());
		
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
	public void update(final TimeStep timeStep) {
		super.update(timeStep);
		
		if (getX() < 0) {

    		if ((getX() + getWidth()) < 0) {
    			deleteMe();
    		}
		}
	}
	

}
