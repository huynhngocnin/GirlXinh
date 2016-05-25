package ninhn.app.girlxinh.service;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ninhn.app.girlxinh.constant.AppConstant;
import ninhn.app.girlxinh.constant.UrlConstant;
import ninhn.app.girlxinh.listener.TaskListener;
import ninhn.app.girlxinh.model.PhotoModel;

/**
 * Created by NinHN on 5/23/16.
 */
public class PhotoGetService extends AsyncTask<Integer, Void, List<PhotoModel>> {

    private List<TaskListener> myListeners = new ArrayList<TaskListener>();

    public void addListener(TaskListener tl) {
        myListeners.add(tl);
    }

    private int type;

    public PhotoGetService(int type) {
        this.type = type;
    }

    @Override
    protected void onPostExecute(List<PhotoModel> photoModels) {
        super.onPostExecute(photoModels);
        for (TaskListener tl : myListeners) {
            if (photoModels != null) {
                tl.onResultAvailable(AppConstant.FLAG_PHOTO_LOAD, this.type, photoModels);
            } else {
                tl.onResultAvailable(AppConstant.FLAG_PHOTO_LOAD, this.type, new ArrayList<>());
            }
        }
    }

    @Override
    protected List<PhotoModel> doInBackground(Integer... params) {
        return callService(params[0]);
    }

    private List<PhotoModel> callService(int page) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try {
            ResponseEntity<PhotoModel[]> responseEntity = restTemplate.getForEntity(UrlConstant.PHOTO_LIST_PAGE + page, PhotoModel[].class);
            PhotoModel[] photoArray = responseEntity.getBody();
            List<PhotoModel> photolist = Arrays.asList(photoArray);
            return photolist;
        } catch (Exception e) {
            Log.d(getClass().toString(), e.getMessage(), e);
        }
        return null;
    }
}
