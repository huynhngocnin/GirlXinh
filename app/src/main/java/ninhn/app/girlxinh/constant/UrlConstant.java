package ninhn.app.girlxinh.constant;

/**
 * Created by NinHN on 5/23/16.
 */
public class UrlConstant {
    public static final String SOCIAL_URL = "http://vngirl.xyz/photo.html?id=";
    public static final String PHOTO_GET = "/photo-get?id=";


    //public static final String SERVER = "http://104.155.232.78:8008";
    public static final String SERVER = "http://192.168.1.2:8008";
    public static final String PHOTO_LIST = SERVER + "/photo-list";
    public static final String PHOTO_LIST_PAGE = PHOTO_LIST + "?page=";
    public static final String PHOTO_LOVE_UP = SERVER + "/photo-love-up";
    public static final String PHOTO_LOVE_DOWN = SERVER + "/photo-love-down";
    public static final String PHOTO_USER_LOVE = SERVER + "/user-photo-love";

    public static final String PHOTO_USER_UPLOAD = SERVER + "/user-photo-upload";

    public static final String PHOTO_PUBLISH_USER = "/photo-publish-user";
    public static final String PHOTO_REVIEW_USER = "/photo-review-user";

    public static final String CONDITION_START = "?";
    public static final String CONDITION_AND = "&";
    public static final String CONDITION_PHOTO_ID = "photoId=";
    public static final String CONDITION_USER_ID = "userId=";
    public static final String CONDITION_PAGE = "page=";

    public static final String PHOTO_INFO = "photo-info";
    public static final String PHOTO_FILE = "photo-file";

    public static final String USER_REGISTER = SERVER + "/user-register";

}
