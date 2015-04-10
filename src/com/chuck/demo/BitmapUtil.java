package com.chuck.demo;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.example.flipclothviewdemo.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;


 /**
 * @Title：FashionDIY
 * @Description：bitmap的处理函数，避免处理的照片过大出现OOM
 * @date 2014-11-5 下午3:06:00
 * @author chengang
 * @version 1.0
 */

public class BitmapUtil {
	
	private static String TAG = "BitmapUtil";
	private static Context mContext;
	
	public BitmapUtil(Context context){
		this.mContext = context;
	}
	

	/**
	 * 解码对应的图片资源
	 * 
	 * @author chengang
	 * @date 2014-11-5 下午3:17:09
	 * @param photoPath 要处理的照片的绝对路径
	 * @param reqWidth 返回照片的宽度
	 * @param reqHeight 返回照片的高度
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromResource(Object photoPath,int reqWidth, int reqHeight) {
	    //通过设置inJustDecodeBounds=true解码，获取照片的尺寸
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
		if(photoPath instanceof String){
			BitmapFactory.decodeFile((String)photoPath, options);
		}else{
			BitmapFactory.decodeResource(mContext.getResources(),(Integer)photoPath, options);
		}

	    //计算inSampleSize
	    //If set to a value > 1, requests the decoder to subsample the original image, returning a smaller image to save memory. 
	    //The sample size is the number of pixels in either dimension that correspond to a single pixel in the decoded bitmap. 
	    //For example, inSampleSize == 4 returns an image that is 1/4 the width/height of the original, and 1/16 the number of pixels. 
	    //Any value <= 1 is treated the same as 1. 
	    //Note: the decoder uses a final value based on powers of 2, any other value will be rounded down to the nearest power of 2. 

	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    //通过inSampleSize的大小返回对应处理后的图片大小
	    options.inJustDecodeBounds = false;
		if(photoPath instanceof String){
			return BitmapFactory.decodeFile((String)photoPath, options);
		}else{
			return BitmapFactory.decodeResource(mContext.getResources(),(Integer)photoPath, options);
		}
	}
	
	public static Bitmap decodeBitmapFromByteData(byte[] data , int resW , int resH) {
	    //通过设置inJustDecodeBounds=true解码，获取照片的尺寸
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    if(data != null && data.length > 0){
	    	BitmapFactory.decodeByteArray(data, 0, data.length, options);
	    }

	    //计算inSampleSize
	    //If set to a value > 1, requests the decoder to subsample the original image, returning a smaller image to save memory. 
	    //The sample size is the number of pixels in either dimension that correspond to a single pixel in the decoded bitmap. 
	    //For example, inSampleSize == 4 returns an image that is 1/4 the width/height of the original, and 1/16 the number of pixels. 
	    //Any value <= 1 is treated the same as 1. 
	    //Note: the decoder uses a final value based on powers of 2, any other value will be rounded down to the nearest power of 2. 
//	    DisplayMetrics screenMetric = FashionDiyApplication.getApplicationInstance().getScreenSize();
//	    int resW = screenMetric.widthPixels;
//	    int resH = (int)(screenMetric.heightPixels - 90 * screenMetric.density);
//	    Log.e("BitmapUtils", "" + screenMetric.density);
	    options.inSampleSize = calculateInSampleSize(options, resW, resH);

	    //通过inSampleSize的大小返回对应处理后的图片大小  
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeByteArray(data, 0, data.length, options);
	}
	
	/**
	 * 计算InSampleSize大小
	 * 
	 * @author chengang
	 * @date 2014-11-5 下午3:21:24
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {
		if (width > height) {
			inSampleSize = Math.round((float) height / (float) reqHeight);
		} else {
			inSampleSize = Math.round((float) width / (float) reqWidth);
		}
    }

    return inSampleSize;
}
	
	/**
	 * 解码对应的照片输入流
	 * 
	 * @author chengang
	 * @date 2014-11-5 下午3:20:41
	 * @param is 要处理的照片的流
	 * @param reqWidth 返回照片的宽度
	 * @param reqHeight 返回照片的高度
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromResource(InputStream inputStream, int reqWidth, int reqHeight){
		byte[] byteArr = new byte[0];
        byte[] buffer = new byte[1024];
        int len;
        int count = 0;

        try {
            while ((len = inputStream.read(buffer)) > -1) {
                if (len != 0) {
                    if (count + len > byteArr.length) {
                        byte[] newbuf = new byte[(count + len) * 2];
                        System.arraycopy(byteArr, 0, newbuf, 0, count);
                        byteArr = newbuf;
                    }

                    System.arraycopy(buffer, 0, byteArr, count, len);
                    count += len;
                }
            }

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(byteArr, 0, count, options);

            options.inSampleSize = calculateInSampleSize(options, reqWidth,reqHeight);
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            return BitmapFactory.decodeByteArray(byteArr, 0, count, options);

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
	}
	
	public void loadBitmap(Object resPath, ImageView imageView , int resWid , int resHei) {
		if(resWid == 0 || resHei == 0){
			return;
		}
		Bitmap bitmap = null;
		if(resPath instanceof String){
			bitmap = getBitmapFromCache((String)resPath);
			if(bitmap == null){
				startAsyncTask(resPath, imageView, resWid, resHei);
			}else{
				loadCacheBitmap(resPath, imageView, bitmap);
			}
		}else{
			startAsyncTask(resPath, imageView, resWid, resHei);
		}
	}
	
	public void startAsyncTask(Object resPath, ImageView imageView , int resWid , int resHei){
        if (cancelPotentialWork(resPath, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView , resWid , resHei);
            Resources resource = mContext.getResources();
            Bitmap defaultBimap = BitmapFactory.decodeStream(resource.openRawResource(R.drawable.ic_launcher));
            final AsyncDrawable asyncDrawable = new AsyncDrawable(resource, defaultBimap, task);
            imageView.setImageDrawable(asyncDrawable);
            imageView.setTag(resPath);
            task.execute(resPath);
        }
	}
	
	public void loadCacheBitmap(Object resPath,ImageView imageView , Bitmap cacheBitmap){
		imageView.setVisibility(View.VISIBLE);
		cancelPotentialWork(resPath, imageView);
		imageView.setTag(resPath);
		imageView.setImageBitmap(cacheBitmap);
	}

    private static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    public static boolean cancelPotentialWork(Object resSource, ImageView imageView) {
	       BitmapWorkerTask bitmapDownloaderTask = getBitmapWorkerTask(imageView);

	       if (bitmapDownloaderTask != null) {
	    	   if(resSource instanceof String){
		           String bitmapPath = bitmapDownloaderTask.resPath;
		           if ((bitmapPath == null) || (!bitmapPath.equals((String)resSource))) {
		               bitmapDownloaderTask.cancel(true);
		           } else {
		               // The same URL is already being downloaded.
		               return false;
		           }
	    	   }else if(resSource instanceof Integer){
	    		   final int bitmapData = bitmapDownloaderTask.drawableId;
	    		   int drawableId = (Integer)resSource;
	               if (bitmapData != drawableId) {
	                   // Cancel previous task
	            	   bitmapDownloaderTask.cancel(true);
	               } else {
	                   // The same work is already in progress
	                   return false;
	               }
	    	   }
	       }
	       return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
       if (imageView != null) {
           final Drawable drawable = imageView.getDrawable();
           if (drawable instanceof AsyncDrawable) {
               final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
               return asyncDrawable.getBitmapWorkerTask();
           }
        }
        return null;
    }
    
    class BitmapWorkerTask extends AsyncTask<Object, Void, Bitmap> {
    	private final WeakReference<ImageView> imageViewReference;
    	private String resPath;
    	private int drawableId;
    	private int mResWid = 0;
    	private int mResHei = 0;
    	
	    public BitmapWorkerTask(ImageView imageView , int resWid , int resHei) {
	        // Use a WeakReference to ensure the ImageView can be garbage collected
	        imageViewReference = new WeakReference<ImageView>(imageView);
	        mResWid = resWid;
	        mResHei = resHei;
	    }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }                      

            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && imageView != null) {
                	if(imageView.getTag() instanceof String){
	                	addBitmapToCache(resPath, bitmap);//添加到缓存
	                	if(resPath.equals((String)imageView.getTag())){
	                		imageView.setImageBitmap(bitmap);
	                	}
                	}else{
                		if(drawableId ==((Integer)imageView.getTag())){
	                		imageView.setImageBitmap(bitmap);
	                	}
                	}
                }
            }
        }

		@Override
		protected Bitmap doInBackground(Object... arg0) {
			if(arg0[0] instanceof String){
				resPath = (String)arg0[0];
				Bitmap currentBitmap = decodeSampledBitmapFromResource(resPath, mResWid, mResHei);
				if(currentBitmap != null){
					return currentBitmap;
				}
			}else{
				drawableId = (Integer)arg0[0];
				Bitmap currentBitmap = decodeSampledBitmapFromResource(drawableId, mResWid, mResHei);
				if(currentBitmap != null){
					return currentBitmap;
				}
			}
			return null;
		}
    }
    
    private static final int HARD_CACHE_CAPACITY = 10;
	 private static final int DELAY_BEFORE_PURGE = 10 * 1000; // in milliseconds

	   // Hard cache, with a fixed maximum capacity and a life duration
	   private final HashMap<String, Bitmap> sHardBitmapCache =
	       new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {
	       @Override
	       protected boolean removeEldestEntry(LinkedHashMap.Entry<String, Bitmap> eldest) {
	           if (size() > HARD_CACHE_CAPACITY) {
	               // Entries push-out of hard reference cache are transferred to soft reference cache
	               sSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
	               return true;
	           } else
	               return false;
	       }
	   };

	   // Soft cache for bitmaps kicked out of hard cache
	   private final static ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache =
	       new ConcurrentHashMap<String, SoftReference<Bitmap>>(HARD_CACHE_CAPACITY / 2);
	   
	   /**
	    * Adds this bitmap to the cache.
	    * @param bitmap The newly downloaded bitmap.
	    */
	   private void addBitmapToCache(String url, Bitmap bitmap) {
	       if (bitmap != null) {
	           synchronized (sHardBitmapCache) {
	               sHardBitmapCache.put(url, bitmap);
	           }
	       }
	   }
	   
	   /**
	    * @param url The URL of the image that will be retrieved from the cache.
	    * @return The cached bitmap or null if it was not found.
	    */
	   private Bitmap getBitmapFromCache(String url) {
	       // First try the hard reference cache
	       synchronized (sHardBitmapCache) {
	           final Bitmap bitmap = sHardBitmapCache.get(url);
	           if (bitmap != null) {
	               // Bitmap found in hard cache
	               // Move element to first position, so that it is removed last
	               sHardBitmapCache.remove(url);
	               sHardBitmapCache.put(url, bitmap);
	               return bitmap;
	           }
	       }

	       // Then try the soft reference cache
	       SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(url);
	       if (bitmapReference != null) {
	           final Bitmap bitmap = bitmapReference.get();
	           if (bitmap != null) {
	               // Bitmap found in soft cache
	               return bitmap;
	           } else {
	               // Soft reference has been Garbage Collected
	               sSoftBitmapCache.remove(url);
	           }
	       }

	       return null;
	   }
	   
	   public void clearImageCache(){
		   if(sSoftBitmapCache != null && sSoftBitmapCache.size() > 0){
			   sSoftBitmapCache.clear();
		   }
		   if(sHardBitmapCache != null && sHardBitmapCache.size() > 0){
			   sHardBitmapCache.clear();
		   }
	   }
	   
	   /**
	    * 将view转化为bitmap
	    * 
	    * @author Administrator
	    * @date 2014-12-5 上午8:56:05
	    * @param sourceView
	    * @return
	    */
	   public static Bitmap getBitmapFromView(View sourceView){
		   if(sourceView == null){
			   return null;
		   }
			Bitmap returnedBitmap = Bitmap.createBitmap(sourceView.getWidth(), sourceView.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(returnedBitmap);
			Drawable bgDrawable = sourceView.getBackground();
			if (bgDrawable != null) {
				bgDrawable.draw(canvas);
			} else {
				canvas.drawColor(Color.TRANSPARENT);
			}
			sourceView.draw(canvas);
			return returnedBitmap;
	   }
}
