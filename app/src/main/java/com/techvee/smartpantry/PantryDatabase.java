package com.techvee.smartpantry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/** SQLite owns the persistent data; Activities will read fresh records when resumed. */
public final class PantryDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE = "pantry_items";

    public PantryDatabase(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry_items ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL CHECK(length(trim(name)) > 0), "
                + "quantity REAL NOT NULL CHECK(quantity > 0), "
                + "unit TEXT NOT NULL CHECK(length(trim(unit)) > 0), "
                + "expiry_date TEXT)");
        db.execSQL("CREATE TABLE recipes ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, method TEXT NOT NULL)");
        db.execSQL("CREATE TABLE recipe_ingredients ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "recipe_id INTEGER NOT NULL REFERENCES recipes(id) ON DELETE CASCADE, "
                + "name TEXT NOT NULL, quantity REAL NOT NULL CHECK(quantity > 0), "
                + "unit TEXT NOT NULL)");
        db.execSQL("CREATE INDEX recipe_ingredients_recipe ON recipe_ingredients(recipe_id)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Add explicit migrations here when changing a released schema; never drop user data.
        throw new IllegalStateException("No migration from " + oldVersion + " to " + newVersion);
    }

    public long addItem(PantryItem item) {
        return getWritableDatabase().insertOrThrow(TABLE, null, valuesFor(item));
    }

    public boolean updateItem(PantryItem item) {
        return getWritableDatabase().update(TABLE, valuesFor(item), "id = ?",
                new String[]{Long.toString(item.id)}) == 1;
    }

    public boolean deleteItem(long id) {
        return getWritableDatabase().delete(TABLE, "id = ?",
                new String[]{Long.toString(id)}) == 1;
    }

    public PantryItem getItem(long id) {
        try (Cursor cursor = getReadableDatabase().query(TABLE, null, "id = ?",
                new String[]{Long.toString(id)}, null, null, null)) {
            return cursor.moveToFirst() ? readItem(cursor) : null;
        }
    }

    public List<PantryItem> getItems() {
        List<PantryItem> items = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().query(TABLE, null, null, null,
                null, null, "name COLLATE NOCASE ASC, id ASC")) {
            while (cursor.moveToNext()) {
                items.add(readItem(cursor));
            }
        }
        return items;
    }

    private ContentValues valuesFor(PantryItem item) {
        ContentValues values = new ContentValues();
        values.put("name", item.name);
        values.put("quantity", item.quantity);
        values.put("unit", item.unit);
        values.put("expiry_date", item.expiryDate);
        return values;
    }

    private PantryItem readItem(Cursor cursor) {
        return new PantryItem(
                cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("quantity")),
                cursor.getString(cursor.getColumnIndexOrThrow("unit")),
                cursor.getString(cursor.getColumnIndexOrThrow("expiry_date")));
    }
}
