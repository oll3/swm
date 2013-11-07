package se.gareth.swm;

import android.graphics.BitmapFactory;

public class BombItem extends ItemBaseObject {

    private static Sprite mBombItemSprite;

    public BombItem(GameBase gameBase) {
        super(gameBase);
        if (mBombItemSprite == null) {
            mBombItemSprite = new Sprite(BitmapFactory.decodeResource(game.res,
                                                                      R.drawable.item_bomb1), 8);
        }

        setAnimation(new Animation(mBombItemSprite, 8, 0));
    }

    @Override
    protected void useItem() {
        for (HitableObject hitableObject: game.gameStage.mHitableObjects) {
            if (hitableObject instanceof Creature) {
                /* Hit all creatures in game with the bomb */
                game.gameStage.hitObject(hitableObject, 1000);
            }
        }
    }

    @Override
    public void picked() {
        game.itemBar.addItem(this);
    }
}
