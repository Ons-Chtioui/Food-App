package com.example.foodapp.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.foodapp.Domain.Category;
import com.example.foodapp.Domain.Foods;
import com.example.foodapp.Domain.Price;
import com.example.foodapp.Domain.Time;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "foodapp.db";
    private static final int DATABASE_VERSION = 10; // augmenter la version

    // Tables
    public static final String TABLE_LOCATION = "locations";
    public static final String TABLE_FOOD = "foods";
    public static final String TABLE_CATEGORY = "categories";
    public static final String TABLE_PRICE = "prices";
    public static final String TABLE_TIME = "times";

    // Locations
    public static final String COLUMN_LOC_ID = "id";
    public static final String COLUMN_LOC_NAME = "loc";

    // Foods
    public static final String COLUMN_FOOD_ID = "id";
    public static final String COLUMN_CATEGORY_ID = "CategoryId";
    public static final String COLUMN_LOCATION_ID = "LocationId";
    public static final String COLUMN_TITLE = "Title";
    public static final String COLUMN_DESC = "Description";
    public static final String COLUMN_PRICE = "Price";
    public static final String COLUMN_IMAGE = "ImagePath";
    public static final String COLUMN_STAR = "Star";
    public static final String COLUMN_TIME_ID = "TimeId";
    public static final String COLUMN_TIME_VALUE = "TimeValue";
    public static final String COLUMN_PRICE_ID = "PriceId";
    public static final String COLUMN_BESTFOOD = "BestFood";
    public static final String COLUMN_NUMBER_CART = "numberInCart";

    // Categories
    public static final String COLUMN_CAT_ID = "id";
    public static final String COLUMN_CAT_NAME = "Name";
    public static final String COLUMN_CAT_IMAGE = "ImagePath";

    // Price
    public static final String COLUMN_PRICE_TABLE_ID = "id";
    public static final String COLUMN_PRICE_VALUE = "Value";

    // Time
    public static final String COLUMN_TIME_TABLE_ID = "id";
    public static final String COLUMN_TIME_TABLE_VALUE = "Value";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Locations
        db.execSQL("CREATE TABLE " + TABLE_LOCATION + " (" +
                COLUMN_LOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LOC_NAME + " TEXT NOT NULL);");

        // Foods avec tous les attributs
        db.execSQL("CREATE TABLE " + TABLE_FOOD + " (" +
                COLUMN_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CATEGORY_ID + " INTEGER," +
                COLUMN_LOCATION_ID + " INTEGER," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_DESC + " TEXT," +
                COLUMN_PRICE + " REAL," +
                COLUMN_IMAGE + " TEXT," +
                COLUMN_STAR + " REAL," +
                COLUMN_TIME_ID + " INTEGER," +
                COLUMN_TIME_VALUE + " INTEGER," +
                COLUMN_PRICE_ID + " INTEGER," +
                COLUMN_BESTFOOD + " INTEGER," +
                COLUMN_NUMBER_CART + " INTEGER" +
                ");");

        // Categories
        db.execSQL("CREATE TABLE " + TABLE_CATEGORY + " (" +
                COLUMN_CAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CAT_NAME + " TEXT," +
                COLUMN_CAT_IMAGE + " TEXT);");

        // Price
        db.execSQL("CREATE TABLE " + TABLE_PRICE + " (" +
                COLUMN_PRICE_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PRICE_VALUE + " TEXT);");

        // Time
        db.execSQL("CREATE TABLE " + TABLE_TIME + " (" +
                COLUMN_TIME_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TIME_TABLE_VALUE + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIME);
        onCreate(db);
    }

    // ============================================================
    // LOCATIONS
    // ============================================================
    public void addLocation(String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOC_NAME, location);
        db.insert(TABLE_LOCATION, null, values);
        db.close();
    }

    public ArrayList<String> getAllLocations() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_LOCATION, null);

        if (c.moveToFirst()) {
            do {
                list.add(c.getString(c.getColumnIndexOrThrow(COLUMN_LOC_NAME)));
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    // ============================================================
    // FOODS
    // ============================================================
    public long addFood(Foods f) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CATEGORY_ID, f.getCategoryId());
        cv.put(COLUMN_LOCATION_ID, f.getLocationId());
        cv.put(COLUMN_TITLE, f.getTitle());
        cv.put(COLUMN_DESC, f.getDescription());
        cv.put(COLUMN_PRICE, f.getPrice());
        cv.put(COLUMN_IMAGE, f.getImagePath());
        cv.put(COLUMN_STAR, f.getStar());
        cv.put(COLUMN_TIME_ID, f.getTimeId());
        cv.put(COLUMN_TIME_VALUE, f.getTimeValue());
        cv.put(COLUMN_PRICE_ID, f.getPriceId());
        cv.put(COLUMN_BESTFOOD, f.isBestFood() ? 1 : 0);
        cv.put(COLUMN_NUMBER_CART, f.getNumberInCart());

        long id = db.insert(TABLE_FOOD, null, cv);
        db.close();
        return id;
    }

    public ArrayList<Foods> getAllFoods() {
        ArrayList<Foods> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_FOOD, null);

        if (c.moveToFirst()) {
            do {
                Foods f = mapCursorToFoods(c);
                list.add(f);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<Foods> getFoodsByCategory(int categoryId) {
        ArrayList<Foods> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM " + TABLE_FOOD + " WHERE CategoryId = ?",
                new String[]{String.valueOf(categoryId)}
        );

        if (c.moveToFirst()) {
            do {
                Foods f = mapCursorToFoods(c);
                list.add(f);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<Foods> searchFoods(String text) {
        ArrayList<Foods> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM " + TABLE_FOOD + " WHERE Title LIKE ?",
                new String[]{"%" + text + "%"}
        );

        if (c.moveToFirst()) {
            do {
                Foods f = mapCursorToFoods(c);
                list.add(f);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    // Méthode utilitaire pour mapper Cursor → Foods
    private Foods mapCursorToFoods(Cursor c) {
        Foods f = new Foods();
        f.setId(c.getInt(c.getColumnIndexOrThrow(COLUMN_FOOD_ID)));
        f.setCategoryId(c.getInt(c.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)));
        f.setLocationId(c.getInt(c.getColumnIndexOrThrow(COLUMN_LOCATION_ID)));
        f.setTitle(c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE)));
        f.setDescription(c.getString(c.getColumnIndexOrThrow(COLUMN_DESC)));
        f.setPrice(c.getDouble(c.getColumnIndexOrThrow(COLUMN_PRICE)));
        f.setImagePath(c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE)));
        f.setStar(c.getDouble(c.getColumnIndexOrThrow(COLUMN_STAR)));
        f.setTimeId(c.getInt(c.getColumnIndexOrThrow(COLUMN_TIME_ID)));
        f.setTimeValue(c.getInt(c.getColumnIndexOrThrow(COLUMN_TIME_VALUE)));
        f.setPriceId(c.getInt(c.getColumnIndexOrThrow(COLUMN_PRICE_ID)));
        f.setBestFood(c.getInt(c.getColumnIndexOrThrow(COLUMN_BESTFOOD)) == 1);
        f.setNumberInCart(c.getInt(c.getColumnIndexOrThrow(COLUMN_NUMBER_CART)));
        return f;
    }

    // ============================================================
// FOODS PAR DÉFAUT
// ============================================================
    public void addDefaultFoods() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // Définir les plats par défaut
       Foods[] defaultFoods = new Foods[]{
               new Foods(10.99, 0, "A timeless classic, the Margherita pizza captures the essence of Italian simplicity. The thin, crispy crust is adorned with a luscious layer of fresh tomato sauce, delicate mozzarella cheese, and fragrant basil leaves. This pizza is a celebration of flavors in every bite, delivering a taste of Italy to your palate.", true, 0, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Margherita%20Flatbread.jpg?alt=media&token=d79daa6d-a9e2-4ba8-900d-271eca183705", 1, 4.5, 1, 15, "Margherita", 0),
               new Foods(12.99, 0, "Indulge in the Pepperoni Lovers pizza, a carnivore's dream come true. The crust is generously topped with zesty pepperoni slices, perfectly melding with the melted mozzarella and robust tomato sauce. Each mouthful is a symphony of savory and spicy notes, making it a go-to choice for those who savor the boldness of pepperoni.", true, 1, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Pepperoni%20Lovers.jpg?alt=media&token=d9d172ff-eb4b-4149-bcc5-5690994a0f1f", 1, 4.7, 1, 18, "Pepperoni Lovers", 0),
               new Foods(11.99, 0, "The Veggie Supreme pizza is a colorful masterpiece that caters to vegetarian cravings. Loaded with a medley of bell peppers, onions, olives, and mushrooms, this pizza offers a symphony of textures and tastes. The combination of fresh, vibrant vegetables atop a golden crust creates a delightful and satisfying experience.", false, 2, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Veggie%20Supreme.jpg?alt=media&token=cfddce97-575a-49af-b330-9299d0d9f776", 1, 4.5, 1, 20, "Veggie Supreme", 0),
               new Foods(13.99, 0, "Experience the smoky allure of the BBQ Chicken Delight pizza. Succulent pieces of BBQ-infused chicken are paired with the sweetness of red onions and the crunch of bell peppers. All of this is embraced by a delectable blend of cheeses, making every slice a journey into the world of bold barbecue flavors.", false, 3, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/BBQ%20Chicken%20Delight.jpg?alt=media&token=5f42cf73-af4e-4e11-bb02-80a3ff2d136d", 1, 4.6, 1, 22, "BBQ Chicken Delight", 0),
               new Foods(11.99, 0, "Transport your taste buds to a tropical paradise with the Hawaiian pizza. This delightful creation features a harmonious blend of savory ham, juicy pineapple chunks, and a tantalizing drizzle of BBQ sauce. Each bite is a blissful combination of sweet and savory, capturing the essence of a Hawaiian escape.", true, 4, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Hawaiian%20Paradise.jpg?alt=media&token=ea25596d-f1ec-4219-9f03-4a8f3448b113", 1, 4.3, 1, 17, "Hawaiian Paradise", 0),
               new Foods(14.99, 0, "The Meat Feast pizza is a carnivore's extravaganza, boasting a tantalizing assortment of meats. From savory sausage to crispy bacon and zesty pepperoni, this pizza is a flavor-packed journey through the rich and hearty world of meaty goodness. Perfect for those who crave a protein-packed pizza experience.", false, 5, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Meat%20Feast%20pizza.jpg?alt=media&token=0bf024d4-4f26-4237-8479-1a346dc5c537", 1, 4.8, 2, 35, "Meat Feast", 0),
               new Foods(12.99, 0, "https://firebasestorage.googleapis.com/v0/b/fir-test-aa9c8.appspot.com/o/Mediterranean%20Joy.jpg?alt=media&token=d1fba67f-e182-4c77-8ec2-cb9b1f459605", false, 6, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Mediterranean%20Joy.jpg?alt=media&token=2855007a-925e-4d0d-9167-5a41dcb96065", 1, 4.5, 1, 21, "Mediterranean Joy", 0),
               new Foods(13.99, 0, "Cheese enthusiasts rejoice in the Four Cheese Delight pizza. A symphony of mozzarella, cheddar, parmesan, and feta cheeses come together to create a mouthwatering blend of cheesy perfection. Each bite is a celebration of indulgence for those who appreciate the rich and diverse world of cheeses.", false, 7, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Four%20Cheese%20Delight.jpg?alt=media&token=79e557b6-7419-4b81-a0f4-443a68651373", 1, 4.7, 1, 23, "Four Cheese Delight", 0),
               new Foods(8.99, 1, "Embrace the simplicity of the Classic Beef Burger, featuring a juicy beef patty nestled between a toasted sesame seed bun. Topped with crisp lettuce, ripe tomato, and a special sauce, it's a timeless rendition of the all-American burger.", true, 8, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Classic%20Beef%20Burger.jpg?alt=media&token=97db8d6d-8c0d-4d22-b13d-31714244eab3", 0, 4.5, 1, 15, "Classic Beef Burger", 0),
               new Foods(9.99, 1, "Heat things up with the Spicy Jalapeño Burger. A flame-kissed patty infused with jalapeños is crowned with pepper jack cheese and a zesty chipotle mayo, offering a tantalizing kick for those who crave a spicy burger adventure.", true, 9, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Spicy%20Jalape%C3%B1o%20Burger.jpg?alt=media&token=b6df636e-d2de-4ef5-8286-b0cb5ef2e3db", 0, 4.7, 1, 18, "Spicy Jalapeño Burger", 0),
               new Foods(10.49, 1, "Indulge in the earthy flavors of the Mushroom Swiss Delight Burger. A succulent patty is topped with sautéed mushrooms and a drizzle of truffle aioli, creating a burger that caters to the discerning palate.", false, 10, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Mushroom%20Swiss%20Delight.jpg?alt=media&token=548e3ab0-02ba-4e72-b708-e5bf7811857b", 1, 4.7, 1, 18, "Mushroom Swiss Delight", 0),
               new Foods(10.49, 1, "Opt for a healthier option with the Chicken Avocado Bliss Burger. A grilled chicken breast takes center stage, adorned with fresh avocado, crisp lettuce, and creamy ranch dressing on a wholesome whole wheat bun.", false, 11, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Chicken%20Avocado%20Bliss.jpg?alt=media&token=e444197e-b9af-4b2c-8790-9c7ab7089148", 1, 4.4, 1, 20, "Chicken Avocado Bliss", 0),
               new Foods(11.99, 1, "For bacon aficionados, the Bacon and Cheese Heaven Burger is a must-try. A beef patty is adorned with crispy bacon, cheddar cheese, and a tangy BBQ sauce, creating a heavenly combination of smoky and savory flavors.", false, 12, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Bacon%20and%20Cheese%20Heaven.jpg?alt=media&token=0a213036-6086-4b29-ad8d-b20ca3216717", 1, 4.3, 1, 17, "Bacon and Cheese Heaven", 0),
               new Foods(10.99, 1, "Experience a symphony of flavors with the Veggie Extravaganza Burger. A hearty veggie patty takes center stage, complemented by grilled vegetables, hummus, and tzatziki sauce, catering to those who prefer a vegetarian delight.", false, 13, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Veggie%20Extravaganza.jpg?alt=media&token=cffbe6fd-0c0b-4b9a-8ea4-996e0fc019f0", 1, 4.8, 2, 35, "Veggie Extravaganza", 0),
               new Foods(11.49, 1, "Transport your taste buds to the tropics with the Teriyaki Pineapple Pleasure Burger. Featuring teriyaki-glazed chicken or tofu, grilled pineapple, and green onions, it's a delightful fusion of sweet and savory flavors.", false, 14, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Teriyaki%20Pineapple%20Pleasure.jpg?alt=media&token=56cec393-7d88-431f-8258-7403d16fa24a", 1, 4.5, 1, 21, "Teriyaki Pineapple Pleasure", 0),
               new Foods(12.49, 1, "Experience a flavor explosion with the BBQ Ranch Delight Burger. A beef patty is joined by crispy bacon, cheddar cheese, BBQ ranch dressing, and crispy onions on a ciabatta bun, creating a symphony of bold and savory notes.", false, 15, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/BBQ%20Ranch%20Delight.jpg?alt=media&token=11e77c82-36f1-432e-9093-5b71bc43c317", 1, 4.7, 1, 23, "BBQ Ranch Delight", 0),
               new Foods(9.99, 2, "Delight in the crispy perfection of our Original Crispy Chicken, seasoned to golden-brown perfection for a satisfying crunch in every bite.", false, 16, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Original%20Crispy%20Chicken.jpg?alt=media&token=b311a54c-684a-4f7c-8983-336d984fa9e7", 0, 4.7, 1, 18, "Original Crispy Chicken", 0),
               new Foods(8.99, 2, "Experience a burst of heat with our Spicy Buffalo Wings, tender chicken wings coated in a zesty buffalo sauce for a fiery kick.", false, 17, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Spicy%20Buffalo%20Wings.jpg?alt=media&token=1146da75-ae9e-42e8-9be8-931e3a558f7f", 0, 4.6, 1, 18, "Spicy Buffalo Wings", 0),
               new Foods(10.49, 2, "Indulge in the sweet and savory goodness of our Honey Mustard Glazed Tenders, featuring succulent chicken tenders coated in a luscious honey mustard glaze.", false, 18, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Honey%20Mustard%20Glazed%20Tenders.jpg?alt=media&token=73f17e69-33ff-4aa0-8a81-76500000f1c4", 1, 4.5, 0, 9, "Honey Mustard Glazed Tenders", 0),
               new Foods(9.49, 2, "Elevate your taste buds with our Lemon Pepper Chicken, crispy tenders seasoned with zesty lemon and aromatic black pepper for a refreshing flavor.", false, 19, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Lemon%20Pepper%20Chicken.jpg?alt=media&token=13eb02eb-93a8-40da-9dc6-105c2346a1aa", 0, 4.4, 1, 16, "Lemon Pepper Chicken", 0),
               new Foods(11.99, 2, "Embark on a flavorful journey with our Korean Fried Chicken, featuring a crispy exterior and a tantalizing sweet and spicy glaze.", false, 20, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Korean%20Fried%20Chicken.jpg?alt=media&token=0c524509-5697-4db6-aeb2-c986fc996467", 1, 4.8, 1, 22, "Korean Fried Chicken", 0),
               new Foods(8.99, 2, "Experience Southern comfort with our Southern-Style Chicken Biscuit, pairing a crispy chicken fillet with a warm, flaky biscuit.", false, 21, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Southern-Style%20Chicken%20Biscuit.jpg?alt=media&token=a63bfb8e-36ad-4371-b194-befb1521bce3", 0, 4.6, 1, 12, "Southern-Style Chicken Biscuit", 0),
               new Foods(8.99, 2, "Enjoy the fusion of flavors with our Teriyaki Chicken Wings, glazed in a luscious teriyaki sauce for a perfect blend of sweet and savory.", false, 22, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Teriyaki%20Chicken%20Wings.jpg?alt=media&token=6934d5cf-5a2e-4383-bb96-0eae2598c804", 0, 4.6, 1, 17, "Teriyaki Chicken Wings", 0),
               new Foods(9.99, 2, "Indulge in the rich and savory goodness of our Garlic Parmesan Chicken, featuring succulent chicken bites coated in a flavorful garlic and parmesan crust.", false, 23, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Garlic%20Parmesan%20Chicken.jpg?alt=media&token=9f143ea1-9c21-4a1f-b406-a29c610d8ca1", 0, 4.5, 1, 19, "Garlic Parmesan Chicken", 0),
               new Foods(5.99, 5, "Embrace tradition with the Classic Beef Hot Dog, a quintessential favorite featuring a succulent beef frankfurter topped with mustard and diced onions.", false, 24, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Classic%20Beef%20Hot%20Dog.jpg?alt=media&token=833e0dd3-d88f-493e-b442-58d2e8c17d9a", 0, 4.2, 0, 9, "Classic Beef Hot Dog", 0),
               new Foods(6.49, 5, "Indulge in comfort with the Chili Cheese Dog, a hearty delight boasting a beef hot dog smothered in rich chili, melted cheddar cheese, and diced onions.", false, 25, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Chili%20Cheese%20Dog.jpg?alt=media&token=f446830c-8d87-45c3-9f7f-9e48df8e241b", 0, 4.5, 1, 12, "Chili Cheese Dog", 0),
               new Foods(6.49, 5, "Enjoy a plant-based option with the Veggie Dog featuring sauerkraut, mustard, and a flavorful veggie frankfurter in a soft bun.", false, 26, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Veggie%20Dog%20with%20Sauerkraut.jpg?alt=media&token=b2f663df-6706-4937-bc2e-942437b16006", 0, 4.5, 1, 12, "Veggie Dog with Sauerkraut", 0),
               new Foods(7.49, 5, "Take a bite of Chicago with the Chicago Style Hot Dog, a beef frankfurter adorned with mustard, onions, sweet pickle relish, tomatoes, and a pickle spear.", false, 27, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Chicago%20Style%20Hot%20Dog.jpg?alt=media&token=fbc6cd42-46fc-460e-ab2d-064cab9e4a85", 0, 4.3, 1, 15, "Chicago Style Hot Dog", 0),
               new Foods(6.99, 5, "Elevate your hot dog experience with the Pretzel Bun Dog, featuring a beef frankfurter nestled in a soft pretzel bun.", false, 28, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Pretzel%20Bun%20Dog.jpg?alt=media&token=06f07486-d6cb-45c6-85f1-cc85be179672", 0, 4.4, 1, 18, "Pretzel Bun Dog", 0),
               new Foods(7.99, 5, "Embark on a tropical journey with the Hawaiian BBQ Dog, featuring a beef frankfurter topped with Hawaiian flavors, including pineapple and BBQ sauce.", false, 29, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Hawaiian%20BBQ%20Dog.jpg?alt=media&token=008f10c7-0b88-4c60-9667-3f915139bbd9", 0, 4.4, 1, 17, "Hawaiian BBQ Dog", 0),
               new Foods(7.99, 5, "Spice things up with the Kimchi Hot Dog, a fusion of flavors with a beef frankfurter topped with spicy kimchi.", false, 30, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Kimchi%20Hot%20Dog.jpg?alt=media&token=53a34d9e-be28-4a81-9b31-7c98dccbca7a", 0, 4.8, 1, 20, "Kimchi Hot Dog", 0),
               new Foods(25.00, 5, "Experience a twist on a classic with the Reuben Style Hot Dog, featuring a beef frankfurter topped with corned beef, sauerkraut, Swiss cheese, and Thousand Island dressing.", false, 31, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Reuben%20Style%20Hot%20Dog.jpg?alt=media&token=6afc13f3-1388-4290-a84b-e4b66914899a", 1, 4.7, 0, 8, "Reuben Style Hot Dog", 0),
               new Foods(9.99, 3, "Dive into the Classic California Roll, featuring crab, avocado, and cucumber wrapped in a sheet of nori.", false, 32, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/California%20Roll.jpg?alt=media&token=66aa4c55-f599-43ea-a9d6-e478776f89b0", 0, 4.6, 1, 20, "California Roll", 0),
               new Foods(10.49, 3, "Heat things up with the Spicy Tuna Roll, a tantalizing blend of spicy tuna, cucumber, and fiery mayo.", false, 33, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Spicy%20Tuna%20Roll.jpg?alt=media&token=16f31ca9-616c-4654-bb60-04beeea53839", 1, 4.7, 1, 22, "Spicy Tuna Roll", 0),
               new Foods(7.99, 3, "Savor simplicity with our Salmon Nigiri, featuring succulent salmon slices atop seasoned rice.", false, 34, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Salmon%20Nigiri.jpg?alt=media&token=357ed6a9-741f-4d0e-8e94-c94583be002e", 0, 4.5, 1, 18, "Salmon Nigiri", 0),
               new Foods(12.99, 3, "Indulge in artistic presentation with the Dragon Roll, featuring eel, avocado, and cucumber topped with thinly sliced avocado.", false, 35, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Dragon%20Roll.jpg?alt=media&token=253901d1-571b-4002-9c30-7b70a5b9f475", 1, 4.9, 1, 25, "Dragon Roll", 0),
               new Foods(11.99, 3, "Enjoy a burst of colors and flavors with the Rainbow Roll, showcasing a variety of fresh vegetables and avocado atop a maki roll.", false, 36, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Rainbow%20Roll.jpg?alt=media&token=7c1a1e5e-8403-48bb-95b6-dbfce1999054", 1, 4.9, 2, 30, "Rainbow Roll", 0),
               new Foods(11.99, 3, "Experience the crispy goodness of our Tempura Shrimp Roll, featuring tempura shrimp, avocado, and cucumber wrapped in a cone of nori.", false, 37, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Tempura%20Shrimp%20Roll.jpg?alt=media&token=71151d53-b4b3-45fc-a092-8f4be2ab372c", 1, 4.9, 1, 26, "Tempura Shrimp Roll", 0),
               new Foods(9.49, 3, "Savor the freshness of our Veggie Roll, filled with a medley of crisp vegetables for a delightful vegetarian option.", false, 38, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Veggie%20Roll.jpg?alt=media&token=e9e4fd61-2f3c-45a3-b648-33d3ff89bac0", 0, 4.5, 1, 21, "Veggie Roll", 0),
               new Foods(16.99, 3, "Indulge in the purity of flavors with our Sashimi Platter, featuring an assortment of fresh and thinly sliced raw fish.", false, 39, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Sashimi%20Platter.jpg?alt=media&token=9af63e84-19ed-4c1b-ab07-b1332b0cfaaa", 1, 4.9, 2, 35, "Sashimi Platter", 0),
               new Foods(34.99, 4, "Savor the richness of our Grilled Ribeye Steak, a succulent and well-marbled cut, expertly grilled to perfection. Accompanied by a flavorful blend of herbs and spices, this dish promises a melt-in-your-mouth experience.", false, 40, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Grilled%20Ribeye%20Steak.jpg?alt=media&token=e04200a4-e8f3-4c40-8f18-001139d4fc34", 2, 4.6, 1, 25, "Grilled Ribeye Steak", 0),
               new Foods(29.99, 4, "Embark on a culinary journey with our Spicy Moroccan Lamb Chops, featuring tender lamb chops marinated in exotic spices and grilled to a spicy perfection. A bold and flavorful choice for lamb enthusiasts.", false, 41, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Spicy%20Moroccan%20Lamb%20Chops.jpg?alt=media&token=2970ec34-c9c3-484e-bd67-84ae2f843318", 1, 4.5, 2, 35, "Spicy Moroccan Lamb Chops", 0),
               new Foods(32.99, 4, "Dive into the smoky goodness of our Smoked BBQ Brisket, a slow-cooked masterpiece featuring tender brisket, seasoned with our signature barbecue rub, and smoked to perfection. A true barbecue delight.", false, 42, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Smoked%20BBQ%20Brisket.jpg?alt=media&token=2f426ea4-0bad-41b3-887c-16b55239f111", 2, 4.8, 2, 60, "Smoked BBQ Brisket", 0),
               new Foods(26.99, 4, "Indulge in the elegance of our Pan-Seared Garlic Butter Sirloin, a juicy sirloin steak pan-seared to perfection and topped with a luscious garlic butter sauce. A symphony of flavors for steak connoisseurs.", false, 43, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Pan-Seared%20Garlic%20Butter%20Sirloin.jpg?alt=media&token=5c278f44-54d7-4d9f-acc7-aefbe83abf3b", 1, 4.7, 1, 23, "Pan-Seared Garlic Butter Sirloin", 0),
               new Foods(18.99, 4, "Experience the sweet and savory fusion of our Teriyaki Glazed Chicken Thighs, featuring succulent chicken thighs marinated in a flavorful teriyaki sauce and grilled to perfection. A delightful Asian-inspired choice.", false, 44, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Teriyaki%20Glazed%20Chicken%20Thighs.jpg?alt=media&token=6028886a-5d01-4cc6-9812-495c4f7881f7", 1, 4.6, 2, 30, "Teriyaki Glazed Chicken Thighs", 0),
               new Foods(36.99, 4, "Elevate your dining experience with our Bacon-Wrapped Filet Mignon, a premium cut of beef tenderloin wrapped in savory bacon, cooked to your desired level of doneness. A true indulgence for steak enthusiasts.", false, 45, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Bacon-Wrapped%20Filet%20Mignon.jpg?alt=media&token=0dbe5ca9-e0ec-469b-aa7a-cfa081208f68", 2, 5.0, 1, 20, "Bacon-Wrapped Filet Mignon", 0),
               new Foods(22.99, 4, "Immerse yourself in the bold flavors of our Korean BBQ Short Ribs, featuring marinated short ribs grilled to perfection, delivering a delightful combination of sweetness and savory goodness.", false, 46, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Korean%20BBQ%20Short%20Ribs.jpg?alt=media&token=d3a06162-0b6e-4d2f-a7bb-fc27598a1ee5", 1, 4.4, 2, 40, "Korean BBQ Short Ribs", 0),
               new Foods(16.99, 4, "Delight in our Stuffed Bell Peppers with Ground Turkey, a wholesome dish featuring bell peppers filled with a flavorful blend of ground turkey, herbs, and spices, baked to perfection.", false, 47, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Stuffed%20Bell%20Peppers%20with%20Ground%20Turkey.jpg?alt=media&token=6d92812c-cf6a-4d91-b4e1-8d6f9519641b", 1, 4.3, 2, 45, "Stuffed Bell Peppers with Ground Turkey", 0),
               new Foods(3.99, 6, "Refresh with our Fresh Orange Juice, a zesty and invigorating drink squeezed from the finest oranges.", false, 48, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Fresh%20Orange%20Juice.jpg?alt=media&token=cf8baceb-ba9e-479e-8759-2eb7aed05876", 0, 4.6, 0, 5, "Fresh Orange Juice", 0),
               new Foods(4.49, 6, "Indulge in the Berry Blast Smoothie, a refreshing blend of mixed berries and yogurt for a fruity and nutritious treat.", false, 49, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Berry%20Blast%20Smoothie.jpg?alt=media&token=e5b843ad-dfda-434a-bc5f-5dc1f3ebfc19", 0, 4.8, 0, 7, "Berry Blast Smoothie", 0),
               new Foods(4.99, 6, "Savor the Iced Caramel Macchiato, a sweet and energizing coffee delight made with espresso, caramel syrup, milk, and ice.", false, 50, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Iced%20Caramel%20Macchiato.jpg?alt=media&token=ce379ec0-24bf-4786-8475-9305b8750f27", 0, 4.5, 0, 3, "Iced Caramel Macchiato", 0),
               new Foods(3.99, 6, "Quench your thirst with the Mint Lemonade, a cool and tangy beverage perfect for a hot day.", false, 51, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Mint%20Lemonade.jpg?alt=media&token=02335c99-49eb-421a-978a-586eb4f1aead", 0, 4.4, 0, 4, "Mint Lemonade", 0),
               new Foods(4.99, 6, "Beat the heat with the Mango Tango Slush, a tropical delight offering a slushy blend of mango goodness.", false, 52, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Mango%20Tango%20Slush.jpg?alt=media&token=b1384665-539c-46ad-b3ca-9b6640b5e02f", 0, 4.7, 0, 6, "Mango Tango Slush", 0),
               new Foods(4.49, 6, "Enjoy the soothing Green Tea Latte, a comforting blend of matcha and steamed milk for a touch of elegance.", false, 53, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Green%20Tea%20Latte.jpg?alt=media&token=d688a418-4cd2-4feb-a977-74f43657cc17", 0, 4.5, 0, 8, "Green Tea Latte", 0),
               new Foods(2.99, 6, "Hydrate naturally with Coconut Water, a refreshing and electrolyte-packed beverage straight from the coconut.", false, 54, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Coconut%20Water.jpg?alt=media&token=49cccb7a-a991-4639-b8cd-110d177bcd63", 0, 4.3, 0, 2, "Coconut Water", 0),
               new Foods(7.99, 6, "Indulge in the sophisticated Espresso Martini, a luxurious cocktail blending espresso, vodka, and coffee liqueur.", false, 55, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Espresso%20Martini.jpg?alt=media&token=5b772672-e789-4141-a09e-509ea2847410", 0, 4.9, 0, 10, "Espresso Martini", 0),
               new Foods(12.99, 7, "Indulge in the classic Italian comfort of Pasta Carbonara, featuring al dente spaghetti tossed in a creamy sauce with pancetta, Parmesan cheese, and black pepper.", false, 56, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Pasta%20Carbonara.jpg?alt=media&token=e7732efa-64a4-4f95-ba81-1a33a49becb6", 1, 4.7, 1, 22, "Pasta Carbonara", 0),
               new Foods(13.49, 7, "Experience the bold and aromatic flavors of Thai cuisine with our Thai Red Curry, a tantalizing dish with tender chicken or tofu, vegetables, and a rich red curry sauce.", false, 57, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Thai%20Red%20Curry.jpg?alt=media&token=252a7b48-e636-4f8f-878b-7fd7c02cb9df", 1, 4.8, 2, 25, "Thai Red Curry", 0),
               new Foods(13.49, 7, "Savor the vibrant and flavorful Vegetarian Pad Thai, a stir-fried noodle dish with tofu, bean sprouts, peanuts, and lime for a perfect balance of sweet, sour, and savory.", false, 58, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Vegetarian%20Pad%20Thai.jpg?alt=media&token=af91cdb8-e6dd-4fbd-b7ed-804033f5c145", 1, 4.8, 1, 25, "Vegetarian Pad Thai", 0),
               new Foods(14.99, 7, "Delight in a gourmet experience with our Spinach and Feta Stuffed Chicken, featuring succulent chicken breasts stuffed with a blend of spinach, feta cheese, and herbs.", false, 59, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Spinach%20and%20Feta%20Stuffed%20Chicken.jpg?alt=media&token=7c88332d-ed4d-487e-8ba0-2b3f699b1976", 1, 4.9, 2, 30, "Spinach and Feta Stuffed Chicken", 0),
               new Foods(13.99, 7, "Enjoy the savory goodness of our Beef Stir-Fry with Broccoli, a quick and delicious dish featuring tender beef strips, crisp broccoli, and a flavorful stir-fry sauce.", false, 60, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Beef%20Stir-Fry%20with%20Broccoli.jpg?alt=media&token=70d3e921-c03e-4022-abaf-b606cca00c8e", 1, 4.7, 2, 32, "Beef Stir-Fry with Broccoli", 0),
               new Foods(10.99, 7, "Experience the simplicity of the Margherita Flatbread, featuring a thin and crispy crust topped with fresh tomatoes, mozzarella cheese, and basil for a taste of Italy.", false, 61, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Margherita%20Flatbread.jpg?alt=media&token=d79daa6d-a9e2-4ba8-900d-271eca183705", 1, 4.5, 1, 18, "Margherita Flatbread", 0),
               new Foods(9.99, 7, "Embrace a nutritious and satisfying option with our Quinoa Salad Bowl, featuring a colorful medley of quinoa, mixed greens, vegetables, and a zesty vinaigrette dressing.", false, 62, 0, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Quinoa%20Salad%20Bowl.jpg?alt=media&token=7a5f0160-c75d-4114-890f-0a4f43e4888c", 0, 4.4, 1, 11, "Quinoa Salad Bowl", 0),
               new Foods(16.99, 7, "Indulge in the delightful flavors of Shrimp Scampi, featuring plump shrimp sautéed in a garlic-infused butter and white wine sauce, served over a bed of pasta.", false, 63, 1, "https://firebasestorage.googleapis.com/v0/b/project162-foodapp.appspot.com/o/Shrimp%20Scampi.jpg?alt=media&token=ca069ef6-70b4-4072-ba5a-864ccf14097c", 1, 4.8, 2, 32, "Shrimp Scampi", 0)
       };
        // Insérer chaque aliment dans la table
        for (Foods f : defaultFoods) {
            cv.clear();
            cv.put(COLUMN_CATEGORY_ID, f.getCategoryId());
            cv.put(COLUMN_LOCATION_ID, f.getLocationId());
            cv.put(COLUMN_TITLE, f.getTitle());
            cv.put(COLUMN_DESC, f.getDescription());
            cv.put(COLUMN_PRICE, f.getPrice());
            cv.put(COLUMN_IMAGE, f.getImagePath());
            cv.put(COLUMN_STAR, f.getStar());
            cv.put(COLUMN_TIME_ID, f.getTimeId());
            cv.put(COLUMN_TIME_VALUE, f.getTimeValue());
            cv.put(COLUMN_PRICE_ID, f.getPriceId());
            cv.put(COLUMN_BESTFOOD, f.isBestFood() ? 1 : 0);
            cv.put(COLUMN_NUMBER_CART, f.getNumberInCart());
            db.insert(TABLE_FOOD, null, cv);
        }

        db.close();
    }


    // ============================================================
    // CATEGORIES
    // ============================================================
    public long addCategory(Category c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CAT_NAME, c.getName());
        cv.put(COLUMN_CAT_IMAGE, c.getImagePath());
        long id = db.insert(TABLE_CATEGORY, null, cv);
        db.close();
        return id;
    }

    public void addDefaultCategories() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Category[] defaultCategories = new Category[]{
                new Category(0, "btn_1", "Pizza"),
                new Category(1, "btn_2", "Burger"),
                new Category(2, "btn_3", "Chicken"),
                new Category(3, "btn_4", "Sushi"),
                new Category(4, "btn_5", "Meat"),
                new Category(5, "btn_6", "Hotdog"),
                new Category(6, "btn_7", "Drink"),
                new Category(7, "btn_8", "More")
        };

        for (Category c : defaultCategories) {
            cv.clear();
            cv.put(COLUMN_CAT_NAME, c.getName());
            cv.put(COLUMN_CAT_IMAGE, c.getImagePath());
            db.insert(TABLE_CATEGORY, null, cv);
        }

        db.close();
    }

    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_CATEGORY, null);

        if (c.moveToFirst()) {
            do {
                Category cat = new Category();
                cat.setId(c.getInt(c.getColumnIndexOrThrow(COLUMN_CAT_ID)));
                cat.setName(c.getString(c.getColumnIndexOrThrow(COLUMN_CAT_NAME)));
                cat.setImagePath(c.getString(c.getColumnIndexOrThrow(COLUMN_CAT_IMAGE)));
                list.add(cat);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    // ============================================================
    // PRICE
    // ============================================================
    public void addDefaultPrices() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String[] prices = {
                "1$ - 10$",
                "10$ - 30$",
                "more than 30$"
        };

        for (String p : prices) {
            cv.clear();
            cv.put(COLUMN_PRICE_VALUE, p);
            db.insert(TABLE_PRICE, null, cv);
        }

        db.close();
    }

    public ArrayList<Price> getAllPrices() {
        ArrayList<Price> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_PRICE, null);

        if (c.moveToFirst()) {
            do {
                Price p = new Price();
                p.setId(c.getInt(c.getColumnIndexOrThrow(COLUMN_PRICE_TABLE_ID)));
                p.setValue(c.getString(c.getColumnIndexOrThrow(COLUMN_PRICE_VALUE)));
                list.add(p);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    // ============================================================
    // TIME
    // ============================================================
    public void addDefaultTimes() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String[] times = {
                "0 - 10 min",
                "10 - 30 min",
                "more than 30 min"
        };

        for (String t : times) {
            cv.clear();
            cv.put(COLUMN_TIME_TABLE_VALUE, t);
            db.insert(TABLE_TIME, null, cv);
        }

        db.close();
    }

    public ArrayList<Time> getAllTimes() {
        ArrayList<Time> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_TIME, null);

        if (c.moveToFirst()) {
            do {
                Time t = new Time();
                t.setId(c.getInt(c.getColumnIndexOrThrow(COLUMN_TIME_TABLE_ID)));
                t.setValue(c.getString(c.getColumnIndexOrThrow(COLUMN_TIME_TABLE_VALUE)));
                list.add(t);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }
    public ArrayList<Foods> getFoodsByLocation(int locationId) {
        ArrayList<Foods> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM " + TABLE_FOOD + " WHERE LocationId = ?",
                new String[]{String.valueOf(locationId)}
        );

        if (c.moveToFirst()) {
            do {
                Foods f = mapCursorToFoods(c);
                list.add(f);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<Foods> getFoodsByPrice(int priceId) {
        ArrayList<Foods> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM " + TABLE_FOOD + " WHERE PriceId = ?",
                new String[]{String.valueOf(priceId)}
        );

        if (c.moveToFirst()) {
            do {
                Foods f = mapCursorToFoods(c);
                list.add(f);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }
    public ArrayList<Foods> getFoodsByTime(int timeId) {
        ArrayList<Foods> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM " + TABLE_FOOD + " WHERE TimeId = ?",
                new String[]{String.valueOf(timeId)}
        );

        if (c.moveToFirst()) {
            do {
                Foods f = mapCursorToFoods(c);
                list.add(f);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    public ArrayList<Foods> getFoodsFiltered(
            Integer locationId,
            Integer priceId,
            Integer timeId
    ) {

        ArrayList<Foods> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder query = new StringBuilder("SELECT * FROM " + TABLE_FOOD + " WHERE 1=1");
        ArrayList<String> args = new ArrayList<>();

        if (locationId != null) {
            query.append(" AND LocationId = ?");
            args.add(String.valueOf(locationId));
        }

        if (priceId != null) {
            query.append(" AND PriceId = ?");
            args.add(String.valueOf(priceId));
        }

        if (timeId != null) {
            query.append(" AND TimeId = ?");
            args.add(String.valueOf(timeId));
        }

        Cursor c = db.rawQuery(query.toString(), args.toArray(new String[0]));

        if (c.moveToFirst()) {
            do {
                Foods f = mapCursorToFoods(c);
                list.add(f);
            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return list;
    }

    public Integer getLocationIdByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Integer id = null;

        Cursor cursor = db.rawQuery(
                "SELECT id FROM locations WHERE loc = ?",
                new String[]{name}
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            }
            cursor.close();
        }

        return id; // peut être null si pas trouvé
    }


}
