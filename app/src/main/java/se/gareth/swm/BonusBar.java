package se.gareth.swm;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

public class BonusBar extends ActiveObject {
	
	private static final int BONUS_LEVEL_TIME = 350;
	private static final int BONUS_LEVELS = 10;
	private static final float DECREASE_BONYUS_DELAY = 0.01f;
	private static final float DECREASE_BONUS_SPEED = 0.09f;

	private final Rect mFrameRect, mBarRect;
	private final Paint mBarPaint, mFramePaint;
	
	private long mLastScoreTS;
	private float mLevelDownSpeed;
	private float mBonusLevel;
	
	public BonusBar(GameBase gameBase) {
		super(gameBase);
		
		mFrameRect = new Rect();
		mBarRect = new Rect();
		
		mBarPaint = new Paint();
		mBarPaint.setStyle(Style.FILL);
		mFramePaint = new Paint();
		mFramePaint.setStyle(Style.FILL);
		mFramePaint.setAlpha(128);
		mBarPaint.setAlpha(128);
		
		mBarPaint.setColor(Color.BLACK);
		
		mFrameRect.top = 0; 
		mFrameRect.bottom = mFrameRect.top + game.convertPixelToDp(5);
		mBarRect.top = mFrameRect.top;
		mBarRect.bottom = mFrameRect.bottom - game.convertPixelToDp(1);
		

		setDrawOrder(30);
		reset();
	}
	
	public void reset() {
		mLastScoreTS = 0;
		mBonusLevel = 0;
		mLevelDownSpeed = 0;
	}
	
	public int calcBonus(int points) {
		
		if (points >= 0) {
			long ts = System.currentTimeMillis();
			
			mLevelDownSpeed = -DECREASE_BONYUS_DELAY;
			
			float bonusRatio = (float)BONUS_LEVEL_TIME/(float)(ts - mLastScoreTS + 1);
			if (bonusRatio > 0.1f && mBonusLevel < (BONUS_LEVELS + 1)) {
				mBonusLevel += bonusRatio;
				if (mBonusLevel > (BONUS_LEVELS + 1)) {
					mBonusLevel = (BONUS_LEVELS + 1);
				}
			}
			
			if (mBonusLevel < 1.0f)
				mBonusLevel = 1.0f;
			
			mLastScoreTS = ts;
			return points * (int)(mBonusLevel - 1.0f);
		}
		else {
	    	mLastScoreTS = 0;
	    	mBonusLevel = 0.0f;
	    	mLevelDownSpeed = 0;
	    	return 0;
		}
	}
	
	
	public void update(final TimeStep timeStep) {
		super.update(timeStep);
		
		mFrameRect.left = 0;
		mFrameRect.right = game.getScreenWidth();

		
		if (mBonusLevel > 0.0f) {
			
			if (mLevelDownSpeed > 0) {
				mBonusLevel -= mLevelDownSpeed;
			}
			if (mBonusLevel < 0.0f) {
				mBonusLevel = 0.0f;
			}
			
			mLevelDownSpeed += timeStep.get() * DECREASE_BONUS_SPEED;
		}
		
		float barRatio = (mBonusLevel / (float)BONUS_LEVELS);
		
		if (barRatio > 1.0f) {
			barRatio = 1.0f;
		}
		
		mBarRect.left = mFrameRect.left;
		mBarRect.right = (int)((mFrameRect.right - mFrameRect.left) * barRatio);
		
		if (barRatio < 0.95) {
			mBarPaint.setColor(Color.rgb(100 - (int)(30 * barRatio), (int)(60 * barRatio) + 128, 50 - (int)(50 * barRatio)));
		}
		else {
			mBarPaint.setColor(Color.rgb(55, 218, 0));
		}
	}
	
	
	public void draw(Canvas canvas) {
		canvas.drawRect(mFrameRect, mFramePaint);
		canvas.drawRect(mBarRect, mBarPaint);
	}
}
