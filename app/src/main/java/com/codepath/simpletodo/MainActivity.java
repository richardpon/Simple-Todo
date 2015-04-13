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

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);

        //read items from DB
        readItems();

        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);

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

        itemsAdapter.add(itemText);
        etNewItem.setText("");

        //write items to filesystem
        writeItems();

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

                    //write to filesystem
                    writeItems();

                    return true;
                }
        });

        // A regular click edits the item
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {

                        String itemText = items.get(pos);

                        //launch the edit item activity
                        launchEditItemActivity(pos, itemText);
                    }
                }

        );
    }

    /**
     * Launches the edit item activity
     * @param pos   int position of the item being edited
     * @param text  String text of the item being edited
     */
    private void launchEditItemActivity(int pos, String text) {
        //Is there a difference to param 1 being MainActivity.this or this?
        Intent i = new Intent(this, EditItemActivity.class);

        //Add pos and id into the bundle
        i.putExtra("position", pos);
        i.putExtra("text", text);

        startActivityForResult(i, REQUEST_CODE);
    }



    /**
     * Read items from the DB
     */
    private void readItems() {

        //read items from DB
        TodoItemDatabase db = new TodoItemDatabase(this);

        List<TodoItem> todoItems = db.getAllTodoItems();

        this.items = new ArrayList<String>();
        for (TodoItem todoItem : todoItems) {
            String body = todoItem.getBody();
            this.items.add(body);
        }
    }

    /**
     * Write items to persistent storage
     * This actually deletes all items and resaves
     */
    private void writeItems() {

        //delete items from DB and then rewrite them all in the current order
        //write all items to DB
        TodoItemDatabase db = new TodoItemDatabase(this);

        db.deleteAllItems();

        int position = 1;
        for (String itemText : this.items) {

            db.addTodoItem(new TodoItem(itemText, position));
            position++;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String text = data.getExtras().getString("text");
            int position = data.getExtras().getInt("position");

            Toast.makeText(this, "Updated "+text, Toast.LENGTH_SHORT).show();

            updateItemWithNewText(text, position);

            this.writeItems();
        }
    }

    /**
     * Updates item with given position and text
     * @param text String
     * @param postion int
     */
    private void updateItemWithNewText(String text, int position) {

        items.set(position, text);

        itemsAdapter.notifyDataSetChanged();
    }

}
