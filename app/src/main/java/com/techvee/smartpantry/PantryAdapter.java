package com.techvee.smartpantry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/** Binds database records to reusable ListView rows. */
public final class PantryAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final List<PantryItem> items = new ArrayList<>();

    public PantryAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void replaceItems(List<PantryItem> updatedItems) {
        items.clear();
        items.addAll(updatedItems);
        notifyDataSetChanged();
    }

    @Override public int getCount() { return items.size(); }
    @Override public PantryItem getItem(int position) { return items.get(position); }
    @Override public long getItemId(int position) { return getItem(position).id; }
    @Override public boolean hasStableIds() { return true; }

    @Override
    public View getView(int position, View recycled, ViewGroup parent) {
        View row = recycled;
        if (row == null) {
            row = inflater.inflate(R.layout.item_pantry, parent, false);
        }
        PantryItem item = getItem(position);
        ((TextView) row.findViewById(R.id.ingredient_name)).setText(item.name);
        String detail = NumberFormat.getNumberInstance().format(item.quantity) + " " + item.unit;
        if (item.expiryDate != null && !item.expiryDate.isEmpty()) {
            detail += "  |  Expires " + item.expiryDate;
        }
        ((TextView) row.findViewById(R.id.ingredient_detail)).setText(detail);
        return row;
    }
}
