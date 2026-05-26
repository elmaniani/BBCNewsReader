package com.example.bbcnewsreader.tasks;

import android.os.AsyncTask;
import android.util.Xml;

import com.example.bbcnewsreader.models.Article;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DownloadNewsTask extends AsyncTask<String, Integer, ArrayList<Article>> {

    public interface NewsDownloadListener {
        void onNewsDownloaded(ArrayList<Article> articles);
        void onNewsDownloadFailed();
    }

    private final NewsDownloadListener listener;

    public DownloadNewsTask(NewsDownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<Article> doInBackground(String... urls) {
        ArrayList<Article> articles = new ArrayList<>();
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urls[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, null);

            boolean insideItem = false;
            String title = "";
            String description = "";
            String pubDate = "";
            String link = "";
            String section = "World / US and Canada";

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();

                if (eventType == XmlPullParser.START_TAG) {
                    if ("item".equalsIgnoreCase(tagName)) {
                        insideItem = true;
                        title = description = pubDate = link = "";
                    } else if (insideItem && "title".equalsIgnoreCase(tagName)) {
                        title = parser.nextText();
                    } else if (insideItem && "description".equalsIgnoreCase(tagName)) {
                        description = parser.nextText();
                    } else if (insideItem && "pubDate".equalsIgnoreCase(tagName)) {
                        pubDate = parser.nextText();
                    } else if (insideItem && "link".equalsIgnoreCase(tagName)) {
                        link = parser.nextText();
                    }
                } else if (eventType == XmlPullParser.END_TAG && "item".equalsIgnoreCase(tagName)) {
                    insideItem = false;
                    articles.add(new Article(0, title, description, pubDate, link, section));
                    publishProgress(articles.size());
                }
                eventType = parser.next();
            }
            inputStream.close();
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return articles;
    }

    @Override
    protected void onPostExecute(ArrayList<Article> articles) {
        if (articles == null) {
            listener.onNewsDownloadFailed();
        } else {
            listener.onNewsDownloaded(articles);
        }
    }
}
