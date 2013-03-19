package com.kosenventure.sansan.others;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {

	private ImageView mImageView;
	
	
	public ImageDownloadTask(ImageView imageView) {
		mImageView = imageView;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		Bitmap image = ImageCache.getImage(params[0]);  
        if (image == null) {  
            byte[] byteArray = HttpClient.getByteArrayFromURL(params[0]);  
            image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            ImageCache.setImage(params[0], image);  
        }  
        return image;  
	}

	// メインスレッドで実行する処理  
    @Override  
    protected void onPostExecute(Bitmap result) {  
        this.mImageView.setImageBitmap(result);  
    }  
    
    public static class ImageCache {

    	private static HashMap<String,Bitmap> cache = new HashMap<String,Bitmap>();  
        
        public static Bitmap getImage(String key) {  
            if (cache.containsKey(key)) {  
                Log.d("cache", "cache hit!");  
                return cache.get(key);  
            }  
            return null;  
        }  
        
        public static void setImage(String key, Bitmap image) {  
            cache.put(key, image);  
        }  
    }
    
    public static class HttpClient {  
        public static byte[] getByteArrayFromURL(String strUrl) {  
            byte[] byteArray = new byte[1024];  
            byte[] result = null;  
            HttpURLConnection con = null;  
            InputStream in = null;  
            ByteArrayOutputStream out = null;  
            int size = 0;  
            try {  
                URL url = new URL(strUrl);  
                con = (HttpURLConnection) url.openConnection();  
                con.setRequestMethod("GET");  
                con.connect();  
                in = con.getInputStream();  
      
                out = new ByteArrayOutputStream();  
                while ((size = in.read(byteArray)) != -1) {  
                    out.write(byteArray, 0, size);  
                }  
                result = out.toByteArray();  
            } catch (Exception e) {  
                e.printStackTrace();  
            } finally {  
                try {  
                    if (con != null)  
                        con.disconnect();  
                    if (in != null)  
                        in.close();  
                    if (out != null)  
                        out.close();  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
            return result;  
        }  
    }  
}
