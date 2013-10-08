package se.gareth.swm;

import android.graphics.BitmapFactory;

public class Scrap1 extends ActiveObject {

	private static Sprite mFlyingSprite;

	private Animation mFlyingAnimation;
	
	private float mRotationSpeed;
	
	public Scrap1(GameBase gameBase) {
    	super(gameBase);
    	
    	if (mFlyingSprite == null) {
    		mFlyingSprite = new Sprite(BitmapFactory.decodeResource(game.res, 
        			R.drawable.scrap_flying1), 1);
    	}


    	mFlyingAnimation = new Animation(mFlyingSprite, 0, 0);
    	mRotationSpeed = ((mRandom.nextInt(60) + 20) * 15) * (1 - (mRandom.nextInt(2) * 2));
    	setRotation(mRandom.nextInt(360));
    	
    	setAnimation(mFlyingAnimation);
       	
		applyForce(game.forces.getGravity());
       	
       	setDrawOrder(20);
       	game.gameStage.addActiveObject(this);
	}

	@Override
	public void update(double frameTime) {
		super.update(frameTime);
	
		addRotation(mRotationSpeed * (float)frameTime);
		
		if (getX() > game.getScreenWidth() || getX() < 0 || getY() > game.getScreenHeight()) {
			/* Delete when outside screen */
			deleteMe();
		}
	}	
}
