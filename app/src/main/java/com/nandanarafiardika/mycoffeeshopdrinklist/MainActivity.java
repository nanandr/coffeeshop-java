package com.nandanarafiardika.mycoffeeshopdrinklist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.nandanarafiardika.mycoffeeshopdrinklist.model.CoffeeDrink;
import com.nandanarafiardika.mycoffeeshopdrinklist.model.CoffeeResponse;
import com.nandanarafiardika.mycoffeeshopdrinklist.rest.ApiClient;
import com.nandanarafiardika.mycoffeeshopdrinklist.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ApiInterface mApiInterface;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView addButton = findViewById(R.id.iv_add);
        addButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddActivity.class));
            finish();
        });

        ImageView profileButton = findViewById(R.id.iv_profile);
        profileButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            finish();
        });

        EditText searchBox = findViewById(R.id.et_search);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                data(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                data(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                data(s.toString());
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_coffee);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        data(null);
    }

    public void data(String keyword) {
        Call<CoffeeResponse> CoffeeCall = mApiInterface.getCoffee("get_coffee");
        if(keyword != null){
            CoffeeCall = mApiInterface.searchCoffee("get_coffee", keyword);
        }
        CoffeeCall.enqueue(new Callback<CoffeeResponse>() {
            @Override
            public void onResponse(Call<CoffeeResponse> call, Response<CoffeeResponse> response) {
                List<CoffeeDrink> CoffeeList = response.body().getData();
                Log.d("Retrofit Get", "Jumlah data Coffee: " + String.valueOf(CoffeeList.size()));
                ListCoffeeDrinkAdapter listCoffeeDrinkAdapter = new ListCoffeeDrinkAdapter(CoffeeList);
                mRecyclerView.setAdapter(listCoffeeDrinkAdapter);
                //Onclick
                listCoffeeDrinkAdapter.setOnItemClickCallback(data -> showSelectedCoffeeDrink(data));
            }

            @Override
            public void onFailure(Call<CoffeeResponse> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(t.getMessage());
                builder.show();
            }
        });
    }

    //Onclick method
    private void showSelectedCoffeeDrink(CoffeeDrink coffeeDrink){
        Intent detail = new Intent(MainActivity.this, DetailActivity.class);
        detail.putExtra("id", coffeeDrink.getId());
        detail.putExtra("name", coffeeDrink.getName());
        detail.putExtra("price", coffeeDrink.getPrice());
        detail.putExtra("detail", coffeeDrink.getDetail());
        detail.putExtra("photoPoster", coffeeDrink.getPhotoPoster());
        detail.putExtra("createdAt", coffeeDrink.getCreatedAt());
        startActivity(detail);
    }
}