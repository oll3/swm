package se.gareth.swm;

import java.util.Iterator;
import java.util.LinkedList;

import android.graphics.BitmapFactory;


public class ItemBox extends HitableObject {

	private static Sprite mItemSprite;
	private static LinkedList<Sprite> mSplittedSpriteList;
	private Vector2D mAccelerationForce;
	
	private ItemBaseObject mItem;
	private LinkedList<Scrap1> mScrapList;

	public ItemBox(GameBase gameBase, double x, double y, ItemBaseObject item) {
		/* Allow two hits. First will pick the item, second will use it. */
		super(gameBase);
       	setDrawOrder(15);
    	
       	setHealthAttributes(0, 1); /* allow one hit */
       	setHitScore(10);
       	
       	setPosition(x, y);
		setVelocity(Vector2D.DIRECTION_DOWN, 1.0);
		setMaxSpeed(170.0);
		
    	if (mItemSprite == null) {
    		mItemSprite = new Sprite(BitmapFactory.decodeResource(game.res,
        			R.drawable.item_box1), 1);
    		mSplittedSpriteList = createSplittedSprite(mItemSprite);
    	}
    	setSplittedSprite(mSplittedSpriteList);
    	setAnimation(new Animation(mItemSprite, 0, 0));
    	setHitableRadius(mCollisionRadius*1.25);
    	
    	mItem = item;

		//applyForce(mGameStage.getWindForce());
		applyForce(game.forces.getGravity());
		
       	mAccelerationForce = new Vector2D(0.0, -7.0);
       	applyForce(mAccelerationForce);
       	
       	
       	/* Make the box swing a bit while falling */
       	setRotationOffset(0.5f, 0);
       	addBehavior(new SwingBehavior(game, 20f));
       	
		mScrapList = new LinkedList<Scrap1>();
		
		for (int i = 0; i < 20; i ++) {
			/* Allocate the scrap objects for effect to use if creature is shot in advance */
			mScrapList.add(new Scrap1(game));
		}
	}
	
	@Override
	protected void wasDestroyed(int damage) {
		
		game.sounds.play(Sounds.Sound.BreakingWood1);
		
		Iterator<Scrap1> itr = mScrapList.iterator();
		
    	while (itr.hasNext()) {
    		Scrap1 scrap = itr.next();
    		itr.remove();
			double direction = mRandom.nextDouble() * 2 * Math.PI;
			double speed = mRandom.nextDouble() * 200 + 200.0 + damage/100.0;
			scrap.setPosition(getX(), getY());
			scrap.setVelocity(direction, speed);
			game.gameStage.addActiveObject(scrap);
    	}
		
		mItem.setPosition(getX(), getY());
		
		/* Tell item it was picked up by player */
		mItem.picked();
		deleteMe();
	}
	
	
	@Override
	public void update(final TimeStep timeStep) {
		double randX = (mRandom.nextDouble() - 0.5) * 500.0;
		if (Math.abs(mAccelerationForce.getX() + randX) < 1000.0)
			mAccelerationForce.addX(randX);
		
		super.update(timeStep);
		
		if (getY() > (game.getScreenHeight() + getHeight()/2)) {
			/* Outside playfield, remove! */
			deleteMe();
		}

	}
}
