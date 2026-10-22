package com.techvee.smartpantry;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.NumberFormat;

/** The Intent carries a stable database ID instead of a potentially stale recipe object. */
public class RecipeDetailActivity extends AppCompatActivity {
    public static final String EXTRA_RECIPE_ID = "recipe_id";
    private PantryDatabase database;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_recipe_detail);
        ScreenInsets.apply(findViewById(R.id.detail_root));
        database = new PantryDatabase(this);
        findViewById(R.id.back_to_recipes).setOnClickListener(view -> finish());
        long id = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);
        Recipe selected = null;
        for (Recipe recipe : database.getRecipes()) {
            if (recipe.id == id) { selected = recipe; break; }
        }
        if (selected == null) {
            Toast.makeText(this, R.string.recipe_missing, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ((TextView) findViewById(R.id.detail_name)).setText(selected.name);
        StringBuilder ingredients = new StringBuilder();
        for (PantryItem item : selected.ingredients) {
            if (ingredients.length() > 0) ingredients.append("\n\n");
            ingredients.append("• ").append(item.name).append(" — ")
                    .append(NumberFormat.getNumberInstance().format(item.quantity))
                    .append(" ").append(item.unit);
        }
        ((TextView) findViewById(R.id.detail_ingredients)).setText(ingredients.toString());
        ((TextView) findViewById(R.id.detail_method)).setText(selected.method);
    }

    @Override protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
