package ninhn.app.girlxinh.helper;

import android.content.Context;
import android.content.SharedPreferences;

import static ninhn.app.girlxinh.constant.AppConstant.ID;
import static ninhn.app.girlxinh.constant.AppConstant.NAME;


/**
 * Created by NinHN on 4/7/16.
 */
public class MyPreferenceManager {
    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences prefUser;

    // Editor for Shared preferences
    SharedPreferences.Editor editorUser;

    // Context
    Context mContext;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "user";


    // Constructor
    public MyPreferenceManager(Context context) {
        this.mContext = context;
        prefUser = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editorUser = prefUser.edit();
    }

    public boolean saveUser(String userId, String userName) {
        editorUser.putString(ID, userId);
        editorUser.putString(NAME, userName);
        editorUser.commit();
        return true;
    }

    public String getUserId(){
        return prefUser.getString(ID, null);
    }

    public String getUserName(){
        return prefUser.getString(NAME, null);
    }

    public void clearUser() {
        editorUser.clear();
        editorUser.commit();
    }

}
