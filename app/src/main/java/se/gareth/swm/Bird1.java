package se.gareth.swm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.LinkedList;


public class Bird1 extends Creature {

    private static ArrayList<Sprite> mFlyingSprite;
    private static ArrayList<LinkedList<Sprite>> mSplittedSpriteList;
    private final Vector2D mAcceleration;

    /* The number of levels supported for this creature */
    protected static final int numLevels = 5;

    private static final int colorList[] = {
    	Color.rgb(0xfa, 0x90, 0xa0),
        Color.rgb(0xf7, 0x4d, 0x67),
        Color.rgb(0xf4, 0x0c, 0x2f),
        Color.rgb(0xb2, 0x08, 0x22),
        Color.rgb(0x6c, 0x05, 0x14)
    };
    
    
    public Bird1(GameBase gameBase, int level) {
        /* Can take 100.0 damage, gives 10 points */
        super(gameBase, 50 + 10 * level, 5 + level * 5);
        double speed = 150 + level * 220;

        if (mFlyingSprite == null) {
            mFlyingSprite = new ArrayList<Sprite>();
            mSplittedSpriteList = new ArrayList<LinkedList<Sprite>>();
            for (int i = 0; i < numLevels; i ++) {
                Bitmap tmpBitmap = BitmapFactory.decodeResource(game.res, R.drawable.bird1_flying);
                Sprite sprite = new Sprite(Sprite.replaceColor(tmpBitmap,
                                                               Sprite.ColorChannel.GREEN,
                                                               colorList[i]), 8);
                mFlyingSprite.add(sprite);
                LinkedList<Sprite> splittedSpriteList = createSplittedSprite(sprite);
                mSplittedSpriteList.add(splittedSpriteList);
            }
        }

        setSplittedSprite(mSplittedSpriteList.get(level));
        setAnimation(new Animation(mFlyingSprite.get(level), 16, 0));

        setVelocity(Vector2D.DIRECTION_LEFT, speed);

        mAcceleration = new Vector2D(-speed, -game.forces.getGravity().getY());
        applyForce(mAcceleration);
        applyForce(game.forces.getGravity());

        /* How big is our hit area */
        setHitableRadius((double)(Math.min(getWidth(), getHeight()))/2.0);

        /* Set default start position */
        setPosition(game.getScreenWidth() + getWidth() / 2,
                    getHeight() / 2 + mRandom.nextInt((game.getPlayfieldHeight() - (int)getHeight())));
    }

    @Override
    protected void wasDestroyed(int damage) {
        deleteMe();
    }

    @Override
    public void update(final TimeStep timeStep) {
        super.update(timeStep);

        if ((getX() + getWidth()) < 0) {
            deleteMe();
        }
    }
}
