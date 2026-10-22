package com.techvee.smartpantry;

import android.os.Bundle;
import android.content.Intent;
import android.widget.ListView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/** Displays the latest persisted pantry whenever this screen becomes visible. */
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
        database = new PantryDatabase(this);
        adapter = new PantryAdapter(this);
        ListView list = findViewById(R.id.pantry_list);
        list.setAdapter(adapter);
        list.setEmptyView(findViewById(R.id.empty_pantry));
        findViewById(R.id.add_ingredient).setOnClickListener(view ->
                startActivity(new Intent(this, IngredientActivity.class)));
        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, IngredientActivity.class);
            intent.putExtra(IngredientActivity.EXTRA_ITEM_ID, id);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.replaceItems(database.getItems());
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
