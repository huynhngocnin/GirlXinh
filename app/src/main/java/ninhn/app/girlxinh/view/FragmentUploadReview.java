package ninhn.app.girlxinh.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ninhn.app.girlxinh.R;

/**
 * Created by NinHN on 7/31/2016.
 */
public class FragmentUploadReview extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.upload_review, null);
    }
}
