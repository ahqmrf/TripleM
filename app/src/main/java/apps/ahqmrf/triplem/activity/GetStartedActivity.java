package apps.ahqmrf.triplem.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import apps.ahqmrf.triplem.R;
import apps.ahqmrf.triplem.util.DataManager;
import apps.ahqmrf.triplem.util.Util;
import de.hdodenhof.circleimageview.CircleImageView;

public class GetStartedActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mUsername;
    private EditText mQuote;
    private Button mStart;
    private CircleImageView mProfilePhoto;
    private DataManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        setTitle("Get Started");
        manager = new DataManager(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        makeNextLaunchDisable();
        initViews();
    }

    private void initViews() {
        mUsername = (EditText) findViewById(R.id.name);
        mQuote = (EditText) findViewById(R.id.quote);
        mStart = (Button) findViewById(R.id.btn_start);
        mStart.setOnClickListener(this);
        mProfilePhoto = (CircleImageView) findViewById(R.id.profile_photo);
        mProfilePhoto.setOnClickListener(this);
    }

    private void makeNextLaunchDisable() {
        SharedPreferences preferences = getSharedPreferences(Util.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Util.FIRST_INSTALLATION, true);
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_photo:
                openGallery();
                break;
            case R.id.btn_start:
                proceed();
                break;
        }
    }

    private void proceed() {
        manager.setUsername(mUsername.getText().toString());
        manager.setQuote(mQuote.getText().toString());
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Util.GALLERY_BROWSE_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Util.GALLERY_BROWSE_REQ_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if(uri != null) {
                String path = "file://" + getRealPathFromURI(this, uri);
                manager.setProfilePhotoPath(path);
                ImageLoader.getInstance().displayImage(
                        path,
                        mProfilePhoto,
                        App.getDisplayImageOptions()
                );
                Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
