package ninhn.app.girlxinh.until;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import ninhn.app.girlxinh.R;

/**
 * Created by NinHN on 5/29/16.
 */
public class DialogUntil {

    private static AlertDialog createDialogOkCancel(Context context, String title, String message) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User ok the dialog
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private static AlertDialog createDialogOk(Context context, String title, String message) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


    public static void showNetworkStage(Context context, boolean stage) {
        if (stage) {
            createDialogOk(context, context.getString(R.string.network_info), context.getString(R.string.network_connect_yes)).show();
        } else {
            createDialogOk(context, context.getString(R.string.network_info), context.getString(R.string.network_connect_no)).show();
        }
    }

}
