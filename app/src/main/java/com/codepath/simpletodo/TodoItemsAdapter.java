package com.codepath.simpletodo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TodoItemsAdapter extends ArrayAdapter<TodoItem>{

    List<TodoItem> items;

    public TodoItemsAdapter(Context context, List<TodoItem> items) {
        super(context, 0, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get data
        TodoItem item = this.items.get(position);

        // Reuse View?
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }

        // Get data
        TextView tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);

        // Populate data
        tvPosition.setText(Integer.toString(position + 1));
        tvBody.setText(item.getBody());

        return convertView;
    }


}
