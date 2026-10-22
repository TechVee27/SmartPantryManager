package com.techvee.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

/** Only complete matches are admitted to the displayed list. */
public class RecipesActivity extends AppCompatActivity {
    private PantryDatabase database;
    private final List<Recipe> suggestions = new ArrayList<>();

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_recipes);
        ScreenInsets.apply(findViewById(R.id.recipes_root));
        database = new PantryDatabase(this);
        findViewById(R.id.back_to_pantry).setOnClickListener(view -> finish());
        ListView list = findViewById(R.id.recipe_list);
        list.setEmptyView(findViewById(R.id.empty_recipes));
        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, suggestions.get(position).id);
            startActivity(intent);
        });
    }

    @Override protected void onResume() {
        super.onResume();
        suggestions.clear();
        List<String> names = new ArrayList<>();
        List<PantryItem> pantry = database.getItems();
        for (Recipe recipe : database.getRecipes()) {
            if (RecipeMatcher.canMake(recipe, pantry)) {
                suggestions.add(recipe);
                names.add(recipe.name);
            }
        }
        ListView list = findViewById(R.id.recipe_list);
        list.setAdapter(new ArrayAdapter<>(this, R.layout.item_recipe, R.id.recipe_name, names));
        ((TextView) findViewById(R.id.recipe_count)).setText(
                getResources().getQuantityString(R.plurals.recipe_matches, names.size(), names.size()));
    }

    @Override protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
