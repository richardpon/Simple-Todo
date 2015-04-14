This Android app is a simple todo list. It follows CodePath's [Intro to Android](http://courses.codepath.com/snippets/intro_to_android/prework.md)

Approximate Total Time Spent: 8 hours

Completed User Stories:

* [x] Required: User can add items using the EditText View and button at the bottom of the main activity
* [x] Required: User can delete items by pressing and holding an any item
* [x] Required: User can edit the text of an item by tapping an item entering new text, and saving
* [x] Optional: Items are persisted in a SQLite DB.

Notes:

Items are persisted as a text-position pair. Whenever an item is added or deleted, all items are deleted from the DB and resaved. This approach makes it simple to maintain correct position of every item in the list. The tradeoff is efficiency.

Walkthrough:

![Video Walkthrough](SimpleTodo_Walkthrough.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/)

