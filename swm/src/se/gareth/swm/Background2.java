package se.gareth.swm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;


public class Background2 extends Background {

	protected final Bitmap mThumbnail;
	private Bitmap mBackground, mTree1, mTree2;
	
	public Background2(GameBase gameBase, int colorParam) {
		super(gameBase, colorParam);
    	
    	mThumbnail = BitmapFactory.decodeResource(game.res, R.drawable.background2_thumbnail);
	}
	
	@Override
	protected void load() {
		mBackground = BitmapFactory.decodeResource(game.res, R.drawable.background2_below);
		mTree1 = BitmapFactory.decodeResource(game.res, R.drawable.background2_tree1);
		mTree2 = BitmapFactory.decodeResource(game.res, R.drawable.background2_tree2);
		
	}
	
	@Override
	protected void unload() {
		mBackground.recycle();
		mTree1.recycle();
		mTree2.recycle();
		mBackground = null;
		mTree1 = null;
		mTree2 = null;
	}
	
	@Override
	protected Bitmap getThumbnail() {
		return mThumbnail;
	}
	
	@Override
	public void update(final TimeStep timeStep) {

	}
	
	@Override
	public void drawBackground(Canvas canvas) {
		for (int x = 0; x < game.getScreenWidth(); x += mBackground.getWidth()) {
			canvas.drawBitmap(mBackground, x, 0, null);
		}
	}
	
	@Override
	public void drawForeground(Canvas canvas) {
		canvas.drawBitmap(mTree1, game.getScreenWidth()/5 - mTree1.getWidth()/2, 0, null);
		canvas.drawBitmap(mTree2, (game.getScreenWidth()/5 * 4) - mTree2.getWidth()/2, 0, null);
	}
}
