package com.techvee.smartpantry;

import android.os.Bundle;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;

/** A local preference changes pantry ordering and survives app restarts. */
public class SettingsActivity extends AppCompatActivity {
    public static final String PREFERENCES = "pantry_preferences";
    public static final String EXPIRY_FIRST = "expiry_first";
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_settings);
        ScreenInsets.apply(findViewById(R.id.settings_root));
        android.content.SharedPreferences preferences =
                getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        CheckBox expiryFirst = findViewById(R.id.expiry_first);
        expiryFirst.setChecked(preferences.getBoolean(EXPIRY_FIRST, false));
        expiryFirst.setOnCheckedChangeListener((button, checked) ->
                preferences.edit().putBoolean(EXPIRY_FIRST, checked).apply());
        findViewById(R.id.settings_back).setOnClickListener(view -> finish());
    }
}
