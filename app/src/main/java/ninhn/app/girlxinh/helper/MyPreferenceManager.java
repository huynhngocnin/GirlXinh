package ninhn.app.girlxinh.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import ninhn.app.girlxinh.model.UserModel;


import static ninhn.app.girlxinh.constant.AppConstant.BLANK;
import static ninhn.app.girlxinh.constant.AppConstant.USER_ID;
import static ninhn.app.girlxinh.constant.AppConstant.USER_NAME;
import static ninhn.app.girlxinh.constant.AppConstant.USER_AVATAR;
import static ninhn.app.girlxinh.constant.AppConstant.USER_EMAIL;
import static ninhn.app.girlxinh.constant.AppConstant.USER_PHONE;
import static ninhn.app.girlxinh.constant.AppConstant.USER_BIRTHDAY;
import static ninhn.app.girlxinh.constant.AppConstant.USER_FACEBOOK;
import static ninhn.app.girlxinh.constant.AppConstant.USER_FROM;
import static ninhn.app.girlxinh.constant.AppConstant.USER_CREATED;

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

    public void saveUser(UserModel userModel) {
        editorUser.putString(USER_ID, userModel.getId());
        editorUser.putString(USER_NAME, userModel.getName());
        editorUser.putString(USER_EMAIL, userModel.getEmail());
        editorUser.putString(USER_PHONE, userModel.getPhone());
        editorUser.putString(USER_BIRTHDAY, userModel.getBirthday());
        editorUser.putString(USER_FROM, userModel.getFrom());
        editorUser.putString(USER_FACEBOOK, userModel.getFacebook());
        editorUser.putString(USER_AVATAR, userModel.getAvatar());
        editorUser.putLong(USER_CREATED, userModel.getCreateTime().getTime());
        editorUser.commit();
    }

    public UserModel getUser() {
        UserModel userModel = new UserModel();
        userModel.setId(prefUser.getString(USER_ID, BLANK));
        userModel.setName(prefUser.getString(USER_NAME, BLANK));
        userModel.setEmail(prefUser.getString(USER_EMAIL, BLANK));
        userModel.setPhone(prefUser.getString(USER_PHONE, BLANK));
        userModel.setBirthday(prefUser.getString(USER_BIRTHDAY, BLANK));
        userModel.setFrom(prefUser.getString(USER_FROM, BLANK));
        userModel.setFacebook(prefUser.getString(USER_FACEBOOK, BLANK));
        userModel.setAvatar(prefUser.getString(USER_AVATAR, BLANK));
        userModel.setCreateTime(new Date(prefUser.getLong(USER_CREATED, 0)));
        return userModel;
    }


    public void clearUser() {
        editorUser.clear();
        editorUser.commit();
    }

}
