package ninhn.app.girlxinh.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.constant.UrlConstant;
import ninhn.app.girlxinh.helper.AppValue;
import ninhn.app.girlxinh.listener.UploadListener;
import ninhn.app.girlxinh.model.PhotoReviewModel;
import ninhn.app.girlxinh.until.ImageUntil;
import ninhn.app.girlxinh.until.SnackbarUtil;
import ninhn.app.girlxinh.until.ToastUntil;

import static ninhn.app.girlxinh.constant.AppConstant.PHOTO_SIZE_MIN;
import static ninhn.app.girlxinh.constant.AppConstant.FILE_SIZE_MAX;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PICK_PHOTO = 0;
    public static final String UPLOAD_OK = "uploaded";

    private EditText edtDescription;
    private ImageView imgPhoto;
    private Button btnUpload;
    private ProgressBar progressBar;
    private File imgFile;
    private File tempFile;

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
                PhotoReviewModel photoModel = new PhotoReviewModel();
                photoModel.setDescription(edtDescription.getText().toString());
                photoModel.setUploadId(AppValue.getInstance().getUserModel().getId());
                photoModel.setUploadName(AppValue.getInstance().getUserModel().getName());
                photoModel.setUploadAvatar(AppValue.getInstance().getUserModel().getAvatar());
                //Conver object to json
                ObjectMapper mapper = new ObjectMapper();
                String jsonInString;
                try {
                    jsonInString = mapper.writeValueAsString(photoModel);
                } catch (JsonProcessingException JPException) {
                    ToastUntil.showShort(this, JPException.getMessage());
                    changeControlToReUpload();
                    return;
                }
                //Set param to upload(photo File and photo Info)
                RequestParams requestParams = new RequestParams();
                requestParams.put(UrlConstant.PHOTO_FILE, imgFile);
                requestParams.put(UrlConstant.PHOTO_INFO, jsonInString);
                ImageUntil.uploadImage(this, requestParams, new UploadListener() {
                    @Override
                    public void onPostUploaded(boolean isSuccess, String message) {
                        if (isSuccess) {
                            ToastUntil.showShort(getApplicationContext(), R.string.upload_success);
                            Intent intent = getIntent();
                            intent.putExtra(UPLOAD_OK, true);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            ToastUntil.showShort(getApplicationContext(), message);
                            changeControlToReUpload();
                        }
                        if (tempFile != null && tempFile.isFile()) {
                            tempFile.delete();
                        }
                    }
                });
            } catch (FileNotFoundException fnfException) {
                ToastUntil.showShort(this, fnfException.getMessage());
                changeControlToReUpload();
            }
        } else {
            SnackbarUtil.showShort(btnUpload, R.string.please_select_photo_before);
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

    private boolean checkPhotoSize(Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        if (imageWidth >= PHOTO_SIZE_MIN || imageHeight >= PHOTO_SIZE_MIN) {
            long size = ImageUntil.getFileSize(this, uri);
            if (size >= FILE_SIZE_MAX) {
                //zip image file
                Bitmap bitmap = BitmapFactory.decodeFile(ImageUntil.getPathFromURI(this, uri), options);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 55, out);
                byte[] bytes = out.toByteArray();
                FileOutputStream stream = null;
                try {
                    stream = new FileOutputStream(ImageUntil.getPathFromURI(this, uri) + ".jpg");
                    stream.write(bytes);
                    stream.close();
                    tempFile = new File(ImageUntil.getPathFromURI(this, uri) + ".jpg");
                    imgFile = tempFile;
                    //size = imgFile.length();
                } catch (IOException ioException) {
                    imgFile = null;
                    tempFile = null;
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    if (checkPhotoSize(imageUri)) {
                        Picasso.with(this)
                                .load(imageUri)
                                .resize(AppValue.getInstance().getDeviceInfo().getWidth(), 0)
                                .error(R.drawable.upload_icon)
                                .placeholder(R.drawable.loading_animation)
                                .into(imgPhoto);
                        imgFile = new File(ImageUntil.getPathFromURI(this, imageUri));
                    } else {
                        SnackbarUtil.showShort(btnUpload, R.string.upload_size_require);
                    }
                }
        }
    }
}
