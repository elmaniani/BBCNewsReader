package com.example.bbcnewsreader.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bbcnewsreader.R;
import com.example.bbcnewsreader.models.Article;
import com.example.bbcnewsreader.tasks.DownloadNewsTask;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends NavigationActivity implements DownloadNewsTask.NewsDownloadListener {
    private static final String BBC_FEED_URL = "https://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml";
    private static final String PREFS_NAME = "bbc_news_prefs";

    private ArrayList<Article> allArticles;
    private ArrayList<Article> visibleArticles;
    private ArrayAdapter<Article> adapter;
    private ProgressBar progressBar;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNavigation(R.string.help_main);

        allArticles = new ArrayList<>();
        visibleArticles = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);
        searchEditText = findViewById(R.id.searchEditText);
        Button loadNewsButton = findViewById(R.id.loadNewsButton);
        ListView newsListView = findViewById(R.id.newsListView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, visibleArticles);
        newsListView.setAdapter(adapter);

        loadNewsButton.setOnClickListener(view -> loadNews());
        newsListView.setOnItemClickListener((parent, view, position, id) -> openDetails(visibleArticles.get(position)));

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { filterArticles(s.toString()); }
            @Override public void afterTextChanged(Editable s) { }
        });
    }

    private void loadNews() {
        progressBar.setVisibility(View.VISIBLE);
        new DownloadNewsTask(this).execute(BBC_FEED_URL);
    }

    private void filterArticles(String text) {
        visibleArticles.clear();
        for (Article article : allArticles) {
            if (article.getTitle().toLowerCase(Locale.CANADA).contains(text.toLowerCase(Locale.CANADA))) {
                visibleArticles.add(article);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void openDetails(Article article) {
        Intent intent = new Intent(this, ArticleDetailsActivity.class);
        intent.putExtra("title", article.getTitle());
        intent.putExtra("description", article.getDescription());
        intent.putExtra("date", article.getPubDate());
        intent.putExtra("link", article.getLink());
        intent.putExtra("section", article.getSection());
        startActivity(intent);
    }

    @Override
    public void onNewsDownloaded(ArrayList<Article> articles) {
        progressBar.setVisibility(View.GONE);
        allArticles.clear();
        allArticles.addAll(articles);
        filterArticles(searchEditText.getText().toString());

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CANADA).format(new Date());
        preferences.edit().putString("last_loaded", now).apply();

        Toast.makeText(this, "News loaded successfully", Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById(R.id.newsListView), articles.size() + " articles loaded", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onNewsDownloadFailed() {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Unable to load news", Toast.LENGTH_LONG).show();
    }

    @Override
    protected String getHelpMessage() {
        return getString(R.string.help_main);
    }
}
