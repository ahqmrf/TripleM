package apps.ahqmrf.triplem.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lenovo on 3/16/2017.
 */

public class DataManager {

    private Context mContext;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public DataManager(Context context) {
        mContext = context;
        preferences = mContext.getSharedPreferences(Util.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public String getUsername() {
        if(preferences.contains(Util.USER_NAME)) {
            return preferences.getString(Util.USER_NAME, null);
        }
        return null;
    }

    public String getProfilePhotoPath() {
        if(preferences.contains(Util.USER_PROFILE_PIC_PATH)) {
            return preferences.getString(Util.USER_PROFILE_PIC_PATH, null);
        }
        return null;
    }

    public void setUsername(String username) {
        editor = preferences.edit();
        editor.putString(Util.USER_NAME, username);
        editor.apply();
    }

    public void setProfilePhotoPath(String path) {
        editor = preferences.edit();
        editor.putString(Util.USER_PROFILE_PIC_PATH, path);
        editor.apply();
    }

    public String getQuote() {
        if(preferences.contains(Util.QUOTE)) {
            return preferences.getString(Util.QUOTE, null);
        }
        return null;
    }

    public void setQuote(String quote) {
        editor = preferences.edit();
        editor.putString(Util.QUOTE, quote);
        editor.apply();
    }
}
