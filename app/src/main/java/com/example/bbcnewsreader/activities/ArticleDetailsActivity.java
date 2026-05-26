package com.example.bbcnewsreader.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.bbcnewsreader.R;
import com.example.bbcnewsreader.database.NewsDatabaseHelper;
import com.example.bbcnewsreader.fragments.ArticleDetailsFragment;
import com.example.bbcnewsreader.models.Article;
import com.google.android.material.snackbar.Snackbar;

public class ArticleDetailsActivity extends NavigationActivity {
    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        setupNavigation(R.string.help_details);

        article = new Article(
                getIntent().getLongExtra("id", 0),
                getIntent().getStringExtra("title"),
                getIntent().getStringExtra("description"),
                getIntent().getStringExtra("date"),
                getIntent().getStringExtra("link"),
                getIntent().getStringExtra("section")
        );

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.articleFragmentContainer, ArticleDetailsFragment.newInstance(
                        article.getTitle(), article.getDescription(), article.getPubDate(), article.getLink()))
                .commit();

        Button saveButton = findViewById(R.id.saveFavouriteButton);
        Button openButton = findViewById(R.id.openArticleButton);

        saveButton.setOnClickListener(view -> saveFavourite());
        openButton.setOnClickListener(view -> openArticleLink());
    }

    private void saveFavourite() {
        NewsDatabaseHelper databaseHelper = new NewsDatabaseHelper(this);
        long result = databaseHelper.addFavourite(article);
        if (result == -1) {
            Toast.makeText(this, "Article is already saved", Toast.LENGTH_SHORT).show();
        } else {
            Snackbar.make(findViewById(R.id.articleFragmentContainer), "Article saved to favourites", Snackbar.LENGTH_LONG).show();
        }
    }

    private void openArticleLink() {
        if (article.getLink() != null && !article.getLink().isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getLink()));
            startActivity(intent);
        }
    }

    @Override
    protected String getHelpMessage() {
        return getString(R.string.help_details);
    }
}
