package se.gareth.swm;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

public class BonusBar {
	
	private static final int BONUS_LEVEL_TIME = 350;
	private static final int BONUS_LEVELS = 10;
	private static final float DECREASE_BONYUS_DELAY = 0.01f;
	private static final float DECREASE_BONUS_SPEED = 0.09f;

	private static Sprite mBonusBarSprite;
	private final Rect mFrameRect, mBarRect;
	private final GameBase game;
	private final Paint mBarPaint;
	
	private long mLastScoreTS;
	private float mLevelDownSpeed;
	private float mBonusLevel;
	
	public BonusBar(GameBase gameBase) {
		game = gameBase;
		
    	if (mBonusBarSprite == null) {
    		mBonusBarSprite = new Sprite(BitmapFactory.decodeResource(game.res,
        			R.drawable.bonusbar_frame), 1);
    	}
		
		mFrameRect = new Rect();
		mBarRect = new Rect();
		
		mBarPaint = new Paint();
		mBarPaint.setStyle(Style.FILL);
		mBarPaint.setColor(Color.rgb(0xfd, 0x59, 0x61));
		
		mFrameRect.top = game.convertPixelToDp(15);
		mFrameRect.bottom = mFrameRect.top + (int)(mBonusBarSprite.getFrameHeight() + 0.5);
		mBarRect.top = mFrameRect.top + game.convertPixelToDp(2);
		mBarRect.bottom = mFrameRect.bottom - game.convertPixelToDp(2);
		
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
		
		mFrameRect.left = game.convertPixelToDp(80);
		mFrameRect.right = game.getScreenWidth() - mFrameRect.left;

		
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
		
		mBarRect.left = mFrameRect.left + game.convertPixelToDp(2) + (int)((mFrameRect.right - mFrameRect.left - game.convertPixelToDp(4)) * barRatio);
		mBarRect.right = mFrameRect.right - game.convertPixelToDp(2);
	}
	
	
	public void draw(Canvas canvas) {
		canvas.drawBitmap(mBonusBarSprite.getFrame(0), null, mFrameRect, null);
		canvas.drawRect(mBarRect, mBarPaint);
	}
}
