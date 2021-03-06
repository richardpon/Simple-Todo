package com.codepath.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TodoItemDatabase extends SQLiteOpenHelper {

    private static final String TAG = "TodoItemDatabase";

    // DB Info
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todoListDatabase";

    // Table Info
    private static final String TABLE_TODO = "todo_items";

    // Columns
    private static final String KEY_ID = "id";
    private static final String KEY_BODY = "body";
    private static final String KEY_POSITION = "position";

    public TodoItemDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Creates initial table
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_BODY + " TEXT,"
                + KEY_POSITION + " INTEGER"
                + ")";

        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 1) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
            onCreate(db);
        }
    }

    /**
     * Adds item to DB
     * @param item item to add
     */
    public void addTodoItem(TodoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BODY, item.getBody());
        values.put(KEY_POSITION, item.getPosition());

        db.insertOrThrow(TABLE_TODO, null, values);
        db.close();
    }

    /**
     * Gets a single item based upon id
     * @param id    int item id
     * @return      item with the given id
     */
    public TodoItem getTodoItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TODO,
                new String[] {KEY_ID, KEY_BODY, KEY_POSITION},
                KEY_ID + "= ?", new String[] { String.valueOf(id) },
                null, null, "id ASC", "100"
                );

        if (cursor != null) {
            cursor.moveToFirst();
        }

        // create model
        TodoItem item = new TodoItem(cursor.getString(1), cursor.getInt(2));
        item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));

        Log.i(TAG, "...");
        Log.i(TAG, "...");
        Log.i(TAG, "id="+item.getId()+" body="+item.getBody()+" position="+item.getPosition());

        return item;
    }



    /**
     * Gets a single item at the specified position
     * @param position int position of requested item
     * @return TodoItem
     */
    public TodoItem getTodoItemAtPosition(int position) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TODO,
                new String[] {KEY_ID, KEY_BODY, KEY_POSITION},
                KEY_POSITION + "= ?", new String[] { String.valueOf(position) },
                null, null, "id ASC", "100"
        );

        if (cursor != null) {
            cursor.moveToFirst();
        }

        // create model
        TodoItem item = new TodoItem(cursor.getString(1), cursor.getInt(2));
        item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));

        return item;
    }


    /**
     * Gets all items from DB
     * @return list of items
     */
    public List<TodoItem> getAllTodoItems() {

        List<TodoItem> todoItems = new ArrayList<TodoItem>();

        String selectQuery = "SELECT * FROM " + TABLE_TODO + " ORDER BY POSITION ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop through all rows adding to list
        if (cursor.moveToFirst()) {
            do {
                TodoItem item = new TodoItem(cursor.getString(1), cursor.getInt(2));
                item.setId(cursor.getInt(0));

                todoItems.add(item);
            } while (cursor.moveToNext());
        }

        return todoItems;
    }

    /**
     * @return total number of items
     */
    public int getTodoItemCount() {
        String countQuery = "SELECT * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    /**
     * Updates an item
     * @param item  item to update
     * @return      int number of rows updated (should be 1)
     */
    public int updateTodoItem(TodoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BODY, item.getBody());
        values.put(KEY_POSITION, item.getPosition());

        int result = db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getId())});

        db.close();
        return result;
    }

    /**
     * @param item  item to delete
     */
    public void deleteTodoItem(TodoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_TODO, KEY_ID + " = ?",
                new String[] {String.valueOf(item.getId()) });

        db.close();
    }

    /**
     * Delete item at given position
     * @param position int
     */
    public void deleteItemAtPosition(int position) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_TODO, KEY_POSITION + " = ?",
                new String[] {String.valueOf(position) });

        db.close();
    }

    /**
     * Deletes all items
     */
    public void deleteAllItems() {
        List<TodoItem> allTodoItems = this.getAllTodoItems();

        for (TodoItem curItem : allTodoItems) {
            this.deleteTodoItem(curItem);
        }
    }

    /**
     * Gets maximum position of all items
     * @return int
     */
    public int getMaxPosition() {

        String selectQuery = "SELECT MAX( " + KEY_POSITION + ")"
                + " FROM " + TABLE_TODO;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

}
