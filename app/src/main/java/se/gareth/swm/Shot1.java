package se.gareth.swm;

import android.graphics.BitmapFactory;

public class Shot1 extends ActiveObject {

    private static Sprite mShotSprite;

    public Shot1(GameBase gameBase) {
        super(gameBase);

        if (mShotSprite == null) {
            mShotSprite = new Sprite(BitmapFactory.decodeResource(game.res, R.drawable.explotion1), 6);
        }

        setAnimation(new Animation(mShotSprite, 40, 1));
        setDrawOrder(30);
        game.gameStage.addActiveObject(this);
    }

    @Override
    public void update(final TimeStep timeStep) {
        super.update(timeStep);

        if (getCurrentAnimation().isAnimationRunning() == false) {
            deleteMe();
        }

    }

}
