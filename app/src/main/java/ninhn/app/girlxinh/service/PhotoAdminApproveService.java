package ninhn.app.girlxinh.service;

import android.os.AsyncTask;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import ninhn.app.girlxinh.constant.UrlConstant;
import ninhn.app.girlxinh.listener.TaskListener;
import static ninhn.app.girlxinh.constant.AppConstant.FLAG_PHOTO_REVIEW;

/**
 * Created by NinHN on 5/23/16.
 */
public class PhotoAdminApproveService extends AsyncTask<String, Void, Boolean> {
    private List<TaskListener> myListeners = new ArrayList<TaskListener>();

    public void addListener(TaskListener tl) {
        myListeners.add(tl);
    }

    @Override
    protected void onPostExecute(Boolean isDeleted) {
        super.onPostExecute(isDeleted);
        for (TaskListener tl : myListeners) {
            tl.onResultAvailable(FLAG_PHOTO_REVIEW, isDeleted);
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return callService(params[0], params[1], params[2], params[3]);
    }

    private Boolean callService(String userId, String photoName, String approve, String message) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate.getForObject(UrlConstant.PHOTO_REVIEW_APPROVE +
                UrlConstant.CONDITION_START + UrlConstant.CONDITION_USER_ID + userId +
                UrlConstant.CONDITION_AND + UrlConstant.CONDITION_PHOTO_NAME + photoName +
                UrlConstant.CONDITION_AND + UrlConstant.CONDITION_APPROVE + approve +
                UrlConstant.CONDITION_AND + UrlConstant.CONDITION_MESSAGE + message, Boolean.class);
    }
}
