package ninhn.app.girlxinh.until;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by NinHN on 5/26/16.
 */
public class ToastUntil {
    private static Toast toast = null;
    public static void showShort(Context context, String message) {
        if(toast!=null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showShort(Context context, int message) {
        if(toast!=null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showLong(Context context, String message) {
        if(toast!=null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showLong(Context context, int message) {
        if(toast!=null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }
}
