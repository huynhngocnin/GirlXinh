package ninhn.app.girlxinh.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ninhn.app.girlxinh.R;

/**
 * Created by NinHN on 4/7/16.
 */
public class PhotoHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public ImageView image;
    public TextView view;
    public ImageView bookmark;
    public TextView like;
    public TextView comment;
    public TextView share;

    public PhotoHolder(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.photo_item_text_title);
        image = (ImageView) itemView.findViewById(R.id.photo_item_image_background);
        view = (TextView) itemView.findViewById(R.id.photo_item_text_view_total);
        bookmark = (ImageView) itemView.findViewById(R.id.photo_item_image_bookmark);
        like = (TextView) itemView.findViewById(R.id.photo_item_text_like_total);
        comment = (TextView) itemView.findViewById(R.id.photo_item_text_comment_total);
        share = (TextView) itemView.findViewById(R.id.photo_item_text_share_total);
    }

}
