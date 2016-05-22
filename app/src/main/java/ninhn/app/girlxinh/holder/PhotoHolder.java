package ninhn.app.girlxinh.holder;

import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookException;
import com.facebook.share.internal.LikeButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;
import com.squareup.picasso.Picasso;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.helper.AppValue;
import ninhn.app.girlxinh.listener.OnItemClickListener;
import ninhn.app.girlxinh.model.PhotoModel;
import ninhn.app.girlxinh.view.FragmentHome;

/**
 * Created by NinHN on 4/7/16.
 */
public class PhotoHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public ImageView image;
    public TextView view;
    public ImageView love;
    //public TextView like;
    //public TextView comment;
    //public TextView share;
    //public ImageView imageLike;
    public ImageView imageComment;
    //public ImageView imageShare;

    public LikeView likeButton;
    public ShareButton shareButton;

    public ImageView imageDownload;

    public PhotoHolder(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.photo_item_body_text_title);
        image = (ImageView) itemView.findViewById(R.id.photo_item_body_image_background);
        view = (TextView) itemView.findViewById(R.id.photo_item_header_text_view);
        love = (ImageView) itemView.findViewById(R.id.photo_item_header_image_love);
        //like = (TextView) itemView.findViewById(R.id.photo_item_footer_text_like_total);
        //comment = (TextView) itemView.findViewById(R.id.photo_item_footer_text_comment_total);
        //share = (TextView) itemView.findViewById(R.id.photo_item_footer_text_share_total);
        //imageLike = (ImageView) itemView.findViewById(R.id.photo_item_footer_image_like);
        imageComment = (ImageView) itemView.findViewById(R.id.photo_item_footer_image_comment);
        //imageShare = (ImageView) itemView.findViewById(R.id.photo_item_footer_image_share);

        likeButton = (LikeView) itemView.findViewById(R.id.fb_like_button);
        shareButton = (ShareButton) itemView.findViewById(R.id.fb_share_button);

        imageDownload = (ImageView) itemView.findViewById(R.id.photo_item_footer_image_download);

    }

    public void bind(final PhotoModel photoModel, final OnItemClickListener listener) {
        title.setText(photoModel.getTitle());
        Picasso.with(itemView.getContext())
                .load(photoModel.getUrl())
                .resize(AppValue.getInstance().getDeviceInfo().getWidth(), 0)
                .error(R.drawable.ic_body_load_error)
                .placeholder(R.drawable.loading_animation)
                .into(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(photoModel, image);
            }
        });
        if (photoModel.isLove()) {
            Picasso.with(itemView.getContext()).load(R.drawable.ic_header_loved).into(love);
        } else {
            Picasso.with(itemView.getContext()).load(R.drawable.ic_header_love).into(love);
        }
        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(photoModel, love);
            }
        });
        view.setText(String.valueOf(photoModel.getView()));
        //like.setText(String.valueOf(photoModel.getLike()));
        //comment.setText(String.valueOf(photoModel.getComment()));
        //share.setText(String.valueOf(photoModel.getShare()));
//        imageLike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onItemClick(photoModel, imageLike);
//            }
//        });
        imageComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(photoModel, imageComment);
            }
        });

//        imageShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onItemClick(photoModel, imageShare);
//            }
//        });
//        if (photoModel.isLike()) {
//            Picasso.with(itemView.getContext()).load(R.drawable.ic_footer_liked).into(imageLike);
//        } else {
//            Picasso.with(itemView.getContext()).load(R.drawable.ic_footer_like).into(imageLike);
//        }


        likeButton.setLikeViewStyle(LikeView.Style.BUTTON);
        likeButton.setObjectIdAndType(
                photoModel.getWebUrl(),
                LikeView.ObjectType.DEFAULT);
//        likeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("NinHN","in Like listener");
//                listener.onItemClick(photoModel, likeButton);
//            }
//        });


        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(photoModel.getWebUrl()))
                .build();
        shareButton.setShareContent(content);

//        shareButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("NinHN","in Share listener");
//                listener.onItemClick(photoModel, shareButton);
//            }
//        });
        imageDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(photoModel, imageDownload);
            }
        });

    }
}
