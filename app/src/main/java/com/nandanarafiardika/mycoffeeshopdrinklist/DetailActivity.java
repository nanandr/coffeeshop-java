package com.nandanarafiardika.mycoffeeshopdrinklist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nandanarafiardika.mycoffeeshopdrinklist.model.CoffeeDrink;
import com.nandanarafiardika.mycoffeeshopdrinklist.model.CoffeeResponse;
import com.nandanarafiardika.mycoffeeshopdrinklist.rest.ApiClient;
import com.nandanarafiardika.mycoffeeshopdrinklist.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        TextView tvName = findViewById(R.id.tv_name);
        TextView tvPrice = findViewById(R.id.tv_price);
        TextView tvDetail = findViewById(R.id.tv_detail);
        ImageView imgPoster = findViewById(R.id.img_poster);
        ImageView edit = findViewById(R.id.iv_edit);
        ImageView delete = findViewById(R.id.iv_delete);

        String id = getIntent().getStringExtra("id");
        String nama = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        String detail = getIntent().getStringExtra("detail");
        String imgPosterFile = getIntent().getStringExtra("photoPoster");
        String createdAt = getIntent().getStringExtra("createdAt");

        tvName.setText(nama);
        tvPrice.setText("Rp. " + price);
        tvDetail.setText(detail + "\nadded " + createdAt);

        Glide.with(DetailActivity.this).load(Config.IMAGES_URL + imgPosterFile).apply(new RequestOptions().placeholder(R.color.teal_200)).into(imgPoster);

        edit.setOnClickListener(v -> {

        });

        delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete " + nama);
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("YES", (dialog, which) -> {
                Call<CoffeeResponse> DeleteCoffee = mApiInterface.deleteCoffee("delete_coffee", id);
                DeleteCoffee.enqueue(new Callback<CoffeeResponse>() {
                    @Override
                    public void onResponse(Call<CoffeeResponse> call, Response<CoffeeResponse> response) {
                        Toast.makeText(DetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DetailActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(Call<CoffeeResponse> call, Throwable t) {
                        Log.e("Retrofit Get", t.toString());
                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                        builder.setMessage(t.getMessage());
                        builder.show();
                    }
                });
            });
            builder.setNegativeButton("NO", ((dialog, which) -> {}));
            builder.show();
        });
    }
}

