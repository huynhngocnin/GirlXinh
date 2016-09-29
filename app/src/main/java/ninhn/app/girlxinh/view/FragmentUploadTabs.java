package ninhn.app.girlxinh.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.constant.AppConstant;
import ninhn.app.girlxinh.event.LoginChangedEvent;
import ninhn.app.girlxinh.helper.AppValue;
import ninhn.app.girlxinh.helper.BusProvider;
import ninhn.app.girlxinh.until.SnackbarUtil;

/**
 * Created by NinHN on 7/31/2016.
 */
public class FragmentUploadTabs extends Fragment implements View.OnClickListener {

    public static final int UPLOAD = 0;

    private static TabLayout tabLayout;
    private static ViewPager viewPager;
    private static FloatingActionButton floatUpload;
    private static int int_items = 2;
    private FragmentUserReview fragmentUserReview = new FragmentUserReview();
    private FragmentUserPublish fragmentUserPublish = new FragmentUserPublish();
    private FragmentAdminReview fragmentAdminReview = new FragmentAdminReview();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View tabsView = inflater.inflate(R.layout.fragment_upload_tabs, null);
        tabLayout = (TabLayout) tabsView.findViewById(R.id.tabs_upload);
        viewPager = (ViewPager) tabsView.findViewById(R.id.viewpager_upload);
        floatUpload = (FloatingActionButton) tabsView.findViewById(R.id.float_upload_new);
        floatUpload.setOnClickListener(this);

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new TabsAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return tabsView;

    }

    @Override
    public void onResume() {
        super.onResume();
        // Register ourselves so that we can provide the initial value.
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onLoginChanged(LoginChangedEvent event) {
        //Update tabview for user
        viewPager.setAdapter(new TabsAdapter(getChildFragmentManager()));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.float_upload_new:
                showUploadActivity();
                break;
            default:
                break;
        }
    }

    private void showUploadActivity() {
        Intent intent = new Intent(getActivity(), UploadActivity.class);
        startActivityForResult(intent, UPLOAD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPLOAD && resultCode == Activity.RESULT_OK) {
            if (data.getExtras().getBoolean(UploadActivity.UPLOAD_OK)) {
                if (AppConstant.USER_ROLE_USER == AppValue.getInstance().getUserModel().getRole()) {
                    fragmentUserReview.handleUploadSuccess();
                }
            }else {
                SnackbarUtil.showShort(floatUpload, R.string.upload_fail);
            }
        }
    }

    class TabsAdapter extends FragmentPagerAdapter {

        public TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (AppConstant.USER_ROLE_USER == AppValue.getInstance().getUserModel().getRole()) {
                        return fragmentUserReview;
                    } else if (AppConstant.USER_ROLE_ADMIN == AppValue.getInstance().getUserModel().getRole()) {
                        return fragmentAdminReview;
                    }
                case 1:
                    return fragmentUserPublish;
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return getString(R.string.review);
                case 1:
                    return getString(R.string.publish);
            }
            return null;
        }
    }

}
