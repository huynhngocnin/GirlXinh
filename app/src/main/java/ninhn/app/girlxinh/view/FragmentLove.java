package ninhn.app.girlxinh.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.adapter.PhotoAdapter;
import ninhn.app.girlxinh.listener.OnItemClickListener;
import ninhn.app.girlxinh.listener.OnLoadMoreListener;
import ninhn.app.girlxinh.model.PhotoModel;

/**
 * Created by NinHN on 4/10/16.
 */
public class FragmentLove extends Fragment implements OnItemClickListener {

    private List<PhotoModel> photoModelList;
    private RecyclerView mRecyclerView;
    private PhotoAdapter photoAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.content_love, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        initPhoto();
        initRecyclerView();

    }

    @Override
    public void onItemClick(PhotoModel photoModel, View type) {
        switch (type.getId()) {
            case R.id.photo_item_body_image_background:
                Toast.makeText(getContext(), "photo_item_image_background Clicked", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.photo_item_footer_image_like:
//                Toast.makeText(getContext(), "photo_item_image_like Clicked", Toast.LENGTH_SHORT).show();
//
//                break;
            case R.id.photo_item_footer_image_comment:
                Toast.makeText(getContext(), "photo_item_image_comment Clicked", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.photo_item_footer_image_share:
//                Toast.makeText(getContext(), "photo_item_image_share Clicked", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.photo_item_header_image_love:
                Toast.makeText(getContext(), "photo_item_image_love Clicked", Toast.LENGTH_SHORT).show();
                if (photoModel.isLove()) {
                    photoModel.setLove(false);
                } else {
                    photoModel.setLove(true);
                }
                photoAdapter.notifyDataSetChanged();
                break;
            default:
                Toast.makeText(getContext(), "default Clicked", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void initPhoto(){
        photoModelList = new ArrayList<PhotoModel>();
        for (int i = 0; i < 10; i++) {
            PhotoModel photo = new PhotoModel();
            photo.setTitle("Title Bla bla bla " + i + "  This is my message from NinHN to everyGuy Cancel to lick here below in the map! - This is my message from NinHN to everyGuy");
            photo.setUrl("http://media.doisongphapluat.com/416/2015/11/21/co-gai-xinh-dep-nhuom-rang-den-gay-bao-mang-8.jpg");
            photo.setWebUrl("http://media.doisongphapluat.com/416/2015/11/21/co-gai-xinh-dep-nhuom-rang-den-gay-bao-mang-" + (i+2) + ".jpg");
            photo.setView(i);
            photo.setLike(i);
            photo.setComment(i);
            photo.setShare(i);
            photoModelList.add(photo);
        }
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recycleView_love);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        photoAdapter = new PhotoAdapter(getActivity(), mRecyclerView, photoModelList, this);
        mRecyclerView.setAdapter(photoAdapter);

        photoAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.e("NinHN", "Load More");
                photoModelList.add(null);
                photoAdapter.notifyItemInserted(photoModelList.size() - 1);

                //Load more data for reyclerview
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("NinHN", "Load More 2");

                        //Remove loading item
                        photoModelList.remove(photoModelList.size() - 1);
                        photoAdapter.notifyItemRemoved(photoModelList.size());

                        //Load data
                        int index = photoModelList.size();
                        int end = index + 10;
                        for (int i = index; i < end; i++) {
                            PhotoModel photo = new PhotoModel();
                            photo.setTitle("Title " + i);
                            photo.setUrl("http://thuvienanhdep.net/wp-content/uploads/2015/09/nhung-hinh-nen-girl-xinh-va-dang-yeu-nhat-cho-de-yeu-cua-ban-nhe-14.jpg");
                            photo.setWebUrl("http://thuvienanhdep.net/wp-content/uploads/2015/09/nhung-hinh-nen-girl-xinh-va-dang-yeu-nhat-cho-de-yeu-cua-ban-nhe-14.jpg");
                            photo.setView(i);
                            photo.setLike(i);
                            photo.setComment(i);
                            photo.setShare(i);
                            photoModelList.add(photo);
                        }
                        photoAdapter.notifyDataSetChanged();
                        photoAdapter.setLoaded();
                    }
                }, 5000);
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
