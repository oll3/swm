package se.gareth.swm;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class DownArrow extends Button {
	
	private static Sprite mArrowSprite;
	private final TextDrawable mText;
	
	public DownArrow(GameBase gameBase, String string) {
		super(gameBase);
		
		
		mText = new TextDrawable(gameBase.gameView.font);
		mText.setTextSize(game.res.getDimension(R.dimen.SmallFontSize), false);
		mText.setOutline(game.res.getDimension(R.dimen.NormalFontOutline), 
				game.res.getColor(R.color.NormalOutlineColor));
		mText.setColor(game.res.getColor(R.color.PanelFontColor));
		mText.setTextAlign(TextDrawable.Align.RIGHT);
		mText.setText(string);
		
    	if (mArrowSprite == null) {
    		mArrowSprite = new Sprite(BitmapFactory.decodeResource(game.res,
        			R.drawable.arrow_down), 1);
    	}
    	setAnimation(new Animation(mArrowSprite, 0, 0));
    	this.setAlpha(196);
	}

	@Override
	public void onDown() {
		this.setAlpha(255);
	}
	
	@Override
	public void onReleased() {
		this.setAlpha(196);
	}
	
	@Override
	public void update(double frameTime) {
		super.update(frameTime);
		mText.setPosition(posRight - width/10, posTop + height / 2);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		mText.draw(canvas);
	}
}
