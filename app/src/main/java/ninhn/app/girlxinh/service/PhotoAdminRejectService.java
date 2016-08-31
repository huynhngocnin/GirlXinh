package ninhn.app.girlxinh.service;

import android.os.AsyncTask;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import ninhn.app.girlxinh.constant.UrlConstant;
import ninhn.app.girlxinh.listener.TaskListener;

/**
 * Created by NinHN on 5/23/16.
 */
public class PhotoAdminRejectService extends AsyncTask<String, Void, Boolean> {

    private List<TaskListener> myListeners = new ArrayList<TaskListener>();

    public void addListener(TaskListener tl) {
        myListeners.add(tl);
    }

    @Override
    protected void onPostExecute(Boolean isDeleted) {
        super.onPostExecute(isDeleted);
        for (TaskListener tl : myListeners) {
            tl.onResultAvailable(isDeleted);
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return callService(params[0], params[1]);
    }

    private Boolean callService(String userId, String photoId) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate.getForObject(UrlConstant.PHOTO_REVIEW_APPROVE +
                UrlConstant.CONDITION_START + UrlConstant.CONDITION_USER_ID + userId +
                UrlConstant.CONDITION_AND + UrlConstant.CONDITION_PHOTO_ID + photoId, Boolean.class);
    }
}
