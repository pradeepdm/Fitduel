package com.fitness.fitduel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fitness.model.UserInfoModel;

import java.io.InputStream;

/**
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    // private CollapsingToolbarLayout collapsingToolbarLayout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View profileFragment = inflater.inflate(
                R.layout.fragment_profile,
                container,
                false
        );

        ImageView imageView = profileFragment.findViewById(R.id.profileImage);
        TextView userEmail = profileFragment.findViewById(R.id.user_email);
        TextView currentBalance = profileFragment.findViewById(R.id.current_balance);
        TextView userName = profileFragment.findViewById(R.id.user_name);
        //collapsingToolbarLayout = profileFragment.findViewById(R.id.collapseToolBar);

        // show The Image in a ImageView
        new LoadProfileImage((imageView))
                .execute(UserInfoModel.instance().getProfilePic() + "/picture?type=small");

        userEmail.setText(UserInfoModel.instance().getEmail());
        currentBalance.setText(String.valueOf(UserInfoModel.instance().getCurrentBalance()));
        userName.setText(UserInfoModel.instance().getName());
        return profileFragment;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                Bitmap resized = Bitmap.createScaledBitmap(result, 200, 200, true);
                bmImage.setImageBitmap(resized);
            }
        }
    }
}


