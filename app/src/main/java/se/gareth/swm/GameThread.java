package se.gareth.swm;


public class GameThread extends Thread {

    private static final String TAG = GameThread.class.getName();

    public interface GameThreadUpdate {
        public void update();
    }


    /* FPS related variables */
    private long mFpsTimeStamp;
    private int mFpsCount;
    private int mRun;
    private int mEndInFrames;

    private GameThreadUpdate mUpdater;


    public GameThread(GameThreadUpdate updater) {

        mUpdater = updater;

        mFpsTimeStamp = System.currentTimeMillis();
        mFpsCount = 0;
        mRun = 0;
        mEndInFrames = 0;

        SLog.i(TAG, "Thread created!");
    }


    /*
     * Tell thread to stop (in a number of frames)
     */
    public void end(int endInFrames) {
        if (mRun != 0) {
            SLog.d(TAG, "Ending thread...");
            mEndInFrames = endInFrames;
            mRun = 0;
            try {
                this.join();
                SLog.d(TAG, "Thread joined");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start() {
        if (mRun == 0) {
            SLog.d(TAG, "Starting thread (" + this.getState() + ")...");
            mRun = 1;
            super.start();
        }
    }

    @Override
    public void run() {

        SLog.d(TAG, "THREAD RUNNING!");

        while (mRun != 0 || mEndInFrames > 0) {

            mUpdater.update();

            mFpsCount ++;

            if (System.currentTimeMillis() > (mFpsTimeStamp + 1000)) {
                mFpsTimeStamp = System.currentTimeMillis();
                SLog.w(TAG, "FPS: " + mFpsCount);
                mFpsCount = 0;
            }

            if (mEndInFrames > 0) {
                mEndInFrames --;
            }
        }

        SLog.w(TAG, "THREAD EXITED!");
    }

}
