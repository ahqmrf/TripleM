package apps.ahqmrf.triplem.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import apps.ahqmrf.triplem.R;
import apps.ahqmrf.triplem.util.DataManager;
import apps.ahqmrf.triplem.util.Util;
import de.hdodenhof.circleimageview.CircleImageView;

public class GetStartedActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mUsername;
    private EditText mQuote;
    private TextView mUsernameWarn;
    private TextView mQuoteWarn;
    private TextView mPhotoWarn;
    private Button mStart;
    private CircleImageView mProfilePhoto;
    private DataManager manager;
    private View parentLayout;
    private ProgressDialog dialog;

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
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        mUsernameWarn = (TextView) findViewById(R.id.warn_name);
        mQuoteWarn = (TextView) findViewById(R.id.warn_quote);
        mPhotoWarn = (TextView) findViewById(R.id.warn_photo);
        parentLayout = findViewById(R.id.activity_get_started);
        mUsername = (EditText) findViewById(R.id.name);
        mQuote = (EditText) findViewById(R.id.quote);
        mStart = (Button) findViewById(R.id.btn_start);
        mStart.setOnClickListener(this);
        mProfilePhoto = (CircleImageView) findViewById(R.id.profile_photo);
        mProfilePhoto.setOnClickListener(this);
        setupUI(findViewById(R.id.activity_get_started));
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
        if (!validateInputs()) return;
        manager.setUsername(mUsername.getText().toString());
        manager.setQuote(mQuote.getText().toString());
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        }, Util.SPLASH_DURATION * 1000);
    }

    private boolean validateInputs() {
        if (mUsername.getText().toString().isEmpty()) {
            mUsernameWarn.setVisibility(View.VISIBLE);
            return false;
        }
        if (mQuote.getText().toString().isEmpty()) {
            mQuoteWarn.setVisibility(View.VISIBLE);
            mUsernameWarn.setVisibility(View.GONE);
            return false;
        }
        mQuoteWarn.setVisibility(View.GONE);
        return true;
    }

    private void openGallery() {
        checkReadExternalStoragePermission();
    }

    private void checkReadExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23){
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(GetStartedActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(GetStartedActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(GetStartedActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            Util.PERMISSION_READ_EXTERNAL_STORAGE_REQ_CODE);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }else{
                ActivityCompat.requestPermissions(GetStartedActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Util.PERMISSION_READ_EXTERNAL_STORAGE_REQ_CODE);
            }
        }else {

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, Util.GALLERY_BROWSE_REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Util.PERMISSION_READ_EXTERNAL_STORAGE_REQ_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, Util.GALLERY_BROWSE_REQ_CODE);

                } else {
                    showSnackbar("Permission required to select media");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Util.GALLERY_BROWSE_REQ_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                mPhotoWarn.setVisibility(View.GONE);
                String path = getRealPathFromUri(this, uri);
                manager.setProfilePhotoPath(path);
                ImageLoader.getInstance().displayImage(
                        path,
                        mProfilePhoto,
                        App.getDisplayImageOptions()
                );
                Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            } else {
                mPhotoWarn.setVisibility(View.VISIBLE);
            }
        } else {
            mPhotoWarn.setVisibility(View.VISIBLE);
        }
    }

    public String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return "file://" + cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(GetStartedActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}
