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
    private StaticLayout mTextLayout;
    
    private final TextPaint mTextPaint;

    private static final String mHelpString = 
    		"- Make sure no creatures gets past your sight unnoticed, all animals should be treated as equals. " + 
    		"Let any creature escape and you have lost the level and will have to replay it\n\n" + 
    				
    		"- Normally you have 3 retries in total for an area. If you're a lucky cat extra retries might be gained through item drops\n\n" +
    		
    		"- Shells are expensive, hence missed shot will be punished through a score reduction\n\n" + 
    		
    		"- Firing at a faster pace will get your bonus up and increase your score\n\n" +
    		
    		"- Boxes contains good to have items, collect and use them right\n\n" +

    		"Enough with this... Go show those animals some love, have fun and make Maud proud!\n";
    
    
    public HelpStage(GameBase gameBase) {
		super(gameBase);
	
				
		mTextPaint = new TextPaint();
		mTextPaint.setAntiAlias(true);
    	mTextPaint.setTypeface(game.gameView.font);
    	mTextPaint.setTextSize(game.res.getDimension(R.dimen.SmallFontSize));
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
    	mTextLayout = new StaticLayout(mHelpString, mTextPaint, (int)(game.getScreenWidth()*0.90), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
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
    	mExitArrow.setPosition(mExitArrow.getWidth() / 1.75, height - mExitArrow.getHeight() / 1.5);
    }
};

