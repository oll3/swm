package se.gareth.swm;

import java.lang.Math;
import java.util.ArrayList;
import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;


public class Bat1 extends Creature {

	private boolean mIsChildBat;
    private static ArrayList<Sprite> mFlyingSprite;
    private static Sprite mSmallBatFlyingSprite;
    private static ArrayList<LinkedList<Sprite>> mSplittedSpriteList;
    private double mSpeed;

    private final Vector2D mAcceleration;
    
    /* The number of levels supported for this creature */
    protected static final int numLevels = 3;

    private static int colorList[] = {
        Color.rgb(0x37, 0x48, 0x45),
        Color.rgb(0x27, 0x38, 0x35),
        Color.rgb(0x17, 0x28, 0x25)
    };
    
    public Bat1(GameBase gameBase, int level) {
        super(gameBase, 150 + 80 * level, 50 + level * 10);
        mSpeed = 800 + 500 * level;
        mIsChildBat = false;
        if (mFlyingSprite == null) {
            mFlyingSprite = new ArrayList<Sprite>();
            mSplittedSpriteList = new ArrayList<LinkedList<Sprite>>();
            for (int i = 0; i < numLevels; i ++) {
                Bitmap tmpBitmap = BitmapFactory.decodeResource(game.res, R.drawable.bat1_flying);
                Sprite sprite = new Sprite(Sprite.replaceColor(tmpBitmap,
                                                               Sprite.ColorChannel.GREEN, colorList[i]), 6);
                mFlyingSprite.add(sprite);
                LinkedList<Sprite> splittedSpriteList = createSplittedSprite(sprite);
                mSplittedSpriteList.add(splittedSpriteList);
            }
            
            mSmallBatFlyingSprite = new Sprite(Sprite.replaceColor(BitmapFactory.decodeResource(game.res, R.drawable.small_bat1_flying),
                    Sprite.ColorChannel.GREEN, Color.rgb(0x17, 0x18, 0x15)), 6);
        }

        setSplittedSprite(mSplittedSpriteList.get(level));
        setAnimation(new Animation(mFlyingSprite.get(level), 24, 0));

        /* How big is our hit area */
        setHitableRadius((double)(Math.min(getWidth(), getHeight()))/2.0);

        /* Set default start position */
        setPosition(game.getScreenWidth() + getWidth() / 2,
                    getHeight() / 2 + mRandom.nextInt((game.getPlayfieldHeight() - (int)getHeight())));
        
        setDrawOrder(21);
        
        
        mAcceleration = new Vector2D(mSpeed / 2 - (mRandom.nextInt((int)mSpeed)), 
        		mSpeed / 2 - (mRandom.nextInt((int)mSpeed)));
        applyForce(mAcceleration);
    }
    
    private void createChildBat(double x, double y) {
    	Bat1 childBat = new Bat1(game, 0);
    	childBat.setAnimation(new Animation(mSmallBatFlyingSprite, 24, 0));
    	childBat.setPosition(x, y);
    	childBat.mIsChildBat = true;
    	childBat.setHealthAttributes(80, 0);
    	childBat.mVelocity.set(childBat.mAcceleration.getX() * 3, childBat.mAcceleration.getY() * 3);
    	game.gameStage.addCreature(childBat);
    }

    @Override
    protected void wasDestroyed(int damage) {
    	if (!mIsChildBat) {
    		for (int i = 0 ; i < (mRandom.nextInt(4) + 3); i ++) {
    			createChildBat(getX(), getY());
    		}
    	}
        deleteMe();
    }

    @Override
    public void update(final TimeStep timeStep) {
        super.update(timeStep);
        if (mAcceleration.getX() < 0) {
        	if (getX() < game.getScreenWidth()/10) {
        		mAcceleration.setX(mRandom.nextInt((int)mSpeed/2) + mSpeed);
        		mAcceleration.setY(mSpeed / 2 - (mRandom.nextInt((int)mSpeed)));
        	}
        }
        else if (mAcceleration.getX() > 0) {
        	if (getX() > (9 * game.getScreenWidth()/10)) {
        		mAcceleration.setX(-(mRandom.nextInt((int)mSpeed/2) + mSpeed));
        		mAcceleration.setY(mSpeed / 2 - (mRandom.nextInt((int)mSpeed)));
        	}
        }
        
        if (mAcceleration.getY() < 0) {
        	if (getY() < game.getScreenHeight()/7) {
        		mAcceleration.setY(mRandom.nextInt((int)mSpeed/2) + mSpeed);
        		mAcceleration.setX(mSpeed / 2 - (mRandom.nextInt((int)mSpeed)));
        	}
        }
        else if (mAcceleration.getY() > 0) {
        	if (getY() > (6 * game.getScreenHeight()/7)) {
        		mAcceleration.setY(-(mRandom.nextInt((int)mSpeed/2) + mSpeed));
        		mAcceleration.setX(mSpeed / 2 - (mRandom.nextInt((int)mSpeed)));
        	}
        }
    }
}
