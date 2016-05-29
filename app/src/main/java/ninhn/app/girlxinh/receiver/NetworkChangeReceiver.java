package ninhn.app.girlxinh.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.squareup.otto.Produce;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.event.ConnectChangedEvent;
import ninhn.app.girlxinh.helper.BusProvider;
import ninhn.app.girlxinh.until.ConnectionUntil;
import ninhn.app.girlxinh.until.ToastUntil;

/**
 * Created by NinHN on 5/29/16.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        // Register ourselves so that we can provide the initial value.
        BusProvider.getInstance().register(this);
        if (ConnectionUntil.isConnection(context)) {
            ToastUntil.showLong(context,context.getString(R.string.network_connect_yes));
            BusProvider.getInstance().post(produceConnectEvent());
        } else {
            ToastUntil.showLong(context,context.getString(R.string.network_connect_no));
        }
        BusProvider.getInstance().unregister(this);
    }
    @Produce
    public ConnectChangedEvent produceConnectEvent() {
        // Provide an initial value for location based on the last known position.
        return new ConnectChangedEvent(ConnectionUntil.isConnection(this.context));
    }
}
