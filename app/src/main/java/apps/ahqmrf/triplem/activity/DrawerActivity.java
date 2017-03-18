package apps.ahqmrf.triplem.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import apps.ahqmrf.triplem.R;
import apps.ahqmrf.triplem.util.DataManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class DrawerActivity extends AppCompatActivity {

    protected FrameLayout mContentFrame;
    protected DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mToggle;
    protected NavigationView mNavigationView;
    protected View mNavigationHeader;
    protected CircleImageView mProfilePhoto;
    protected TextView mName;
    protected TextView mQuote;
    protected DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        mContentFrame = (FrameLayout) findViewById(R.id.fl_content_frame);
        dataManager = new DataManager(this);
        setDrawerLayout();
        setNavigationView();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setNavigationView() {
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationHeader = mNavigationView.getHeaderView(0);
        mName = (TextView) mNavigationHeader.findViewById(R.id.user_name);
        mQuote = (TextView) mNavigationHeader.findViewById(R.id.quote);
        mProfilePhoto = (CircleImageView) mNavigationHeader.findViewById(R.id.profile_image);
        if(dataManager.getUsername() != null) {
            mName.setText(dataManager.getUsername());
        }
        if(dataManager.getQuote() != null) {
            mQuote.setText(dataManager.getQuote());
        }
        if(dataManager.getProfilePhotoPath() != null) {
            ImageLoader.getInstance().displayImage(
                    dataManager.getProfilePhotoPath(),
                    mProfilePhoto,
                    App.getDisplayImageOptions()
            );
        }
    }

    private void setDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.settings:
                startSettingsActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startSettingsActivity() {
        mDrawerLayout.closeDrawers();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
