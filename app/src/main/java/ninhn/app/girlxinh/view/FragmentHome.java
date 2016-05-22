package ninhn.app.girlxinh.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.CallbackManager;
import com.facebook.share.widget.LikeView;
import com.squareup.picasso.Picasso;

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

/**
 * Created by NinHN on 4/10/16.
 */
public class FragmentHome extends Fragment implements OnItemClickListener, TaskListener {

    private List<PhotoModel> photoModelList;
    private RecyclerView mRecyclerView;
    private PhotoAdapter photoAdapter;
    private PullRefreshLayout pullRefreshLayout;

    private CallbackManager callbackManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.content_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        initPhoto();
        initRecyclerView();
        initPullRefresh();
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onItemClick(PhotoModel photoModel, View type) {
        switch (type.getId()) {
            case R.id.photo_item_body_image_background:
                Toast.makeText(getContext(), "photo_item_image_background Clicked", Toast.LENGTH_SHORT).show();
                pullRefreshLayout.setRefreshing(false);

                break;
//            case R.id.photo_item_footer_image_like:
//                Toast.makeText(getContext(), "photo_item_image_like Clicked", Toast.LENGTH_SHORT).show();
//                if (photoModel.isLike()) {
//                    photoModel.setLike(false);
//                } else {
//                    photoModel.setLike(true);
//                    YoYo.with(Techniques.StandUp)
//                            .duration(700)
//                            .playOn(type);
//                }
//                photoAdapter.notifyDataSetChanged();
//                break;
//            case R.id.fb_like_button:
//                Toast.makeText(getActivity(), "Like Click", Toast.LENGTH_LONG).show();
//                break;
            case R.id.photo_item_footer_image_comment:
                Toast.makeText(getContext(), "photo_item_image_comment Clicked", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.photo_item_footer_image_share:
//                Toast.makeText(getContext(), "photo_item_image_share Clicked", Toast.LENGTH_SHORT).show();
//                break;
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
        photoModelList.addAll((List<PhotoModel>) objects[0]);
        photoAdapter.notifyDataSetChanged();
    }

    private void initPhoto() {
        photoModelList = new ArrayList<PhotoModel>();

        PhotoGetService photoGetService = new PhotoGetService();
        photoGetService.addListener(this);
        photoGetService.execute(0);


//        for (int i = 0; i < 8; i++) {
//            PhotoModel photo = new PhotoModel();
//            photo.setName("Title Bla bla bla " + i + "");
//            photo.setUrl("http://media.doisongphapluat.com/416/2015/11/21/co-gai-xinh-dep-nhuom-rang-den-gay-bao-mang-" + (i + 2) + ".jpg");
//            photo.setWebUrl("http://media.doisongphapluat.com/416/2015/11/21/co-gai-xinh-dep-nhuom-rang-den-gay-bao-mang-" + (i + 2) + ".jpg");
//            photo.setView(i);
//            photo.setLike(i);
//            photo.setComment(i);
//            photo.setShare(i);
//            photoModelList.add(photo);
//        }
    }

    private void initRecyclerView() {
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
                Toast.makeText(getContext(), "Bat dau refresh!", Toast.LENGTH_SHORT).show();
            }
        });

        // refresh complete
        //pullRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getActivity(), "Data: " + requestCode + " - " + resultCode + " - " + data.toString(), Toast.LENGTH_LONG).show();
        //callbackManager.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            if ("com.facebook.platform.action.request.LIKE_DIALOG".equals(data.getStringExtra("com.facebook.platform.protocol.PROTOCOL_ACTION"))) {
                Bundle bundle = data.getExtras().getBundle("com.facebook.platform.protocol.RESULT_ARGS");
                if (bundle != null) {
                    // Here your get the like result
                    Log.d("NinHN", "Result: " + bundle.getBoolean("object_is_liked"));
                    callbackManager.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
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
