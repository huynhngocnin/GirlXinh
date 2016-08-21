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
import ninhn.app.girlxinh.helper.AppValue;
import ninhn.app.girlxinh.listener.TaskListener;
import ninhn.app.girlxinh.model.PhotoModel;

/**
 * Created by NinHN on 08/21/16.
 */
public class PhotoPublishUserService extends AsyncTask<Integer, Void, List<PhotoModel>> {

    private List<TaskListener> myListeners = new ArrayList<TaskListener>();

    public void addListener(TaskListener tl) {
        myListeners.add(tl);
    }

    private int type;

    public PhotoPublishUserService(int type) {
        this.type = type;
    }

    @Override
    protected void onPostExecute(List<PhotoModel> photoModels) {
        super.onPostExecute(photoModels);
        for (TaskListener tl : myListeners) {
            if (photoModels != null) {
                tl.onResultAvailable(AppConstant.FLAG_PHOTO_LOAD, this.type, photoModels);
            } else {
                tl.onResultAvailable(AppConstant.FLAG_PHOTO_LOAD, this.type, AppConstant.ARRAY_EMPTY);
            }
        }
    }

    @Override
    protected List<PhotoModel> doInBackground(Integer... params) {
        return callService(AppValue.getInstance().getUserModel().getId(), params[0]);
    }

    private List<PhotoModel> callService(String userId, int page) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try {
            ResponseEntity<PhotoModel[]> responseEntity = restTemplate.getForEntity(UrlConstant.PHOTO_PUBLISH_USER +
                    UrlConstant.CONDITION_START + UrlConstant.CONDITION_USER_ID + userId +
                    UrlConstant.CONDITION_AND + UrlConstant.CONDITION_PAGE + page, PhotoModel[].class);
            PhotoModel[] photoArray = responseEntity.getBody();
            List<PhotoModel> photolist = Arrays.asList(photoArray);
            return photolist;
        } catch (Exception e) {
            Log.d(getClass().toString(), e.getMessage(), e);
        }
        return AppConstant.ARRAY_EMPTY;
    }
}
