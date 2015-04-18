package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private final int REQUEST_CODE = 20;

    ArrayList<TodoItem> items;
    ArrayAdapter<TodoItem> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);

        //debug
        TodoItemDatabase db = new TodoItemDatabase(this);
        //db.deleteAllItems();

        //read items from DB
        readItems();

        itemsAdapter = new TodoItemsAdapter(this, this.items);

        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Add item when user clicks the "add item" button
     * @param v View
     */
    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();

        //calculate position of new item
        int position = this.items.size() + 1;

        //add new item to adapter
        TodoItem newItem = new TodoItem(itemText, position);
        itemsAdapter.add(newItem);
        etNewItem.setText("");

        //write new item to DB
        this.writeItem(newItem);
    }

    private void setupListViewListener() {

        // A long click deletes the item
        lvItems.setOnItemLongClickListener(
            new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapter,
                        View view, int pos, long id) {
                    items.remove(pos);
                    itemsAdapter.notifyDataSetChanged();

                    deleteItemAtPosition(pos);

                    return true;
                }
        });

        // A regular click edits the item
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {

                        TodoItem item = items.get(pos);

                        String itemText = item.getBody();

                        //launch the edit item activity
                        launchEditItemActivity(pos, itemText, item.getId());
                    }
                }

        );
    }

    /**
     * Launches the edit item activity
     * @param pos   int position of the item being edited
     * @param text  String text of the item being edited
     * @param id    int item id
     */
    private void launchEditItemActivity(int pos, String text, int id) {
        //Is there a difference to param 1 being MainActivity.this or this?
        Intent i = new Intent(this, EditItemActivity.class);

        //Add pos and id into the bundle
        i.putExtra("position", pos);
        i.putExtra("text", text);
        i.putExtra("id", id);

        startActivityForResult(i, REQUEST_CODE);
    }



    /**
     * Read all items from the DB
     */
    private void readItems() {

        //read items from DB
        TodoItemDatabase db = new TodoItemDatabase(this);

        this.items = (ArrayList) db.getAllTodoItems();
    }

    /**
     * Writes a single item to the DB
     * @param item TodoItem
     */
    private void writeItem(TodoItem item) {
        TodoItemDatabase db = new TodoItemDatabase(this);

        db.addTodoItem(item);
    }

    /**
     * Deletes item with given position
     * @param position int
     */
    private void deleteItemAtPosition(int position) {
        TodoItemDatabase db = new TodoItemDatabase(this);
        db.deleteItemAtPosition(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String text = data.getExtras().getString("text");
            int position = data.getExtras().getInt("position");
            int id = data.getExtras().getInt("id");

            Toast.makeText(this, "Updated "+text, Toast.LENGTH_SHORT).show();

            updateItemWithNewText(text, position, id);
        }
    }

    /**
     * Updates item with given position and text
     * @param text String
     * @param postion int
     * @param id int
     */
    private void updateItemWithNewText(String text, int position, int id) {

        //create new item
        TodoItem newItem = new TodoItem(text, position);
        newItem.setId(id);

        items.set(position, newItem);
        itemsAdapter.notifyDataSetChanged();

        //update item in DB
        TodoItemDatabase db = new TodoItemDatabase(this);
        //TodoItem item = db.getTodoItem(id);
        newItem.setBody(text);
        db.updateTodoItem(newItem);
    }

}
