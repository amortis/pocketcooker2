package com.example.pocketcooker2.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketcooker2.R;
import com.example.pocketcooker2.data.Product;
import com.example.pocketcooker2.data.Recipe;
import com.example.pocketcooker2.data.RecipeAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeFragment extends Fragment {

    private List<Recipe> recipeList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();
    private RecipeAdapter adapter;
    public RecipeFragment(List<Product> productList){
        this.productList = productList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recipe_view, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recipe_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RecipeAdapter(recipeList);
        recyclerView.setAdapter(adapter);


        // Загрузка списка рецептов из файла recipe.json
        List<Recipe> allRecipes = loadRecipesFromJson();

        // Отфильтровать рецепты на основе списка продуктов пользователя
        List<Recipe> filteredRecipes = filterRecipes(allRecipes, productList);

        // Отобразить отфильтрованный список рецептов
        displayRecipes(filteredRecipes);

        return root;
    }

    private List<Recipe> loadRecipesFromJson() {
        List<Recipe> recipes = new ArrayList<>();
        try {
            InputStream inputStream = requireContext().getResources().openRawResource(R.raw.recipe);
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(inputStream);
            recipes = gson.fromJson(reader, new TypeToken<List<Recipe>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipes;
    }

    private List<Recipe> filterRecipes(List<Recipe> allRecipes, List<Product> productList) {
        List<Recipe> filteredRecipes = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            boolean isFullyAvailable = true;
            boolean isPartiallyAvailable = false;
            for (Map.Entry<Product, Double> entry : recipe.getProducts().entrySet()) {
                String productId = String.valueOf(entry.getKey());
                double requiredQuantity = entry.getValue();
                boolean productFound = false;
                for (Product userProduct : productList) {
                    if (userProduct.get_id().equals(productId) && userProduct.getQuantity() >= requiredQuantity) {
                        productFound = true;
                        break;
                    }
                }
                if (!productFound) {
                    isFullyAvailable = false;
                    if (!isPartiallyAvailable) {
                        isPartiallyAvailable = true;
                    }
                }
            }
            if (isFullyAvailable || isPartiallyAvailable) {
                filteredRecipes.add(recipe);
            }
        }
        return filteredRecipes;
    }

    private void displayRecipes(List<Recipe> recipes) {
        recipeList.clear();
        recipeList.addAll(recipes);
        adapter.notifyDataSetChanged();
    }
}
