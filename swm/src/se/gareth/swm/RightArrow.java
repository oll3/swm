package se.gareth.swm;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class RightArrow extends Button {
	
	private static Sprite mArrowSprite;
	private final TextDrawable mText;
	
	public RightArrow(GameBase gameBase, String string) {
		super(gameBase);
		
		mText = new TextDrawable(gameBase.gameView.font);
		mText.setTextSize(game.res.getDimension(R.dimen.SmallFontSize), false);
		mText.setOutline(game.res.getDimension(R.dimen.NormalFontOutline), 
				game.res.getColor(R.color.NormalOutlineColor));
		mText.setColor(game.res.getColor(R.color.PanelFontColor));
		mText.setTextAlign(TextDrawable.Align.LEFT);
		mText.setText(string);
		
    	if (mArrowSprite == null) {
    		mArrowSprite = new Sprite(BitmapFactory.decodeResource(game.res,
        			R.drawable.arrow_right), 1);
    	}
    	setAnimation(new Animation(mArrowSprite, 0, 0));
	}
	
	public void setText(String text) {
		mText.setText(text);
	}
	
	@Override
	public void update(final TimeStep timeStep) {
		super.update(timeStep);
		mText.setPosition(getX(0.1), getY(0.5));
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (mVisible)
			mText.draw(canvas);
	}
}
