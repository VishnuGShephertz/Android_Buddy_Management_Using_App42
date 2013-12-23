package com.shephertz.app42.buddy.util;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shephertz.app42.buddy.listener.AvatarEventListener;

public class Utils {

	/*
	 * This function allows user to load image from Url in a background Thraed
	 * 
	 * @param image ImageView on which image is loaded
	 * 
	 * @param url of image
	 */
	public static void loadImageFromUrl(final String url,
			final AvatarEventListener callback) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				final Bitmap bitmap = loadBitmap(url);
				callerThreadHandler.post(new Runnable() {
					@Override
					public void run() {
						if (callback != null)
							callback.onLoadImage(bitmap);
					}
				});
			}
		}.start();
	}

	

	// decodes image and scales it to reduce memory consumption
	private static Bitmap loadBitmap(String url) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			if (url.startsWith("http")) {
				InputStream in = new java.net.URL(url).openStream();
				BitmapFactory.decodeStream(in, null, o);
			} else
				BitmapFactory.decodeFile(url, o);
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < 150 && height_tmp / 2 < 150)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			o2.inPreferredConfig = Bitmap.Config.RGB_565;
			o2.inJustDecodeBounds = false;
			if (url.startsWith("http")) {
				InputStream in = new java.net.URL(url).openStream();
				return BitmapFactory.decodeStream(in, null, o2);
			} else
				return BitmapFactory.decodeFile(url, o2);
		} catch (Exception e) {
		}
		return null;
	}

	/*
	 * This function allows user to load image from Url in a background Thraed
	 * 
	 * @param image ImageView on which image is loaded
	 * 
	 * @param url of image
	 */
	public static void loadBackground(final LinearLayout linlay,
			final String url) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				final Bitmap bitmap = loadBitmap(url);
				callerThreadHandler.post(new Runnable() {
					@Override
					public void run() {
						if (bitmap != null) {
							linlay.setBackgroundDrawable(new BitmapDrawable(
									loadBitmap(url)));

						}
					}
				});
			}
		}.start();
	}

	/*
	 * This function allows user to load image from Url in a background Thraed
	 * 
	 * @param image ImageView on which image is loaded
	 * 
	 * @param url of image
	 */
	public static void loadImageFromUrl(final ImageView image, final String url) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				final Bitmap bitmap = loadBitmap(url);
				callerThreadHandler.post(new Runnable() {
					@Override
					public void run() {
						if (bitmap != null) {
							image.setImageBitmap(bitmap);
						}
					}
				});
			}
		}.start();
	}

	/*
	 * This method is used to check availability of network connection in
	 * android device uses CONNECTIVITY_SERVICE of android device to get desired
	 * network internet connection
	 * 
	 * @return status of availability of internet connection in true or false
	 * manner
	 */
	public static boolean haveNetworkConnection(Context context) {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo[] netInfo = cm.getAllNetworkInfo();
			for (NetworkInfo ni : netInfo) {
				if (ni.getTypeName().equalsIgnoreCase("WIFI"))
					if (ni.isConnected())
						haveConnectedWifi = true;
				if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
					if (ni.isConnected())
						haveConnectedMobile = true;
			}

		} catch (Exception e) {

		}
		return haveConnectedWifi || haveConnectedMobile;
	}

}
