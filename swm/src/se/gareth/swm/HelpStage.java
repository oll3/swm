package se.gareth.swm;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.util.LinkedList;


public class HelpStage extends Stage {


	private final LeftArrow mExitArrow;
    private final TextDrawable mHelpText;
    private final TextDrawable mInfoText;
    private StaticLayout mTextLayout;
    
    private final TextPaint mTextPaint;

    private static final String mHelpString = "Make sure no creatures gets past your sight unnoticed, \nall animals should treated equally. " + 
    		"Let any creature escape your aim and you have lost the level and will have to replay it.\nNormally you have 3 retries in total for an area." +
    		"Shots are expensive, so do not miss. Firing at a faster pace will get your bonus up and \nincrease your score. " +
    		"Boxes contains (mostly) good to have items, collect and use them right.\n" +
    		"Now, enough with this... Go show those creatures some love, have fun and make Maud proud!\n";
    
    
    public HelpStage(GameBase gameBase) {
		super(gameBase);
	
		mHelpText = new TextDrawable(gameBase.gameView.font);
		mHelpText.setTextSize(game.res.getDimension(R.dimen.SmallFontSize), true);
		mHelpText.setOutline(game.res.getDimension(R.dimen.SmallFontOutline),
				      game.res.getColor(R.color.NormalOutlineColor));
		mHelpText.setTextAlign(TextDrawable.Align.CENTER);
		mHelpText.setColor(game.res.getColor(R.color.HeadingFontColor));
		
		mHelpText.setText("Test1&#10;Test2");
		
		mInfoText = new TextDrawable(gameBase.gameView.font);
		mInfoText.setTextSize(game.res.getDimension(R.dimen.NormalFontSize), false);
		mInfoText.setColor(game.res.getColor(R.color.HeadingFontColor));
		mInfoText.setOutline(game.res.getDimension(R.dimen.NormalFontOutline), 
				      game.res.getColor(R.color.NormalOutlineColor));
		mInfoText.setTextAlign(TextDrawable.Align.LEFT);
		
		mInfoText.setText(mHelpString);
		
		mTextPaint = new TextPaint();
		mTextPaint.setAntiAlias(true);
    	mTextPaint.setTypeface(game.gameView.font);
    	mTextPaint.setTextSize(game.res.getDimension(R.dimen.NormalFontSize));
    	mTextPaint.setColor(game.res.getColor(R.color.LightFontColor));
    	
    	mExitArrow = new LeftArrow(gameBase, "Back");
    }

    @Override
    public void update(final TimeStep timeStep) {
    	mExitArrow.update(timeStep);
    }

    
    @Override
    public void draw(Canvas canvas) {
    	
        canvas.drawColor(game.res.getColor(R.color.NormalBackground));
        game.drawWorldBackground(canvas);
        
        
        canvas.save();
        
        canvas.translate(game.getScreenWidth()*0.05f, 30);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setStrokeWidth(3);
        mTextPaint.setColor(Color.rgb(0, 0, 0));
        mTextLayout.draw(canvas);
        
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setStyle(Paint.Style.FILL); 
        mTextPaint.setColor(game.res.getColor(R.color.LightFontColor));
        mTextLayout.draw(canvas);
        canvas.restore();
        
        mExitArrow.draw(canvas);
    }
    

    /* 
     * Called when this stage is activated.
     */
    @Override
    public void activated(Stage previousStage) {
    	mTextLayout = new StaticLayout(mHelpString, mTextPaint, (int)(game.getScreenWidth()*0.95), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    }
    
    @Override
    public void deactivated(Stage nextStage) {
    	mTextLayout = null;
    }

    @Override
    public void onTouch(LinkedList<TouchEvent> touchEventList) {
    	
    	for (TouchEvent touchEvent: touchEventList) {
    		if (mExitArrow.wasPressed(touchEvent)) {
	    		game.setStage(game.worldSelectStage);
	    		break;
	    	}
    	}
    }

    @Override
    public void playfieldSizeChanged(int width, int height) {
    	mHelpText.setPosition(50, 50);
    	mInfoText.setPosition(width/2, height/2 + 30);
    	mExitArrow.setPosition(mExitArrow.getWidth() / 1.75, height - mExitArrow.getHeight() / 1.5);
    }
};

