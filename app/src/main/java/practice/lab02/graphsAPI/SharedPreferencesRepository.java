package practice.lab02.graphsAPI;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesRepository {
    private SharedPreferences prefs;

    public SharedPreferencesRepository(Context context) {
        prefs = context.getSharedPreferences("GRAPHS", Context.MODE_PRIVATE);
    }

    public void saveAccountName(String name) {
        prefs.edit().putString(ACCOUNT_NAME, name).apply();
    }

    public void saveAccountSecret(String secret) {
        prefs.edit().putString(ACCOUNT_SECRET, secret).apply();
    }

    public void saveToken(String token) {
        prefs.edit().putString(TOKEN, token).apply();
    }

    public String getAccountName() {
        return prefs.getString(ACCOUNT_NAME, "");
    }

    public String getAccountSecret() {
        return prefs.getString(ACCOUNT_SECRET, "");
    }

    public String getToken() {
        return prefs.getString(TOKEN, "");
    }


    private static final String ACCOUNT_NAME = "account_name";
    private static final String ACCOUNT_SECRET = "account_secret";
    private static final String TOKEN = "token";
}
