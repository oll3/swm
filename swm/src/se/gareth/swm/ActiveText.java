package se.gareth.swm;

import android.graphics.Canvas;
import android.graphics.Color;

public class ActiveText extends ActiveObject {
	
	private String mString;
    protected TextDrawable mText;
	
	public ActiveText(GameBase gameBase, double x, double y, double size, String string) {
		super(gameBase);

		mString = string;
		
    	mText = new TextDrawable(gameBase.gameView.font, super.mPaint);
    	mText.setTextSize(size, false);
        mText.setColor(Color.YELLOW);
        mText.setTextAlign(TextDrawable.Align.CENTER);
        mText.setText(mString);
        
        setPosition(x, y);
        
       	setDrawOrder(30);
	}
	
	public void setText(String string) {
		mString = string;
		mText.setText(string);
	}
	
	public void setFontSize(double size, boolean bold) {
		mText.setTextSize(size, bold);
	}
	
	public void setTextOutline(int width, int color) {
		mText.setOutline(width, color);
	}
	
	public void setColor(int color) {
		mText.setColor(color);
	}
		
	@Override
	public void setAlpha(int alpha) {
		super.setAlpha(alpha);
		mText.forceRedraw();
	}
	
	@Override
	public void update(final TimeStep timeStep) {
		super.update(timeStep);
		mText.setPosition(getX(), getY());		
	}
	
	
	@Override
	public void draw(Canvas canvas) {
		mText.draw(canvas);
	}

	
}
