package ninhn.app.girlxinh.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;

import ninhn.app.girlxinh.MyApplication;
import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.adapter.ViewPagerAdapter;
import ninhn.app.girlxinh.helper.AppValue;
import ninhn.app.girlxinh.model.DeviceInfo;
import ninhn.app.girlxinh.until.ConnectionUntil;
import ninhn.app.girlxinh.until.DialogUntil;
import ninhn.app.girlxinh.until.ToastUntil;

import static ninhn.app.girlxinh.constant.AppConstant.HOME;
import static ninhn.app.girlxinh.constant.AppConstant.LOVE;
import static ninhn.app.girlxinh.constant.AppConstant.ME;

public class MainActivity extends AppCompatActivity {


    //    private Toolbar mToolbar;
//    private FloatingActionButton mFabButton;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;

//    private void hideSystemUI() {
//        View mDecorView = getWindow().getDecorView();
//        // Set the IMMERSIVE flag.
//        // Set the content to appear under the system bars so that the content
//        // doesn't resize when the system bars hide and show.
//        mDecorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
//    }

    private void getDeviceInfo() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setWidth(metrics.widthPixels);
        deviceInfo.setHeight(metrics.heightPixels);
        AppValue.getInstance().setDeviceInfo(deviceInfo);
    }

//    private void createToolbar() {
//        //mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        //mToolbar.setTitle("NinHN ");
//        //setSupportActionBar(mToolbar);
//    }

//    private void createFloatingButton() {
//        //        mFabButton = (FloatingActionButton) findViewById(R.id.fab);
//        //        mFabButton.setOnClickListener(new View.OnClickListener() {
//        //            @Override
//        //            public void onClick(View view) {
//        //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//        //                        .setAction("Action", null).show();
//        //            }
//        //        });
//    }

    private void initUser() {
        AppValue.getInstance().setUserModel(MyApplication.getInstance().getPrefManager().getUser());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hideSystemUI();
        setContentView(R.layout.activity_main);

        getDeviceInfo();

        initViewPager();

        initBottomNavigation();

        initUser();

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

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentHome(), HOME);
        adapter.addFragment(new FragmentLove(), LOVE);
        adapter.addFragment(new FragmentMe(), ME);
        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                ToastUntil.showLong(getApplication(), "Fragment: " +position);
//                if (1 == position) {
//                    ((FragmentLove) getSupportFragmentManager().getFragments().get(1)).loginDone();
//                    ToastUntil.showShort(getApplication(), getSupportFragmentManager().getFragments().get(1).getClass().toString());
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
    }

    private void initBottomNavigation() {
        int[] colorBar = new int[]{ContextCompat.getColor(this, R.color.colorBarHome), ContextCompat.getColor(this, R.color.colorBarLove), ContextCompat.getColor(this, R.color.colorBarMe)};
        int[] drawableBar = new int[]{R.drawable.ic_bar_home, R.drawable.ic_bar_love, R.drawable.ic_bar_me};

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        bottomNavigationView.isColoredBackground(true);
        //bottomNavigationView.disableViewPagerSlide();
        //Set viewpaper to navigation
        bottomNavigationView.setUpWithViewPager(viewPager, colorBar, drawableBar);
        //Disable Shadow
        bottomNavigationView.disableShadow();
    }

    public void changeTabTo(int tabIndex) {
        bottomNavigationView.selectTab(tabIndex);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
