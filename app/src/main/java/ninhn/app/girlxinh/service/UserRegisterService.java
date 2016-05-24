package ninhn.app.girlxinh.service;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import ninhn.app.girlxinh.constant.AppConstant;
import ninhn.app.girlxinh.constant.UrlConstant;
import ninhn.app.girlxinh.listener.TaskListener;
import ninhn.app.girlxinh.model.PhotoModel;
import ninhn.app.girlxinh.model.UserModel;

/**
 * Created by NinHN on 5/23/16.
 */
public class UserRegisterService extends AsyncTask<UserModel, Void, UserModel> {

    private List<TaskListener> myListeners = new ArrayList<TaskListener>();

    public void addListener(TaskListener tl) {
        myListeners.add(tl);
    }

    @Override
    protected void onPostExecute(UserModel userModel) {
        super.onPostExecute(userModel);
        for (TaskListener tl : myListeners) {
            tl.onResultAvailable(userModel);
        }
    }

    @Override
    protected UserModel doInBackground(UserModel... params) {
        return callService(params[0]);
    }

    private UserModel callService(UserModel userModel){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate.postForObject(UrlConstant.USER_REGISTER, userModel, UserModel.class);
    }
}
