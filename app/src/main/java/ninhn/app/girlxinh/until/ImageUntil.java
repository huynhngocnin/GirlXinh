package ninhn.app.girlxinh.until;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

import org.json.JSONObject;

import java.io.File;

import ninhn.app.girlxinh.constant.AppConstant;
import ninhn.app.girlxinh.constant.UrlConstant;
import ninhn.app.girlxinh.helper.AppValue;
import ninhn.app.girlxinh.listener.UploadListener;

/**
 * Created by NinHN on 8/15/16.
 */
public class ImageUntil {

    public static String getPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, filePathColumn, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    /***
     * Upload image with call-back listener
     *
     * @param context
     * @param rqParams
     * @param callback
     */
    public static void uploadImage(final Context context, String url, RequestParams rqParams, final UploadListener callback) {
        try {
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(url, rqParams, new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    callback.onPostUploaded(true, null);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    callback.onPostUploaded(false, throwable.getMessage());
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            callback.onPostUploaded(false, ex.getMessage());
        }
    }

    public static long getFileSize(Context context, Uri uri) {
        File img = new File(getPathFromURI(context, uri));
        return img.length();
    }

}
