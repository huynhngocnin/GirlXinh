package ninhn.app.girlxinh.constant;

import java.util.ArrayList;

/**
 * Created by NinHN on 5/22/16.
 */
public class AppConstant {

    public static final String BLANK = "";
    public static final String _JPG = ".jpg";

    public static final ArrayList ARRAY_EMPTY = new ArrayList<>();

    public static final int FLAG_PAGE_MORE = 2;
    public static final int FLAG_PAGE_ONE = 1;
    public static final int FLAG_REFRESH = 0;

    public static final int FLAG_PHOTO_LOAD = 0;
    public static final int FLAG_PHOTO_LOVE = 1;
    public static final int FLAG_PHOTO_REVIEW = 2;

    //Key is saved in share_preference
    public static final String USER_LOGIN = "userLogin";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final String USER_EMAIL = "userEmail";
    public static final String USER_PHONE = "userPhone";
    public static final String USER_BIRTHDAY = "userBirthday";
    public static final String USER_FROM = "userFrom";
    public static final String USER_FACEBOOK = "userFacebook";
    public static final String USER_AVATAR = "userAvatar";
    public static final String USER_CREATED = "userCreated";
    public static final String USER_TOKEN = "userToken";
    public static final String USER_ROLE = "userRole";

    public static final int ADMOB_INIT_POSITION = -1;
    public static final int ADMOB_CYCLE_SHOW = 10;

    public static final int USER_ROLE_ADMIN = 1;
    public static final int USER_ROLE_USER = 0;

    public static final int PHOTO_SIZE_MIN = 960;
    public static final int FILE_SIZE_MAX = 3072;
}
