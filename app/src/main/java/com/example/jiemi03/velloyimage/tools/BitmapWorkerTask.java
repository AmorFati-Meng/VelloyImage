package com.example.jiemi03.velloyimage.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jiemi03 on 2017/2/28.
 */

public class BitmapWorkerTask extends AsyncTask<String,Void,Bitmap> {

    private String imgUrl;
    private DiskLruCache mDiskLruCache;


    private CacheUtils cacheUtils;

    private ImageView imageView ;




    public BitmapWorkerTask(DiskLruCache mDiskLruCache,ImageView imageView,CacheUtils cacheUtils){
        this.mDiskLruCache=mDiskLruCache;

        this.imageView=imageView;
        this.cacheUtils=cacheUtils;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {

        imgUrl=strings[0];

        FileDescriptor fileDescriptor=null;
        FileInputStream fileInputStream=null;
        DiskLruCache.Snapshot snapshot;
        String key=MD5Utils.generateKey(imgUrl);
        try {
            snapshot=mDiskLruCache.get(key);

            if(snapshot==null){
                DiskLruCache.Editor editor=mDiskLruCache.edit(key);
                if(editor!=null){
                    OutputStream outputStream=editor.newOutputStream(0);
                    if(downloadUrlToSttream(imgUrl,outputStream)){
                        editor.commit();
                    }
                    else {
                        editor.abort();
                    }
                }
            }
            snapshot=mDiskLruCache.get(key);
            if(snapshot!=null){
                fileInputStream=(FileInputStream)snapshot.getInputStream(0);
                fileDescriptor=fileInputStream.getFD();

            }
            Bitmap bitmap=null;
            if(fileDescriptor!=null){
                bitmap= BitmapFactory.decodeStream(fileInputStream);

            }
            if(bitmap!=null){
                cacheUtils.addBitmapToMemoryCache(imgUrl,bitmap);
            }
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(fileDescriptor==null&&fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return  null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if(imageView!=null&&bitmap!=null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    private boolean downloadUrlToSttream(String urlStirng, OutputStream outputStream) {
        HttpURLConnection urlConnection=null;
        BufferedOutputStream out=null;
        BufferedInputStream in=null;
        try {
            URL url=new URL(urlStirng);
            urlConnection= (HttpURLConnection) url.openConnection();
            in=new BufferedInputStream(urlConnection.getInputStream());
            out=new BufferedOutputStream(outputStream);
            int b;
            while ((b=in.read())!=-1){
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();

        }
        finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            try {
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return false;
    }
}
