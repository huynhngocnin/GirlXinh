package ninhn.app.girlxinh.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.constant.AppConstant;
import ninhn.app.girlxinh.constant.UrlConstant;
import ninhn.app.girlxinh.helper.AppValue;
import ninhn.app.girlxinh.listener.UploadListener;
import ninhn.app.girlxinh.model.PhotoModel;
import ninhn.app.girlxinh.until.ImageUntil;
import ninhn.app.girlxinh.until.ToastUntil;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PICK_PHOTO = 0;

    private EditText edtDescription;
    private ImageView imgPhoto;
    private Button btnUpload;
    private ProgressBar progressBar;
    private File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        initControl();
        pickPhoto();
    }

    private void initControl() {
        edtDescription = (EditText) findViewById(R.id.edt_upload_description);
        imgPhoto = (ImageView) findViewById(R.id.img_upload_photo);
        btnUpload = (Button) findViewById(R.id.btn_upload_submit);
        progressBar = (ProgressBar) findViewById(R.id.progress_upload_status);
        //Set event click
        imgPhoto.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_upload_submit:
                uploadPhoto();
                break;
            case R.id.img_upload_photo:
                pickPhoto();
                break;
            default:
                break;
        }
    }

    private void pickPhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_PHOTO);
    }

    private void uploadPhoto() {
        changeControlToStartUpload();
        if (imgFile.isFile()) {
            try {
                //Set photo info
                PhotoModel photoModel = new PhotoModel();
                photoModel.setDescription(edtDescription.getText().toString());
                photoModel.setUploadId(AppValue.getInstance().getUserModel().getId());
                photoModel.setUploadName(AppValue.getInstance().getUserModel().getName());
                photoModel.setUploadAvatar(AppValue.getInstance().getUserModel().getAvatar());
                //Set param to upload(photo File and photo Info)
                RequestParams requestParams = new RequestParams();
                requestParams.put(UrlConstant.PHOTO_FILE, imgFile);
                requestParams.put(UrlConstant.PHOTO_INFO, photoModel);
                ImageUntil.uploadImage(this, requestParams, new UploadListener() {
                    @Override
                    public void onPostUploaded(boolean isSuccess, String message) {
                        if (isSuccess) {
                            finish();
                        } else {
                            ToastUntil.showShort(getApplicationContext(), message);
                            changeControlToReUpload();
                        }
                    }
                });
            } catch (FileNotFoundException fnfException) {
                ToastUntil.showShort(this, fnfException.getMessage());
                changeControlToReUpload();
            }
        } else {
            ToastUntil.showShort(this, "Vui long chon anh nha ku!");
            changeControlToReUpload();
        }
    }

    private void changeControlToStartUpload() {
        progressBar.setVisibility(View.VISIBLE);
        edtDescription.setEnabled(false);
        imgPhoto.setEnabled(false);
        btnUpload.setEnabled(false);
    }

    private void changeControlToReUpload() {
        progressBar.setVisibility(View.GONE);
        edtDescription.setEnabled(true);
        imgPhoto.setEnabled(true);
        btnUpload.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    Picasso.with(this)
                            .load(imageUri)
                            .resize(AppValue.getInstance().getDeviceInfo().getWidth(), 0)
                            .error(R.drawable.upload_icon)
                            .placeholder(R.drawable.loading_animation)
                            .into(imgPhoto);
                    imgFile = new File(ImageUntil.getPathFromURI(this, imageUri));
//                    try {
//                        InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                        Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
//                    } catch (FileNotFoundException fileExeption) {
//
//                    }
                }
        }
    }
}
