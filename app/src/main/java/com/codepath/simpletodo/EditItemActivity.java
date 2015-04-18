package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class EditItemActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private int itemPosition;
    private int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        //get item's text and populate into EditTextView
        String text = getIntent().getStringExtra("text");
        populateEditText(text);

        itemPosition = getIntent().getIntExtra("position", 0);
        itemId = getIntent().getIntExtra("id", 0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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
     * Populates the Edit Text field with the given value
     * @param String text
     */
    private void populateEditText(String text) {
        EditText etEditText = (EditText) findViewById(R.id.etEditText);
        etEditText.setText(text);
    }

    public void onEditItem(View view) {
        EditText etEditText = (EditText) findViewById(R.id.etEditText);
        String newItemText = etEditText.getText().toString();

        Intent data = new Intent();
        data.putExtra("text", newItemText);
        data.putExtra("position", itemPosition);
        data.putExtra("id", itemId);

        setResult(RESULT_OK, data);
        finish();
    }
}
