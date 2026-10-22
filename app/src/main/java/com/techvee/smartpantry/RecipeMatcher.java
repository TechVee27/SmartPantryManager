package com.techvee.smartpantry;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Pure Java business logic: every requirement must be fully covered. */
public final class RecipeMatcher {
    private RecipeMatcher() { }

    public static String canonicalName(String name) {
        String value = name.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
        switch (value) {
            case "tomatoes": return "tomato";
            case "potatoes": return "potato";
            case "eggs": return "egg";
            case "onions": return "onion";
            case "carrots": return "carrot";
            case "bananas": return "banana";
            case "apples": return "apple";
            case "peppers": return "pepper";
            case "bell peppers": return "bell pepper";
            case "chickpeas": return "chickpea";
            case "oats": return "oat";
            default: return value;
        }
    }

    private static String dimension(String unit) {
        switch (unit.toLowerCase(Locale.ROOT)) {
            case "g": case "kg": return "mass";
            case "ml": case "l": case "tsp": case "tbsp": case "cup": return "volume";
            case "piece": case "pieces": return "count";
            default: return "unknown:" + unit;
        }
    }

    private static double factor(String unit) {
        switch (unit.toLowerCase(Locale.ROOT)) {
            case "kg": case "l": return 1000;
            case "tsp": return 5;
            case "tbsp": return 15;
            case "cup": return 250;
            default: return 1;
        }
    }

    private static String key(PantryItem item) {
        return canonicalName(item.name) + "|" + dimension(item.unit);
    }

    private static double quantityOrZero(Map<String, Double> quantities, String key) {
        Double value = quantities.get(key);
        return value == null ? 0.0 : value;
    }

    public static boolean canMake(Recipe recipe, List<PantryItem> pantry) {
        if (recipe.ingredients.isEmpty()) return false;
        Map<String, Double> available = new HashMap<>();
        for (PantryItem item : pantry) {
            String key = key(item);
            available.put(key, quantityOrZero(available, key) + item.quantity * factor(item.unit));
        }
        // Aggregate repeated requirements too, so the same stock cannot be counted twice.
        Map<String, Double> required = new HashMap<>();
        for (PantryItem item : recipe.ingredients) {
            String key = key(item);
            required.put(key, quantityOrZero(required, key) + item.quantity * factor(item.unit));
        }
        for (Map.Entry<String, Double> entry : required.entrySet()) {
            double stock = quantityOrZero(available, entry.getKey());
            if (stock + 1e-9 < entry.getValue()) return false;
        }
        return true;
    }
}
