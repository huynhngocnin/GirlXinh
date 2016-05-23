package ninhn.app.girlxinh.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import org.json.JSONException;
import org.json.JSONObject;

import me.grantland.widget.AutofitHelper;
import ninhn.app.girlxinh.MyApplication;
import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.constant.AppConstant;
import ninhn.app.girlxinh.helper.MyPreferenceManager;

/**
 * Created by NinHN on 4/10/16.
 */
public class FragmentMe extends Fragment {

    private LoginButton loginButton;
    private TextView me_info;
    private TextView textName;
    private ProfilePictureView profilePictureView;
    private CallbackManager callbackManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_me, container, false);

        callbackManager = CallbackManager.Factory.create();

        me_info = (TextView) view.findViewById(R.id.me_info);
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        //loginButton.setReadPermissions("user_friends"); //"publish_actions"
        //loginButton.setReadPermissions(Arrays.asList("user_status"));
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        textName = (TextView) view.findViewById(R.id.me_cover_text_name);
        //Fix full name in single line
        AutofitHelper.create(textName);

        profilePictureView = (ProfilePictureView) view.findViewById(R.id.image_face);

        //If login  before
        if (MyApplication.getInstance().getPrefManager() != null) {
            //Set username to cover
            textName.setText(MyApplication.getInstance().getPrefManager().getUserName());
            //Set photo to cover
            profilePictureView.setProfileId(MyApplication.getInstance().getPrefManager().getUserFacebook());
        }

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
                                me_info.setText(object.toString());
                                if (object.has("name")) {

                                    try {
                                        //Set username to cover
                                        textName.setText(object.getString(AppConstant.NAME));
                                        //Set photo to cover
                                        profilePictureView.setProfileId(object.getString(AppConstant.ID));
                                        //Save user info to local
                                        MyApplication.getInstance().getPrefManager().saveUser(object.getString(AppConstant.ID), object.getString(AppConstant.NAME));
                                    } catch (JSONException e) {

                                    }


                                }

                            }
                        });
                //Request login facebook
                Bundle parameters = new Bundle();
                //Put fields wanna get value
                //parameters.putString("fields", "id,name,email,link,picture.type(normal)");
                parameters.putString("fields", "id,name,email,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Cancen roi nhe ku", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getActivity(), "Error roi! Lam lai di", Toast.LENGTH_SHORT).show();
            }
        });

        //Handle Access Token changed
        new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    Toast.makeText(getActivity(), AppConstant.LOGOUT, Toast.LENGTH_LONG).show();
                    MyApplication.getInstance().getPrefManager().clearUser();

                }
            }
        };

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
