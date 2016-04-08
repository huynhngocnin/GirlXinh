package ninhn.app.girlxinh.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.adapter.PhotoAdapter;
import ninhn.app.girlxinh.listener.HidingScrollListener;
import ninhn.app.girlxinh.listener.OnItemClickListener;
import ninhn.app.girlxinh.listener.OnLoadMoreListener;
import ninhn.app.girlxinh.model.PhotoModel;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private List<PhotoModel> photoModelList;
    private RecyclerView mRecyclerView;
    private PhotoAdapter photoAdapter;

    private Toolbar mToolbar;
    private FloatingActionButton mFabButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Scroll and ...");
        setSupportActionBar(mToolbar);

        mFabButton = (FloatingActionButton) findViewById(R.id.fab);
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        photoModelList = new ArrayList<PhotoModel>();
        for (int i = 0; i < 10; i++) {
            PhotoModel photo = new PhotoModel();
            photo.setTitle("Title Bla bla bla " + i + "Cancel to lick here below in the map!");
            photo.setUrl("http://media.doisongphapluat.com/416/2015/11/21/co-gai-xinh-dep-nhuom-rang-den-gay-bao-mang-8.jpg");
            photo.setView(i);
            photo.setLike(i);
            photo.setComment(i);
            photo.setShare(i);
            photoModelList.add(photo);
        }

        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        photoAdapter = new PhotoAdapter(this, mRecyclerView, photoModelList, this);
        mRecyclerView.setAdapter(photoAdapter);

        photoAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.e("haint", "Load More");
                photoModelList.add(null);
                photoAdapter.notifyItemInserted(photoModelList.size() - 1);

                //Load more data for reyclerview
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("haint", "Load More 2");

                        //Remove loading item
                        photoModelList.remove(photoModelList.size() - 1);
                        photoAdapter.notifyItemRemoved(photoModelList.size());

                        //Load data
                        int index = photoModelList.size();
                        int end = index + 10;
                        for (int i = index; i < end; i++) {
                            PhotoModel photo = new PhotoModel();
                            photo.setTitle("Title " + i);
                            photo.setUrl("http://img-us.24hstatic.com/upload/1-2016/images/2016-02-20/1455969882-1455968466-em-gai-nha-phuong-16.jpg");
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
        mRecyclerView.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });

    }


    @Override
    public void onItemClick(PhotoModel photoModel, int type) {
        switch (type) {
            case R.id.photo_item_image_background:
                Toast.makeText(this, "photo_item_image_background Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.photo_item_image_like:
                Toast.makeText(this, "photo_item_image_like Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.photo_item_image_comment:
                Toast.makeText(this, "photo_item_image_comment Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.photo_item_image_share:
                Toast.makeText(this, "photo_item_image_share Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.photo_item_image_bookmark:
                Toast.makeText(this, "photo_item_image_bookmark Clicked", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "default Clicked", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void hideViews() {
        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mFabButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        mFabButton.animate().translationY(mFabButton.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
