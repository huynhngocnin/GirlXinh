package ninhn.app.girlxinh.service;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import ninhn.app.girlxinh.constant.AppConstant;
import ninhn.app.girlxinh.listener.TaskListener;
import ninhn.app.girlxinh.model.PhotoModel;

import static ninhn.app.girlxinh.constant.UrlConstant.PHOTO_LOVE_UP;
import static ninhn.app.girlxinh.constant.UrlConstant.PHOTO_LOVE_DOWN;
import static ninhn.app.girlxinh.constant.UrlConstant.CONDITION_START;
import static ninhn.app.girlxinh.constant.UrlConstant.CONDITION_AND;
import static ninhn.app.girlxinh.constant.UrlConstant.CONDITION_PHOTO_ID;
import static ninhn.app.girlxinh.constant.UrlConstant.CONDITION_USER_ID;

/**
 * Created by NinHN on 5/23/16.
 */
public class PhotoLoveUserService extends AsyncTask<String, Void, Boolean> {

    public static final String LOVE_UP = "1";
    public static final String LOVE_DOWN = "0";

    private List<TaskListener> myListeners = new ArrayList<TaskListener>();

    public void addListener(TaskListener tl) {
        myListeners.add(tl);
    }

    @Override
    protected void onPostExecute(Boolean mBoolean) {
        super.onPostExecute(mBoolean);
        for (TaskListener tl : myListeners) {
            tl.onResultAvailable(AppConstant.FLAG_PHOTO_LOVE, mBoolean);
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if (LOVE_UP.equals(params[0])) {
            return callService(PHOTO_LOVE_UP, params[1], params[2]);
        } else {
            return callService(PHOTO_LOVE_DOWN, params[1], params[2]);
        }
    }

    private Boolean callService(String serviceUrl, String photoId, String userId) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate.getForObject(serviceUrl + CONDITION_START + CONDITION_PHOTO_ID + photoId + CONDITION_AND + CONDITION_USER_ID + userId, Boolean.class);
    }
}
