package se.gareth.swm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import android.util.Log;

public class Level {

    private static final String TAG = Level.class.getName();

    class HitableObjectContainer {
        protected HitableObject mHitableObject;
        protected int mTimeToNext;

        public HitableObjectContainer(HitableObject hitableObject, int timeToNext) {
            mHitableObject = hitableObject;
            mTimeToNext = timeToNext;
        }

        public HitableObject getObject() {
            return mHitableObject;
        }

        public int getTimeToNext() {
            return mTimeToNext;
        }

    }


    private Random mRandom;
    private ArrayList<HitableObjectContainer> mObjectList;
    private LinkedList<HitableObject> mKillList;
    private LinkedList<HitableObject> mMissList;

    private int mTotalObjects;
    private int mTotalFinished;

    private int mScore;
    private int mKills;
    private int mMisses;
    private int mHitPoints;
    private int mBonusPoints;
    private int mLostPoints;
    private int mCreaturesAlive;

    private long mLastCreation;
    private int mAllowedCreaturesAlive;
    private long mMinInterval;
    private long mMaxInterval;
    private long mCreationPause;

    private boolean mFinished;
    private boolean mFailed;

    public Level(int allowedCreaturesAlive, long minInterval, long maxInterval) {
        mRandom = new Random();
        mObjectList = new ArrayList<HitableObjectContainer>();
        mKillList = new LinkedList<HitableObject>();
        mMissList = new LinkedList<HitableObject>();

        mAllowedCreaturesAlive = allowedCreaturesAlive;
        mMinInterval = minInterval;
        mMaxInterval = maxInterval;

        reset();
    }

    public void reset() {
        mScore = 0;
        mKills = 0;
        mMisses = 0;
        mTotalFinished = 0;
        mTotalObjects = 0;
        mHitPoints = 0;
        mBonusPoints = 0;
        mLostPoints = 0;
        mFinished = false;
        mFailed = false;
        mObjectList.clear();
        mKillList.clear();
        mMissList.clear();
        mCreaturesAlive = 0;
        mLastCreation = 0;
        mCreationPause = 0;
    }

    public void creatureIsAlive(HitableObject object) {
        mCreaturesAlive ++;
        //SLog.d(TAG, "Creature " + object + " is now alive (alive now: " + mCreaturesAlive + ")");
    }

    public void addObject(HitableObject object, int timeToNext) {
        SLog.d(TAG, "Added object " + object +" to level");
        mTotalObjects ++;
        HitableObjectContainer aoHolder = new HitableObjectContainer(object, timeToNext);
        mObjectList.add(aoHolder);
    }

    public boolean hasFailed() {
        return mFailed;
    }

    public void addScore(int score) {
        mScore += score;
        if (mScore < 0)
            mScore = 0;
    }

    public void addHitPoints(int points) {
        mHitPoints += points;
        if (mHitPoints < 0)
            mHitPoints = 0;
    }

    public void addBonusPoints(int points) {
        mBonusPoints += points;
        if (mBonusPoints < 0)
            mBonusPoints = 0;
    }

    public void addLostPoints(int points) {
        mLostPoints += points;
    }

    public int getScore() {
        return mScore;
    }

    public int getBonusPoints() {
        return mBonusPoints;
    }

    public int getLostPoints() {
        return mLostPoints;
    }

    public int getHitPoints() {
        return mHitPoints;
    }

    public void addFinishedObject(HitableObject object) {
        if (object.hasBeenDestroyed()) {
            /* Object was shot by player */
            mKillList.add(object);
            mKills ++;
            SLog.d(TAG, "Object " + object +" destroyed");
        }
        else {
            /* Object wasn't shot by player, but got away */
            SLog.d(TAG, "Object " + object +" survived (mustBeKilled=" + object.mustBeKilled() + ")");
            if (object.mustBeKilled()) {
                /* Object was supposed to be shot. Level failed. */
                mFailed = true;
                SLog.d(TAG, "Level failed");
            }
        }
        mCreaturesAlive --;
        //SLog.d(TAG, "Creature " + object + " is now dead (alive now: " + mCreaturesAlive + ")");
        mTotalFinished ++;
        //SLog.d(TAG, "TotalObjects=" + mTotalObjects + ", TotalFinished=" + mTotalFinished + ", mCreaturesAlive=" + mCreaturesAlive);
        if (mCreaturesAlive == 0 && mTotalFinished >= mTotalObjects && !mFailed) {
            mFinished = true;
            SLog.d(TAG, "Level successfully finished");
        }
    }

    public void pauseCreation(long delay) {
        mCreationPause = delay;
    }

    public boolean timeForNextCreature() {
        if (!mFinished && !mFailed) {
            if (objectsLeft() > 0) {
                long currentTime = System.currentTimeMillis();

                if (mCreationPause > 0) {
                    if ((mLastCreation + mCreationPause) >= currentTime) {
                        return false;
                    }
                    mCreationPause = 0;
                }

                if (mCreaturesAlive < mAllowedCreaturesAlive) {
                    if ((mLastCreation + mMinInterval) < currentTime) {
                        mLastCreation = System.currentTimeMillis();
                        return true;
                    }
                }
                else if ((mLastCreation + mMaxInterval) < currentTime) {
                    mLastCreation = System.currentTimeMillis();
                    return true;
                }
            }
        }
        return false;
        //mNextBird = aoHolder.getTimeToNext() + System.currentTimeMillis();
    }

    public HitableObject getNext() {
        int size = mObjectList.size();
        return mObjectList.remove(mRandom.nextInt(size)).getObject();
    }

    public void addMiss() {
        mMisses ++;
    }

    public int getMisses() {
        return mMisses;
    }

    public int getKills() {
        return mKills;
    }

    public boolean hasFinished() {
        return mFinished;
    }

    public int objectsLeft() {
        return mObjectList.size();
    }

}
