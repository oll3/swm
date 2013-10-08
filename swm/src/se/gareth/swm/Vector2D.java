package se.gareth.swm;

import java.util.LinkedList;

public class Vector2D {
	
	/* Constant direction definitions */
	public static final double DIRECTION_LEFT = Math.PI;
	public static final double DIRECTION_RIGHT = 0.0;
	public static final double DIRECTION_UP = (Math.PI * 1.5);
	public static final double DIRECTION_DOWN = (Math.PI / 2.0);
	public static final double DIRECTION_180 = Math.PI;

	private boolean mMagnitudeValid, mDirectionValid;
	
	private double mX, mY;
	private double mMagnitude, mDirection;
	
	public Vector2D(double x, double y) {
		mX = x;
		mY = y;
	}
	

	public Vector2D() {
		mX = 0.0;
		mY = 0.0;
		mMagnitude = 0.0;
		mDirection = 0.0;
		mMagnitudeValid = true;
		mDirectionValid = true;
	}
	
	
	public void set(Vector2D vector) {
		mX = vector.mX;
		mY = vector.mY;
		mMagnitudeValid = vector.mMagnitudeValid;
		mDirectionValid = vector.mDirectionValid;
		mMagnitude = vector.mMagnitude;
		mDirection = vector.mDirection;
	}
	
	public void set(double x, double y) {
		mX = x;
		mY = y;
		mMagnitudeValid = false;
		mDirectionValid = false;
	}
	
	public void addX(double x) {
		set(mX + x, mY);
	}
	
	public void addY(double y) {
		set(mX, mY + y);
	}
	
	public void lookAt(double fromX, double fromY, double toX, double toY) {
		set(fromX - toX, fromY - toY);
	}
	
	public double getMagnitude() {
		if (!mMagnitudeValid) {
			mMagnitude = Math.sqrt(mX * mX + mY * mY);
			mMagnitudeValid = true;
		}
		return mMagnitude;
	}
		
	public double getDirection() {
		if (!mDirectionValid) {
			mDirection = (double)Math.atan2(mY, mX);			
			mDirectionValid = true;
		}
		return mDirection;
	}
	
	public void setMagnitude(double magnitude) {
		double direction = getDirection();
		mX = Math.cos(direction) * magnitude;
		mY = Math.sin(direction) * magnitude;
		mMagnitude = magnitude;
		mMagnitudeValid = true;
	}
	
	public void setDirection(double direction) {
		double magnitude = getMagnitude();
		mX = Math.cos(direction) * magnitude;
		mY = Math.sin(direction) * magnitude;
		mDirection = direction;
		mDirectionValid = true;
	}
	
	public void setDirectionMagnitude(double direction, double magnitude) {
		mX = Math.cos(direction) * magnitude;
		mY = Math.sin(direction) * magnitude;
		mMagnitudeValid = true;
		mDirectionValid = true;
		mMagnitude = magnitude;
		mDirection = direction;
	}
		
	public void add(Vector2D vector) {
		mX += vector.mX;
		mY += vector.mY;
		mMagnitudeValid = false;
		mDirectionValid = false;
	}
	
	public void add(LinkedList<Vector2D> vectorList) {
		for (Vector2D vector: vectorList) {
			mX += vector.mX;
			mY += vector.mY;
		}
		mMagnitudeValid = false;
		mDirectionValid = false;
	}
	
	public void invert() {
		mX = -mX;
		mY = -mY;
		
		/* Add 180 degrees to the direction if valid */
		if (mDirectionValid)
			mDirection += DIRECTION_180;
		
		/* Magnitude should be the same - leave valid if valid */
	}
	
	public void multiplyMagnitude(double multiply) {
		mX *= multiply;
		mY *= multiply;
		
		if (mMagnitudeValid)
			mMagnitude *= multiply;
		
		/* Direction should be the same - leave valid if valid */
	}
	
	public void divideMagnitude(double divider) {
		mX /= divider;
		mY /= divider;
		
		if (mMagnitudeValid)
			mMagnitude /= divider;
		
		/* Direction should be the same - leave valid if valid */
	}


	public Vector2D getNormal() {
		return new Vector2D(-mY, mX);
	}

	
	/*
	 * Limit the vector to a max length
	 */
	public void truncate(double maxLength) {
		double lengthSquared = mX * mX + mY * mY;
		if (lengthSquared > (maxLength * maxLength) && lengthSquared > 0) {
			double ratio = maxLength / getMagnitude();
			multiplyMagnitude(ratio);
		}
	}
	
	
	public double getX() {
		return mX;
	}
	
	public double getY() {
		return mY;
	}
	
	@Override
	public String toString() {
		return "" + getClass() + " (" + mX + "," + mY + ")";
	}
}
