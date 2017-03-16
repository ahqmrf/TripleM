package apps.ahqmrf.triplem.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import apps.ahqmrf.triplem.R;

public class HomeActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_home, mContentFrame);
    }
}
