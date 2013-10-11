package se.gareth.swm;

import android.graphics.BitmapFactory;

public class ExtraLifeItem extends ItemBaseObject {

	private static Sprite mHeartItemSprite;
	
	public ExtraLifeItem(GameBase gameBase) {
		super(gameBase);
    	if (mHeartItemSprite == null) {
    		mHeartItemSprite = new Sprite(BitmapFactory.decodeResource(game.res,
        			R.drawable.item_heart1), 1);
    	}
    	
    	setAnimation(new Animation(mHeartItemSprite, 0, 0));
	}
	
	protected void behaviorFinished(Behavior behavior) {
		game.health.addLifes(1);
		deleteMe();
	}
	
	@Override
	protected void useItem() {

	}
	
	@Override
	public void picked() {
		double x = game.health.getX();
		double y = game.health.getY();
		addBehavior(new MoveToBehavior(game, x, y, 1000.0));
		game.gameStage.addActiveObject(this);
	}
}

