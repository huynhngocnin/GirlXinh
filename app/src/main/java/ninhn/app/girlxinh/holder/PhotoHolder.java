package ninhn.app.girlxinh.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
    public ImageView love;
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
        love = (ImageView) itemView.findViewById(R.id.photo_item_image_love);
        like = (TextView) itemView.findViewById(R.id.photo_item_text_like_total);
        comment = (TextView) itemView.findViewById(R.id.photo_item_text_comment_total);
        share = (TextView) itemView.findViewById(R.id.photo_item_text_share_total);
        imageLike = (ImageView) itemView.findViewById(R.id.photo_item_image_like);
        imageComment = (ImageView) itemView.findViewById(R.id.photo_item_image_comment);
        imageShare = (ImageView) itemView.findViewById(R.id.photo_item_image_share);
    }

    public void bind(final PhotoModel photoModel, final OnItemClickListener listener) {
        title.setText(photoModel.getTitle());
        Picasso.with(itemView.getContext()).load(photoModel.getUrl()).into(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(photoModel, image);
            }
        });
        if (photoModel.isLove()) {
            Picasso.with(itemView.getContext()).load(R.drawable.ic_photo_loved).into(love);
        } else {
            Picasso.with(itemView.getContext()).load(R.drawable.ic_photo_love).into(love);
        }
        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(photoModel, love);
            }
        });
        view.setText(String.valueOf(photoModel.getView()));
        like.setText(String.valueOf(photoModel.getLike()));
        comment.setText(String.valueOf(photoModel.getComment()));
        share.setText(String.valueOf(photoModel.getShare()));
        imageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(photoModel, imageLike);
            }
        });
        imageComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(photoModel, imageComment);
            }
        });

        imageShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(photoModel, imageShare);
            }
        });
        if (photoModel.isLike()) {
            Picasso.with(itemView.getContext()).load(R.drawable.ic_post_liked).into(imageLike);
        } else {
            Picasso.with(itemView.getContext()).load(R.drawable.ic_post_like).into(imageLike);
        }
    }
}
