package com.nandanarafiardika.mycoffeeshopdrinklist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nandanarafiardika.mycoffeeshopdrinklist.model.CoffeeResponse;
import com.nandanarafiardika.mycoffeeshopdrinklist.rest.ApiClient;
import com.nandanarafiardika.mycoffeeshopdrinklist.rest.ApiInterface;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {
    ApiInterface mApiInterface;
    ProgressDialog progressDialog;
    EditText name, price, detail;
    String pathPoster = null;
    String pathThumbnail = null;
    TextView thumbnailPrev, posterPrev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        ImageView homeButton = findViewById(R.id.iv_home);
        homeButton.setOnClickListener(v -> {
            startActivity(new Intent(AddActivity.this, MainActivity.class));
            finish();
        });

        ImageView profileButton = findViewById(R.id.iv_profile);
        profileButton.setOnClickListener(v -> {
            new Intent(AddActivity.this, ProfileActivity.class);
            finish();
        });

        name = findViewById(R.id.et_name);
        price = findViewById(R.id.et_price);
        detail = findViewById(R.id.et_detail);

        thumbnailPrev = findViewById(R.id.tv_thumbnail);
        posterPrev = findViewById(R.id.tv_poster);

        Button thumbnail = findViewById(R.id.btn_thumbnail);
        thumbnail.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery, 0);
        });
        Button poster = findViewById(R.id.btn_poster);
        poster.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery, 1);
        });

        Button submit = findViewById(R.id.btn_submit);
        submit.setOnClickListener(v -> {
            post(name.getText().toString(), price.getText().toString(), detail.getText().toString());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(resultCode == RESULT_OK && null != data){
                Uri selected = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selected, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                if(requestCode == 0){
                    pathThumbnail = cursor.getString(columnIndex);
                    thumbnailPrev.setText(pathThumbnail);
                }
                else if(requestCode == 1){
                    pathPoster = cursor.getString(columnIndex);
                    posterPrev.setText(pathPoster);
                }
                cursor.close();
            }
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void post(String name, String price, String detail){
        if(!checkField()){
            return;
        };

        progressDialog.show();

        File fileThumbnail = new File(pathThumbnail);
        RequestBody requestBodyThumbnail = RequestBody.create(MediaType.parse("*/*"), fileThumbnail);
        MultipartBody.Part uploadThumbnail = MultipartBody.Part.createFormData("photoThumbnail", fileThumbnail.getName(), requestBodyThumbnail);

        File filePoster = new File(pathPoster);
        RequestBody requestBodyPoster = RequestBody.create(MediaType.parse("*/*"), filePoster);
        MultipartBody.Part uploadPoster = MultipartBody.Part.createFormData("photoPoster", filePoster.getName(), requestBodyPoster);

        Call<CoffeeResponse> CoffeeCall = mApiInterface.postCoffee("add_coffee",
                RequestBody.create(MediaType.parse("text/plain"), name),
                RequestBody.create(MediaType.parse("text/plain"), price),
                RequestBody.create(MediaType.parse("text/plain"), detail),
                uploadThumbnail,
                uploadPoster);
        CoffeeCall.enqueue(new Callback<CoffeeResponse>() {
            @Override
            public void onResponse(Call<CoffeeResponse> call, Response<CoffeeResponse> response) {
                progressDialog.dismiss();
                Toast.makeText(AddActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                clearField();
            }

            @Override
            public void onFailure(Call<CoffeeResponse> call, Throwable t) {
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                builder.setMessage(t.getMessage());
                builder.show();
            }
        });
    }

    private boolean checkField(){
        if(name.getText().toString().trim().isEmpty() || price.getText().toString().trim().isEmpty() || detail.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "you have to fill all the required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pathThumbnail == null){
            Toast.makeText(this, "you have to select a thumbnail", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pathPoster == null){
            Toast.makeText(this, "you have to select a poster", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void clearField(){
        name.setText("");
        price.setText("");
        detail.setText("");
        thumbnailPrev.setText("No Thumbnail selected");
        posterPrev.setText("No Poster selected");

        pathThumbnail = null;
        pathPoster = null;
    }
}