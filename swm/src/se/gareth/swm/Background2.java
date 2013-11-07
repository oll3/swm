package se.gareth.swm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;


public class Background2 extends Background {


    public class ForegroundThree extends HitableObject {

        private Sprite mSprite;

        public ForegroundThree(GameBase gameBase, Bitmap bitmap) {
            super(gameBase);
            mSprite = new Sprite(bitmap, 1);
            setAnimation(new Animation(mSprite, 0, 0));

            setHealthAttributes(0, 0);
            setFreeStanding(true);
            disableMovement(true);
            setDrawOrder(25);
            setKillable(false);
        }

        @Override
        public double isHit(ActiveObject object) {
            if (containsPos(object.getX(), object.getY())) {

                double distance = Math.abs(getX() - object.getX());
                double hit = (getWidth() / 2.0 - distance) * 1.2 / (getWidth() / 2.0);
                hit = Math.min(1.0, hit);
                Log.d(null, "Tree was hit: " + hit);
                return hit;
            }
            return 0;
        }

        @Override
        public int getHitPoints() {
            return 200;
        }

        public void unload() {
            mSprite.recycle();
        }
    }


    protected final Bitmap mThumbnail;
    private Bitmap mBackground;
    public ForegroundThree tree1, tree2;

    public Background2(GameBase gameBase, int colorParam) {
        super(gameBase, colorParam);

        mThumbnail = BitmapFactory.decodeResource(game.res, R.drawable.background2_thumbnail);
    }

    @Override
    protected void load() {
        mBackground = BitmapFactory.decodeResource(game.res, R.drawable.background2_below);

        tree1 = new ForegroundThree(game, BitmapFactory.decodeResource(game.res, R.drawable.background2_tree1));
        tree1.setPosition(game.getScreenWidth()/5, game.getScreenHeight()/2);

        tree2 = new ForegroundThree(game, BitmapFactory.decodeResource(game.res, R.drawable.background2_tree2));
        tree2.setPosition(game.getScreenWidth()/5 * 4, game.getScreenHeight()/2);
    }

    @Override
    protected void unload() {
        mBackground.recycle();
        mBackground = null;
        tree1.unload();
        tree1 = null;
        tree2.unload();
        tree2 = null;
    }

    @Override
    public void prepare(GameStage gameStage) {
        gameStage.addActiveObject(tree1);
        gameStage.addActiveObject(tree2);
    }

    @Override
    protected Bitmap getThumbnail() {
        return mThumbnail;
    }

    @Override
    public void update(final TimeStep timeStep) {

    }

    @Override
    public void drawBackground(Canvas canvas) {
        for (int x = 0; x < game.getScreenWidth(); x += mBackground.getWidth()) {
            canvas.drawBitmap(mBackground, x, 0, null);
        }
    }
}
