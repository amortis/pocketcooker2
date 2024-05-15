package com.example.pocketcooker2.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;


import com.example.pocketcooker2.R;
import com.example.pocketcooker2.data.Product;
import com.example.pocketcooker2.data.ProductAdapter;
import com.example.pocketcooker2.databinding.FragmentDashboardBinding;

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

        return root;
    }
    private void addProduct() {
        EditText nameEditText = requireView().findViewById(R.id.product_name_edit_text);
        EditText quantityEditText = requireView().findViewById(R.id.product_quantity_edit_text);


        String name = nameEditText.getText().toString().trim();
        // Здесь вы можете получить другие свойства продукта из формы ввода, если необходимо

        if (!name.isEmpty() && isProductAvailable(name)) {
            Product product = new Product(name);
            // Здесь вы можете задать другие свойства продукта
            productList.add(product);
            adapter.notifyDataSetChanged();
            nameEditText.setText("");
            quantityEditText.setText("");

        }
    }
    private boolean isProductAvailable(String productName) {
        try {
            // Чтение содержимого файла product.json
            String path = "android.resource://" + getContext().getPackageName() + "/res/raw/" + "product.json";

            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            reader.close();

            // Преобразование содержимого файла в объект JSON
            JSONArray productsArray = new JSONArray(jsonContent.toString());

            // Проверка наличия продукта в массиве
            for (int i = 0; i < productsArray.length(); i++) {
                JSONObject productObj = productsArray.getJSONObject(i);
                String name = productObj.getString("name");
                Log.d("json", name);

                // Если имя продукта совпадает с введенным, возвращаем true
                if (productName.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("json", "error");
        }
        return false;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}