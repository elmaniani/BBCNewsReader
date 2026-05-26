package com.example.bbcnewsreader.models;

public class Article {
    private long id;
    private String title;
    private String description;
    private String pubDate;
    private String link;
    private String section;

    public Article(long id, String title, String description, String pubDate, String link, String section) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.link = link;
        this.section = section;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPubDate() { return pubDate; }
    public String getLink() { return link; }
    public String getSection() { return section; }
    public void setId(long id) { this.id = id; }

    @Override
    public String toString() {
        return title;
    }
}
