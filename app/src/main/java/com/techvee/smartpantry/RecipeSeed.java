package com.techvee.smartpantry;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
final class RecipeSeed {
    private RecipeSeed() { }
    static void insertAll(SQLiteDatabase db) {
        add(db, "Tomato and egg scramble", "1. Chop the tomato.\n2. Heat the oil in a pan and soften the tomato.\n3. Beat in the eggs and stir until fully cooked.",
            new String[]{"egg","tomato","oil"},
            new double[]{2,150,5},
            new String[]{"piece","g","ml"});
        add(db, "Cheese omelette", "1. Beat the eggs.\n2. Heat oil in a pan and add eggs.\n3. Add grated cheese, fold, and cook until set.",
            new String[]{"egg","cheese","oil"},
            new double[]{2,40,5},
            new String[]{"piece","g","ml"});
        add(db, "Banana oat pancakes", "1. Mash banana and mix with egg and oats.\n2. Heat oil in a pan.\n3. Cook small pancakes on both sides until set.",
            new String[]{"banana","egg","oat","oil"},
            new double[]{1,1,50,5},
            new String[]{"piece","piece","g","ml"});
        add(db, "Milk porridge", "1. Combine oats and milk in a saucepan.\n2. Simmer gently, stirring, until thick and cooked.",
            new String[]{"oat","milk"},
            new double[]{60,250},
            new String[]{"g","ml"});
        add(db, "Tomato pasta", "1. Boil pasta in the water until tender and drain.\n2. Chop tomatoes and soften in oil.\n3. Toss pasta through the tomato sauce.",
            new String[]{"pasta","tomato","oil","water"},
            new double[]{150,250,10,1000},
            new String[]{"g","g","ml","ml"});
        add(db, "Egg fried rice", "1. Heat oil and scramble eggs until cooked.\n2. Add cooked rice and stir until piping hot throughout. Use safely stored cooked rice.",
            new String[]{"cooked rice","egg","oil"},
            new double[]{200,2,10},
            new String[]{"g","piece","ml"});
        add(db, "Potato and onion hash", "1. Dice potatoes small and slice onion.\n2. Fry in oil over medium heat, stirring, until potatoes are tender and browned.",
            new String[]{"potato","onion","oil"},
            new double[]{300,100,15},
            new String[]{"g","g","ml"});
        add(db, "Carrot soup", "1. Soften chopped onion in oil.\n2. Add sliced carrot and water.\n3. Simmer until tender, then mash or blend carefully.",
            new String[]{"carrot","onion","water","oil"},
            new double[]{300,100,500,5},
            new String[]{"g","g","ml","ml"});
        add(db, "Tomato toast", "1. Toast the bread.\n2. Slice tomatoes and arrange over toast.",
            new String[]{"bread","tomato"},
            new double[]{2,100},
            new String[]{"piece","g"});
        add(db, "Cheese toast", "1. Cover bread with grated cheese.\n2. Grill until cheese melts and bread is toasted.",
            new String[]{"bread","cheese"},
            new double[]{2,50},
            new String[]{"piece","g"});
        add(db, "French toast", "1. Beat egg and milk.\n2. Dip bread in the mixture.\n3. Fry in oil on both sides until the egg is fully cooked.",
            new String[]{"bread","egg","milk","oil"},
            new double[]{2,1,60,5},
            new String[]{"piece","piece","ml","ml"});
        add(db, "Banana milk smoothie", "1. Peel and slice banana.\n2. Blend with milk until smooth. Serve immediately.",
            new String[]{"banana","milk"},
            new double[]{1,250},
            new String[]{"piece","ml"});
        add(db, "Apple oat bowl", "1. Chop apple.\n2. Simmer apple, oats and milk together until the oats are cooked.",
            new String[]{"apple","oat","milk"},
            new double[]{1,60,250},
            new String[]{"piece","g","ml"});
        add(db, "Chickpea tomato stew", "1. Soften chopped onion in oil.\n2. Add chopped tomato and simmer.\n3. Add cooked chickpeas and heat thoroughly.",
            new String[]{"cooked chickpea","tomato","onion","oil"},
            new double[]{250,200,100,10},
            new String[]{"g","g","g","ml"});
        add(db, "Spinach scrambled eggs", "1. Wilt washed spinach in oil.\n2. Add beaten eggs and stir until fully cooked.",
            new String[]{"spinach","egg","oil"},
            new double[]{80,2,5},
            new String[]{"g","piece","ml"});
        add(db, "Buttered pasta", "1. Boil pasta in the water until tender and drain.\n2. Stir butter through the hot pasta.",
            new String[]{"pasta","butter","water"},
            new double[]{150,20,1000},
            new String[]{"g","g","ml"});
        add(db, "Mashed potatoes", "1. Peel, dice and boil potatoes in water until tender.\n2. Drain and mash with milk and butter.",
            new String[]{"potato","milk","butter","water"},
            new double[]{400,80,20,1000},
            new String[]{"g","ml","g","ml"});
        add(db, "Cucumber tomato salad", "1. Wash and chop cucumber and tomato.\n2. Toss with oil and serve.",
            new String[]{"cucumber","tomato","oil"},
            new double[]{150,150,10},
            new String[]{"g","g","ml"});
        add(db, "Peanut butter banana toast", "1. Toast bread and spread with peanut butter.\n2. Top with sliced banana.",
            new String[]{"bread","peanut butter","banana"},
            new double[]{2,30,1},
            new String[]{"piece","g","piece"});
        add(db, "Carrot rice bowl", "1. Grate carrot and soften in oil.\n2. Add cooked rice and heat until piping hot throughout. Use safely stored cooked rice.",
            new String[]{"cooked rice","carrot","oil"},
            new double[]{200,100,10},
            new String[]{"g","g","ml"});
    }
    private static void add(SQLiteDatabase db, String name, String method,
            String[] names, double[] quantities, String[] units) {
        ContentValues recipe = new ContentValues();
        recipe.put("name", name);
        recipe.put("method", method);
        long id = db.insertOrThrow("recipes", null, recipe);
        for (int i = 0; i < names.length; i++) {
            ContentValues ingredient = new ContentValues();
            ingredient.put("recipe_id", id);
            ingredient.put("name", names[i]);
            ingredient.put("quantity", quantities[i]);
            ingredient.put("unit", units[i]);
            db.insertOrThrow("recipe_ingredients", null, ingredient);
        }
    }
}