package ninhn.app.girlxinh.service;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ninhn.app.girlxinh.constant.AppConstant;
import ninhn.app.girlxinh.constant.UrlConstant;
import ninhn.app.girlxinh.listener.TaskListener;
import ninhn.app.girlxinh.model.PhotoModel;

import static ninhn.app.girlxinh.constant.UrlConstant.CONDITION_START;
import static ninhn.app.girlxinh.constant.UrlConstant.CONDITION_USER_ID;

/**
 * Created by NinHN on 5/23/16.
 */
public class UserLoveService extends AsyncTask<String, Void, List<PhotoModel>> {

    private List<TaskListener> myListeners = new ArrayList<TaskListener>();

    public void addListener(TaskListener tl) {
        myListeners.add(tl);
    }

    @Override
    protected void onPostExecute(List<PhotoModel> photoModels) {
        super.onPostExecute(photoModels);
        for (TaskListener tl : myListeners) {
            tl.onResultAvailable(AppConstant.FLAG_PHOTO_LOAD, photoModels);
        }
    }

    @Override
    protected List<PhotoModel> doInBackground(String... params) {
        return  callService(params[0]);
    }

    private List<PhotoModel> callService(String userId) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try {
            Log.d(getClass().toString(), UrlConstant.PHOTO_USER_LOVE + CONDITION_START + CONDITION_USER_ID + userId);
            ResponseEntity<PhotoModel[]> responseEntity = restTemplate.getForEntity(UrlConstant.PHOTO_USER_LOVE + CONDITION_START + CONDITION_USER_ID + userId, PhotoModel[].class);
            PhotoModel[] photoArray = responseEntity.getBody();
            List<PhotoModel> photolist = Arrays.asList(photoArray);
            return photolist;
        } catch (Exception e) {
            Log.d(getClass().toString(), e.getMessage(), e);
        }
        return new ArrayList<>();
    }
}
