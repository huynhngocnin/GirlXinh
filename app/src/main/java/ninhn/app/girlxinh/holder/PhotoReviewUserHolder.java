package ninhn.app.girlxinh.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.helper.AppValue;
import ninhn.app.girlxinh.listener.OnPhotoReviewItemClickListener;
import ninhn.app.girlxinh.model.PhotoReviewModel;

/**
 * Created by NinHN on 4/7/16.
 */
public class PhotoReviewUserHolder extends RecyclerView.ViewHolder {

    private ImageView avatar;
    private TextView name;
    private TextView time;
    private TextView title;
    private ImageView image;
    private ImageView remove;
    private TextView message;

    public PhotoReviewUserHolder(View itemView) {
        super(itemView);
        avatar = (ImageView) itemView.findViewById(R.id.photo_item_review_user_header_image_avatar);
        name = (TextView) itemView.findViewById(R.id.photo_item_review_user_header_text_name);
        time = (TextView) itemView.findViewById(R.id.photo_item_review_user_header_text_time);
        title = (TextView) itemView.findViewById(R.id.photo_item_review_user_body_text_title);
        image = (ImageView) itemView.findViewById(R.id.photo_item_review_user_body_image_background);
        remove = (ImageView) itemView.findViewById(R.id.photo_item_review_user_header_image_remove);
        message = (TextView) itemView.findViewById(R.id.photo_item_review_user_body_text_feedback);
    }

    public void bind(final Context context, final PhotoReviewModel photoReviewModel, final OnPhotoReviewItemClickListener listener) {

        Picasso.with(itemView.getContext())
                .load(photoReviewModel.getUploadAvatar())
                .error(R.drawable.ic_bar_me)
                .placeholder(R.drawable.ic_bar_me)
                .into(avatar);

        name.setText(photoReviewModel.getUploadName());

        CharSequence photoTime = DateUtils.getRelativeDateTimeString(context, photoReviewModel.getCreateTime().getTime(), 0, 0, DateUtils.FORMAT_ABBREV_TIME);
        time.setText(photoTime);

        title.setText(photoReviewModel.getDescription());

        Picasso.with(itemView.getContext())
                .load(photoReviewModel.getUrl())
                .resize(AppValue.getInstance().getDeviceInfo().getWidth(), 0)
                .error(R.drawable.ic_body_load_error)
                .placeholder(R.drawable.loading_animation)
                .into(image);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(photoReviewModel, remove);
            }
        });

        message.setText(photoReviewModel.getMessage());

    }
}
