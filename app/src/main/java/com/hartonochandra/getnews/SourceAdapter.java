package com.hartonochandra.getnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SourceAdapter extends BaseAdapter {
    private Context           context;
    private LayoutInflater    inflater;
    private ArrayList<Source> sources;

    public SourceAdapter(Context context, ArrayList<Source> sources) {
        this.context = context;
        this.sources = sources;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sources.size();
    }

    @Override
    public Object getItem(int i) {
        return sources.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = inflater.inflate(R.layout.list_item_source, viewGroup, false);
        Source source = (Source)getItem(i);

        TextView sourceNameTextView = rowView.findViewById(R.id.sourceNameTextView);
        sourceNameTextView.setText(source.name);

        TextView sourceDescriptionTextView = rowView.findViewById(R.id.sourceDescriptionTextView);
        sourceDescriptionTextView.setText(source.description);

        return rowView;
    }
}
