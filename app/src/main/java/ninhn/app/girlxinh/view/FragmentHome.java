package ninhn.app.girlxinh.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.adapter.PhotoAdapter;
import ninhn.app.girlxinh.listener.OnItemClickListener;
import ninhn.app.girlxinh.listener.OnLoadMoreListener;
import ninhn.app.girlxinh.listener.TaskListener;
import ninhn.app.girlxinh.model.PhotoModel;
import ninhn.app.girlxinh.service.PhotoGetService;
import ninhn.app.girlxinh.until.DownloadUntil;

import  static ninhn.app.girlxinh.constant.AppConstant.FLAG_PAGE_MORE;
import  static ninhn.app.girlxinh.constant.AppConstant.FLAG_PAGE_ONE;
import  static ninhn.app.girlxinh.constant.AppConstant.FLAG_REFRESH;

/**
 * Created by NinHN on 4/10/16.
 */
public class FragmentHome extends Fragment implements OnItemClickListener, TaskListener {

    private List<PhotoModel> photoModelList;
    private RecyclerView mRecyclerView;
    private PhotoAdapter photoAdapter;
    private PullRefreshLayout pullRefreshLayout;

    private int page = 1;

    //private CallbackManager callbackManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.content_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initRecyclerView();
        getPhotoPage();
        initPullRefresh();
        //callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onItemClick(PhotoModel photoModel, View type) {
        switch (type.getId()) {
            case R.id.photo_item_body_image_background:
                Toast.makeText(getContext(), "photo_item_image_background Clicked", Toast.LENGTH_SHORT).show();
                pullRefreshLayout.setRefreshing(false);

                break;
            case R.id.photo_item_footer_image_comment:
                Toast.makeText(getContext(), "photo_item_image_comment Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.photo_item_header_image_love:
                Toast.makeText(getContext(), "photo_item_image_love Clicked", Toast.LENGTH_SHORT).show();
//                if (photoModel.isLove()) {
//                    photoModel.setLove(false);
//                } else {
//                    photoModel.setLove(true);
//                    YoYo.with(Techniques.Swing)
//                            .duration(700)
//                            .playOn(type);
//                }
                photoAdapter.notifyDataSetChanged();
                break;
            case R.id.photo_item_footer_image_download:
                DownloadUntil.downloadPhoto(getActivity(), photoModel);
                break;
            default:
                Toast.makeText(getContext(), "default Clicked", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    public void onResultAvailable(Object... objects) {
        if(FLAG_PAGE_MORE == (int) objects[0]){
            photoModelList.addAll((List<PhotoModel>) objects[1]);
            //Remove loading item
            photoModelList.remove(photoModelList.size() - 1);
            photoAdapter.notifyItemRemoved(photoModelList.size());
            //Hide load more progress
            photoAdapter.setLoaded();
            page++;
        }else  if(FLAG_PAGE_ONE == (int)objects[0]){
            photoModelList.addAll((List<PhotoModel>) objects[1]);
        }else{
            photoModelList.clear();
            photoModelList.addAll((List<PhotoModel>) objects[1]);
            pullRefreshLayout.setRefreshing(false);
            page = 1;
        }
        photoAdapter.notifyDataSetChanged();
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
        photoAdapter = new PhotoAdapter(getActivity(), mRecyclerView, photoModelList, this);
        mRecyclerView.setAdapter(photoAdapter);

        photoAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.e("NinHN", "Load More");
                photoModelList.add(null);
                photoAdapter.notifyItemInserted(photoModelList.size() - 1);



                getPhotoMore();

//                //Load more data for reyclerview
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.e("NinHN", "Load More 2");
//
//                        //Remove loading item
//                        photoModelList.remove(photoModelList.size() - 1);
//                        photoAdapter.notifyItemRemoved(photoModelList.size());
//
//                        //Load data
//                        int index = photoModelList.size();
//                        int end = index + 10;
//                        for (int i = index; i < end; i++) {
//                            PhotoModel photo = new PhotoModel();
//                            photo.setTitle("Title " + i);
//                            photo.setUrl("http://thuvienanhdep.net/wp-content/uploads/2015/09/nhung-hinh-nen-girl-xinh-va-dang-yeu-nhat-cho-de-yeu-cua-ban-nhe-14.jpg");
//                            photo.setWebUrl("http://thuvienanhdep.net/wp-content/uploads/2015/09/nhung-hinh-nen-girl-xinh-va-dang-yeu-nhat-cho-de-yeu-cua-ban-nhe-14.jpg");
//                            photo.setView(i);
//                            photo.setLike(i);
//                            photo.setComment(i);
//                            photo.setShare(i);
//                            photoModelList.add(photo);
//                        }
//                        photoAdapter.notifyDataSetChanged();
//                        photoAdapter.setLoaded();
//                    }
//                }, 5000);
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

    private void initPullRefresh() {
        pullRefreshLayout = (PullRefreshLayout) getActivity().findViewById(R.id.pullRefreshLayout);

        // listen refresh event
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh
                getPhotoRefresh();
            }
        });

        // refresh complete
        //pullRefreshLayout.setRefreshing(false);
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
