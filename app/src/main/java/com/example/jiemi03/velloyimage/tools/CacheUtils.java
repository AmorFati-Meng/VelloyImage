package com.example.jiemi03.velloyimage.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.LruCache;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by jiemi03 on 2017/2/28.
 */

public class CacheUtils {

    private LruCache<String,Bitmap> mLruCache;
    private  DiskLruCache mDiskLruCahce;

    private Context mContext;
    private String uniqueName;
    private Set<BitmapWorkerTask> taskCollection;


    public CacheUtils (Context mContext, String uniqueName){
        this.mContext=mContext;
        this.uniqueName=uniqueName;
        initCache();
    }
    private void initCache(){

        taskCollection=new HashSet<BitmapWorkerTask>();

        //获取的运行的最大内存
        int maxCache= (int) Runtime.getRuntime().maxMemory();
        int cacheSize=maxCache/8;

        mLruCache=new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
        try {
            File cacheDir=getDiskCacheDir(mContext,uniqueName);
             if(!cacheDir.exists()){
                 cacheDir.mkdir();
             }
            mDiskLruCahce =DiskLruCache.open(cacheDir,getAppVersion(mContext),1,10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将图片放入LruCache当中
     * @param key 键
     * @param bitmap 值
     */
    public void addBitmapToMemoryCache(String key,Bitmap bitmap){
        if(getBitmapFromMemoryCache(key)==null){
            mLruCache.put(key,bitmap);
        }
    }

    /**
     * 从LruCache中取出对应图片
     * @param key
     * @return  图片
     */
    public Bitmap getBitmapFromMemoryCache(String key){
        return  mLruCache.get(key);
    }

    private File getDiskCacheDir(Context context,String uniqueName){
        String cachePath;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                ||!Environment.isExternalStorageRemovable()){
            cachePath=context.getExternalCacheDir().getPath();

        }else {
            cachePath=context.getCacheDir().getPath();
        }
        return  new File(cachePath+File.separator+uniqueName);
    }
    /**
     * 获取当前应用程序的版本号。
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 加载图片
     * @param imageView
     * @param url
     */
    public void loadBitmap(ImageView imageView,String url){
        Bitmap bitmap=getBitmapFromMemoryCache(url);
        if(bitmap==null){
            BitmapWorkerTask task=new BitmapWorkerTask(mDiskLruCahce,imageView,this);
            taskCollection.add(task);
            task.execute(url);
        }
        else {
            if(bitmap!=null&&imageView!=null){
                imageView.setImageBitmap(bitmap);
            }
        }
    }


    public void fluchCache(){
        if(mDiskLruCahce!=null)
            try {
                mDiskLruCahce.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}
