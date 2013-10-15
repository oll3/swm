package se.gareth.swm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;


public class Background1 extends Background {

	protected final Bitmap mThumbnail;
	private Bitmap mBottomBitmap, mBelowBitmap;
	private float mBottomX, mBelowX;
	
	public Background1(GameBase gameBase, int colorParam) {
		super(gameBase, colorParam);
    	mBottomX = 0;
    	mBelowX = 0;
    	
    	mThumbnail = BitmapFactory.decodeResource(game.res, R.drawable.background1_thumbnail);
	}
	
	@Override
	protected void load() {
		mBottomBitmap = BitmapFactory.decodeResource(game.res, R.drawable.background1_bottom);
		mBelowBitmap = BitmapFactory.decodeResource(game.res, R.drawable.background1_below);
	}
	
	@Override
	protected void unload() {
		mBottomBitmap.recycle();
		mBelowBitmap.recycle();
		mBottomBitmap = null;
		mBelowBitmap = null;
	}
	
	@Override
	protected Bitmap getThumbnail() {
		return mThumbnail;
	}
	
	public void update(final TimeStep timeStep) {
		mBottomX += 20.0 * timeStep.get();
		mBelowX += 10.0 * timeStep.get();
		
		while (mBelowX > mBelowBitmap.getWidth()) {
			mBelowX -= mBelowBitmap.getWidth();
		}
		
		while (mBottomX > mBottomBitmap.getWidth()) {
			mBottomX -= mBottomBitmap.getWidth();
		}
	}
	
	public void draw(Canvas canvas) {
		
		canvas.drawColor(backgroundColor);
		
		
		for (int x = -mBelowBitmap.getWidth() + (int)mBelowX; x < game.getScreenWidth(); x += mBelowBitmap.getWidth()) {
			canvas.drawBitmap(mBelowBitmap, x, (float)game.getScreenHeight() / 4f, null);
		}
		
		for (int x = -mBottomBitmap.getWidth() + (int)mBottomX; x < game.getScreenWidth(); x += mBottomBitmap.getWidth()) {
			canvas.drawBitmap(mBottomBitmap, x, (float)game.getScreenHeight() / 2f, null);
		}
	}
	
}
