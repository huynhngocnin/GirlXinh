package ninhn.app.girlxinh.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.listener.OnItemClickListener;
import ninhn.app.girlxinh.model.PhotoModel;

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
    public ImageView imageLike;
    public ImageView imageComment;
    public ImageView imageShare;

    public PhotoHolder(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.photo_item_text_title);
        image = (ImageView) itemView.findViewById(R.id.photo_item_image_background);
        view = (TextView) itemView.findViewById(R.id.photo_item_text_view_total);
        bookmark = (ImageView) itemView.findViewById(R.id.photo_item_image_bookmark);
        like = (TextView) itemView.findViewById(R.id.photo_item_text_like_total);
        comment = (TextView) itemView.findViewById(R.id.photo_item_text_comment_total);
        share = (TextView) itemView.findViewById(R.id.photo_item_text_share_total);
        imageLike = (ImageView) itemView.findViewById(R.id.photo_item_image_like);
        imageComment = (ImageView) itemView.findViewById(R.id.photo_item_image_comment);
        imageShare = (ImageView) itemView.findViewById(R.id.photo_item_image_share);
    }

    public void bind(final PhotoModel photoModel, final OnItemClickListener listener) {
        title.setText(photoModel.getTitle());
        Picasso.with(itemView.getContext()).load("http://img-us.24hstatic.com/upload/1-2016/images/2016-02-20/1455969882-1455968466-em-gai-nha-phuong-16.jpg").into(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(photoModel, R.id.photo_item_image_background);
            }
        });
        Picasso.with(itemView.getContext()).load(R.drawable.ic_bookmark_off).into(bookmark);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(photoModel, R.id.photo_item_image_bookmark);
            }
        });
        view.setText(String.valueOf(photoModel.getView()));
        like.setText(String.valueOf(photoModel.getLike()));
        comment.setText(String.valueOf(photoModel.getComment()));
        share.setText(String.valueOf(photoModel.getShare()));
        imageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(photoModel, R.id.photo_item_image_like);
            }
        });
        imageComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(photoModel, R.id.photo_item_image_comment);
            }
        });

        imageShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(photoModel, R.id.photo_item_image_share);
            }
        });

    }
}
