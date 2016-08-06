package ninhn.app.girlxinh.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.helper.AppValue;
import ninhn.app.girlxinh.until.ToastUntil;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PICK_PHOTO = 0;

    private EditText edtDescription;
    private ImageView imgPhoto;
    private Button btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        initControl();
    }

    private void initControl() {
        edtDescription = (EditText) findViewById(R.id.edt_upload_description);
        imgPhoto = (ImageView) findViewById(R.id.img_upload_photo);
        btnUpload = (Button) findViewById(R.id.btn_upload_submit);
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
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_PHOTO);
    }

    private void uploadPhoto(){
        ToastUntil.showShort(this, "Đợi mai rồi upload nha ku!");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    Picasso.with(this)
                            .load(selectedImage)
                            //.resize(AppValue.getInstance().getDeviceInfo().getWidth(), 0)
                            .error(R.drawable.upload_icon)
                            .placeholder(R.drawable.loading_animation)
                            .into(imgPhoto);
                    try {
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    } catch (FileNotFoundException fileExeption) {

                    }
                }
        }
    }
}
