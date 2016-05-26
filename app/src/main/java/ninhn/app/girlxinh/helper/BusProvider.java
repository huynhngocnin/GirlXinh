package ninhn.app.girlxinh.helper;

import com.squareup.otto.Bus;

/**
 * Created by NinHN on 5/27/16.
 */
public final class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
    }
}
