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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private final int REQUEST_CODE = 20;

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

//    TodoItemDatabase db;
//
//    public void MainActivity() {
//        this.db = new TodoItemDatabase(this);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);

        //read items from filesystem
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

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();

        itemsAdapter.add(itemText);
        etNewItem.setText("");

        //write items to filesystem
        writeItems();

    }

    private void setupListViewListener() {
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

    private void launchEditItemActivity(int pos, String text) {
        //Is there a difference to param 1 being MainActivity.this or this?
        Intent i = new Intent(this, EditItemActivity.class);

        //Add pos and id into the bundle
        i.putExtra("position", pos);
        i.putExtra("text", text);

        startActivityForResult(i, REQUEST_CODE);
    }



    /**
     * Read items from filesystem
     */
    private void readItems() {

        //Read from filesystem
//        try {
//            items = new ArrayList<String>(FileUtils.readLines(getTodoFile()));
//        } catch (IOException e) {
//            items = new ArrayList<String>();
//        }


        //read items from DB
        TodoItemDatabase db = new TodoItemDatabase(this);
        //db.addTodoItem(new TodoItem("Get peas", 1));

        List<TodoItem> todoItems = db.getAllTodoItems();

        Log.i(TAG,"1");
        Log.i(TAG,"reading items\n");
        //Log.i(TAG,"count="+todoItems.size());

        this.items = new ArrayList<String>();
        for (TodoItem todoItem : todoItems) {
            String log = "todoItem===id="+todoItem.getId() + " pos=" + todoItem.getPosition() + " body=" + todoItem.getBody();
            Log.i(TAG, log);

            String body = todoItem.getBody();
            this.items.add(body);
        }


    }

    /**
     * Write items to persistent storage
     * This actually deletes all items and resaves
     */
    private void writeItems() {

        //write all items to filesystem
//        try {
//            FileUtils.writeLines(getTodoFile(), items);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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



    /**
     * Get common File that stores todo list
     * @return File
     */
    private File getTodoFile() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");

        return todoFile;
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
