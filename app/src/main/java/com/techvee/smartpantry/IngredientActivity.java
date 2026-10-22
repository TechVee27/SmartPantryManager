package com.techvee.smartpantry;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;

/** One form handles both creating and editing a persisted ingredient. */
public class IngredientActivity extends AppCompatActivity {
    public static final String EXTRA_ITEM_ID = "pantry_item_id";
    private PantryDatabase database;
    private EditText nameField, quantityField, expiryField;
    private Spinner unitField;
    private long itemId;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingredient);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ingredient_root), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets keyboard = insets.getInsets(WindowInsetsCompat.Type.ime());
            view.setPadding(bars.left, bars.top, bars.right, Math.max(bars.bottom, keyboard.bottom));
            return insets;
        });
        database = new PantryDatabase(this);
        nameField = findViewById(R.id.edit_name);
        quantityField = findViewById(R.id.edit_quantity);
        expiryField = findViewById(R.id.edit_expiry);
        unitField = findViewById(R.id.edit_unit);
        ArrayAdapter<CharSequence> units = ArrayAdapter.createFromResource(this,
                R.array.ingredient_units, android.R.layout.simple_spinner_item);
        units.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitField.setAdapter(units);
        itemId = getIntent().getLongExtra(EXTRA_ITEM_ID, 0);
        if (itemId != 0) {
            PantryItem item = database.getItem(itemId);
            if (item == null) {
                Toast.makeText(this, R.string.item_missing, Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            ((android.widget.TextView) findViewById(R.id.form_title)).setText(R.string.edit_ingredient);
            nameField.setText(item.name);
            quantityField.setText(String.format(Locale.ROOT, "%s", item.quantity));
            expiryField.setText(item.expiryDate);
            int position = units.getPosition(item.unit);
            if (position >= 0) unitField.setSelection(position);
            findViewById(R.id.delete_ingredient).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.save_ingredient).setOnClickListener(view -> save());
        findViewById(R.id.cancel_ingredient).setOnClickListener(view -> finish());
        findViewById(R.id.delete_ingredient).setOnClickListener(view ->
            new AlertDialog.Builder(this).setTitle(R.string.delete_title)
                .setMessage(R.string.delete_message)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    if (database.deleteItem(itemId)) {
                        Toast.makeText(this, R.string.item_deleted, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, R.string.item_missing, Toast.LENGTH_SHORT).show();
                    }
                }).show());
    }

    private void save() {
        String name = nameField.getText().toString().trim();
        if (name.isEmpty() || name.length() > 80) {
            nameField.setError(getString(R.string.name_error));
            nameField.requestFocus();
            return;
        }
        double quantity;
        try {
            quantity = Double.parseDouble(quantityField.getText().toString().trim().replace(',', '.'));
            if (Double.isNaN(quantity) || Double.isInfinite(quantity) || quantity <= 0 || quantity > 1000000) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException error) {
            quantityField.setError(getString(R.string.quantity_error));
            quantityField.requestFocus();
            return;
        }
        String expiry = expiryField.getText().toString().trim();
        if (!expiry.isEmpty()) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
            format.setLenient(false);
            ParsePosition position = new ParsePosition(0);
            if (!expiry.matches("\\d{4}-\\d{2}-\\d{2}")
                    || format.parse(expiry, position) == null || position.getIndex() != expiry.length()) {
                expiryField.setError(getString(R.string.expiry_error));
                expiryField.requestFocus();
                return;
            }
        }
        PantryItem item = new PantryItem(itemId, name, quantity,
                unitField.getSelectedItem().toString(), expiry.isEmpty() ? null : expiry);
        try {
            if (itemId == 0) database.addItem(item);
            else if (!database.updateItem(item)) {
                Toast.makeText(this, R.string.item_missing, Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, R.string.item_saved, Toast.LENGTH_SHORT).show();
            finish();
        } catch (android.database.SQLException error) {
            Toast.makeText(this, R.string.save_failed, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
