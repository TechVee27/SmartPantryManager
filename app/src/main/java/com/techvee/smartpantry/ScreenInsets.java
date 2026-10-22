package com.techvee.smartpantry;
import android.view.View;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/** Keeps content clear of system bars on edge-to-edge Android versions. */
final class ScreenInsets {
    private ScreenInsets() { }
    static void apply(View root) {
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
        ViewCompat.requestApplyInsets(root);
    }
}
