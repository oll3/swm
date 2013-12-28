package se.gareth.swm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;


public class Background3 extends Background {
	
	
    public class Foreground extends GraphicObject {

        private Sprite mSprite;

        public Foreground(GameBase gameBase, Bitmap bitmap) {
            super(gameBase);
            mSprite = new Sprite(bitmap, 1);
            setAnimation(new Animation(mSprite, 0, 0));
            setDrawOrder(30);
        }

        @Override
        public void draw(Canvas canvas) {
        	for (int x = 0; x < game.getScreenWidth(); x += getWidth() - 1) {
        		setPosition(getWidth()/2 + x, game.getScreenHeight() - getHeight()/2);
        		super.draw(canvas);
        	}
        }
        

        public void unload() {
            mSprite.recycle();
        }
    }
	

	protected final Bitmap mThumbnail;
	private Bitmap mBackgroundBitmap;
	private Foreground mForeground;
	
	public Background3(GameBase gameBase, int colorParam) {
		super(gameBase, colorParam);
    	mThumbnail = BitmapFactory.decodeResource(game.res, R.drawable.background3_thumbnail);
	}
	
	@Override
	protected void load() {
		mBackgroundBitmap = BitmapFactory.decodeResource(game.res, R.drawable.background3_background);
		mForeground = new Foreground(game, BitmapFactory.decodeResource(game.res, R.drawable.background3_foreground));
	}
	
	@Override
	protected void unload() {
		mForeground.unload();
		mBackgroundBitmap.recycle();
		mForeground = null;
		mBackgroundBitmap = null;
	}
	
	@Override
	protected Bitmap getThumbnail() {
		return mThumbnail;
	}
	
	
    @Override
    public void prepare(GameStage gameStage) {
        gameStage.addGraphicObject(mForeground);
    }
	
	@Override
	public void update(final TimeStep timeStep) {
		
	}
	
	
	@Override
	public void drawBackground(Canvas canvas) {
		//canvas.drawBitmap(mBackgroundBitmap, 0, 0, null);
        for (int x = 0; x < game.getScreenWidth(); x += mBackgroundBitmap.getWidth()-1) {
            canvas.drawBitmap(mBackgroundBitmap, x, 0, null);
        }
	}
	
}
