package com.example.jiemi03.velloyimage;

import android.graphics.Bitmap;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.jiemi03.velloyimage.tools.CacheUtils;
import com.example.jiemi03.velloyimage.tools.LruImageCache;

public class MainActivity extends AppCompatActivity {

   private CacheUtils cacheUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ImageView  img= (ImageView) findViewById(R.id.img);
        ImageView  img2= (ImageView) findViewById(R.id.img2);
        ImageView  img3= (ImageView) findViewById(R.id.img3);
        ImageView  img4= (ImageView) findViewById(R.id.img4);
        ImageView  img5= (ImageView) findViewById(R.id.img5);
        ImageView  img6= (ImageView) findViewById(R.id.img6);

      /*  RequestQueue queue= Volley.newRequestQueue(this);
       // LruImageCache imageCach=new LruImageCache();
        ImageLoader loader=new ImageLoader(queue,new LruImageCache());



        ImageLoader.ImageListener listener = ImageLoader.getImageListener(img,R.mipmap.ic_launcher,R.mipmap.ic_launcher);
        loader.get("http://p0.so.qhmsg.com/t012337b420071dd7ea.jpg",listener);*/
        String url="http://p0.so.qhmsg.com/t012337b420071dd7ea.jpg";
         cacheUtils=new CacheUtils(this,"jiemi");
        cacheUtils.loadBitmap(img,url);

        String url2="http://p4.so.qhmsg.com/sdr/957_1440_/t010f2572a060c1d0f4.jpg";

        cacheUtils.loadBitmap(img2,url2);

        String url3="http://p3.so.qhmsg.com/sdr/960_1440_/t01df63fc79acb8e058.jpg";

        cacheUtils.loadBitmap(img3,url3);

        String url4="http://p4.so.qhmsg.com/sdr/960_1440_/t01710bcddeb79fa46e.jpg";

        cacheUtils.loadBitmap(img4,url4);

        String url5="http://p0.so.qhmsg.com/sdr/960_1440_/t012c9e65434954069f.jpg";

        cacheUtils.loadBitmap(img5,url5);

        String url6="http://p2.so.qhmsg.com/sdr/945_1440_/t01dd005ee07d537f42.jpg";

        cacheUtils.loadBitmap(img6,url6);



    }

    @Override
    protected void onPause() {
        super.onPause();
        cacheUtils.fluchCache();
    }
}
