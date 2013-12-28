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

import android.app.Activity;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SlaughterWithMaud extends Activity {

	private static final String TAG = SlaughterWithMaud.class.getName();

    private GameView mGameView;


    /**
     * Invoked when the Activity is created.
     *
     * @param savedInstanceState a Bundle containing state saved from a previous
     *        execution, or null if this is a new execution
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (!BuildConfig.DEBUG) {
        	/* Disable debugging if release build */
        	SLog.enable = false;
        }
        
        SLog.i(TAG, "Application created!");

        setContentView(R.layout.activity_main); //R.layout.main);
        mGameView = (GameView) findViewById(R.id.game);

        if (savedInstanceState == null) {
            SLog.i(TAG, "New game started.");
        } else {
            SLog.i(TAG, "Game Resumed.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SLog.i(TAG, "Pause called.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        SLog.i(TAG, "Stop called.");
        mGameView.onStop();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SLog.i(TAG, "Resume called.");
    }

    @Override
	public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
        SLog.i(TAG, "Configuration changed.");
    	
    }
    
    
	@Override
	public void onBackPressed() {
		if (mGameView.getStage() == mGameView.game.gameStage) {
			mGameView.getStage().onPause();
		}
		else {
			finish();
		}
	}
    
    /**
     * Notification that something is about to happen, to give the Activity a
     * chance to save state.
     *
     * @param outState a Bundle into which this Activity should save its state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // just have the View's thread save its state into our Bundle
        super.onSaveInstanceState(outState);
    }
}