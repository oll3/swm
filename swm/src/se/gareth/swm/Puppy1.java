package se.gareth.swm;

import java.lang.Math;
import java.util.ArrayList;
import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;


public class Puppy1 extends Creature {

    private static ArrayList<Sprite> mSlidingSprite;
    private static ArrayList<LinkedList<Sprite>> mSplittedSpriteList;
    private final Animation mSlidingAnimation;
    private double mSpeed;

    /* The number of levels supported for this creature */
    protected static final int numLevels = 5;

    private static int colorList[] = {
        Color.rgb(0xff, 0x1e, 0x00),
        Color.rgb(0xdf, 0x15, 0x00),
        Color.rgb(0xcf, 0x0e, 0x00),
        Color.rgb(0xaf, 0x05, 0x00),
        Color.rgb(0x8f, 0x00, 0x00)
    };

    public Puppy1(GameBase gameBase, int level) {
        /* Can take 100.0 damage, gives 10 points */
        super(gameBase, 500 + level * 100, 50 + level * 10);
        mSpeed = level * 50;
        if (mSlidingSprite == null) {
            mSlidingSprite = new ArrayList<Sprite>();
            mSplittedSpriteList = new ArrayList<LinkedList<Sprite>>();
            for (int i = 0; i < numLevels; i ++) {
                Bitmap tmpBitmap = BitmapFactory.decodeResource(game.res, R.drawable.puppy1_sliding);
                Sprite sprite = new Sprite(Sprite.replaceColor(tmpBitmap, Sprite.ColorChannel.GREEN, colorList[i], 1), 4);
                mSlidingSprite.add(sprite);
                mSplittedSpriteList.add(createSplittedSprite(sprite));
            }
        }
        setSplittedSprite(mSplittedSpriteList.get(level));
        mSlidingAnimation = new Animation(mSlidingSprite.get(level), 20, 0);

        setAnimation(mSlidingAnimation);

        /* How big is our hit area */
        setHitableRadius((double)(Math.min(getWidth(), getHeight()))/2.0);

        /* Set default start position */
        if (mRandom.nextInt(2) == 0) {
            setPosition(game.getScreenWidth()/5, -getHeight());
        }
        else {
            setPosition(game.getScreenWidth()/5 * 4, -getHeight());
        }

        setDrawOrder(27);
        applyForce(game.forces.getGravity());
        applyForce(new Vector2D(0, -1400.0));
        setCollisionId(11);
    }
    
    @Override
    public void handleCollision(ActiveObject activeObject) {
    	if (activeObject.getY() > getY()) {
    		setSpeed(0);
    	}
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

        if ((getY() - getWidth()) > game.getScreenHeight()) {
            deleteMe();
        }
    }

}
