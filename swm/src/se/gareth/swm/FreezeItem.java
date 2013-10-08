package se.gareth.swm;

import android.graphics.BitmapFactory;

public class FreezeItem extends ItemBaseObject {

	private static Sprite mFreezeItemSprite;
	
	public FreezeItem(GameBase gameBase) {
		super(gameBase);
    	if (mFreezeItemSprite == null) {
    		mFreezeItemSprite = new Sprite(BitmapFactory.decodeResource(game.res,
        			R.drawable.item_freeze1), 8);
    	}
    	
    	setAnimation(new Animation(mFreezeItemSprite, 8, 0));
	}
	
	@Override
	protected void useItem() {
		
		game.sounds.play(Sounds.Sound.Freeze1);
		
		for (GraphicObject graphicObject: game.gameStage.mGraphicObjects) {
			if (graphicObject instanceof Creature) {
				/* Freeze all items currently in playfield for 2000 ms */
				game.gameStage.addActiveObject(new IceCube(game, (Creature)graphicObject, 3000));
//				((Creature) graphicObject).addBehavior(new FreezeBehavior(game, 3000));
				game.gameStage.getLevel().pauseCreation(3000);
			}
		}
	}
	
	@Override
	public void picked() {
		game.itemBar.addItem(this);
	}
}
