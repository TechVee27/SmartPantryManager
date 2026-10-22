package com.techvee.smartpantry;

import android.os.Bundle;
import android.content.Intent;
import android.widget.ListView;
import java.util.List;
import java.util.Collections;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private PantryDatabase database;
    private PantryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.pantry_toolbar);
        setSupportActionBar(toolbar);
        database = new PantryDatabase(this);
        adapter = new PantryAdapter(this);
        ListView list = findViewById(R.id.pantry_list);
        list.setAdapter(adapter);
        list.setEmptyView(findViewById(R.id.empty_pantry));
        findViewById(R.id.add_ingredient).setOnClickListener(view ->
                startActivity(new Intent(this, IngredientActivity.class)));
        findViewById(R.id.open_recipes).setOnClickListener(view ->
                startActivity(new Intent(this, RecipesActivity.class)));
        findViewById(R.id.open_settings).setOnClickListener(view ->
                startActivity(new Intent(this, SettingsActivity.class)));
        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, IngredientActivity.class);
            intent.putExtra(IngredientActivity.EXTRA_ITEM_ID, id);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.pantry_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_add) {
            startActivity(new Intent(this, IngredientActivity.class));
            return true;
        }
        if (id == R.id.nav_recipes) {
            startActivity(new Intent(this, RecipesActivity.class));
            return true;
        }
        if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<PantryItem> items = database.getItems();
        if (getSharedPreferences(SettingsActivity.PREFERENCES, MODE_PRIVATE)
                .getBoolean(SettingsActivity.EXPIRY_FIRST, false)) {
            Collections.sort(items, (first, second) -> {
                String a = first.expiryDate;
                String b = second.expiryDate;
                boolean noA = a == null || a.isEmpty();
                boolean noB = b == null || b.isEmpty();
                if (noA && noB) return first.name.compareToIgnoreCase(second.name);
                if (noA) return 1;
                if (noB) return -1;
                int dateOrder = a.compareTo(b);
                return dateOrder == 0 ? first.name.compareToIgnoreCase(second.name) : dateOrder;
            });
        }
        adapter.replaceItems(items);
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
