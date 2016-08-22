package ninhn.app.girlxinh.service;

import android.os.AsyncTask;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import ninhn.app.girlxinh.constant.UrlConstant;
import ninhn.app.girlxinh.listener.TaskListener;
import ninhn.app.girlxinh.model.UserModel;

/**
 * Created by NinHN on 5/23/16.
 */
public class PhotoUserDeleteService extends AsyncTask<String, Void, Boolean> {

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
        return callService(params[0]);
    }

    private Boolean callService(String photoName){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate.postForObject(UrlConstant.PHOTO_USER_DELETE, photoName, Boolean.class);
    }
}
