package ninhn.app.girlxinh.until;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by ninhn on 9/20/2016.
 */
public class SnackbarUtil {
    private static Snackbar snackbar = null;

    public static void showShort(View view, String message) {
        if (snackbar != null) {
            snackbar.dismiss();
        }
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public static void showShort(View view, int message) {
        if (snackbar != null) {
            snackbar.dismiss();
        }
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void showLong(View view, String message) {
        if (snackbar != null) {
            snackbar.dismiss();
        }
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public static void showLong(View view, int message) {
        if (snackbar != null) {
            snackbar.dismiss();
        }
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
