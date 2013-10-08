package se.gareth.swm;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;


public class Sprite {

	private static final String TAG = Sprite.class.getName();

	enum ColorChannel {
		RED,
		GREEN,
		BLUE
	}
	
	double mFrameWidth, mFrameHeight;
	private ArrayList<Bitmap> mFramesList;
	private int mFrames;
	
	public Sprite(Bitmap bitmap, int frames) {
		
		mFrames = frames;
		mFramesList = new ArrayList<Bitmap>();
		
		mFrameWidth = (double)bitmap.getWidth() / (double)frames;
		mFrameHeight = bitmap.getHeight();
		
		Rect src = new Rect();
		Rect dest = new Rect(0, 0, (int)mFrameWidth, (int)mFrameHeight);
		int frameWidth = (int)(mFrameWidth + 0.5);
		
		for (int i = 0; i < frames; i ++) {
			int frameXPos = (int)((double)i * mFrameWidth + 0.5);
			
			src.set(frameXPos, 0, frameXPos + frameWidth, (int)(mFrameHeight + 0.5));
			Bitmap frame = Bitmap.createBitmap(frameWidth, (int)(mFrameHeight + 0.5), bitmap.getConfig());
			Canvas frameCanvas = new Canvas(frame);
			frameCanvas.drawBitmap(bitmap, src, dest, null);
			mFramesList.add(frame);
		}
	}

	public Bitmap getFrame(int frameIndex) {
		return mFramesList.get(frameIndex);
	}
	
	public double getFrameWidth() {
		return mFrameWidth;
	}

	public double getFrameHeight() {
		return mFrameHeight;
	}
	
	public int getFrames() {
		return mFrames;
	}
	
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	    return inSampleSize;
	}
	
	/* Load a bitmap and adjust it to fit given width */
	static public Bitmap loadFitBitmap(Resources res, int resId, int fitWidth, int fitHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		/* Calculate inSampleSize */
		options.inSampleSize = calculateInSampleSize(options, fitWidth, fitHeight);
		/* Log.d(TAG, "Load bitmap scaled to " + reqWidth + " x " + reqHeight + " (sample size=" + options.inSampleSize + ")"); */

		/* Decode bitmap with inSampleSize set */
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeResource(res, resId, options);
   
		float widthRatio = (float)fitWidth / (float)options.outWidth;
		int dstHeight = (int)((float)options.outHeight * widthRatio);

		Bitmap tmpBitmap = Bitmap.createScaledBitmap(bitmap, fitWidth, dstHeight, true);
		if (tmpBitmap != bitmap) {
			bitmap.recycle();
		}
		return tmpBitmap;
	}
	
	static public int multiplyColor(int color, float mul) {
		int r,g,b;
		r = Color.red(color);
		g = Color.green(color);
		b = Color.blue(color);
		r = Math.max(Math.min((int)(r * mul), 255), 0);
		g = Math.max(Math.min((int)(g * mul), 255), 0);
		b = Math.max(Math.min((int)(b * mul), 255), 0);
		return Color.rgb(r, g, b);
	}
	
	static public int add2Color(int color, int rInc, int gInc, int bInc) {
		int r,g,b;
		r = Color.red(color);
		g = Color.green(color);
		b = Color.blue(color);
		r = Math.max(Math.min(r + rInc, 255), 0);
		g = Math.max(Math.min(g + gInc, 255), 0);
		b = Math.max(Math.min(b + bInc, 255), 0);
		return Color.rgb(r, g, b);
	}
	
	/*
	 * Create sprite from bitmap, and replace all pixels with shades of the given 
	 * lightMapChannel with newColor. And put layers together in order of appearance.
	 */
	static public Bitmap replaceColor(Bitmap bitmap, ColorChannel lightMapChannel, int newColor, int layers) {
		
		/* Replace pixels in lightmap */
		bitmap = replaceColor(bitmap, lightMapChannel, newColor);
		
		int spriteWidth = bitmap.getWidth();
		int spriteHeight = bitmap.getHeight() / layers;
		
		/* Place the lower sprite above the upper one */
		Rect destRect = new Rect(0, 0, spriteWidth, spriteHeight);
		Bitmap sprite = Bitmap.createBitmap(spriteWidth, spriteHeight, bitmap.getConfig());
		
		Canvas canvas = new Canvas(sprite);
		
		for (int i = 0; i < layers; i ++) {
			/* Draw the upper bitmap at sprite */
			canvas.drawBitmap(bitmap, new Rect(0, spriteHeight * i, spriteWidth, spriteHeight * (i + 1)), destRect, null);
		}
		
		return sprite;
	}

	static public int limitColor(int colorComponent) {
		if (colorComponent > 255)
			return 255;
		else if (colorComponent < 0)
			return 0;
		return colorComponent;
	}
	
	/*
	 * Create sprite from bitmap, and replace all pixels with shades of the given 
	 * lightMapChannel with newColor.
	 */
	static public Bitmap replaceColor(Bitmap bitmap, ColorChannel lightMapChannel, int newColor) {

		final int spriteWidth = bitmap.getWidth();
		final int spriteHeight = bitmap.getHeight();

		Bitmap sprite = bitmap.copy(bitmap.getConfig(), true);
		final int numPixels = spriteWidth * spriteHeight;
		int pixelBuf[] = new int[numPixels];
		bitmap.getPixels(pixelBuf, 0, spriteWidth, 0, 0, spriteWidth, spriteHeight);
		
		final int newRed = Color.red(newColor);
		final int newGreen = Color.green(newColor);
		final int newBlue = Color.blue(newColor);
		
		/* Replace color in mask with new color */
		
		for (int i = 0; i < numPixels; i ++) {
			int pixelColor = pixelBuf[i];
			
			final int alpha = Color.alpha(pixelColor);
			final int red = Color.red(pixelColor);
			final int green = Color.green(pixelColor);
			final int blue = Color.blue(pixelColor);
			
			if (lightMapChannel == ColorChannel.RED) {
				if (red > 0 && green == 0 && blue == 0) {
					int light = red;
					light -= 128;
					pixelColor = Color.argb(alpha,
							limitColor(newRed + light),
							limitColor(newGreen + light),
							limitColor(newBlue + light));
				}
			}
			else if (lightMapChannel == ColorChannel.GREEN) {
				if (red == 0 && green > 0 && blue == 0) {
					int light = green;
					light -= 128;
					pixelColor = Color.argb(alpha, 
							limitColor(newRed + light),
							limitColor(newGreen + light),
							limitColor(newBlue + light));
				}
			}
			else if (lightMapChannel == ColorChannel.BLUE) {
				if (red == 0 && green == 0 && blue > 0) {
					int light = blue;
					light -= 128;
					pixelColor = Color.argb(alpha, 
							limitColor(newRed + light),
							limitColor(newGreen + light),
							limitColor(newBlue + light));
				}
			}
			pixelBuf[i] = pixelColor;
		}
		
		sprite.setPixels(pixelBuf, 0, spriteWidth, 0, 0, spriteWidth, spriteHeight);
		return sprite;
	}
	
	
	/*
	 * Create sprite from depth and mask partitions, and replace all non 
	 * transparent pixels which match replaceMaskColor in mask with newColor.
	 */
	static public Bitmap ReplaceColor(Bitmap bitmapWithMask, int replaceMaskColor, int newColor) {
		
		int spriteWidth = bitmapWithMask.getWidth();
		int spriteHeight = bitmapWithMask.getHeight() / 2;
		
		Rect destRect = new Rect(0, 0, spriteWidth, spriteHeight);
		Bitmap sprite = Bitmap.createBitmap(spriteWidth, spriteHeight, bitmapWithMask.getConfig());
		
		Canvas canvas = new Canvas(sprite);
		/* Draw mask bitmap at sprite */
		canvas.drawBitmap(bitmapWithMask, new Rect(0, spriteHeight, spriteWidth, spriteHeight + spriteHeight), destRect, null);
		
		/* Replace color in mask with new color */
		for (int y = 0; y < sprite.getHeight(); y ++) {
			for (int x = 0; x < sprite.getWidth(); x ++) {
				if (sprite.getPixel(x, y) == replaceMaskColor) {
					sprite.setPixel(x, y, newColor);
				}
			}
		}
		
		/* Draw the actual image as an overlay on the mask */
		canvas.drawBitmap(bitmapWithMask, new Rect(0, 0, spriteWidth, spriteHeight), destRect, null);
		return sprite;
	}
	
	
	/*
	 * Create sprite from depth and mask partitions, and replace all non 
	 * transparent pixels in mask with newColor.
	 */
	static public Bitmap ReplaceMask(Bitmap bitmapWithMask, int newColor) {
		
		int spriteWidth = bitmapWithMask.getWidth();
		int spriteHeight = bitmapWithMask.getHeight() / 2;
		
		Rect destRect = new Rect(0, 0, spriteWidth, spriteHeight);
		Bitmap sprite = Bitmap.createBitmap(spriteWidth, spriteHeight, 
				bitmapWithMask.getConfig());
		
		Canvas canvas = new Canvas(sprite);
		/* Draw mask bitmap at sprite */
		canvas.drawBitmap(bitmapWithMask, 
				new Rect(0, spriteHeight, spriteWidth, spriteHeight + spriteHeight), 
				destRect, null);
		
		/* Replace color in mask with new color */
		for (int y = 0; y < sprite.getHeight(); y ++) {
			for (int x = 0; x < sprite.getWidth(); x ++) {
				if (Color.alpha(sprite.getPixel(x, y)) == 0xff) {
					sprite.setPixel(x, y, newColor);
				}
			}
		}
		
		/* Draw the actual image as an overlay on the mask */
		canvas.drawBitmap(bitmapWithMask, new Rect(0, 0, spriteWidth, spriteHeight), 
				destRect, null);
		return sprite;
	}
	
	/*
	 * Create sprite from depth and mask partitions, and replace all non 
	 * transparent pixels in mask with newColor. Pixels which aren't in 
	 * the mask won't be included in the resulting image.
	 */
	static public Bitmap ReplaceAndClipMask(Bitmap bitmapWithMask, int newColor) {
		
		int spriteWidth = bitmapWithMask.getWidth();
		int spriteHeight = bitmapWithMask.getHeight() / 2;
		
		Rect destRect = new Rect(0, 0, spriteWidth, spriteHeight);
		Bitmap sprite = Bitmap.createBitmap(spriteWidth, spriteHeight, 
				bitmapWithMask.getConfig());
		
		Canvas canvas = new Canvas(sprite);
		/* Draw mask bitmap at sprite */
		canvas.drawBitmap(bitmapWithMask, 
				new Rect(0, spriteHeight, spriteWidth, spriteHeight + spriteHeight), 
				destRect, null);
		
		/* Replace color in mask with new color */
		for (int y = 0; y < spriteHeight; y ++) {
			for (int x = 0; x < spriteWidth; x ++) {
				if (Color.alpha(bitmapWithMask.getPixel(x, y + spriteHeight)) == 0xff) {
					sprite.setPixel(x, y, newColor);
				}
			}
		}
		
		/* Draw the actual image as an overlay on the mask */
		canvas.drawBitmap(bitmapWithMask, new Rect(0, 0, spriteWidth, spriteHeight), 
				destRect, null);
		
		/* Clear pixels outside of mask */
		for (int y = 0; y < spriteHeight; y ++) {
			for (int x = 0; x < spriteWidth; x ++) {
				if (Color.alpha(bitmapWithMask.getPixel(x, y + spriteHeight)) != 0xff) {
					sprite.setPixel(x, y, 0);
				}
			}
		}
		return sprite;
	}
}
