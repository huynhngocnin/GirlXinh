package ninhn.app.girlxinh.view;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {


    //    private Toolbar mToolbar;
//    private FloatingActionButton mFabButton;
    private ViewPager viewPager;

    private void hideSystemUI() {
        View mDecorView = getWindow().getDecorView();
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hideSystemUI();
        setContentView(R.layout.activity_main);
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setTitle("NinHN ");
//        setSupportActionBar(mToolbar);

//        mFabButton = (FloatingActionButton) findViewById(R.id.fab);
//        mFabButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        initViewPager();

        initBottomNavigation();

        //initRecyclerView();
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentHome(), "Home");
        adapter.addFragment(new FragmentLove(), "Love");
        adapter.addFragment(new FragmentMe(), "Me");
        viewPager.setAdapter(adapter);
    }

    private void initBottomNavigation() {
        int[] colorBar = new int[]{ContextCompat.getColor(this, R.color.colorBarHome), ContextCompat.getColor(this, R.color.colorBarLove), ContextCompat.getColor(this, R.color.colorBarMe)};
        int[] drawableBar = new int[]{R.drawable.ic_bar_home, R.drawable.ic_bar_love, R.drawable.ic_bar_me};

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        bottomNavigationView.isColoredBackground(true);
        bottomNavigationView.disableViewPagerSlide();
        bottomNavigationView.setUpWithViewPager(viewPager, colorBar, drawableBar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
