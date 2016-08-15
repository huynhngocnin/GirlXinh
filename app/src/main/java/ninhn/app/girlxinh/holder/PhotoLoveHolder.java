package ninhn.app.girlxinh.holder;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.helper.AppValue;
import ninhn.app.girlxinh.listener.OnItemClickListener;
import ninhn.app.girlxinh.model.PhotoModel;

import static ninhn.app.girlxinh.constant.UrlConstant.SOCIAL_URL;

/**
 * Created by NinHN on 4/7/16.
 */
public class PhotoLoveHolder extends RecyclerView.ViewHolder {

    private ImageView avatar;
    private TextView name;
    private TextView time;
    private TextView title;
    private ImageView image;
    private TextView view;
    private ImageView remove;
    private ImageView imageComment;
    private LikeView likeButton;
    private ShareButton shareButton;
    private ImageView imageDownload;

    public PhotoLoveHolder(View itemView) {
        super(itemView);
        avatar = (ImageView) itemView.findViewById(R.id.photo_item_view_header_image_avatar);
        name = (TextView) itemView.findViewById(R.id.photo_item_view_header_text_name);
        time = (TextView) itemView.findViewById(R.id.photo_item_view_header_text_time);
        title = (TextView) itemView.findViewById(R.id.photo_item_view_body_text_title);
        image = (ImageView) itemView.findViewById(R.id.photo_item_view_body_image_background);
        view = (TextView) itemView.findViewById(R.id.photo_item_view_header_text_view);
        remove = (ImageView) itemView.findViewById(R.id.photo_item_view_header_image_love);
        imageComment = (ImageView) itemView.findViewById(R.id.photo_item_view_footer_image_comment);
        likeButton = (LikeView) itemView.findViewById(R.id.fb_like_button);
        shareButton = (ShareButton) itemView.findViewById(R.id.fb_share_button);
        imageDownload = (ImageView) itemView.findViewById(R.id.photo_item_view_footer_image_download);
    }

    public void bind(final Context context, final PhotoModel photoModel, final OnItemClickListener listener) {

        Picasso.with(itemView.getContext())
                .load(photoModel.getUploadAvatar())
                .error(R.drawable.ic_bar_me)
                .placeholder(R.drawable.ic_bar_me)
                .into(avatar);

        name.setText(photoModel.getUploadName());

        CharSequence photoTime = DateUtils.getRelativeDateTimeString(context, photoModel.getCreateTime().getTime(), 0, 0, DateUtils.FORMAT_ABBREV_TIME);
        time.setText(photoTime);

        title.setText(photoModel.getDescription());

        Picasso.with(itemView.getContext())
                .load(photoModel.getUrl())
                .resize(AppValue.getInstance().getDeviceInfo().getWidth(), 0)
                .error(R.drawable.ic_body_load_error)
                .placeholder(R.drawable.loading_animation)
                .into(image);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(photoModel, remove);
            }
        });

        view.setText(NumberFormat.getNumberInstance(Locale.US).format(photoModel.getView()));

        imageComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(photoModel, imageComment);
            }
        });

        likeButton.setLikeViewStyle(LikeView.Style.BUTTON);
        likeButton.setObjectIdAndType(
                SOCIAL_URL + photoModel.getId(),
                LikeView.ObjectType.DEFAULT);

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(SOCIAL_URL + photoModel.getId()))
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
