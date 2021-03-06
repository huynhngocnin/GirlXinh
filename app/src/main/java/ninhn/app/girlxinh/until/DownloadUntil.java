package ninhn.app.girlxinh.until;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.File;

import ninhn.app.girlxinh.Manifest;
import ninhn.app.girlxinh.constant.AppConstant;
import ninhn.app.girlxinh.model.PhotoModel;


/**
 * Created by NinHN on 5/22/16.
 */
public class DownloadUntil {

    public static void downloadPhoto(Context context, PhotoModel photo) {
        Uri uri = Uri.parse(photo.getUrl());

        //check folder if it doesn't exist, this code will create it.
        //Environment.getExternalStorageState();
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!file.exists())
            file.mkdirs();

        //path to save file after downloaded
        Uri dest = Uri.fromFile(file);
        DownloadManager.Request req = new DownloadManager.Request(uri);
        req.setTitle(photo.getDescription());
        req.setDescription(AppConstant.APP_URL);

        //set notification can be seen
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        //set Destination save
        req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, photo.getDescription() + AppConstant._JPG);

        //show file in android gallery after downloaded
        req.allowScanningByMediaScanner();
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);

        //start to download
        downloadManager.enqueue(req);
    }

}
