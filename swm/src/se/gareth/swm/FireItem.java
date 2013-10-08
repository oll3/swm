package se.gareth.swm;

import android.graphics.BitmapFactory;

public class FireItem extends ItemBaseObject {

	private static Sprite mFireItemSprite;
	
	public FireItem(GameBase gameBase) {
		super(gameBase);
    	if (mFireItemSprite == null) {
    		mFireItemSprite = new Sprite(BitmapFactory.decodeResource(game.res,
        			R.drawable.item_fire1), 8);
    	}
    	
    	setAnimation(new Animation(mFireItemSprite, 8, 0));
	}
	
	@Override
	protected void useItem() {
		
		for (GraphicObject graphicObject: game.gameStage.mGraphicObjects) {
			if (graphicObject instanceof Creature) {
				Creature creature = (Creature)graphicObject;
				creature.setHealthAttributes(10, 0);
				int hitWith = creature.getHitPoints() - 1;
				if (hitWith < 0)
					hitWith = 1;
				game.gameStage.hitObject(creature, hitWith);
				
				/* Make the creature burn! */
				game.gameStage.addActiveObject(new Fire1(game, creature));
			}
		}
	}
	
	@Override
	public void picked() {
		game.itemBar.addItem(this);
	}
}
