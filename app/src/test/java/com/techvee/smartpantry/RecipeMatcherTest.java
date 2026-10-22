package com.techvee.smartpantry;

import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.Assert.*;

/** Regression tests for the assignment's most important business rule. */
public class RecipeMatcherTest {
    private PantryItem item(String name, double quantity, String unit) {
        return new PantryItem(0, name, quantity, unit, null);
    }
    private Recipe recipe() {
        Recipe recipe = new Recipe(1, "Tomato eggs", "Test method");
        recipe.ingredients.add(item("tomato", 500, "g"));
        recipe.ingredients.add(item("egg", 2, "piece"));
        return recipe;
    }
    @Test public void excludesMissingIngredient() {
        assertFalse(RecipeMatcher.canMake(recipe(), Collections.singletonList(item("tomato", 500, "g"))));
    }
    @Test public void excludesInsufficientQuantity() {
        assertFalse(RecipeMatcher.canMake(recipe(), Arrays.asList(item("tomato", 499, "g"), item("egg", 2, "piece"))));
    }
    @Test public void normalisesNamesAndConvertsKilograms() {
        assertTrue(RecipeMatcher.canMake(recipe(), Arrays.asList(item(" TOMATOES ", 0.5, "kg"), item("eggs", 2, "piece"))));
    }
    @Test public void rejectsIncompatibleDimensions() {
        assertFalse(RecipeMatcher.canMake(recipe(), Arrays.asList(item("tomato", 500, "ml"), item("egg", 2, "piece"))));
    }
    @Test public void combinesCompatiblePantryEntries() {
        assertTrue(RecipeMatcher.canMake(recipe(), Arrays.asList(item("tomato", 200, "g"), item("tomatoes", 300, "g"), item("egg", 2, "piece"))));
    }
    @Test public void combinesRepeatedRecipeRequirements() {
        Recipe recipe = recipe();
        recipe.ingredients.add(item("tomato", 100, "g"));
        assertFalse(RecipeMatcher.canMake(recipe, Arrays.asList(item("tomato", 500, "g"), item("egg", 2, "piece"))));
    }
    @Test public void convertsVolumeUnits() {
        Recipe recipe = new Recipe(2, "Milk", "Test method");
        recipe.ingredients.add(item("milk", 250, "ml"));
        assertTrue(RecipeMatcher.canMake(recipe, Collections.singletonList(item("milk", 1, "cup"))));
    }
    @Test public void rejectsRecipeWithNoIngredients() {
        assertFalse(RecipeMatcher.canMake(new Recipe(3, "Empty", ""), Collections.emptyList()));
    }
}
