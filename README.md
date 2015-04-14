# Simple-Todo

 Simple Android Todo List App.

## Features
1.  Add items quickly and easily on the main Activity using the EditText View on the main activity
2. Delete items be pressing and holding on any item
3. Edit items by simply tapping on any item

## Bonus Features
1. Items are persisted in an SQLite DB. Each item is stored with the text and its position in the list.   Whenever any item is deleted or an item is added, all items are deleted and then resaved into the DB. This makes it easy to maintain proper position of each item, the downside being that each item is rewritten every time an item is added or deleted.