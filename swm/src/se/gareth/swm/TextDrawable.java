package se.gareth.swm;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.FontMetrics;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;



public class TextDrawable extends Drawable {

    private static final String TAG = TextDrawable.class.getName();

    enum Align {
        LEFT,
            CENTER,
            RIGHT,
            }

    private String text;
    private final Paint mPaint;
    private final Paint mFilling;
    private Paint mOutline;
    private final FontMetrics mFontMetrics;

    private double mX, mY;
    private final Rect mSize;

    private Align mAlign;

    private boolean mDoUpdateBitmap;
    private boolean mResizeBitmap;
    private boolean mBold;

    private Bitmap mBitmap;
    private final Canvas mCanvas;

    private float mOutlineWidth;


    public TextDrawable(Typeface font, Paint paint) {
        super();
        mPaint = paint;
        mFilling = new Paint();
        if (font != null) {
            mPaint.setTypeface(font);
            mFilling.setTypeface(font);
        }
        mSize = new Rect();
        mCanvas = new Canvas();
        mFontMetrics = new FontMetrics();
        init();
    }

    public TextDrawable(Typeface font) {
        super();
        mPaint = new Paint();
        if (font != null)
            mPaint.setTypeface(font);
        mFilling = mPaint;
        mSize = new Rect();
        mCanvas = new Canvas();
        mFontMetrics = new FontMetrics();
        init();
    }

    private void init() {
        this.text = "";
        mX = 0;
        mY = 0;
        mAlign = Align.LEFT;
        mBold = true;
        mOutlineWidth = 0;
        mFilling.setColor(Color.WHITE);
        mFilling.setTextSize(22f);
        mFilling.setAntiAlias(true);
        mFilling.setFakeBoldText(mBold);
        mFilling.setStyle(Paint.Style.FILL);
        mFilling.setTextAlign(Paint.Align.LEFT);
        mFilling.getFontMetrics(mFontMetrics);
        mOutline = null;
        mDoUpdateBitmap = true;
        mResizeBitmap = true;

    }

    public void setTextSize(double size, boolean bold) {
        mBold = bold;
        mFilling.setTextSize((float)size);
        mFilling.setFakeBoldText(mBold);
        if (mOutline != null) {
            mOutline.setTextSize((float)size);
            mOutline.setFakeBoldText(mBold);
        }

        /* Force update of size */
        updateBitmap();
    }


    /*
      Update size of bitmap
    */
    private void updateBitmap() {

        mFilling.getTextBounds(text, 0, text.length(), mSize);
        if (mOutline != null && mOutlineWidth > 0) {
            mSize.left -= (mOutlineWidth + 1.0)/2;
            mSize.right += (mOutlineWidth + 1.0)/2;
            mOutline.getFontMetrics(mFontMetrics);
        }
        else {
            mFilling.getFontMetrics(mFontMetrics);
        }
        mSize.bottom = (int)(mFontMetrics.bottom + 0.5f);
        mSize.top = (int)(mFontMetrics.top - 4.5f);

        //SLog.d(TAG, "Text top=" + mFontMetrics.top + ", bottom=" + mFontMetrics.bottom + ", ascent=" + mFontMetrics.ascent + ", bottom=" + mFontMetrics.descent);
        //mSize.bottom = (int)(mFontMetrics.bottom + 0.5f);
        //mSize.top = (int)(mFontMetrics.top - 0.5f);

        if (mSize.width() > 0 && mSize.height() > 0) {

            if (mBitmap == null) {
                /* Create bitmap */
                mBitmap = Bitmap.createBitmap(mSize.width(), mSize.height(), Bitmap.Config.ARGB_8888);
            }
            else if (mSize.width() > mBitmap.getWidth() || mSize.height() > mBitmap.getHeight()) {

                /* Only resize bitmap if the new size is bigger then before */
                mBitmap.recycle();
                mBitmap = Bitmap.createBitmap(mSize.width(), mSize.height(), Bitmap.Config.ARGB_8888);
            }
            else if (mBitmap != null) {
                mBitmap.eraseColor(Color.TRANSPARENT);
            }
            mCanvas.setBitmap(mBitmap);
            mDoUpdateBitmap = true;
        }
    }

    public void setText(final String text) {
        this.text = text;
        updateBitmap();
    }

    public int getWidth() {
        return mSize.width();
    }

    public int getHeight() {
        return mSize.height();
    }

    public void setPosition(double x, double y) {
        mX = x;
        mY = y;
    }

    public double getX() {
        return mX;
    }

    public double getY() {
        return mY;
    }

    public void setCenterPosition(double x, double y) {
        mX = (x - (mSize.width() / 2));
        mY = (y - (mSize.height() / 2));
    }


    public void setColor(int color) {
        mFilling.setColor(color);
        mDoUpdateBitmap = true;
    }

    public void setTextAlign(Align align) {
        mAlign = align;
        mDoUpdateBitmap = true;
    }

    public void forceRedraw() {
        mDoUpdateBitmap = true;
    }

    @Override
    public void draw(Canvas canvas) {


        if (mDoUpdateBitmap) {

            /* Enable to fill background */
            //mCanvas.drawColor(0xffff50a0);

            if (mSize.width() > 0 && mSize.height() > 0) {

                if (mOutline != null && mOutlineWidth > 0) {
                    /* Draw outline */
                    mCanvas.drawText(text, -mSize.left, -mSize.top, mOutline);
                }

                mCanvas.drawText(text, -mSize.left, -mSize.top, mFilling);
            }
            mDoUpdateBitmap = false;
        }

        if (mBitmap != null) {

            if (mSize.width() > 0 && mSize.height() > 0) {
                mBitmap.prepareToDraw();

                float x = (int)mX; // - getWidth() / 2;
                float y = (int)mY - getHeight()/2;

                if (mAlign == Align.LEFT) {

                }
                else if (mAlign == Align.CENTER) {
                    x = x - getWidth()/2;
                }
                else if (mAlign == Align.RIGHT) {
                    x = x - getWidth();
                }

                canvas.drawBitmap(mBitmap, x, y, mPaint);
            }
        }
    }

    public void setOutline(float width, int color) {
        mOutlineWidth = width;
        if (mOutline == null) {
            /* Setup outline paint */
            mOutline = new Paint();
            mOutline.setStyle(Paint.Style.STROKE);
            mOutline.setTextSize(mFilling.getTextSize());
            mOutline.setFakeBoldText(mBold);
            mOutline.setAntiAlias(true);
            mOutline.setFakeBoldText(mBold);
            mOutline.setTextAlign(Paint.Align.LEFT);
            mOutline.setTypeface(mFilling.getTypeface());
        }
        mOutline.setStrokeWidth(mOutlineWidth);
        mOutline.setColor(color);

        /* force size update */
        updateBitmap();
    }

    public void disableOutline() {
        mOutline = null;

        /* force size update */
        updateBitmap();
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        mDoUpdateBitmap = true;
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
        mDoUpdateBitmap = true;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
