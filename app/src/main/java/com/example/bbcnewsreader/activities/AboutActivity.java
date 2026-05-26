package com.example.bbcnewsreader.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bbcnewsreader.R;

public class AboutActivity extends NavigationActivity {
    private static final String PREFS_NAME = "bbc_news_prefs";
    private EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setupNavigation(R.string.help_about);

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String lastLoaded = preferences.getString("last_loaded", "News has not been loaded yet.");
        String savedName = preferences.getString("user_name", "");

        TextView lastLoadedTextView = findViewById(R.id.lastLoadedTextView);
        nameEditText = findViewById(R.id.nameEditText);
        Button saveNameButton = findViewById(R.id.saveNameButton);

        lastLoadedTextView.setText("Last loaded: " + lastLoaded);
        nameEditText.setText(savedName);

        saveNameButton.setOnClickListener(view -> {
            preferences.edit().putString("user_name", nameEditText.getText().toString()).apply();
            Toast.makeText(this, "Name saved", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected String getHelpMessage() {
        return getString(R.string.help_about);
    }
}
