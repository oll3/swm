package se.gareth.swm;

import java.util.LinkedList;

import android.graphics.BitmapFactory;
import android.graphics.Color;

public class Cat1 extends Creature  {
	private static Sprite mHangingSprite;
	private static LinkedList<Sprite> mSplittedSpriteList;
	
	/* The number of levels supported for this creature */
	protected static final int numLevels = 1;
	protected SwingBehavior swingBehavior;
	
	public Cat1(GameBase gameBase, int level) {
		/* Can take 500 damage, gives 10 points */
    	super(gameBase, 500, 100);
    	
    	if (mHangingSprite == null) {
    		mHangingSprite = new Sprite(Sprite.replaceColor(BitmapFactory.decodeResource(game.res,
        			R.drawable.cat1_hanging), Sprite.ColorChannel.GREEN, Color.rgb(0x92, 0x3a, 0x00)), 18);
    		mSplittedSpriteList = createSplittedSprite(mHangingSprite);
    	}

    	setSplittedSprite(mSplittedSpriteList);
    	
    	setAnimation(new Animation(mHangingSprite, 16, 0));
		
		applyForce(game.forces.getGravity());
		
       	setRotationOffset(0.5f, 0.15f);
       	swingBehavior = new SwingBehavior(game, 10f);
       	addBehavior(swingBehavior);
		
		/* How big is our hit area */
		setHitableRadius((double)(Math.min(getWidth(), getHeight()))/2.0);
		
		setBloodStainOffset(0.5, 0.75);
	}

	@Override
	protected void wasDestroyed(int damage) {
		deleteMe();
	}
	
	@Override
	public void update(final TimeStep timeStep) {
		super.update(timeStep);
		
		if (getRight() < 0 || 
			getTop() > (game.getScreenHeight() * 2) || 
			getBottom() < 0) {
    		deleteMe();
		}
	}
}
