package ninhn.app.girlxinh;

import android.app.Application;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import ninhn.app.girlxinh.helper.MyPreferenceManager;

/**
 * Created by NinHN on 4/7/16.
 */
public class MyApplication extends Application {
    public static final String TAG = MyApplication.class
            .getSimpleName();

    private static MyApplication mInstance;

    private MyPreferenceManager pref;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Picasso p = new Picasso.Builder(getApplicationContext())
                .memoryCache(new LruCache(1024 * 1024 * 50))
                .build();
        Picasso.setSingletonInstance(p);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }
        return pref;
    }

}
