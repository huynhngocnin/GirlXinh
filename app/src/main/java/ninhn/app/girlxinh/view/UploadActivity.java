package ninhn.app.girlxinh.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.io.OutputStream;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.constant.AppConstant;
import ninhn.app.girlxinh.constant.JSONConstant;
import ninhn.app.girlxinh.constant.UrlConstant;
import ninhn.app.girlxinh.helper.AppValue;
import ninhn.app.girlxinh.listener.UploadListener;
import ninhn.app.girlxinh.model.PhotoModel;
import ninhn.app.girlxinh.model.PhotoReviewModel;
import ninhn.app.girlxinh.until.ImageUntil;
import ninhn.app.girlxinh.until.SnackbarUtil;
import ninhn.app.girlxinh.until.ToastUntil;

import static ninhn.app.girlxinh.constant.AppConstant.PHOTO_SIZE_MIN;
import static ninhn.app.girlxinh.constant.AppConstant.FILE_SIZE_3MB;
import static ninhn.app.girlxinh.constant.AppConstant.FILE_SIZE_6MB;
import static ninhn.app.girlxinh.constant.AppConstant.FILE_SIZE_9MB;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PICK_PHOTO = 0;
    public static final String UPLOAD_OK = "uploaded";
    private static final int PERMISSION_REQUEST_CODE = 0;

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

        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission()) {
                requestPermission();
            } else {
                pickPhoto();
            }
        } else {
            pickPhoto();
        }
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
                String url;
                //Set param to upload(photo File and photo Info)
                RequestParams requestParams = new RequestParams();
                if (AppConstant.USER_ROLE_ADMIN == AppValue.getInstance().getUserModel().getRole()) {
                    //Set photo info
                    PhotoModel photoModel = new PhotoModel();
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
                        SnackbarUtil.showShort(btnUpload, JPException.getMessage());
                        changeControlToReUpload();
                        return;
                    }


                    url = UrlConstant.PHOTO_ADMIN_UPLOAD;
                    requestParams.put(UrlConstant.PHOTO_FILE, imgFile);
                    requestParams.put(UrlConstant.PHOTO_INFO, jsonInString);
                    requestParams.put(JSONConstant.USER_ID, AppValue.getInstance().getUserModel().getId());
                } else {
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
                    requestParams.put(UrlConstant.PHOTO_FILE, imgFile);
                    requestParams.put(UrlConstant.PHOTO_INFO, jsonInString);
                    url = UrlConstant.PHOTO_USER_UPLOAD;
                }

                ImageUntil.uploadImage(this, url, requestParams, new UploadListener() {
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
        BitmapFactory.decodeFile(new File(ImageUntil.getPathFromURI(this, uri)).getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        if (imageWidth >= PHOTO_SIZE_MIN || imageHeight >= PHOTO_SIZE_MIN) {
            long size = ImageUntil.getFileSize(this, uri);
            if (size >= FILE_SIZE_9MB) {
                return compressFile(uri, 15);
            } else if (size >= FILE_SIZE_6MB) {
                return compressFile(uri, 25);
            } else if (size >= FILE_SIZE_3MB) {
                return compressFile(uri, 35);
            }
            imgFile = new File(ImageUntil.getPathFromURI(this, uri));
            return true;
        }
        return false;
    }

    private boolean compressFile(Uri uri, int percent) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Assume block needs to be inside a Try/Catch block.
            String path = Environment.getExternalStorageDirectory().toString();
            OutputStream fOut = null;
            tempFile = new File(path, System.currentTimeMillis() + AppConstant.COMPRESSED_JPG); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
            try {
                fOut = new FileOutputStream(tempFile);
                Bitmap pictureBitmap = BitmapFactory.decodeFile(ImageUntil.getPathFromURI(this, uri)); // obtaining the Bitmap
                pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                fOut.flush(); // Not really required
                fOut.close(); // do not forget to close the stream
                imgFile = tempFile;
                return true;
            } catch (IOException ioException) {
            }
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
                    } else {
                        SnackbarUtil.showShort(btnUpload, R.string.upload_size_require);
                    }
                }
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                    pickPhoto();
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                    finish();
                }
                break;
        }
    }
}
