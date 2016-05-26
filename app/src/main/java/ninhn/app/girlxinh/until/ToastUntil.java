package ninhn.app.girlxinh.until;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by NinHN on 5/26/16.
 */
public class ToastUntil {
    public static void showShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
