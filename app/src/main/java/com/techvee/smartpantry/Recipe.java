package com.techvee.smartpantry;
import java.util.ArrayList;
import java.util.List;

/** A recipe and all of its required ingredients, loaded from SQLite. */
public final class Recipe {
    public final long id;
    public final String name;
    public final String method;
    public final List<PantryItem> ingredients = new ArrayList<>();
    public Recipe(long id, String name, String method) {
        this.id = id;
        this.name = name;
        this.method = method;
    }
}
