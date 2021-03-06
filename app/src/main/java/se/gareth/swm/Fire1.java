package se.gareth.swm;

import android.graphics.BitmapFactory;

public class Fire1 extends HitableObject {

    private static Sprite mFireSprite;
    private HitableObject mAttachedTo;

    private static int mUseCnt = 0;

    public Fire1(GameBase gameBase, HitableObject attachTo) {
        super(gameBase);
        setDrawOrder(100); /* above all objects */

        mAttachedTo = attachTo;

        setHealthAttributes(0, 1); /* only destroy when our parent is destroyed */
        setHitScore(0);

        disableMovement(true);

        if (mFireSprite == null) {
            mFireSprite = new Sprite(BitmapFactory.decodeResource(game.res, R.drawable.fire1), 6);
        }
        setAnimation(new Animation(mFireSprite, 15, 0));
        if (mUseCnt == 0) {
            game.sounds.loop(Sounds.Sound.Fire1, -1);
        }
        mUseCnt ++;
    }

    @Override
    protected void wasDestroyed(int damage) {
        deleteMe();
    }

    @Override
    public double isHit(ActiveObject object) {
        /* only destroy when our parent is destroyed */
        return 0;
    }

    @Override
    public void update(final TimeStep timeStep) {
        super.update(timeStep);

        setPosition(mAttachedTo.getX(), mAttachedTo.getY() - mAttachedTo.getHeight() / 3.0);

        if (mAttachedTo.hasBeenDestroyed() || mAttachedTo.isToBeDeleted()) {
            /* If our parent object has been destroyed, then destroy our selves */
            mUseCnt --;
            if (mUseCnt == 0) {
                game.sounds.stop(Sounds.Sound.Fire1);
            }
            destroyMe(0);
        }
    }
}
