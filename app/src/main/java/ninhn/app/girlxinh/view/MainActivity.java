package ninhn.app.girlxinh.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.facebook.appevents.AppEventsLogger;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.helper.AppValue;
import ninhn.app.girlxinh.model.DeviceInfo;
import ninhn.app.girlxinh.until.ConnectionUntil;
import ninhn.app.girlxinh.until.DialogUntil;
import ninhn.app.girlxinh.until.SnackbarUtil;

public class MainActivity extends AppCompatActivity implements AHBottomNavigation.OnTabSelectedListener {

    private static final int PERMISSION_REQUEST_CODE = 0;

    //private FloatingActionButton mFabButton;
    private AHBottomNavigation ahBottomNavigation;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private FragmentHome fragmentHome;
    private FragmentLove fragmentLove;
    private FragmentUploadTabs fragmentUploadTabs;
    private FragmentMe fragmentMe;

    private void getDeviceInfo() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setWidth(metrics.widthPixels);
        deviceInfo.setHeight(metrics.heightPixels);
        AppValue.getInstance().setDeviceInfo(deviceInfo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission()) {
                requestPermission();
            }
        }

        getDeviceInfo();

        initBottomNavigation();
        initFragment();
        initHome();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (!ConnectionUntil.isConnection(this)) {
            DialogUntil.showNetworkStage(this, false);
        }
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    private void initBottomNavigation() {

        ahBottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        // Create items
        AHBottomNavigationItem itemHome = new AHBottomNavigationItem(R.string.home, R.drawable.ic_bar_home, R.color.colorBarBackground);
        AHBottomNavigationItem itemLove = new AHBottomNavigationItem(R.string.love, R.drawable.ic_bar_love, R.color.colorBarBackground);
        AHBottomNavigationItem itemUpload = new AHBottomNavigationItem(R.string.upload, R.drawable.ic_bar_upload, R.color.colorBarBackground);
        AHBottomNavigationItem itemMe = new AHBottomNavigationItem(R.string.me, R.drawable.ic_bar_me, R.color.colorBarBackground);

        // Add items
        ahBottomNavigation.addItem(itemHome);
        ahBottomNavigation.addItem(itemLove);
        ahBottomNavigation.addItem(itemUpload);
        ahBottomNavigation.addItem(itemMe);

        // Disable the translation inside the CoordinatorLayout
        ahBottomNavigation.setBehaviorTranslationEnabled(false);

        // Force to tint the drawable (useful for font with icon for example)
        ahBottomNavigation.setForceTint(true);

        // Force the titles to be displayed (against Material Design guidelines!)
        ahBottomNavigation.setForceTitlesDisplay(true);

        // Use colored navigation with circle reveal effect
        ahBottomNavigation.setColored(true);

        // Customize notification (title, background, typeface)
        ahBottomNavigation.setNotificationBackgroundColorResource(R.color.colorBarNotification);

        // Set listeners
        ahBottomNavigation.setOnTabSelectedListener(this);

    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        if (!wasSelected) {
            switch (position) {
                case 0:
                    displayFragmentHome();
                    break;
                case 1:
                    displayFragmentLove();
                    break;
                case 2:
                    if (AppValue.getInstance().isLogin()) {
                        displayFragmentUpload();
                    } else {
                        SnackbarUtil.showShort(ahBottomNavigation, R.string.require_login_upload);
                    }
                    break;
                case 3:
                    displayFragmentMe();
                    break;
                default:
                    break;
            }
        } else {
            switch (position) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    private void initFragment() {
        fragmentHome = new FragmentHome();
        fragmentLove = new FragmentLove();
        fragmentUploadTabs = new FragmentUploadTabs();
        fragmentMe = new FragmentMe();
    }

    private void initHome() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.containerView, fragmentHome);
        mFragmentTransaction.commit();
    }

    private void displayFragmentHome() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (fragmentHome.isAdded()) { // if the fragment is already in container
            ft.show(fragmentHome);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.containerView, fragmentHome);
        }
        // Hide fragmentLove
        if (fragmentLove.isAdded()) {
            ft.hide(fragmentLove);
        }
        // Hide fragmentHome
        if (fragmentUploadTabs.isAdded()) {
            ft.hide(fragmentUploadTabs);
        }
        // Hide fragmentMe
        if (fragmentMe.isAdded()) {
            ft.hide(fragmentMe);
        }
        // Commit changes
        ft.commit();
    }

    private void displayFragmentLove() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (fragmentLove.isAdded()) { // if the fragment is already in container
            ft.show(fragmentLove);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.containerView, fragmentLove);
        }

        // Hide fragmentHome
        if (fragmentHome.isAdded()) {
            ft.hide(fragmentHome);
        }
        // Hide fragmentHome
        if (fragmentUploadTabs.isAdded()) {
            ft.hide(fragmentUploadTabs);
        }
        // Hide fragmentMe
        if (fragmentMe.isAdded()) {
            ft.hide(fragmentMe);
        }
        // Commit changes
        ft.commit();
    }

    private void displayFragmentUpload() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (fragmentUploadTabs.isAdded()) { // if the fragment is already in container
            ft.show(fragmentUploadTabs);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.containerView, fragmentUploadTabs);
        }

        // Hide fragmentHome
        if (fragmentHome.isAdded()) {
            ft.hide(fragmentHome);
        }
        // Hide fragmentLove
        if (fragmentLove.isAdded()) {
            ft.hide(fragmentLove);
        }
        // Hide fragmentMe
        if (fragmentMe.isAdded()) {
            ft.hide(fragmentMe);
        }
        // Commit changes
        ft.commit();
    }

    private void displayFragmentMe() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (fragmentMe.isAdded()) { // if the fragment is already in container
            ft.show(fragmentMe);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.containerView, fragmentMe);
        }

        // Hide fragmentLove
        if (fragmentLove.isAdded()) {
            ft.hide(fragmentLove);
        }
        // Hide fragmentHome
        if (fragmentUploadTabs.isAdded()) {
            ft.hide(fragmentUploadTabs);
        }
        // Hide fragmentHome
        if (fragmentHome.isAdded()) {
            ft.hide(fragmentHome);
        }
        // Commit changes
        ft.commit();
    }

    public void changeNavigationTabTo(int tabIndex) {
        // Set current item programmatically
        ahBottomNavigation.setCurrentItem(tabIndex);
    }

    public void setNavigationNotification(int tab, int total) {
        // Add or remove notification for each item
        ahBottomNavigation.setNotification(String.valueOf(total), tab);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

}
