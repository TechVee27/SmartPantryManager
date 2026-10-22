package com.techvee.smartpantry;

/** One stored pantry record. A zero ID represents an item not yet saved. */
public final class PantryItem {
    public final long id;
    public final String name;
    public final double quantity;
    public final String unit;
    /** Optional expiry date in yyyy-MM-dd format; null means no date supplied. */
    public final String expiryDate;

    public PantryItem(long id, String name, double quantity, String unit, String expiryDate) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Enter an ingredient name.");
        }
        if (Double.isNaN(quantity) || Double.isInfinite(quantity) || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive number.");
        }
        if (unit == null || unit.trim().isEmpty()) {
            throw new IllegalArgumentException("Choose a unit.");
        }
        this.id = id;
        this.name = name.trim();
        this.quantity = quantity;
        this.unit = unit.trim();
        this.expiryDate = expiryDate;
    }
}
