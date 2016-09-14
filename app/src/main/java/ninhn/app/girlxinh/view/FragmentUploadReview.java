package ninhn.app.girlxinh.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.adapter.PhotoReviewUserAdapter;
import ninhn.app.girlxinh.listener.OnLoadMoreListener;
import ninhn.app.girlxinh.listener.OnPhotoReviewItemClickListener;
import ninhn.app.girlxinh.listener.TaskListener;
import ninhn.app.girlxinh.model.PhotoReviewModel;
import ninhn.app.girlxinh.service.PhotoReviewUserService;
import ninhn.app.girlxinh.until.ConnectionUntil;
import ninhn.app.girlxinh.until.DialogUntil;
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
public class FragmentUploadReview extends Fragment implements TaskListener, OnPhotoReviewItemClickListener {

    private List<PhotoReviewModel> photoReviewModelList;
    private RecyclerView mRecyclerView;
    private PhotoReviewUserAdapter photoReviewUserAdapter;
    private PullRefreshLayout pullRefreshLayout;

    private List<PhotoReviewModel> photoReviewModelListTemp;

    private int page = 1;
    private int admobCount = ADMOB_INIT_POSITION;
    private PhotoReviewModel admobModel;
    private TextView textNoPhoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.upload_review, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initAdmobModel();
        initRecyclerView();
        getPhotoPage();
        initPullRefresh();

        textNoPhoto = (TextView) getActivity().findViewById(R.id.upload_text_no_photo);
    }

    private void initAdmobModel() {
        admobModel = new PhotoReviewModel();
        admobModel.setId(getString(R.string.banner_ad_unit_id));
    }

    private void addAdmobToList(List<PhotoReviewModel> photoModels) {
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
            photoReviewModelList.add(admobCount, admobModel);
        }
    }

    public void handleUploadSuccess(){
        getPhotoRefresh();
    }

    private void initRecyclerView() {
        photoReviewModelList = new ArrayList<>();
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycleView_upload_review);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        photoReviewUserAdapter = new PhotoReviewUserAdapter(getActivity(), mRecyclerView, photoReviewModelList, this);
        mRecyclerView.setAdapter(photoReviewUserAdapter);

        photoReviewUserAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                photoReviewModelList.add(null);
                photoReviewUserAdapter.notifyItemInserted(photoReviewModelList.size() - 1);
                //Get more photo when scroll down to bottom
                getPhotoMore();
            }
        });

//        //Setting up our OnScrollListener
//        mRecyclerView.setOnScrollListener(new HidingScrollListener() {
//            @Override
//            public void onHide() {
//                ((MainActivity) getActivity()).hideNavigation();
//            }
//
//            @Override
//            public void onShow() {
//                ((MainActivity) getActivity()).restoreNagivation();
//            }
//        });

    }

    private void initPullRefresh() {
        //Map component to control
        pullRefreshLayout = (PullRefreshLayout) getActivity().findViewById(R.id.pullRefreshLayout_upload_review);

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
        PhotoReviewUserService photoReviewUserService = new PhotoReviewUserService(FLAG_PAGE_MORE);
        photoReviewUserService.addListener(this);
        photoReviewUserService.execute(page);
    }

    private void getPhotoPage() {
        PhotoReviewUserService photoReviewUserService = new PhotoReviewUserService(FLAG_PAGE_ONE);
        photoReviewUserService.addListener(this);
        photoReviewUserService.execute(0);
    }

    private void getPhotoRefresh() {
        PhotoReviewUserService photoReviewUserService = new PhotoReviewUserService(FLAG_REFRESH);
        photoReviewUserService.addListener(this);
        photoReviewUserService.execute(0);
    }

    private void addPhotoToList(boolean isClearList) {
        if (isClearList) {
            //Clear current photo list
            photoReviewModelList.clear();
            //Clear admob are added before
            admobCount = ADMOB_INIT_POSITION;
        }
        //Add new photo list just loaded
        photoReviewModelList.addAll(photoReviewModelListTemp);
        //Add admob to show list
        addAdmobToList(photoReviewModelListTemp);
    }


    @Override
    public void onResultAvailable(Object... objects) {
        if (FLAG_PHOTO_LOAD == (int) objects[0]) {
            photoReviewModelListTemp = (List<PhotoReviewModel>) objects[2];
            if (FLAG_PAGE_MORE == (int) objects[1]) {
                if (photoReviewModelList.size() > 0) {
                    //Remove loading item
                    photoReviewModelList.remove(photoReviewModelList.size() - 1);
                    photoReviewUserAdapter.notifyItemRemoved(photoReviewModelList.size());
                    //Add list photo had just loaded and admob to list
                    addPhotoToList(false);
                    //count page
                    page++;
                    //Hide load more progress
                    photoReviewUserAdapter.setLoaded();
                }
            } else if (FLAG_PAGE_ONE == (int) objects[1]) {
                //Add list photo had just loaded and admob to list
                addPhotoToList(false);
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
        photoReviewUserAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(PhotoReviewModel photoReviewModel, View type) {
        switch (type.getId()) {
            case R.id.photo_item_view_header_image_love:
                if (ConnectionUntil.isConnection(getActivity())) {
//                    photoLoveAdapter.notifyDataSetChanged();
//                    //Call service remove love in this photo
//                    setPhotoLove(PhotoLoveUserService.LOVE_DOWN, photoModel);
//                    //Handle local
//                    photoModelList.remove(photoModel);
                    if (photoReviewModelList.size() == 0) {
                        //Show text when have photo
                        textNoPhoto.setVisibility(View.VISIBLE);
                    }
                } else {
                    DialogUntil.showNetworkStage(getActivity(), false);
                }
                break;
            default:
                break;

        }
    }
}
