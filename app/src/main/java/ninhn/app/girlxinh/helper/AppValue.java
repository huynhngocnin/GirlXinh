package ninhn.app.girlxinh.helper;

import ninhn.app.girlxinh.model.DeviceInfo;

/**
 * Created by NinHN on 4/7/16.
 */
public class AppValue {

    private static AppValue instace;
    private DeviceInfo deviceInfo;

    public static AppValue getInstance() {
        if (instace == null) {
            instace = new AppValue();
        }
        return instace;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
