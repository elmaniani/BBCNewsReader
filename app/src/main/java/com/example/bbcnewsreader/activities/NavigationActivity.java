package com.example.bbcnewsreader.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.bbcnewsreader.R;
import com.google.android.material.navigation.NavigationView;

public abstract class NavigationActivity extends AppCompatActivity {

    protected void setupNavigation(int helpMessageId) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.menu_home, R.string.menu_exit);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            drawerLayout.closeDrawers();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
            } else if (id == R.id.nav_favourites) {
                startActivity(new Intent(this, FavouritesActivity.class));
            } else if (id == R.id.nav_about) {
                startActivity(new Intent(this, AboutActivity.class));
            } else if (id == R.id.nav_exit) {
                finishAffinity();
            }
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_favourites) {
            startActivity(new Intent(this, FavouritesActivity.class));
            return true;
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        } else if (id == R.id.action_help) {
            showHelpDialog(getHelpMessage());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract String getHelpMessage();

    protected void showHelpDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.menu_help)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
