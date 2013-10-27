package se.gareth.swm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;


public class Background2 extends Background {


	public class ForegroundThree extends HitableObject {
		
		private Sprite mSprite;

		public ForegroundThree(GameBase gameBase, Bitmap bitmap) {
			super(gameBase);
			mSprite = new Sprite(bitmap, 1);
			setAnimation(new Animation(mSprite, 0, 0));

			setHealthAttributes(0, 0);
			setFreeStanding(true);
			disableMovement(true);
		}
	}

	protected final Bitmap mThumbnail;
	private Bitmap mBackground;
	private ForegroundThree mTree1, mTree2;

	public Background2(GameBase gameBase, int colorParam) {
		super(gameBase, colorParam);
    	
    	mThumbnail = BitmapFactory.decodeResource(game.res, R.drawable.background2_thumbnail);
	}
	
	@Override
	protected void load() {
		mBackground = BitmapFactory.decodeResource(game.res, R.drawable.background2_below);

		mTree1 = new ForegroundThree(game, BitmapFactory.decodeResource(game.res, R.drawable.background2_tree1));
		mTree1.setKillable(false);
		mTree1.setDrawOrder(25);
		mTree1.setPosition(game.getScreenWidth()/5, game.getScreenHeight()/2);

		mTree2 = new ForegroundThree(game, BitmapFactory.decodeResource(game.res, R.drawable.background2_tree2));
		mTree2.setKillable(false);
		mTree2.setDrawOrder(25);
		mTree2.setPosition(game.getScreenWidth()/5 * 4, game.getScreenHeight()/2);
	}
	
	@Override
	protected void unload() {
		mBackground.recycle();
		mBackground = null;
		mTree1 = null;
		mTree2 = null;
	}
	
	@Override
	public void prepare(GameStage gameStage) {
		gameStage.addActiveObject(mTree1);
		gameStage.addActiveObject(mTree2);
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
		//canvas.drawBitmap(mTree1, game.getScreenWidth()/5 - mTree1.getWidth()/2, 0, null);
		//canvas.drawBitmap(mTree2, (game.getScreenWidth()/5 * 4) - mTree2.getWidth()/2, 0, null);
	}
}
