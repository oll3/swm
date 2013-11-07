package se.gareth.swm;

import java.lang.Math;
import java.util.ArrayList;
import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;


public class BirdWithCat extends Creature {

    private static ArrayList<Sprite> mFlyingSprite;
    private static ArrayList<LinkedList<Sprite>> mSplittedSpriteList;
    private final Vector2D mAcceleration;

    private Cat1 mCat;
    private double mSpeed;

    /* The number of levels supported for this creature */
    protected static final int numLevels = 5;

    private static final int baseColor = Color.rgb(0x20, 0x30, 0x23);

    public BirdWithCat(GameBase gameBase, int level) {
        /* Can take 100.0 damage, gives 10 points */
        super(gameBase, 50 + 10 * level, 5 + level * 5);
        mSpeed = game.calcVerticalSpeed(200 + level * 150);

        if (mFlyingSprite == null) {
            mFlyingSprite = new ArrayList<Sprite>();
            mSplittedSpriteList = new ArrayList<LinkedList<Sprite>>();
            for (int i = 0; i < numLevels; i ++) {
                Bitmap tmpBitmap = BitmapFactory.decodeResource(game.res, R.drawable.bird2_carrying);
                Sprite sprite = new Sprite(Sprite.replaceColor(tmpBitmap,
                                                               Sprite.ColorChannel.GREEN,
                                                               Sprite.multiplyColor(baseColor, 6.0f / (i + 6))), 8);
                mFlyingSprite.add(sprite);
                LinkedList<Sprite> splittedSpriteList = createSplittedSprite(sprite);
                mSplittedSpriteList.add(splittedSpriteList);
            }
        }

        setSplittedSprite(mSplittedSpriteList.get(level));
        setAnimation(new Animation(mFlyingSprite.get(level), 16, 0));

        mAcceleration = new Vector2D(0, -(1500 + mSpeed));
        applyForce(mAcceleration);
        applyForce(game.forces.getGravity());

        /* How big is our hit area */
        setHitableRadius((double)(Math.min(getWidth(), getHeight()))/3.0);

        /* Set default start position */
        setPosition(game.getScreenWidth() / 4 + mRandom.nextFloat() * (game.getScreenWidth() / 2),
                    game.getPlayfieldHeight() + getHeight());

        mCat = new Cat1(game, level);

        /* place cat behind the bird */
        mCat.setDrawOrder(getDrawOrder() - 1);
        mCat.setMustBeKilled(false);

        setDensity(400);
    }

    @Override
    protected void isAlive() {
        game.gameStage.addCreature(mCat);
    }

    @Override
    protected void wasDestroyed(int damage) {
        deleteMe();
        mCat.mVelocity.set(this.mVelocity);
        mCat.swingBehavior.limitRotation(false);
        mCat.getCurrentAnimation().pause(true);
    }

    @Override
    public void update(final TimeStep timeStep) {
        super.update(timeStep);
        mCat.setPosition(getX(0.07), getY(1.0));

        if ((getBottom() + getHeight()) < 0) {
            deleteMe();

            if (!mCat.isDead()) {
                mCat.deleteMe();
            }
        }
        else {
            if (mCat.isDead()) {
                mAcceleration.set(0, -(1500 + mSpeed * 6));
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
