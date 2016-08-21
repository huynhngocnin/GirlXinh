package ninhn.app.girlxinh.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.holder.AdmobViewHolder;
import ninhn.app.girlxinh.holder.LoadingViewHolder;
import ninhn.app.girlxinh.holder.PhotoReviewAdminHolder;
import ninhn.app.girlxinh.listener.OnPhotoPublishItemClickListener;
import ninhn.app.girlxinh.listener.OnLoadMoreListener;
import ninhn.app.girlxinh.listener.OnPhotoReviewItemClickListener;
import ninhn.app.girlxinh.model.PhotoReviewModel;

/**
 * Created by ninhn on 4/8/2016.
 */
public class PhotoReviewAdminAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final int VIEW_TYPE_ADMOB = 2;

    private Context context;
    private List<PhotoReviewModel> photoReviewModelList;

    private OnLoadMoreListener mOnLoadMoreListener;
    private OnPhotoReviewItemClickListener listener;

    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    public PhotoReviewAdminAdapter(Context context, RecyclerView recyclerView, List<PhotoReviewModel> photoReviewModelList, OnPhotoReviewItemClickListener listener) {
        this.context = context;
        this.photoReviewModelList = photoReviewModelList;
        this.listener = listener;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemCount() {
        return photoReviewModelList == null ? 0 : photoReviewModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (photoReviewModelList.get(position) != null) {
            if (!context.getString(R.string.banner_ad_unit_id).equals(photoReviewModelList.get(position).getId())) {
                return VIEW_TYPE_ITEM;
            } else {
                return VIEW_TYPE_ADMOB;
            }
        } else {
            return VIEW_TYPE_LOADING;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(this.context).inflate(R.layout.photo_item_view, parent, false);
            return new PhotoReviewAdminHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            view = LayoutInflater.from(this.context).inflate(R.layout.loading_item, parent, false);
            return new LoadingViewHolder(view);
        } else if (viewType == VIEW_TYPE_ADMOB) {
            view = LayoutInflater.from(this.context).inflate(R.layout.admob_item, parent, false);
            return new AdmobViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PhotoReviewAdminHolder) {
            ((PhotoReviewAdminHolder) holder).bind(this.context, photoReviewModelList.get(position), this.listener);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        } else if (holder instanceof AdmobViewHolder) {
            //Dont handle
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }
}
