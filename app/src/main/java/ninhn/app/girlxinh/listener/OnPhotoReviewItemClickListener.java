package ninhn.app.girlxinh.listener;

import android.view.View;

import ninhn.app.girlxinh.model.PhotoModel;
import ninhn.app.girlxinh.model.PhotoReviewModel;

/**
 * Created by NinHN on 4/8/16.
 */
public interface OnPhotoReviewItemClickListener {
    void onItemClick(PhotoReviewModel photoReviewModel, View type);
}
