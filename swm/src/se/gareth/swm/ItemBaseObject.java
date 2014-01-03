package se.gareth.swm;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ItemBaseObject extends ActiveObject {

	public boolean inBar = false;
	public boolean hasStopped = false;
    private static Sprite mItemIconSprite, mCounterBgSprite;
    private boolean mDisplayIcon;
    private final Animation mItemIconAnimation, mCounterAnimation;
    private final Paint mIconPaint;
    private final TextDrawable mCounterText;
    private int mItemCounter;

    public ItemBaseObject(GameBase gameBase) {
        super(gameBase);
        if (mItemIconSprite == null) {
            mItemIconSprite = new Sprite(BitmapFactory.decodeResource(game.res, R.drawable.item_icon1), 1);
            mCounterBgSprite = new Sprite(BitmapFactory.decodeResource(game.res, R.drawable.item_icon_counter), 1);
        }
        mDisplayIcon = false;
        setAlpha(255);
        mItemIconAnimation = new Animation(mItemIconSprite, 0, 0);
        mCounterAnimation = new Animation(mCounterBgSprite, 0, 0);
        mIconPaint = new Paint();
        mCounterText = new TextDrawable(gameBase.gameView.font);
        mCounterText.setTextSize(game.res.getDimension(R.dimen.MicroFontSize), false);
        mCounterText.setOutline(game.res.getDimension(R.dimen.SmallFontOutline),
                              game.res.getColor(R.color.NormalOutlineColor));
        mCounterText.setTextAlign(TextDrawable.Align.CENTER);
        mCounterText.setColor(game.res.getColor(R.color.LightGreenFontColor));
        mItemCounter = 0;
        increaseCount();
    }

    /* When behavior has finished we display icon */
    protected void behaviorFinished(Behavior behavior) {
        mDisplayIcon = true;
        hasStopped = true;
    }

    public void displayIcon() {
        mDisplayIcon = true;
    }

    public boolean iconIsDisplayed() {
        return mDisplayIcon;
    }
    
    public void increaseCount() {
    	mItemCounter ++;
    	mCounterText.setText("" + mItemCounter);
    }
    
    public void decreaseCount() {
    	mItemCounter --;
    	mCounterText.setText("" + mItemCounter);
    }
    
    public int getCount() {
    	return mItemCounter;
    }

    @Override
    public void update(final TimeStep timeStep) {
        if (mDisplayIcon) {
            mItemIconAnimation.update();
        }
        super.update(timeStep);
        mCounterText.setPosition(getRight()-mCounterAnimation.getWidth()/2, getBottom() - mCounterAnimation.getHeight()*0.52);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mDisplayIcon) {
            mItemIconAnimation.draw(canvas, getLeft(), getTop(), mIconPaint);
        }
        super.draw(canvas);
        if (mDisplayIcon) {
        	if (mItemCounter > 0) {
        		mCounterAnimation.draw(canvas, getRight()-mCounterAnimation.getWidth(), getBottom() - mCounterAnimation.getHeight(), mIconPaint);
        		mCounterText.draw(canvas);
        	}
        }
    }

    /* Should be overridden by item */
    public void picked() {

    }

    protected void useItem() {

    }
}
