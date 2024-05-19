package com.example.pocketcooker2.ui.dashboard;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.pocketcooker2.R;
import com.example.pocketcooker2.data.Product;
import com.example.pocketcooker2.data.Recipe;
import com.example.pocketcooker2.data.RecipeAdapter;
import com.example.pocketcooker2.data.RecipeDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeDialogFragment extends DialogFragment {
    private List<Recipe> recipeList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();

    public RecipeDialogFragment(List<Product> productList) {
        this.productList = productList;
    }

    public RecipeDialogFragment() {
    }

    public static RecipeDialogFragment newInstance(List<Product> productList) {
        RecipeDialogFragment fragment = new RecipeDialogFragment(productList);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recipe_view, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recipe_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Recipe> allRecipes = loadRecipesFromJson();
        List<Recipe> filteredRecipes = filterRecipes(allRecipes, productList);

        RecipeAdapter adapter = new RecipeAdapter(filteredRecipes);
        recyclerView.setAdapter(adapter);

        return root;
    }

    private List<Recipe> loadRecipesFromJson() {
        List<Recipe> recipes = new ArrayList<>();
        try {
            InputStream inputStream = requireContext().getResources().openRawResource(R.raw.recipe);
            GsonBuilder gsonBuilder = new GsonBuilder();

            // Загрузите все продукты один раз и передайте их в десериализатор
            Map<String, Product> productMap = loadAllProducts();
            gsonBuilder.registerTypeAdapter(Recipe.class, new RecipeDeserializer(productMap));

            Gson gson = gsonBuilder.create();
            Reader reader = new InputStreamReader(inputStream);
            recipes = gson.fromJson(reader, new TypeToken<List<Recipe>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipes;
    }

    private Map<String, Product> loadAllProducts() {
        Map<String, Product> productMap = new HashMap<>();
        try {
            InputStream inputStream = requireContext().getResources().openRawResource(R.raw.product);
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(inputStream);
            List<Product> products = gson.fromJson(reader, new TypeToken<List<Product>>(){}.getType());
            for (Product product : products) {
                productMap.put(product.get_id(), product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productMap;
    }

    private List<Recipe> filterRecipes(List<Recipe> allRecipes, List<Product> productList) {
        List<Recipe> filteredRecipes = new ArrayList<>();
        Map<String, Double> userProducts = new HashMap<>();
        for (Product product : productList) {
            userProducts.put(product.getName(), product.getQuantity());
        }

        for (Recipe recipe : allRecipes) {
            boolean canMake = true;
            for (Map.Entry<Product, Double> entry : recipe.getProducts().entrySet()) {
                if (!userProducts.containsKey(entry.getKey().getName()) || userProducts.get(entry.getKey().getName()) < entry.getValue()) {
                    canMake = false;
                    break;
                }
            }
            if (canMake) {
                filteredRecipes.add(recipe);
            }
        }
        return filteredRecipes;
    }
}
