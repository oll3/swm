package se.gareth.swm;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtil {

	
	static Bitmap loadBitmap(Resources resources, int bitmapId) {
		return BitmapFactory.decodeResource(resources, bitmapId);
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
		/* SLog.d(TAG, "Load bitmap scaled to " + reqWidth + " x " + reqHeight + " (sample size=" + options.inSampleSize + ")"); */

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
	
	static public Bitmap loadScaledBitmap(Resources res, int resId, boolean toWidth, int outWidth, boolean toHeight, int outHeight) {
		
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		if (!toWidth) {
			outWidth = options.outWidth;
		}
		
		if (!toHeight) {
			outHeight = options.outHeight;
		}
		
		/* Calculate inSampleSize */
		options.inSampleSize = calculateInSampleSize(options, outWidth, outHeight);
		/* SLog.d(TAG, "Load bitmap scaled to " + reqWidth + " x " + reqHeight + " (sample size=" + options.inSampleSize + ")"); */

		/* Decode bitmap with inSampleSize set */
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeResource(res, resId, options);

		Bitmap tmpBitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, true);
		if (tmpBitmap != bitmap) {
			bitmap.recycle();
		}
		return tmpBitmap;
	}
}
