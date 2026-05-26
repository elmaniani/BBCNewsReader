package com.example.bbcnewsreader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bbcnewsreader.R;
import com.example.bbcnewsreader.database.NewsDatabaseHelper;
import com.example.bbcnewsreader.models.Article;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class FavouritesActivity extends NavigationActivity {
    private ArrayList<Article> favourites;
    private ArrayAdapter<Article> adapter;
    private Article selectedArticle;
    private NewsDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        setupNavigation(R.string.help_favourites);

        databaseHelper = new NewsDatabaseHelper(this);
        favourites = databaseHelper.getAllFavourites();

        ListView favouritesListView = findViewById(R.id.favouritesListView);
        Button deleteSelectedButton = findViewById(R.id.deleteSelectedButton);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, favourites);
        favouritesListView.setAdapter(adapter);
        favouritesListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        favouritesListView.setOnItemClickListener((parent, view, position, id) -> {
            selectedArticle = favourites.get(position);
            openDetails(selectedArticle);
        });

        favouritesListView.setOnItemLongClickListener((parent, view, position, id) -> {
            selectedArticle = favourites.get(position);
            Snackbar.make(view, "Selected: " + selectedArticle.getTitle(), Snackbar.LENGTH_SHORT).show();
            return true;
        });

        deleteSelectedButton.setOnClickListener(view -> deleteSelectedArticle());
    }

    private void openDetails(Article article) {
        selectedArticle = article;
        Intent intent = new Intent(this, ArticleDetailsActivity.class);
        intent.putExtra("id", article.getId());
        intent.putExtra("title", article.getTitle());
        intent.putExtra("description", article.getDescription());
        intent.putExtra("date", article.getPubDate());
        intent.putExtra("link", article.getLink());
        intent.putExtra("section", article.getSection());
        startActivity(intent);
    }

    private void deleteSelectedArticle() {
        if (selectedArticle == null) {
            Toast.makeText(this, "Long-press an article before deleting", Toast.LENGTH_SHORT).show();
            return;
        }

        int deleted = databaseHelper.deleteFavourite(selectedArticle.getId());
        if (deleted > 0) {
            favourites.remove(selectedArticle);
            adapter.notifyDataSetChanged();
            Snackbar.make(findViewById(R.id.favouritesListView), "Favourite deleted", Snackbar.LENGTH_LONG).show();
            selectedArticle = null;
        }
    }

    @Override
    protected String getHelpMessage() {
        return getString(R.string.help_favourites);
    }
}
