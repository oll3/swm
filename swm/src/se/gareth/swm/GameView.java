/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.gareth.swm;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;


/**
 * Handles events
 */
class GameView extends SurfaceView implements SurfaceHolder.Callback, GameThread.GameThreadUpdate {

	private static final String TAG = GameView.class.getName();
	
    public int mWidth, mHeight;
    public double mSpeedUp;
    
    private final AverageValue mFrameTimeCounter;
    
    private long mLastUpdate;
    private GameThread mGameThread;
    
    private SurfaceHolder mSurfaceHolder;
    
    private Stage mNextStage;
    private Stage mCurrentStage;
    private LinkedList<TouchEvent> mTouchList;

    private final TextDrawable mLoadingText;
    
    public String mMetricsString;
    
    protected GameBase game;
    
    public final Typeface font;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        mFrameTimeCounter = new AverageValue(60);
        
        font = Typeface.createFromAsset(context.getAssets(), "Laffayette_Comic_Pro.ttf"); 
        
        mCurrentStage = null;
        mNextStage = null;
        game = null;
        mTouchList = new LinkedList<TouchEvent>();
        
        /* register our interest in hearing about changes to our surface */
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        
        mSpeedUp = 1.0;
        
        setFocusable(true); // make sure we get key events


		mLoadingText = new TextDrawable(font);
		mLoadingText.setTextSize(getResources().getDimension(R.dimen.BigFontSize), true);
		mLoadingText.setOutline(getResources().getDimension(R.dimen.BigFontOutline),
				      getResources().getColor(R.color.NormalOutlineColor));
		mLoadingText.setTextAlign(TextDrawable.Align.CENTER);
		mLoadingText.setColor(getResources().getColor(R.color.HeadingFontColor));
		
		mLoadingText.setText("Loading...");
				
    	Log.d(TAG, "Object created!");
    }
    
    public void setStage(Stage stage) {
    	mNextStage = stage;
    }

    public Stage getStage() {
    	return mCurrentStage;
    }



    /**
     * Handle game events and update objects.
     */
    @Override
    public void update() {

		long ticks = System.currentTimeMillis();
		mFrameTimeCounter.addValue(ticks - mLastUpdate);
		double frameTime = mFrameTimeCounter.getAvarageDouble()/1000.0;
		mLastUpdate = ticks;
		
		synchronized(mTouchList) {
			if (mTouchList.size() > 0) {
				if (mCurrentStage != null) {
					mCurrentStage.onTouch(mTouchList);
				}
				mTouchList.clear();
			}
		}
		
		if (mCurrentStage != null) {
			mCurrentStage.update(frameTime);
		}
		
		Canvas canvas = null;
		try {
			canvas = mSurfaceHolder.lockCanvas(null);
			synchronized (mSurfaceHolder) {
				if (mCurrentStage == null) {
					/* Draw splash screen if no stage is set */
					Bitmap splashScreen = Sprite.loadFitBitmap(this.getContext().getResources(), R.drawable.intro_background1, mWidth, mHeight);
					
					canvas.drawColor(getResources().getColor(R.color.NormalBackground));
					canvas.drawBitmap(splashScreen, 0, 0, null);
					mLoadingText.draw(canvas);
					splashScreen.recycle();
				}
				else {
					mCurrentStage.draw(canvas);
				}
			}
		}
		finally {
			if (canvas != null) {
				mSurfaceHolder.unlockCanvasAndPost(canvas);
			}
		}

		if (game == null) {
			/* Load game base object */
			if (game == null) {
				long loadingTime = System.currentTimeMillis();
				game = new GameBase(this);
				
				if (!BuildConfig.DEBUG) {
					while(System.currentTimeMillis() < (loadingTime + 5000)) {
						/* Make loading screen be visible for at least 5 seconds always */
					}
				}
			}
		}

		if (mNextStage != null) {
			/* Change to next stage */
			Stage prevStage = mCurrentStage;
			if (mCurrentStage != null)
				mCurrentStage.deactivated(mNextStage);
			mCurrentStage = mNextStage;
			mNextStage = null;

			/* Ensure stage aware of the screen size */
			mCurrentStage.playfieldSizeChanged(game.getScreenWidth(), game.getScreenHeight());
			mCurrentStage.activated(prevStage);
			
			/* As activated() call might be slow we reset the last update time, 
			 * 	so first frame will be seen as fast rather then too slow */
			mLastUpdate = System.currentTimeMillis();
		}
    }
    
    public double getSpeedUp() {
    	return mSpeedUp;
    }


    
    @Override
    public boolean onTouchEvent(MotionEvent event) {

    	int pi;
    	int action = event.getAction();
    	int actionCode = action & MotionEvent.ACTION_MASK;
    	
    	int pointerCount = event.getPointerCount();
    	
    	for (pi = 0; pi < pointerCount; pi ++) {
    	
		    TouchEvent.TouchType touchType = TouchEvent.TouchType.Unknown;
	    	if (actionCode == MotionEvent.ACTION_DOWN || 
			    actionCode == MotionEvent.ACTION_POINTER_DOWN) {
	    		touchType = TouchEvent.TouchType.Down;
	    	}
	    	else if (actionCode == MotionEvent.ACTION_UP || 
	    		    actionCode == MotionEvent.ACTION_POINTER_UP) {
	    		touchType = TouchEvent.TouchType.Up;
	    	}
	    	else if (actionCode == MotionEvent.ACTION_MOVE) {
	    		touchType = TouchEvent.TouchType.Move;
	    	}
	    	
	    	if (touchType != TouchEvent.TouchType.Unknown) {
	    		TouchEvent touchEvent = new TouchEvent(touchType, pi, event.getX(pi), event.getY(pi));
			    synchronized(mTouchList) {
			    	mTouchList.add(touchEvent);
			    }
	    	}
    	}

    	return true;
    }
    
    
    /* Callback invoked when the surface dimensions change. */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, 
    		int width, int height) {
    	
    	mWidth = width;
    	mHeight = height;
    	
    	/* Calculate the speed up factor if running on a bigger display 
    	 * than "normal" (800 pixels width) */
    	mSpeedUp = (double)mWidth / 800.0;

		mLoadingText.setPosition(width/2, height - mLoadingText.getHeight());
	
		if (game != null) {
		    game.setPlayfieldSize(width, height);
		}
    	    	    	
    	Log.d(TAG, "Surface changed (" + width + ", " + height + ")");
    }

    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	Log.d(TAG, "Surface created");
    }

    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    	synchronized (this) {
    		endThread(0);
    	}
        Log.d(TAG, "Surface destroyed!"); 
    }


    private void startThread() {
    	if (mGameThread == null) {
    		mGameThread = new GameThread(this);
    		mGameThread.start();
    	}		
    }
    
    private void endThread(int endInFrames) {
    	if (mGameThread != null) {
    		mGameThread.end(endInFrames);
    		mGameThread = null;
    	}
    }
    
    public void pause() {
    	synchronized (this) {
	    	if (game != null) {
	    		getStage().onPause();
		    	endThread(2);
	    	}
	    	else {
		    	endThread(0);
	    	}
	        Log.d(TAG, "End thread"); 
    	}
    }
    
    public void onStop() {
    	synchronized (this) {
    		if (game != null) {
    			getStage().onStop();
    		}
    	}
    }
    
    
    public void resume() {
    	synchronized (this) {
        	/* Update the last update time at resume */
    		mLastUpdate = System.currentTimeMillis();
	        startThread();
    	}
    }
    
    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
    	super.onWindowFocusChanged(hasWindowFocus);
    	
		if (hasWindowFocus == false) {
	    	Log.d(TAG, "Lost Focus!");
			pause();
		}
		else {
	    	Log.d(TAG, "Got Focus!");
			resume();
		}
    }

}