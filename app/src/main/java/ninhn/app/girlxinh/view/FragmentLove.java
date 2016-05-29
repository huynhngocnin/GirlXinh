package ninhn.app.girlxinh.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.adapter.PhotoLoveAdapter;
import ninhn.app.girlxinh.constant.AppConstant;
import ninhn.app.girlxinh.constant.UrlConstant;
import ninhn.app.girlxinh.event.LoginChangedEvent;
import ninhn.app.girlxinh.helper.AppValue;
import ninhn.app.girlxinh.helper.BusProvider;
import ninhn.app.girlxinh.listener.OnItemClickListener;
import ninhn.app.girlxinh.listener.TaskListener;
import ninhn.app.girlxinh.model.PhotoModel;
import ninhn.app.girlxinh.service.PhotoLoveService;
import ninhn.app.girlxinh.service.UserLoveService;
import ninhn.app.girlxinh.until.ConnectionUntil;
import ninhn.app.girlxinh.until.ToastUntil;

/**
 * Created by NinHN on 4/10/16.
 */
public class FragmentLove extends Fragment implements OnItemClickListener, TaskListener {

    private List<PhotoModel> photoModelList;
    private RecyclerView mRecyclerView;
    private PhotoLoveAdapter photoLoveAdapter;
    private PullRefreshLayout pullRefreshLayout;

    private LinearLayout linearLogin;
    private Button buttonLogin;
    private TextView textNoPhoto;

    private List<PhotoModel> photoModelListTemp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_love, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initRecyclerView();
        initPullRefresh();

        linearLogin = (LinearLayout) getActivity().findViewById(R.id.love_linear_login);
        buttonLogin = (Button) getActivity().findViewById(R.id.love_button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.changeTabTo(2);
            }
        });

        textNoPhoto = (TextView) getActivity().findViewById(R.id.love_text_no_photo);

        loginDone();
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

    @Subscribe
    public void onLoginChanged(LoginChangedEvent event) {
        loginDone();
    }

    @Override
    public void onItemClick(PhotoModel photoModel, View type) {
        switch (type.getId()) {
            case R.id.photo_item_footer_image_comment:
                showComment(photoModel);
            break;
            case R.id.photo_item_header_image_love:
                photoLoveAdapter.notifyDataSetChanged();
                //Call service remove love in this photo
                setPhotoLove(PhotoLoveService.LOVE_DOWN, photoModel);
                //Handle local
                photoModelList.remove(photoModel);
                if (photoModelList.size() == 0) {
                    //Show text when have photo
                    textNoPhoto.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResultAvailable(Object... objects) {
        if (AppConstant.FLAG_PHOTO_LOAD == (int) objects[0]) {
            //Clear current photo list
            photoModelList.clear();
            photoModelListTemp = (List<PhotoModel>) objects[1];
            if (photoModelListTemp != null && photoModelListTemp.size() > 0) {
                //Add new photo list just loaded
                photoModelList.addAll(photoModelListTemp);
                //Remove text show when no photo
                textNoPhoto.setVisibility(View.GONE);
            } else {
                //Show text when have photo
                textNoPhoto.setVisibility(View.VISIBLE);
            }
            //Disable refresh control
            pullRefreshLayout.setRefreshing(false);
        } else {

        }
        //Update list after change
        photoLoveAdapter.notifyDataSetChanged();
    }

    private void loginDone() {
        if (AppValue.getInstance().getUserModel().getId() != AppConstant.BLANK) {
            getPhotoLove();
            linearLogin.setVisibility(View.GONE);
        } else {
            linearLogin.setVisibility(View.VISIBLE);
            photoModelList.clear();
            //Update list after change
            photoLoveAdapter.notifyDataSetChanged();
        }
    }

    private void initRecyclerView() {
        photoModelList = new ArrayList<>();
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycleView_love);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        photoLoveAdapter = new PhotoLoveAdapter(getActivity(), mRecyclerView, photoModelList, this);
        mRecyclerView.setAdapter(photoLoveAdapter);

    }

    private void getPhotoLove() {
        UserLoveService userLoveService = new UserLoveService();
        userLoveService.addListener(this);
        userLoveService.execute(AppValue.getInstance().getUserModel().getId());
    }

    private void setPhotoLove(String loveType, PhotoModel photoModel) {
        PhotoLoveService photoLoveService = new PhotoLoveService();
        photoLoveService.addListener(this);
        photoLoveService.execute(loveType, photoModel.getId(), AppValue.getInstance().getUserModel().getId());
    }

    private void initPullRefresh() {
        //Map component to control
        pullRefreshLayout = (PullRefreshLayout) getActivity().findViewById(R.id.pullRefreshLayout_love);

        // listen refresh event
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!ConnectionUntil.isConnection(getActivity())){
                    ToastUntil.showShort(getActivity(), getString(R.string.network_connect_no));
                    pullRefreshLayout.setRefreshing(false);
                }else {
                    // start refresh
                    getPhotoLove();
                }
            }
        });
    }

    private void showComment (PhotoModel photo){
        Intent intent = new Intent(getActivity(), CommentActivity.class);
        intent.putExtra(CommentActivity.COMMENT_URL, UrlConstant.SOCIAL_URL + photo.getId());
        startActivity(intent);
    }
}
