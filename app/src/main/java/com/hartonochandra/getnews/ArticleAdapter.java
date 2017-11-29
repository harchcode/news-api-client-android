package com.hartonochandra.getnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.util.ArrayList;

public class ArticleAdapter extends BaseAdapter {
    private Context           context;
    private LayoutInflater    inflater;
    private ArrayList<Article> articles;

    public ArticleAdapter(Context context, ArrayList<Article> articles) {
        this.context = context;
        this.articles = articles;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Object getItem(int i) {
        return articles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = inflater.inflate(R.layout.list_item_article, viewGroup, false);
        Article article = (Article)getItem(i);

        TextView articleNameTextView = rowView.findViewById(R.id.articleTitleTextView);
        articleNameTextView.setText(article.title);

        TextView articleDescriptionTextView = rowView.findViewById(R.id.articleDescriptionTextView);
        articleDescriptionTextView.setText(article.description);

        DateTimeZone localTimeZone = DateTimeZone.getDefault();
        DateTime utcTime = DateTime.parse(article.publishedAt);
        DateTime localTime = utcTime.withZone(localTimeZone);

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendDayOfWeekShortText()
                .appendLiteral(", ")
                .appendDayOfMonth(2)
                .appendLiteral('-')
                .appendMonthOfYearShortText()
                .appendLiteral('-')
                .appendYear(4, 4)
                .appendLiteral(' ')
                .appendHourOfDay(2)
                .appendLiteral(':')
                .appendMinuteOfHour(2)
                .appendLiteral(':')
                .appendSecondOfMinute(2)
                .appendLiteral(' ')
                .appendLiteral('(')
                .appendTimeZoneShortName()
                .appendLiteral(')')
                .toFormatter();

        TextView articleDateTextView = rowView.findViewById(R.id.articleDateTextView);
        articleDateTextView.setText(formatter.print(localTime));

        return rowView;
    }
}
