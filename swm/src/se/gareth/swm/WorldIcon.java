package se.gareth.swm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class WorldIcon extends Button {
	
	private static Sprite mIconSprite;
	private static Sprite mIconSpriteDisabled;
	private Animation mIconAnimation;
	private Animation mIconDisabledAnimation;
	private RectF mThumbnailRect;
	
	private final TextDrawable mWorldNameText;
	private final TextDrawable mHighScoreText;
	private final TextDrawable mLevelsText;
	private final WorldDescriptor mWorldDescriptor;
	
	private final Bitmap mMapThumbnail;
	private Paint mMapPaint;
	
	public WorldIcon(GameBase gameBase, WorldDescriptor worldDescriptor) {
		super(gameBase);
		mWorldDescriptor = worldDescriptor;
		mWorldNameText = new TextDrawable(gameBase.gameView.font);
		mWorldNameText.setTextSize(game.res.getDimension(R.dimen.SmallFontSize), true);
		mWorldNameText.setOutline(game.res.getDimension(R.dimen.SmallFontOutline),
				game.res.getColor(R.color.NormalOutlineColor));
		mWorldNameText.setColor(game.res.getColor(R.color.LightFontColor));
		mWorldNameText.setTextAlign(TextDrawable.Align.LEFT);
		mWorldNameText.setText(worldDescriptor.getName());
		
		mHighScoreText = new TextDrawable(gameBase.gameView.font);
		mHighScoreText.setTextSize(game.res.getDimension(R.dimen.HeadingFontSize), false);
		//mHighScoreText.setOutline(game.res.getDimension(R.dimen.SmallFontOutline),
		//		game.res.getColor(R.color.NormalOutlineColor));
		mHighScoreText.setColor(Color.rgb(0xfe, 0xda, 0xdb));
		mHighScoreText.setTextAlign(TextDrawable.Align.CENTER);
		
		
		mLevelsText = new TextDrawable(gameBase.gameView.font);
		mLevelsText.setTextSize(game.res.getDimension(R.dimen.HugeFontSize), false);
		//mLevelsText.setOutline(game.res.getDimension(R.dimen.NormalFontOutline),
		//		game.res.getColor(R.color.GreyFontColor));
		mLevelsText.setColor(game.res.getColor(R.color.LightFontColor));
		mLevelsText.setTextAlign(TextDrawable.Align.CENTER);
		
    	if (mIconSprite == null) {
    		mIconSprite = new Sprite(BitmapFactory.decodeResource(game.res,
        			R.drawable.world_icon), 1);
    	}
    	if (mIconSpriteDisabled == null) {
    		mIconSpriteDisabled = new Sprite(BitmapFactory.decodeResource(game.res,
        			R.drawable.world_icon_disabled), 1);
    	}
    	
    	mIconAnimation = new Animation(mIconSprite, 0, 0);
    	mIconDisabledAnimation = new Animation(mIconSpriteDisabled, 0, 0);
    	
    	setAnimation(mIconAnimation);
    	
    	mMapPaint = new Paint();
    	
    	/* get map thumbnail */
    	Background background = worldDescriptor.getBackground();
    	mMapThumbnail = background.getThumbnail();
    	mThumbnailRect = new RectF();
    	
    	
    	setAlpha(255);
	}
	
	public WorldDescriptor getDescriptor() {
		return mWorldDescriptor;
	}
	
	public void setStats(boolean disable, int highscore, int currentLevel) {
    	if (highscore >= 0) {
    		mHighScoreText.setText("" + highscore);
    	}
    	else {
    		mHighScoreText.setText("-");
    	}
    	mLevelsText.setText(currentLevel + "/"+ mWorldDescriptor.getNumLevels());
    	
    	if (disable) {
    		disable();
    		setAnimation(mIconDisabledAnimation);
    		mWorldNameText.setColor(game.res.getColor(R.color.GreyFontColor));
    	}
    	else {
    		enable();
    		setAnimation(mIconAnimation);
    		mWorldNameText.setColor(game.res.getColor(R.color.LightFontColor));
    	}
	}
	
	@Override
	public void setAlpha(int alpha) {
		super.setAlpha(alpha);
		mWorldNameText.setAlpha(alpha);
		mHighScoreText.setAlpha(alpha);
		mLevelsText.setAlpha((int)((double)alpha * 0.5));
		mMapPaint.setAlpha((int)((double)alpha * 0.75));
	}
	
	@Override
	public void update(double frameTime) {
		super.update(frameTime);
		mWorldNameText.setPosition(posLeft + width / 14, posTop + mWorldNameText.getHeight() / 1.6);
		mLevelsText.setPosition(posLeft + width / 3, posTop + height * 0.57);
		mHighScoreText.setPosition(posRight - width * 0.15, posTop + height * 0.57);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (mVisible) {

			if (this.isEnable()) {
				if (mMapThumbnail != null) {
					
					mThumbnailRect.set((float)(posLeft + height * 0.1), (float)(posTop + height * 0.1), 
							(float)(posLeft + width * 0.67), (float)(posBottom - height * 0.1));
					canvas.drawBitmap(mMapThumbnail, null, mThumbnailRect, mMapPaint);
				}
				mHighScoreText.draw(canvas);
				mLevelsText.draw(canvas);
			}

			mWorldNameText.draw(canvas);
		}
	}
}
