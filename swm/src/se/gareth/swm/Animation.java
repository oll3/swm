package se.gareth.swm;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Animation {
	
	private Sprite mSprite;
	
	private int mCurrentFrame;

	private int mFps;
	private long mTimeToNext;
	
	private int mRepeat;
	private int mRepeated;
	
	private boolean mRunning;
	private boolean mReversed;
	private boolean mPaused;
	
	private Matrix mTransformMatrix;
	
	public Animation(Sprite sprite, int fps, int repeat) {
		
		mSprite = sprite;
		mFps = fps;
		mRepeat = repeat;
		mRepeated = 0;
		mRunning = true;
		mReversed = false;		
		mPaused = false;
		
		mCurrentFrame = 0;
		if (mFps > 0) {
			mTimeToNext = System.currentTimeMillis() + (1000 / mFps);
		}
		
		mTransformMatrix = new Matrix();
	}
	
	public void runReversed(boolean reversed) {
		mReversed = reversed;
	}
	
	public void restart() {
		restartAtFrame(0);
	}
	
	public void pause(boolean do_pause) {
		mPaused = do_pause;
	}
		
	public void restartAtFrame(int frame) {
		mRunning = true;
		mRepeated = 0;
		if (frame < mSprite.getFrames())
			mCurrentFrame = frame;
		else
			mCurrentFrame = 0;
		
		mTimeToNext = System.currentTimeMillis() + (1000 / mFps);
	}
	
	public void update() {
		if (mFps > 0 && mSprite.getFrames() > 1  && mRunning && !mPaused) {
			if (System.currentTimeMillis() > mTimeToNext) {
				
				mTimeToNext = System.currentTimeMillis() + (1000 / mFps);

				if (mReversed) {
					/* Running animation in reversed order */
					mCurrentFrame --;
				}
				else {
					/* Running animation forward */
					mCurrentFrame ++;
				}

				if (mCurrentFrame >= mSprite.getFrames() || mCurrentFrame < 0) {

					if (mRepeat > 0) {
						mRepeated ++;

						if (mRepeated < mRepeat) {
							if (mReversed)
								/* Restart at last frame */
								mCurrentFrame = mSprite.getFrames() - 1;
							else
								/* Restart at first frame */
								mCurrentFrame = 0;
						}
						else {
							mRunning = false;
						}
					}
					else {
						if (mReversed)
							/* Restart at last frame */
							mCurrentFrame = mSprite.getFrames() - 1;
						else
							/* Restart at first frame */
							mCurrentFrame = 0;
					}
				}
			}
		}
	}
	
	public void draw(Canvas canvas, double x, double y, Paint paint) {
		mTransformMatrix.reset();
		mTransformMatrix.postTranslate((float)x, (float)y);
		Bitmap bitmap = mSprite.getFrame(mCurrentFrame);
		canvas.drawBitmap(bitmap, mTransformMatrix, paint);
	}
	
	public void draw(Canvas canvas, double x, double y, float rotation, float rotationX, float rotationY, Paint paint) {
		mTransformMatrix.reset();
		mTransformMatrix.postRotate(rotation, rotationX, rotationY);
		mTransformMatrix.postTranslate((float)x, (float)y);
		Bitmap bitmap = mSprite.getFrame(mCurrentFrame);
		canvas.drawBitmap(bitmap, mTransformMatrix, paint);
	}
	
	public boolean isAnimationRunning() {
		return mRunning;
	}
	
	public int getWidth() {
		return mSprite.getFrameWidth();
	}
	
	public int getHeight() {
		return mSprite.getFrameHeight();
	}
}
