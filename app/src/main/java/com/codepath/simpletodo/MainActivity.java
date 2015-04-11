package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

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
    }

    /**
     * Read items from filesystem
     */
    private void readItems() {
        try {
            items = new ArrayList<String>(FileUtils.readLines(getTodoFile()));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    /**
     * Write items to the filesystem
     */
    private void writeItems() {
        try {
            FileUtils.writeLines(getTodoFile(), items);
        } catch (IOException e) {
            e.printStackTrace();
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

}
