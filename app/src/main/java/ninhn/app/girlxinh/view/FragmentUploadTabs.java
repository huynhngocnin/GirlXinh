package ninhn.app.girlxinh.view;

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

import ninhn.app.girlxinh.R;
import ninhn.app.girlxinh.until.ToastUntil;

/**
 * Created by NinHN on 7/31/2016.
 */
public class FragmentUploadTabs extends Fragment implements View.OnClickListener {

    private static TabLayout tabLayout;
    private static ViewPager viewPager;
    private static FloatingActionButton floatUpload;
    public static int int_items = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x = inflater.inflate(R.layout.fragment_upload_tabs, null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs_upload);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager_upload);
        floatUpload = (FloatingActionButton) x.findViewById(R.id.float_upload_new);
        floatUpload.setOnClickListener(this);

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

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

        return x;

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
        startActivity(intent);
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentUploadReview();
                case 1:
                    return new FragmentUploadPublish();
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
