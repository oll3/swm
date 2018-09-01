package se.gareth.swm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.LinkedList;


public class Hummingbird1 extends Creature {

    private static ArrayList<Sprite> mFlyingSprite;
    private static ArrayList<LinkedList<Sprite>> mSplittedSpriteList;
    private MoveToBehavior mMoveToBehavior;
    private Animation mFlyingAnimation;

    private long mPauseTime;

    private double mSpeed;

    /* The number of levels supported for this creature */
    protected static final int numLevels = 5;

    private static int colorList[] = {
        Color.rgb(0x00, 0xae, 0x9c),
        Color.rgb(0x00, 0x88, 0x7a),
        Color.rgb(0x00, 0x63, 0x58),
        Color.rgb(0x00, 0x34, 0x2e),
        Color.rgb(0x00, 0x1a, 0x17)
    };

    public Hummingbird1(GameBase gameBase, int level) {
        /* Can take 100.0 damage, gives 10 points */
        super(gameBase, 100, 20 + level * 10);
        mSpeed = level * 350;
        if (mFlyingSprite == null) {
            mFlyingSprite = new ArrayList<Sprite>();
            mSplittedSpriteList = new ArrayList<LinkedList<Sprite>>();
            for (int i = 0; i < numLevels; i ++) {
                Bitmap tmpBitmap = BitmapFactory.decodeResource(game.res, R.drawable.hummingbird1_flying);
                Sprite sprite = new Sprite(Sprite.replaceColor(tmpBitmap, Sprite.ColorChannel.GREEN, colorList[i], 2), 9);
                mFlyingSprite.add(sprite);
                mSplittedSpriteList.add(createSplittedSprite(sprite));
            }
        }
        setSplittedSprite(mSplittedSpriteList.get(level));
        mFlyingAnimation = new Animation(mFlyingSprite.get(level), 27, 0);

        setAnimation(mFlyingAnimation);
        //mAcceleration = new Vector2D();
        //setVelocity(Vector2D.DIRECTION_LEFT, speed);
        //setMaxSpeed(speed);
        //applyForce(mAcceleration);
        //applyForce(game.forces.getWind());
        //applyForce(game.forces.getGravity());
        //mAcceleration.set(-100.0, -1000);

        mPauseTime = System.currentTimeMillis();

        /* How big is our hit area */
        setHitableRadius((double)(Math.min(getWidth(), getHeight()))/2.0);

        mMoveToBehavior = null;

        /* Set default start position */
        setPosition(game.getScreenWidth() + getWidth() / 2,
                    getHeight() / 2 + mRandom.nextInt((game.getPlayfieldHeight() - (int)getHeight())));
    }

    @Override
    protected void wasDestroyed(int damage) {
        deleteMe();
    }

    @Override
    protected void wasHit(int damage) {
        super.wasHit(damage);
        setSpeed(0.0);
    }

    @Override
    public void update(final TimeStep timeStep) {
        super.update(timeStep);

        if (System.currentTimeMillis() > (mPauseTime)) {

            if (mMoveToBehavior != null) {
                /* delete a previous set move to behavior */
                removeBehavior(mMoveToBehavior);
                mMoveToBehavior = null;
            }

            mPauseTime = System.currentTimeMillis() + 1400 - (int)mSpeed/2;
            int hrange = game.getScreenWidth() / 10;
            int vrange = (int)(game.getPlayfieldHeight() * 1.0);
            double x = getX() - (mRandom.nextInt(hrange) + hrange);
            double y = getY() + (vrange - mRandom.nextInt(vrange * 2));
            if (y < getHeight() / 2)
                y = getHeight() / 2;
            else if (y > game.getPlayfieldHeight() - getHeight() / 2)
                y = game.getPlayfieldHeight() - getHeight() / 2;

            mMoveToBehavior = new MoveToBehavior(game, x, y, 1200.0 - mSpeed/2);
            addBehavior(mMoveToBehavior);
        }

        if (getX() < 0) {

            if ((getX() + getWidth()) < 0) {
                deleteMe();
            }
        }
    }


}
