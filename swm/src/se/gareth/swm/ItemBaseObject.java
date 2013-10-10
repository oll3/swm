package se.gareth.swm;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class ItemBaseObject extends ActiveObject {

	private static Sprite mItemIconSprite;
	private boolean mDisplayIcon;
	private Animation mItemIconAnimation;
	private Paint mIconPaint;
	
	public ItemBaseObject(GameBase gameBase) {
		super(gameBase);
	    if (mItemIconSprite == null) {
	    	mItemIconSprite = new Sprite(BitmapFactory.decodeResource(game.res,
	       			R.drawable.item_icon1), 1);
	    }
	    mDisplayIcon = false;
	    setAlpha(255);
	    mItemIconAnimation = new Animation(mItemIconSprite, 0, 0);
	    mIconPaint = new Paint();
	}
	
	/* When behavior has finished we display icon */
	protected void behaviorFinished(Behavior behavior) {
		mDisplayIcon = true;
	}
	
	public void displayIcon() {
		mDisplayIcon = true;
	}
	
	public boolean iconIsDisplayed() {
		return mDisplayIcon;
	}
	
	@Override
	public void update(final TimeStep timeStep) {
		if (mDisplayIcon) {
			mItemIconAnimation.update();
		}
		super.update(timeStep);
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (mDisplayIcon) {
			mItemIconAnimation.draw(canvas, getLeft(), getTop(), mIconPaint);
		}
		super.draw(canvas);
	}
	
	/* Should be overridden by item */
	public void picked() {
		
	}
	
	protected void useItem() {
		
	}
}
