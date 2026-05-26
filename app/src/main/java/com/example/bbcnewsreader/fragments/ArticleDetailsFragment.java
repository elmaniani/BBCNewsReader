package com.example.bbcnewsreader.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bbcnewsreader.R;

public class ArticleDetailsFragment extends Fragment {
    public static final String ARG_TITLE = "title";
    public static final String ARG_DESCRIPTION = "description";
    public static final String ARG_DATE = "date";
    public static final String ARG_LINK = "link";

    public static ArticleDetailsFragment newInstance(String title, String description, String date, String link) {
        ArticleDetailsFragment fragment = new ArticleDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESCRIPTION, description);
        args.putString(ARG_DATE, date);
        args.putString(ARG_LINK, link);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_details, container, false);
        Bundle args = getArguments();

        if (args != null) {
            ((TextView) view.findViewById(R.id.titleTextView)).setText(args.getString(ARG_TITLE, ""));
            ((TextView) view.findViewById(R.id.dateTextView)).setText(args.getString(ARG_DATE, ""));
            ((TextView) view.findViewById(R.id.descriptionTextView)).setText(args.getString(ARG_DESCRIPTION, ""));
            ((TextView) view.findViewById(R.id.linkTextView)).setText(args.getString(ARG_LINK, ""));
        }
        return view;
    }
}
