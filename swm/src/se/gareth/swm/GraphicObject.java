package se.gareth.swm;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class GraphicObject {

	/* The position of the object */
	private double mX, mY;
	private int mWidth, mHeight;
	
	private boolean mDoDrawArea;
	protected boolean mVisible;
	
	private boolean mToBeDeleted;
	
	private int mAlpha;
	
	protected Paint mPaint;
	
	private int mDrawOrder;
	
	/* Game object reference */
	protected GameBase game;
	
	/* Contains the animations of object */	
	private Animation mCurrentAnimation;
	
	private float mRotation;
	private float mRotationOffsetX, mRotationOffsetY;
	
	public GraphicObject(GameBase gameBase) {
		game = gameBase;
		mDrawOrder = 0;
	    mX = 0;
		mY = 0;
		mWidth = 0;
		mHeight = 0;
		mRotation = 0;
		mVisible = true;
		
		mDoDrawArea = false;
		mCurrentAnimation = null;
		
		mToBeDeleted = false;
		mRotationOffsetX = 0.5f;
		mRotationOffsetY = 0.5f;
		
		mPaint = new Paint();
	}
	
	public void hide() {
		mVisible = false;
	}
	
	public void show() {
		mVisible = true;
	}
	
	protected void setDrawOrder(int drawOrder) {
		mDrawOrder = drawOrder;		
	}
	
	public int getDrawOrder() {
		return mDrawOrder;
	}
	
	public void enableDrawArea(boolean enable) {
		mDoDrawArea = enable;
	}
	
	public void setPosition(double x, double y) {
		mX = x;
		mY = y;
	}

	public void setPosition(double x, double widthScale, double y, double heightScale) {
		setPosition(x + mWidth * widthScale, y + mHeight * heightScale);
	}
	
	public void setX(double x) {
		mX = x;
	}

	public void setY(double y) {
		mY = y;
	}

	public void setX(double x, double scale) {
		setX(x + mWidth * scale);
	}

	public void setY(double y, double scale) {
		setY(y + mHeight * scale);
	}

	public void addPosition(double relX, double relY) {
		mX += relX;
		mY += relY;
	}

	public double getX(double scale) {
		return mX + mWidth * scale;
	}

	public double getY(double scale) {
		return mY + mHeight * scale;
	}

	public double getX() {
		return mX;
	}
	
	public double getY() {
		return mY;
	}
	
	public double getLeft() {
		return mX - mWidth / 2;
	}
	
	public double getTop() {
		return mY - mHeight / 2;
	}
	
	public double getRight() {
		return mX + mWidth / 2;
	}
	
	public double getBottom() {
		return mY + mHeight / 2;
	}
	
	public int getWidth() {
		return mWidth;
	}
	
	public int getHeight() {
		return mHeight;
	}
	
	public void setAnimation(Animation animation) {
		mCurrentAnimation = animation;
		if (animation != null) {
			mWidth = animation.getWidth();
			mHeight = animation.getHeight();
		}
		else {
			mWidth = 0;
			mHeight = 0;
		}
	}
	
	public boolean containsPos(double x, double y) {
		if (mVisible)
			return (x >= getLeft() && x < getRight() && y >= getTop() && y < getBottom());
		return false;
	}

	public Animation getCurrentAnimation() {
		return mCurrentAnimation;
	}
	
	public void setAlpha(int alpha) {
		if (alpha != mAlpha) {
			mAlpha = alpha;
			mPaint.setAlpha(mAlpha);
		}
	}
	
	public int getAlpha() {
		return mAlpha;
	}
	
	
	/* Tell game stage to stop using this object */
	protected void deleteMe() {
		mToBeDeleted = true;
	}
	
	/* Test if object should be deleted */
	public boolean isToBeDeleted() {
		return mToBeDeleted;
	}
	
	public void setRotationOffset(float widthScale, float heightScale) {
		mRotationOffsetX = widthScale;
		mRotationOffsetY = heightScale;
	}
	
	public void setRotation(float degrees) {
		mRotation = degrees;
	}
	
	public float getRotation() {
		return mRotation;
	}
	
	public void addRotation(float degrees) {
		mRotation += degrees;
	}
	
	public void draw(Canvas canvas) {

		if (mVisible) {
		
			if (mDoDrawArea) {
				/* Enable to draw the object area */
				Rect areaRect = new Rect();
				areaRect.left = (int)(getLeft() + 0.5);
				areaRect.top = (int)(getTop() + 0.5);
				areaRect.right = (int)(getRight() + 0.5);
				areaRect.bottom = (int)(getBottom() + 0.5);
				canvas.drawRect(areaRect, mPaint);
			}
			
			if (mCurrentAnimation != null) {
				mCurrentAnimation.draw(canvas, 
						mX - mWidth / 2, mY - mHeight / 2, 
						mRotation, (float)mWidth * mRotationOffsetX, (float)mHeight * mRotationOffsetY, 
						mPaint);
			}
		}
	}
	
	public void update(final TimeStep timeStep) {
		if (mCurrentAnimation != null) {
			mCurrentAnimation.update();
		}
	}
	

}
