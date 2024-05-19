package com.example.pocketcooker2.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;


import com.example.pocketcooker2.R;
import com.example.pocketcooker2.data.Product;
import com.example.pocketcooker2.data.ProductAdapter;
import com.example.pocketcooker2.databinding.FragmentDashboardBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private ProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.product_recycler_view);
        adapter = new ProductAdapter(productList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        Button addButton = root.findViewById(R.id.add_product_button);
        addButton.setOnClickListener(v -> addProduct());

        Button calcButton = root.findViewById(R.id.button_calc);
        calcButton.setOnClickListener(v -> openRecipeFragment(productList));

        return root;
    }
    private void addProduct() {
        EditText nameEditText = requireView().findViewById(R.id.product_name_edit_text);
        EditText quantityEditText = requireView().findViewById(R.id.product_quantity_edit_text);


        String name = nameEditText.getText().toString().trim();
        String quantityString = quantityEditText.getText().toString().trim();
        // Здесь вы можете получить другие свойства продукта из формы ввода, если необходимо

        if (!name.isEmpty() && isProductAvailable(name) && !quantityString.isEmpty()) {
            double quantity = Double.parseDouble(quantityString);
            Product product = makeProduct(name, quantity);
            // Здесь вы можете задать другие свойства продукта
            productList.add(product);
            adapter.notifyDataSetChanged();
            nameEditText.setText("");
            quantityEditText.setText("");

        }
    }
    private boolean isProductAvailable(String productName) {
        try {
            // Получаем InputStream для файла product.json из ресурсов
            InputStream inputStream = requireContext().getResources().openRawResource(R.raw.product);

            // Создаем объект Gson
            Gson gson = new Gson();

            // Читаем JSON из InputStream и парсим его в список объектов Product
            Reader reader = new InputStreamReader(inputStream);
            List<Product> products = gson.fromJson(reader, new TypeToken<List<Product>>(){}.getType());

            // Проверяем наличие продукта в списке
            for (Product product : products) {
                if (productName.equalsIgnoreCase(product.getName())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private Product makeProduct(String productName, double quanity){
        try {
            // Получаем InputStream для файла product.json из ресурсов
            InputStream inputStream = requireContext().getResources().openRawResource(R.raw.product);

            // Создаем объект Gson
            Gson gson = new Gson();

            // Читаем JSON из InputStream и парсим его в список объектов Product
            Reader reader = new InputStreamReader(inputStream);
            List<Product> products = gson.fromJson(reader, new TypeToken<List<Product>>(){}.getType());

            // Проверяем наличие продукта в списке
            for (Product product : products) {
                if (productName.equalsIgnoreCase(product.getName())) {
                    product.setQuantity(quanity);
                    return product;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // не знаю как лучше сделать
        return new Product();
    }
    private void openRecipeFragment(List<Product> productList) {
        if (productList.isEmpty()) return;
        RecipeDialogFragment dialog = RecipeDialogFragment.newInstance(productList);
        dialog.show(getParentFragmentManager(), "RecipeDialogFragment");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}