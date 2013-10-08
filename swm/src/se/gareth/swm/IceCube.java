package se.gareth.swm;


import java.util.LinkedList;

import android.graphics.BitmapFactory;

public class IceCube extends HitableObject {

	private static Sprite mIceCubeSprite;
	private static LinkedList<Sprite> mSplittedSpriteList;
	private HitableObject mAttachedTo;
	private long mFreezeTime;

	public IceCube(GameBase gameBase, HitableObject attachTo, long freezeTime) {
		super(gameBase);
       	setDrawOrder(100); /* above all objects */
       	
       	mAttachedTo = attachTo;
    	
       	setHealthAttributes(0, 1); /* only destroy when our parent is destroyed */
       	setHitScore(0);
   
       	mFreezeTime = freezeTime + System.currentTimeMillis();
       	
		setVelocity(0.0, 0.0);
		setMaxSpeed(0.0);
		
		if (mIceCubeSprite == null) {
			mIceCubeSprite = new Sprite(BitmapFactory.decodeResource(game.res,
        			R.drawable.icecube1), 1);
			mSplittedSpriteList = createSplittedSprite(mIceCubeSprite);
    	}
		setSplittedSprite(mSplittedSpriteList);
    	setAnimation(new Animation(mIceCubeSprite, 0, 0));
    	setHitableRadius(mCollisionRadius*1.25);
	}
	
	@Override
	protected void wasDestroyed(int damage) {
		game.sounds.play(Sounds.Sound.BreakingGlass1);
		mAttachedTo.getCurrentAnimation().pause(false);
		mAttachedTo.disableMovement(false);
		deleteMe();
	}
	
	@Override
	public double isHit(ActiveObject object) {
		/* only destroy when our parent is destroyed */
		return 0;
	}
	
	@Override
	public void update(double frameTime) {
		super.update(frameTime);
		
		setPosition(mAttachedTo.getCenterX(), mAttachedTo.getCenterY());
		
		if (mAttachedTo.hasBeenDestroyed()) {
			/* If our parent object has been destroyed, then destroy our selves */
			destroyMe(0);
		}
		else if (System.currentTimeMillis() > mFreezeTime) {
			destroyMe(0);
		}
		else {
			mAttachedTo.setVelocity(0.0, 0.0); /* keep it still */
			mAttachedTo.getCurrentAnimation().pause(true);
			mAttachedTo.disableMovement(true);
		}
	}
}
