package com.metagaming.allstarsolitare.gameSelect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


class GameSelectAsyncImageLoader{

    URL init(){
        URL theURL;
        try{
            theURL = new URL("https://www.inb4.org/test/update_imagr.png");
        }catch(MalformedURLException e){
            e.printStackTrace();

            //IF RETURNS NULL, DON'T TRY TO LOAD THE IMAGE.
            //IT'S A CRAPPY URL
            return null;
        }

        //
        return theURL;
    }

    public interface OnImageLoad{
        void onImageLoadCompleted(Bitmap result);
        void onImageLoadError();
    }

    public static class AsyncImageLoader extends AsyncTask<URL, Void, Bitmap> {

        private OnImageLoad listener;

        AsyncImageLoader(OnImageLoad tempListener){
            listener = tempListener;
        }

        @Override
        protected Bitmap doInBackground(URL... url) {
            //
            Bitmap bitmap = downloadBitmap(url[0]);
            if(bitmap == null){
                listener.onImageLoadError();
                return null;
            }else{
                return bitmap;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap != null){
                listener.onImageLoadCompleted(bitmap);
            }else{
                listener.onImageLoadError();
            }
        }

        private Bitmap downloadBitmap(URL url) {
            HttpURLConnection urlConnection = null;
            try{
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if(statusCode != HttpURLConnection.HTTP_OK){
                    //
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if(inputStream != null){
                    //
                    return BitmapFactory.decodeStream(inputStream);
                }
            }catch(Exception e){
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }finally{
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }
}