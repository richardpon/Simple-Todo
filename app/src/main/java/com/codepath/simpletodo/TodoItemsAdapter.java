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

    //lookup cache
    private static class ViewHolder {
        TextView position;
        TextView body;
    }

    public TodoItemsAdapter(Context context, List<TodoItem> items) {
        super(context, 0, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get data
        TodoItem item = this.items.get(position);

        // Reuse View?
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            //LayoutInflater inflater = LayoutInflater.from(getContext());
            //convertView = inflater.inflate(R.layout.todo_item, parent, false);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
            viewHolder.body = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.position = (TextView) convertView.findViewById(R.id.tvPosition);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate data
        viewHolder.position.setText(Integer.toString(item.getPosition()));
        viewHolder.body.setText(item.getBody());

        return convertView;
    }


}
