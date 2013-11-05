package se.gareth.swm;

import java.util.ArrayList;


public class Creature extends HitableObject {

	private final ArrayList<ActiveObject> mBloodList;
	private long mLastDrip;
	private double mBloodStainScaleX, mBloodStainScaleY;
	
	/* The number of levels supported for this creature (override to change)  */
	protected static final int numLevels = 1;
	
	
	public Creature(GameBase gameBase, int hitPoints, int hitScore) {
		super(gameBase);
		
		setHealthAttributes(hitPoints, 0);
		setHitScore(hitScore);
		mLastDrip = 0;
		mBloodList = new ArrayList<ActiveObject>();
		
		mBloodStainScaleX = 0.5;
		mBloodStainScaleY = 0.5;
		
		for (int i = 0; i < 10; i ++) {
			/* Allocate the blood for effect to use if creature is shot in advance */
			mBloodList.add(new Blood1(game));
			mBloodList.add(new Blood2(game));
		}
	}
	
	@Override
	protected void wasHit(int damage) {
		
		game.sounds.play(Sounds.Sound.Splash1);

		/* how much of our blood bank to use on this hit */
		double killPercentage = (double)damage / (double)getHitPoints();
		
		int useCnt = (int)((double)mBloodList.size() * killPercentage);

		while (useCnt > 0 && mBloodList.size() > 0) {
			final ActiveObject blood = mBloodList.remove(mBloodList.size() - 1);
			double direction = mRandom.nextDouble() * 2 * Math.PI;
			double speed = mRandom.nextDouble() * 300 + 300.0 + damage/100.0;
			blood.setPosition(getX(), getY());
			blood.setVelocity(direction, speed);
			blood.addVelocity(mVelocity);
			blood.setDrawOrder(getDrawOrder());
			game.gameStage.addActiveObject(blood);
    		useCnt --;
    	}
	}
	
	protected void setBloodStainOffset(double x, double y) {
		mBloodStainScaleX = x;
		mBloodStainScaleY = y;
	}
	
	
	@Override
	public void update(final TimeStep timeStep) {
		super.update(timeStep);
		
		/* If we've been hit but still alive - drip some blood */
		if (getHitCount() > 0) {
			if (System.currentTimeMillis() > mLastDrip) {
				Blood2 blood = new Blood2(game);
				double x = getLeft() + mBloodStainScaleX * getWidth() + mRandom.nextInt(20) - 10;
				double y = getTop() + mBloodStainScaleY * getHeight() + mRandom.nextInt(20) - 10;
				blood.setPosition(x, y);
				blood.setVelocity(mRandom.nextDouble() * Math.PI * 2, 50);
				blood.addVelocity(this.mVelocity);
				blood.setDrawOrder(getDrawOrder());
				game.gameStage.addActiveObject(blood);
				mLastDrip = System.currentTimeMillis() + mRandom.nextInt(70) + 10;
			}
		}
		
	}
	
}
