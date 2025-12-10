/*package com.example.foodapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Adapter.BestFoodsAdapter;
import com.example.foodapp.Adapter.CategoryAdapter;
import com.example.foodapp.Domain.Category;
import com.example.foodapp.Domain.Foods;
import com.example.foodapp.Domain.Location;
import com.example.foodapp.Domain.Price;
import com.example.foodapp.Domain.Time;
import com.example.foodapp.Helper.DatabaseHelper;

import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DatabaseHelper db = new DatabaseHelper(this);

        if (db.getAllFoods().isEmpty()) {
                db.addDefaultFoods();
            }


        RecyclerView bestFoodView = findViewById(R.id.bestFoodView);

        ArrayList<Foods> foods = db.getAllFoods();

        BestFoodsAdapter adapter = new BestFoodsAdapter(foods);
        bestFoodView.setAdapter(adapter);
        bestFoodView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));



// Ajouter toutes les catégories par défaut
        if (db.getAllCategories().isEmpty()) {
            db.addDefaultCategories();
       }
// Lire les catégories et les afficher
        ArrayList<Category> categories = db.getAllCategories();
        RecyclerView bestCatView = findViewById(R.id.categoryView);
        CategoryAdapter adaptercat = new CategoryAdapter(categories);
       bestCatView.setAdapter(adaptercat);
        bestCatView.setLayoutManager(new GridLayoutManager(MainActivity.this,4));


// Ajouter les locations
        if (db.getAllLocations().isEmpty()){

        db.addLocation("LA california");

        db.addLocation("NY manhattan");}

// Récupérer les locations depuis la base
        ArrayList<String> locations = db.getAllLocations();

// Récupérer le Spinner
        Spinner locationSpinner = findViewById(R.id.locationsp);

// Créer un ArrayAdapter avec les données
        ArrayAdapter<String> adapterLoc = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                locations // <-- ATTENTION ici, il faut passer la liste
        );

// Définir le layout pour le menu déroulant
        adapterLoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Attacher l'adapter au Spinner
        locationSpinner.setAdapter(adapterLoc);


// Ajouter les données par défaut si vide
        if (db.getAllPrices().isEmpty()) {
            db.addDefaultPrices();
        }

        if (db.getAllTimes().isEmpty()) {
            db.addDefaultTimes();
        }

// Maintenant récupérer les listes
        ArrayList<Price> prices = db.getAllPrices();
        ArrayList<Time> times = db.getAllTimes();
        Spinner timeSpinner = findViewById(R.id.timesp);

        ArrayAdapter<Time> adapterTime = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                times
        );

        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapterTime);
        Spinner priceSpinner = findViewById(R.id.pricesp);

        ArrayAdapter<Price> adapterPrice = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                prices
        );

        adapterPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceSpinner.setAdapter(adapterPrice);
        binding.progressBarBestFood.setVisibility(View.GONE);
        binding.progressBarBestCategory.setVisibility(View.GONE);
    setVariable();
        locationSpinner.setOnItemSelectedListener(new SimpleFilterListener(db));
        priceSpinner.setOnItemSelectedListener(new SimpleFilterListener(db));
        timeSpinner.setOnItemSelectedListener(new SimpleFilterListener(db));

    }
    private void setVariable() {
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Déconnexion de Firebase
                FirebaseAuth.getInstance().signOut();

                // Redirection vers LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

                // Optionnel : fermer MainActivity pour ne pas revenir en arrière
                finish();
            }
        });
        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt=binding.etSearch.getText().toString();
                if(!txt.isEmpty()){
                    Intent i=new Intent(MainActivity.this,ListFoodsActivity.class);
                    i.putExtra("text",txt);
                    i.putExtra("isSearch",true);
                    startActivity(i);
                }
            }
        });

    }

    private void updateBestFoodList(ArrayList<Foods> foods) {
        RecyclerView bestFoodView = findViewById(R.id.bestFoodView);
        BestFoodsAdapter adapter = new BestFoodsAdapter(foods);
        bestFoodView.setAdapter(adapter);
        bestFoodView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
    }
    private class SimpleFilterListener implements android.widget.AdapterView.OnItemSelectedListener {

        DatabaseHelper db;

        SimpleFilterListener(DatabaseHelper db) {
            this.db = db;
        }

        @Override
        public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
            applyFilters(db); // ➤ Refiltre à chaque changement
        }

        @Override
        public void onNothingSelected(android.widget.AdapterView<?> parent) {}
    }
    private void applyFilters(DatabaseHelper db) {

        Spinner locationSpinner = findViewById(R.id.locationsp);
        Spinner priceSpinner = findViewById(R.id.pricesp);
        Spinner timeSpinner = findViewById(R.id.timesp);

        // Lire sélection Location
        String selectedLocationName = (String) locationSpinner.getSelectedItem();
        Integer locationId = db.getLocationIdByName(selectedLocationName);

        // Lire sélection Price
        Price selectedPrice = (Price) priceSpinner.getSelectedItem();
        Integer priceId = (selectedPrice != null) ? selectedPrice.getId() : null;

        // Lire sélection Time
        Time selectedTime = (Time) timeSpinner.getSelectedItem();
        Integer timeId = (selectedTime != null) ? selectedTime.getId() : null;

        // Récupérer les foods filtrés
        ArrayList<Foods> filteredFoods = db.getFoodsFiltered(locationId, priceId, timeId);

        // Afficher dans le RecyclerView
        updateBestFoodList(filteredFoods);
    }

}*/
package com.example.foodapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Adapter.BestFoodsAdapter;
import com.example.foodapp.Adapter.CategoryAdapter;
import com.example.foodapp.Domain.Category;
import com.example.foodapp.Domain.Foods;
import com.example.foodapp.Domain.Price;
import com.example.foodapp.Domain.Time;
import com.example.foodapp.Helper.DatabaseHelper;

import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private DatabaseHelper db;

    private Spinner locationSpinner, priceSpinner, timeSpinner;
    private RecyclerView bestFoodView, bestCatView;
    private final android.os.Handler handler = new android.os.Handler();
    private final Runnable resetFiltersRunnable = this::resetFilters;
    private boolean isLocationSpinnerInitialized = false;
    private boolean isPriceSpinnerInitialized = false;
    private boolean isTimeSpinnerInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new DatabaseHelper(this);

        initViews();
        initDatabaseDefaults();
        loadCategories();
        loadFoods();
        setupSpinners();
        setupSpinnerListeners();
        setupButtons();
    }

    // ------------------------------------------------------------
    // 1️⃣ Initialisation des vues
    // ------------------------------------------------------------
    private void initViews() {
        bestFoodView = findViewById(R.id.bestFoodView);
        bestCatView = findViewById(R.id.categoryView);

        locationSpinner = findViewById(R.id.locationsp);
        priceSpinner = findViewById(R.id.pricesp);
        timeSpinner = findViewById(R.id.timesp);
    }

    // ------------------------------------------------------------
    // 2️⃣ Ajouter les données par défaut si base vide
    // ------------------------------------------------------------
    private void initDatabaseDefaults() {
        if (db.getAllFoods().isEmpty()) db.addDefaultFoods();
        if (db.getAllCategories().isEmpty()) db.addDefaultCategories();
        if (db.getAllLocations().isEmpty()) {
            db.addLocation("LA California");
            db.addLocation("NY Manhattan");
        }
        if (db.getAllPrices().isEmpty()) db.addDefaultPrices();
        if (db.getAllTimes().isEmpty()) db.addDefaultTimes();
    }

    // ------------------------------------------------------------
    // 3️⃣ Charger la liste des catégories
    // ------------------------------------------------------------
    private void loadCategories() {
        ArrayList<Category> categories = db.getAllCategories();
        CategoryAdapter adapter = new CategoryAdapter(categories);

        bestCatView.setAdapter(adapter);
        bestCatView.setLayoutManager(new GridLayoutManager(this, 4));

        binding.progressBarBestCategory.setVisibility(View.GONE);
    }

    // ------------------------------------------------------------
    // 4️⃣ Charger la liste initiale des foods
    // ------------------------------------------------------------
    private void loadFoods() {
        ArrayList<Foods> foods = db.getAllFoods();
        updateBestFoodList(foods);

        binding.progressBarBestFood.setVisibility(View.GONE);
    }

    // ------------------------------------------------------------
    // 5️⃣ Mise à jour du RecyclerView BestFoods
    // ------------------------------------------------------------
    private void updateBestFoodList(ArrayList<Foods> foods) {
        BestFoodsAdapter adapter = new BestFoodsAdapter(foods);
        bestFoodView.setAdapter(adapter);
        bestFoodView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
    }

    // ------------------------------------------------------------
    // 6️⃣ Configuration des Spinners
    // ------------------------------------------------------------
    private void setupSpinners() {
        // Location
        ArrayAdapter<String> adapterLoc = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, db.getAllLocations()
        );
        adapterLoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapterLoc);

        // Price
        ArrayAdapter<Price> adapterPrice = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, db.getAllPrices()
        );
        adapterPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceSpinner.setAdapter(adapterPrice);

        // Time
        ArrayAdapter<Time> adapterTime = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, db.getAllTimes()
        );
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapterTime);
    }

    // ------------------------------------------------------------
    // 7️⃣ Listeners des Spinners → lancement du filtre
    // ------------------------------------------------------------
    private void setupSpinnerListeners() {

        locationSpinner.setOnItemSelectedListener(new FilterListener());
        priceSpinner.setOnItemSelectedListener(new FilterListener());
        timeSpinner.setOnItemSelectedListener(new FilterListener());
    }

    private class FilterListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(android.widget.AdapterView<?> parent, View view, int pos, long id) {
            // Ignorer le premier appel automatique des Spinners
            if (parent == locationSpinner && !isLocationSpinnerInitialized) {
                isLocationSpinnerInitialized = true;
                return;
            }
            if (parent == priceSpinner && !isPriceSpinnerInitialized) {
                isPriceSpinnerInitialized = true;
                return;
            }
            if (parent == timeSpinner && !isTimeSpinnerInitialized) {
                isTimeSpinnerInitialized = true;
                return;
            }

            // Appliquer les filtres seulement après la première sélection réelle
            applyFilters();
        }

        @Override
        public void onNothingSelected(android.widget.AdapterView<?> parent) {}
    }


    // ------------------------------------------------------------
    // 8️⃣ Fonction centrale du filtre
    // ------------------------------------------------------------
    private void applyFilters() {

        // Lire la location
        String locationName = (String) locationSpinner.getSelectedItem();
        Integer locationId = db.getLocationIdByName(locationName);

        // Lire le price
        Price selectedPrice = (Price) priceSpinner.getSelectedItem();
        Integer priceId = selectedPrice != null ? selectedPrice.getId() : null;

        // Lire le time
        Time selectedTime = (Time) timeSpinner.getSelectedItem();
        Integer timeId = selectedTime != null ? selectedTime.getId() : null;

        // Si aucun filtre n’est choisi (tous à 0 ou null), afficher tous les foods
        if (locationId == null && priceId == null && timeId == null) {
            loadFoods();
        } else {
            // Récupérer les données filtrées
            ArrayList<Foods> filteredFoods = db.getFoodsFiltered(locationId-1, priceId-1, timeId-1);
            updateBestFoodList(filteredFoods);
        }

        // Annuler un reset précédent et programmer un nouveau reset après 45s
        handler.removeCallbacks(resetFiltersRunnable);
        handler.postDelayed(resetFiltersRunnable, 45000); // 45s
    }

    // ------------------------------------------------------------
    // 9️⃣ Boutons logout + search
    // ------------------------------------------------------------
    private void setupButtons() {
        binding.logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        binding.searchBtn.setOnClickListener(v -> {
            String txt = binding.etSearch.getText().toString();
            if (!txt.isEmpty()) {
                Intent i = new Intent(MainActivity.this, ListFoodsActivity.class);
                i.putExtra("text", txt);
                i.putExtra("isSearch", true);
                startActivity(i);
            }
        });
    }
    private void resetFilters() {
        // Remettre les Spinners à la position 0 (aucun filtre)
        locationSpinner.setSelection(0);
        priceSpinner.setSelection(0);
        timeSpinner.setSelection(0);

        // Recharger tous les foods
        loadFoods();
    }

}
