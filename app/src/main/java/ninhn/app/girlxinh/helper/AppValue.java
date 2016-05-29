package ninhn.app.girlxinh.helper;

import ninhn.app.girlxinh.model.DeviceInfo;
import ninhn.app.girlxinh.model.UserModel;

/**
 * Created by NinHN on 4/7/16.
 */
public class AppValue {

    private static AppValue instace;
    private DeviceInfo deviceInfo;
    private UserModel userModel;
    private boolean isLogin;

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

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public void clearUserModel(){
        this.userModel = new UserModel();
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
