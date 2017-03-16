package apps.ahqmrf.triplem.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import apps.ahqmrf.triplem.R;
import apps.ahqmrf.triplem.util.Util;

public class SplashActivity extends Activity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(Util.SPLASH_DURATION);

        final ImageView splash = (ImageView) findViewById(R.id.splash);
        splash.startAnimation(anim);
        intent = new Intent(getApplicationContext(), GetStartedActivity.class);
        SharedPreferences preferences = getSharedPreferences(Util.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if(preferences.contains(Util.FIRST_INSTALLATION)) {
            //intent = new Intent(getApplicationContext(), HomeActivity.class);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, Util.SPLASH_DURATION * 1000);
    }
}
