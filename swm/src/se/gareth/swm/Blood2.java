package se.gareth.swm;

import android.graphics.BitmapFactory;

public class Blood2 extends ActiveObject {

	private int mState;
	public static final int STATE_FLYING = 1;
	public static final int STATE_SLIDING = 2;

	private static Sprite mFlyingSprite;

	private final Animation mFlyingAnimation;
	
	public Blood2(GameBase gameBase) {
    	super(gameBase);
    	
    	if (mFlyingSprite == null) {
    		mFlyingSprite = new Sprite(BitmapFactory.decodeResource(game.res, 
        			R.drawable.blood2), 8);
    	}


    	mFlyingAnimation = new Animation(mFlyingSprite, 12, 1);

    	setAnimation(mFlyingAnimation);
    	
       	mState = STATE_FLYING;
       	
		applyForce(game.forces.getGravity());
       	
		this.setMaxSpeed(600);
		
       	setDrawOrder(20);
	}
	
	@Override
	public void update(final TimeStep timeStep) {
		super.update(timeStep);
		
		if (mState == STATE_FLYING) {
			if (getX() > game.getScreenWidth()) {
				mState = STATE_SLIDING;
				setVelocity(Vector2D.DIRECTION_DOWN, 0.0);
			}
			else if(getX() < 0) {
				mState = STATE_SLIDING;
				setVelocity(Vector2D.DIRECTION_DOWN, 0.0);
			}
		}
		
		if (mFlyingAnimation.isAnimationRunning() == false) {
			deleteMe();
		}
	}

/*
	@Override
	public void handleCollision(ActiveObject object) {
		if (object.getClass() == Bird.class) {
			Vector2D normal = new Vector2D();
			normal.lookAt(getX(), getY(), object.getX(), object.getY());
			double directionDiff = mVelocity.getDirection() - normal.getDirection();
			mVelocity.addDirection(directionDiff * 2);
			addPosition(normal.getX(), normal.getY());
			//mVelocity.add(object.mVelocity);
		}
	}
	*/
	
}
