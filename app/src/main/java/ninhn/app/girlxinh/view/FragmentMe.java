package ninhn.app.girlxinh.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import me.grantland.widget.AutofitHelper;
import ninhn.app.girlxinh.MyApplication;
import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.constant.AppConstant;
import ninhn.app.girlxinh.constant.JSONConstant;
import ninhn.app.girlxinh.event.LoginChangedEvent;
import ninhn.app.girlxinh.helper.AppValue;
import ninhn.app.girlxinh.helper.BusProvider;
import ninhn.app.girlxinh.listener.TaskListener;
import ninhn.app.girlxinh.model.UserModel;
import ninhn.app.girlxinh.service.UserRegisterService;

/**
 * Created by NinHN on 4/10/16.
 */
public class FragmentMe extends Fragment implements TaskListener {

    private LoginButton loginButton;
    private TextView textName;
    private ProfilePictureView profilePictureView;
    private CallbackManager callbackManager;

    //private int loginStatus = AppConstant.BUS_LOGOUT;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_me, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Register callback
        callbackManager = CallbackManager.Factory.create();

        //Map control to component
        mapComponent();
        //Set profile show if login before
        setCoverInfo(AppValue.getInstance().getUserModel());
        //Handle callback after login success
        registerLogin();
        //Handle callback after logout
        handleLogout();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves so that we can provide the initial value.
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onResultAvailable(Object... objects) {
        UserModel userModel = (UserModel) objects[0];
        if (userModel != null) {
            //Save user info to local
            MyApplication.getInstance().getPrefManager().saveUser(userModel);
            //Save login to local
            MyApplication.getInstance().getPrefManager().setLogin(true);
            //Set to global
            AppValue.getInstance().setUserModel(userModel);
            //Set status login
            AppValue.getInstance().setLogin(true);
            //Set change status of Bus
            BusProvider.getInstance().post(produceLoginEvent());
        } else {
            Snackbar.make(textName, getString(R.string.login_fail), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void mapComponent() {
        textName = (TextView) getActivity().findViewById(R.id.me_cover_text_name);
        //Fix full name in single line
        AutofitHelper.create(textName);
        profilePictureView = (ProfilePictureView) getActivity().findViewById(R.id.image_face);
        loginButton = (LoginButton) getActivity().findViewById(R.id.login_button);
        // If using in a fragment
        loginButton.setFragment(this);
    }

    private void setCoverInfo(UserModel userModel) {
        //Set username to cover
        textName.setText(userModel.getName());
        //Set photo to cover
        profilePictureView.setProfileId(userModel.getFacebook());
    }

    private void clearCoverInfo() {
        //Set username to cover
        textName.setText(AppConstant.BLANK);
        //Set photo to cover
        profilePictureView.setProfileId(null);
    }

    private void registerLogin() {
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                //Handle response after login complete
                                handleLoginComplete(object);

                            }
                        });
                //Request login facebook
                Bundle parameters = new Bundle();
                //Put fields wanna get value
                //parameters.putString("fields", "id,name,email,link,picture.type(normal)");
                parameters.putString("fields", "id,name,email,birthday,picture.type(normal)");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });
    }

    private void handleLogout() {
        //Handle Access Token changed
        new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    //Clear user info in local
                    MyApplication.getInstance().getPrefManager().clearUser();
                    //Set login is false
                    MyApplication.getInstance().getPrefManager().setLogin(false);
                    //Clear cover info
                    clearCoverInfo();
                    //Set to gloval
                    AppValue.getInstance().clearUserModel();
                    //Set status login
                    AppValue.getInstance().setLogin(false);
                    //Set change status of Bus
                    BusProvider.getInstance().post(produceLoginEvent());
                }
            }
        };
    }

    private UserModel parseJSON(JSONObject object) {
        //Create User
        UserModel userModel = new UserModel();
        try {
            if (object.has(JSONConstant.NAME)) {
                userModel.setName(object.getString(JSONConstant.NAME));
            }
            if (object.has(JSONConstant.PICTURE)) {
                userModel.setAvatar(object.getJSONObject(JSONConstant.PICTURE).getJSONObject(JSONConstant.DATA).getString(JSONConstant.URL));
            }
            if (object.has(JSONConstant.BIRTHDAY)) {
                userModel.setBirthday(object.getString(JSONConstant.BIRTHDAY));
            }
            if (object.has(JSONConstant.EMAIL)) {
                userModel.setEmail(object.getString(JSONConstant.EMAIL));
            }
            if (object.has(JSONConstant.PHONE)) {
                userModel.setPhone(JSONConstant.PHONE);
            }
            if (object.has(JSONConstant.ID)) {
                userModel.setFacebook(object.getString(JSONConstant.ID));
            }
            //if (object.has(JSONConstant.FROM)) {
            userModel.setFrom(AppConstant.BLANK);
            //}
            return userModel;
        } catch (JSONException jsonException) {
            Log.d(getClass().toString(), jsonException.getMessage(), jsonException);
        }
        return null;
    }

    private void handleLoginComplete(JSONObject object) {
        //Parse json to UserModel
        UserModel userModel = parseJSON(object);
        //Check json response
        if (userModel != null) {
            //Send user info to server
            UserRegisterService userRegisterService = new UserRegisterService();
            userRegisterService.addListener(this);
            userRegisterService.execute(userModel);
            //Set profile info
            setCoverInfo(userModel);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Produce
    public LoginChangedEvent produceLoginEvent() {
        // Provide an initial value for location based on the last known position.
        return new LoginChangedEvent(AppValue.getInstance().isLogin());
    }
}
