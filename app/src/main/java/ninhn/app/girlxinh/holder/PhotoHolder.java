package ninhn.app.girlxinh.holder;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;
import com.squareup.picasso.Picasso;

import ninhn.app.girlxinh.MyApplication;
import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.helper.AppValue;
import ninhn.app.girlxinh.listener.OnItemClickListener;
import ninhn.app.girlxinh.model.PhotoModel;

import static ninhn.app.girlxinh.constant.UrlConstant.BASE_URL;
import static ninhn.app.girlxinh.constant.UrlConstant.PHOTO_GET;

/**
 * Created by NinHN on 4/7/16.
 */
public class PhotoHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public ImageView image;
    public TextView view;
    public ImageView love;
    public ImageView imageComment;
    public LikeView likeButton;
    public ShareButton shareButton;
    public ImageView imageDownload;

    public PhotoHolder(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.photo_item_body_text_title);
        image = (ImageView) itemView.findViewById(R.id.photo_item_body_image_background);
        view = (TextView) itemView.findViewById(R.id.photo_item_header_text_view);
        love = (ImageView) itemView.findViewById(R.id.photo_item_header_image_love);
        imageComment = (ImageView) itemView.findViewById(R.id.photo_item_footer_image_comment);
        likeButton = (LikeView) itemView.findViewById(R.id.fb_like_button);
        shareButton = (ShareButton) itemView.findViewById(R.id.fb_share_button);
        imageDownload = (ImageView) itemView.findViewById(R.id.photo_item_footer_image_download);

    }

    public void bind(final PhotoModel photoModel, final OnItemClickListener listener) {
        title.setText(photoModel.getDescription());
        Picasso.with(itemView.getContext())
                .load(photoModel.getUrl())
                .resize(AppValue.getInstance().getDeviceInfo().getWidth(), 0)
                .error(R.drawable.ic_body_load_error)
                .placeholder(R.drawable.loading_animation)
                .into(image);

        if (MyApplication.getInstance().getPrefManager() != null && photoModel.getLove().contains(MyApplication.getInstance().getPrefManager().getUserId())) {
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
        imageComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(photoModel, imageComment);
            }
        });


        likeButton.setLikeViewStyle(LikeView.Style.BUTTON);
        likeButton.setObjectIdAndType(
                BASE_URL + PHOTO_GET + photoModel.getId(),
                LikeView.ObjectType.DEFAULT);


        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(BASE_URL + PHOTO_GET + photoModel.getId()))
                .build();
        shareButton.setShareContent(content);

        imageDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(photoModel, imageDownload);
            }
        });

    }
}
