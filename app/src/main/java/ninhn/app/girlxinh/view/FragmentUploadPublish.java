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

import com.baoyz.widget.PullRefreshLayout;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.adapter.PhotoPublishAdapter;
import ninhn.app.girlxinh.constant.UrlConstant;
import ninhn.app.girlxinh.helper.AppValue;
import ninhn.app.girlxinh.listener.HidingScrollListener;
import ninhn.app.girlxinh.listener.OnPhotoPublishItemClickListener;
import ninhn.app.girlxinh.listener.OnLoadMoreListener;
import ninhn.app.girlxinh.listener.TaskListener;
import ninhn.app.girlxinh.model.PhotoModel;
import ninhn.app.girlxinh.service.PhotoLoveUserService;
import ninhn.app.girlxinh.service.PhotoPublishUserService;
import ninhn.app.girlxinh.until.ConnectionUntil;
import ninhn.app.girlxinh.until.DialogUntil;
import ninhn.app.girlxinh.until.DownloadUntil;
import ninhn.app.girlxinh.until.ToastUntil;

import static ninhn.app.girlxinh.constant.AppConstant.ADMOB_CYCLE_SHOW;
import static ninhn.app.girlxinh.constant.AppConstant.ADMOB_INIT_POSITION;
import static ninhn.app.girlxinh.constant.AppConstant.FLAG_PAGE_MORE;
import static ninhn.app.girlxinh.constant.AppConstant.FLAG_PAGE_ONE;
import static ninhn.app.girlxinh.constant.AppConstant.FLAG_PHOTO_LOAD;
import static ninhn.app.girlxinh.constant.AppConstant.FLAG_REFRESH;

/**
 * Created by NinHN on 7/31/2016.
 */
public class FragmentUploadPublish extends Fragment implements TaskListener, OnPhotoPublishItemClickListener {

    private List<PhotoModel> photoModelList;
    private RecyclerView mRecyclerView;
    private PhotoPublishAdapter photoPublishAdapter;
    private PullRefreshLayout pullRefreshLayout;

    private List<PhotoModel> photoModelListTemp;

    private int page = 1;
    private int admobCount = ADMOB_INIT_POSITION;
    private PhotoModel admobModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.upload_publish, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initAdmobModel();
        initRecyclerView();
        getPhotoPage();
        initPullRefresh();
    }

    private void initAdmobModel() {
        admobModel = new PhotoModel();
        admobModel.setId(getString(R.string.banner_ad_unit_id));
    }

    private void addAdmobToList(List<PhotoModel> photoModels) {
        if (photoModels.size() == 30) {
            addAdmobItem(3);
        } else if (photoModels.size() >= 20) {
            addAdmobItem(2);
        } else if (photoModels.size() >= 10) {
            addAdmobItem(1);
        }
    }

    private void addAdmobItem(int number) {
        for (int i = 0; i < number; i++) {
            admobCount += ADMOB_CYCLE_SHOW;
            photoModelList.add(admobCount, admobModel);
        }
    }

    private void initRecyclerView() {
        photoModelList = new ArrayList<>();
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycleView_upload_publish);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        photoPublishAdapter = new PhotoPublishAdapter(getActivity(), mRecyclerView, photoModelList, this);
        mRecyclerView.setAdapter(photoPublishAdapter);

        photoPublishAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                photoModelList.add(null);
                photoPublishAdapter.notifyItemInserted(photoModelList.size() - 1);
                //Get more photo when scroll down to bottom
                getPhotoMore();
            }
        });

        //Setting up our OnScrollListener
        mRecyclerView.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                ((MainActivity) getActivity()).hideNavigation();
            }

            @Override
            public void onShow() {
                ((MainActivity) getActivity()).restoreNagivation();
            }
        });

    }

    private void initPullRefresh() {
        //Map component to control
        pullRefreshLayout = (PullRefreshLayout) getActivity().findViewById(R.id.pullRefreshLayout_upload_publish);

        // listen refresh event
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!ConnectionUntil.isConnection(getActivity())) {
                    ToastUntil.showShort(getActivity(), getString(R.string.network_connect_no));
                    pullRefreshLayout.setRefreshing(false);
                } else {
                    // start refresh
                    getPhotoRefresh();
                }
            }
        });
    }

    private void getPhotoMore() {
        PhotoPublishUserService photoPublishUserService = new PhotoPublishUserService(FLAG_PAGE_MORE);
        photoPublishUserService.addListener(this);
        photoPublishUserService.execute(page);
    }

    private void getPhotoPage() {
        PhotoPublishUserService photoPublishUserService = new PhotoPublishUserService(FLAG_PAGE_ONE);
        photoPublishUserService.addListener(this);
        photoPublishUserService.execute(0);
    }

    private void getPhotoRefresh() {
        PhotoPublishUserService photoPublishUserService = new PhotoPublishUserService(FLAG_REFRESH);
        photoPublishUserService.addListener(this);
        photoPublishUserService.execute(0);
    }

    private void setPhotoLove(String loveType, PhotoModel photoModel) {
        PhotoLoveUserService photoLoveUserService = new PhotoLoveUserService();
        photoLoveUserService.addListener(this);
        photoLoveUserService.execute(loveType, photoModel.getId(), AppValue.getInstance().getUserModel().getId());
    }

    private void addPhotoToList(boolean isClearList) {
        if (isClearList) {
            //Clear current photo list
            photoModelList.clear();
            //Clear admob are added before
            admobCount = ADMOB_INIT_POSITION;
        }
        //Add new photo list just loaded
        photoModelList.addAll(photoModelListTemp);
        //Add admob to show list
        addAdmobToList(photoModelListTemp);
    }

    private void showComment(PhotoModel photo) {
        Intent intent = new Intent(getActivity(), CommentActivity.class);
        intent.putExtra(CommentActivity.COMMENT_URL, UrlConstant.SOCIAL_URL + photo.getId());
        startActivity(intent);
    }

    @Override
    public void onResultAvailable(Object... objects) {
        if (FLAG_PHOTO_LOAD == (int) objects[0]) {
            photoModelListTemp = (List<PhotoModel>) objects[2];
            if (FLAG_PAGE_MORE == (int) objects[1]) {
                if (photoModelList.size() > 0) {
                    //Remove loading item
                    photoModelList.remove(photoModelList.size() - 1);
                    photoPublishAdapter.notifyItemRemoved(photoModelList.size());
                    //Add list photo had just loaded and admob to list
                    addPhotoToList(false);
                    //count page
                    page++;
                    //Hide load more progress
                    photoPublishAdapter.setLoaded();
                }
            } else if (FLAG_PAGE_ONE == (int) objects[1]) {
                //Add list photo had just loaded and admob to list
                addPhotoToList(true);
            } else {
                //Add list photo had just loaded and admob to list
                addPhotoToList(true);
                //Disable refresh control
                pullRefreshLayout.setRefreshing(false);
                //Reset page to start
                page = 1;
            }

        } else {
        }
        //Update list after change
        photoPublishAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(PhotoModel photoModel, View type) {
        switch (type.getId()) {
            case R.id.photo_item_view_footer_image_comment:
                if (ConnectionUntil.isConnection(getActivity())) {
                    showComment(photoModel);
                } else {
                    DialogUntil.showNetworkStage(getActivity(), false);
                }
                break;
            case R.id.photo_item_view_header_image_love:
                if (ConnectionUntil.isConnection(getActivity())) {
                    if (AppValue.getInstance().isLogin()) {
                        if (photoModel.getLove().contains(AppValue.getInstance().getUserModel().getId())) {
                            //Call service remove love in this photo
                            setPhotoLove(PhotoLoveUserService.LOVE_DOWN, photoModel);
                            //Handle local
                            photoModel.getLove().remove(AppValue.getInstance().getUserModel().getId());
                        } else {
                            //Call service remove love in this photo
                            setPhotoLove(PhotoLoveUserService.LOVE_UP, photoModel);
                            //Handle local
                            photoModel.getLove().add(AppValue.getInstance().getUserModel().getId());
                            //Set animation after loved
                            YoYo.with(Techniques.Swing)
                                    .duration(700)
                                    .playOn(type);
                        }
                        photoPublishAdapter.notifyDataSetChanged();
                    } else {
                        ToastUntil.showLong(getActivity(), getString(R.string.require_login_love));
                    }
                } else {
                    DialogUntil.showNetworkStage(getActivity(), false);
                }
                break;
            case R.id.photo_item_view_footer_image_download:
                if (ConnectionUntil.isConnection(getActivity())) {
                    DownloadUntil.downloadPhoto(getActivity(), photoModel);
                } else {
                    DialogUntil.showNetworkStage(getActivity(), false);
                }
                break;
            case R.id.photo_item_view_footer_button_login:
                ((MainActivity) getActivity()).changeNavigationTabTo(3);
                break;
            default:
                break;

        }
    }
}
