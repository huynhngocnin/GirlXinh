package ninhn.app.girlxinh.until;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import ninhn.app.girlxinh.model.PhotoModel;


/**
 * Created by NinHN on 5/22/16.
 */
public class DownloadUntil {

    public static void downloadPhoto(Context context, PhotoModel photo) {
        Uri uri = Uri.parse(photo.getUrl());

        //check folder if it doesn't exist, this code will create it.
//        File file = new File(Variables.PUBLIC_PATH_FOLDER);
//        if (!file.exists())
//            file.mkdirs();
//
        //path to save file after downloaded
//        Uri dest = Uri.fromFile(file);
        DownloadManager.Request req = new DownloadManager.Request(uri);
        req.setTitle(photo.getTitle());
        req.setDescription(photo.getTitle());

        //set notification can be seen
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        //show file in android gallery after downloaded
        req.allowScanningByMediaScanner();
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);

        //start to download
        downloadManager.enqueue(req);
    }
}
