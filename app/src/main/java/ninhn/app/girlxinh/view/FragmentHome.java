package ninhn.app.girlxinh.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.adapter.PhotoHomeAdapter;
import ninhn.app.girlxinh.constant.AppConstant;
import ninhn.app.girlxinh.helper.AppValue;
import ninhn.app.girlxinh.helper.BusProvider;
import ninhn.app.girlxinh.listener.OnItemClickListener;
import ninhn.app.girlxinh.listener.OnLoadMoreListener;
import ninhn.app.girlxinh.listener.TaskListener;
import ninhn.app.girlxinh.model.PhotoModel;
import ninhn.app.girlxinh.service.PhotoGetService;
import ninhn.app.girlxinh.service.PhotoLoveService;
import ninhn.app.girlxinh.until.DownloadUntil;
import ninhn.app.girlxinh.until.ToastUntil;

import static ninhn.app.girlxinh.constant.AppConstant.FLAG_PHOTO_LOAD;

import static ninhn.app.girlxinh.constant.AppConstant.FLAG_PAGE_MORE;
import static ninhn.app.girlxinh.constant.AppConstant.FLAG_PAGE_ONE;
import static ninhn.app.girlxinh.constant.AppConstant.FLAG_REFRESH;


/**
 * Created by NinHN on 4/10/16.
 */
public class FragmentHome extends Fragment implements OnItemClickListener, TaskListener {

    private List<PhotoModel> photoModelList;
    private RecyclerView mRecyclerView;
    private PhotoHomeAdapter photoHomeAdapter;
    private PullRefreshLayout pullRefreshLayout;

    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initRecyclerView();
        getPhotoPage();
        initPullRefresh();
    }

    @Override
    public void onItemClick(PhotoModel photoModel, View type) {
        switch (type.getId()) {
            case R.id.photo_item_footer_image_comment:
                showComment(photoModel);
                break;
            case R.id.photo_item_header_image_love:
                if (AppValue.getInstance().getUserModel().getId() != AppConstant.BLANK) {
                    if (photoModel.getLove().contains(AppValue.getInstance().getUserModel().getId())) {
                        //Call service remove love in this photo
                        setPhotoLove(PhotoLoveService.LOVE_DOWN, photoModel);
                        //Handle local
                        photoModel.getLove().remove(AppValue.getInstance().getUserModel().getId());
                    } else {
                        //Call service remove love in this photo
                        setPhotoLove(PhotoLoveService.LOVE_UP, photoModel);
                        //Handle local
                        photoModel.getLove().add(AppValue.getInstance().getUserModel().getId());
                        //Set animation after loved
                        YoYo.with(Techniques.Swing)
                                .duration(700)
                                .playOn(type);
                    }
                    photoHomeAdapter.notifyDataSetChanged();
                } else {
                    ToastUntil.showLong(getActivity(), getString(R.string.require_login_love));
                }
                break;
            case R.id.photo_item_footer_image_download:
                DownloadUntil.downloadPhoto(getActivity(), photoModel);
                break;
            case R.id.photo_item_footer_button_login:
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.changeTabTo(2);
                break;
            default:
                break;

        }
    }

    @Override
    public void onResultAvailable(Object... objects) {
        if (FLAG_PHOTO_LOAD == (int) objects[0]) {
            if (FLAG_PAGE_MORE == (int) objects[1]) {
                //Remove loading item
                photoModelList.remove(photoModelList.size() - 1);
                photoHomeAdapter.notifyItemRemoved(photoModelList.size());
                //Add list photo had just loaded
                photoModelList.addAll((List<PhotoModel>) objects[2]);
                //count page
                page++;
                //Hide load more progress
                photoHomeAdapter.setLoaded();
            } else if (FLAG_PAGE_ONE == (int) objects[1]) {
                photoModelList.addAll((List<PhotoModel>) objects[2]);
            } else {
                //Clear current photo list
                photoModelList.clear();
                //Add new photo list just loaded
                photoModelList.addAll((List<PhotoModel>) objects[2]);
                //Disable refresh control
                pullRefreshLayout.setRefreshing(false);
                //Reset page to start
                page = 1;
            }

        } else {
        }
        //Update list after change
        photoHomeAdapter.notifyDataSetChanged();
    }

    private void setPhotoLove(String loveType, PhotoModel photoModel) {
        PhotoLoveService photoLoveService = new PhotoLoveService();
        photoLoveService.addListener(this);
        photoLoveService.execute(loveType, photoModel.getId(), AppValue.getInstance().getUserModel().getId());
    }

    private void getPhotoMore() {
        PhotoGetService photoGetService = new PhotoGetService(FLAG_PAGE_MORE);
        photoGetService.addListener(this);
        photoGetService.execute(page);
    }

    private void getPhotoPage() {
        PhotoGetService photoGetService = new PhotoGetService(FLAG_PAGE_ONE);
        photoGetService.addListener(this);
        photoGetService.execute(0);
    }

    private void getPhotoRefresh() {
        PhotoGetService photoGetService = new PhotoGetService(FLAG_REFRESH);
        photoGetService.addListener(this);
        photoGetService.execute(0);
    }

    private void initRecyclerView() {
        photoModelList = new ArrayList<>();
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycleView_home);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        photoHomeAdapter = new PhotoHomeAdapter(getActivity(), mRecyclerView, photoModelList, this);
        mRecyclerView.setAdapter(photoHomeAdapter);

        photoHomeAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                photoModelList.add(null);
                photoHomeAdapter.notifyItemInserted(photoModelList.size() - 1);
                //Get more photo when scroll down to bottom
                getPhotoMore();
            }
        });

        //setting up our OnScrollListener
//        mRecyclerView.setOnScrollListener(new HidingScrollListener() {
//            @Override
//            public void onHide() {
//                hideViews();
//            }
//
//            @Override
//            public void onShow() {
//                showViews();
//            }
//        });

    }

    private void showComment(PhotoModel photo){
        Intent intent = new Intent(getActivity(), CommentActivity.class);
        intent.putExtra("url", "http://vngirl.xyz/app");
        startActivity(intent);
    }

    private void initPullRefresh() {
        //Map component to control
        pullRefreshLayout = (PullRefreshLayout) getActivity().findViewById(R.id.pullRefreshLayout_home);

        // listen refresh event
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh
                getPhotoRefresh();
            }
        });
    }

    //    private void hideViews() {
//        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
//
//        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mFabButton.getLayoutParams();
//        int fabBottomMargin = lp.bottomMargin;
//        mFabButton.animate().translationY(mFabButton.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
//    }
//
//    private void showViews() {
//        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
//        mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
//    }
}
