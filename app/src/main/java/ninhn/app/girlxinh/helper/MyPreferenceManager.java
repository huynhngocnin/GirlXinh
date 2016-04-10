package ninhn.app.girlxinh.helper;

import android.content.Context;
import android.content.SharedPreferences;

import ninhn.app.girlxinh.model.PhotoModel;

/**
 * Created by NinHN on 4/7/16.
 */
public class MyPreferenceManager {
    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context mContext;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "girlXinh";

    // All Shared Preferences Keys
    private static final String KEY_TOKEN_ID = "token_id";


    // Constructor
    public MyPreferenceManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public PhotoModel getPhoto() {
        return new PhotoModel();
    }


    public void clear() {
        editor.clear();
        editor.commit();
    }
}
