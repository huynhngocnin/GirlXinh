package ninhn.app.girlxinh.service;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onPostExecute(List<PhotoModel> photoModels) {
        super.onPostExecute(photoModels);
        for (TaskListener tl : myListeners) {
            tl.onResultAvailable(photoModels);
        }
    }

    @Override
    protected List<PhotoModel> doInBackground(Integer... params) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Object object = restTemplate.getForObject(new URI(UrlConstant.PHOTO_LIST_PAGE + params[0]), Object.class);
            List<PhotoModel> photolist = (List<PhotoModel>) object;
            return photolist;
        } catch (Exception e) {
            Log.e("photo-get exeption", e.getMessage(), e);
        }
        return null;
    }
}
