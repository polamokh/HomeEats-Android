package com.example.homeeats.Activities.FoodMaker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.homeeats.Dao.MealItemDao;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.R;
import com.example.homeeats.TaskListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

public class FoodMakerAddMealActivity extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_maker_add_meal);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Button selectImage = (Button)findViewById(R.id.uploadImage);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        final MealItemDao MD= MealItemDao.GetInstance();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        ImageButton fab =(ImageButton)findViewById(R.id.add);
        final EditText Name=(EditText)findViewById(R.id.Name_textbox);
       final EditText Desc=(EditText)findViewById(R.id.Desc_textbox);
     final   EditText Price=(EditText)findViewById(R.id.Price_textbox);
       final EditText Category=(EditText)findViewById(R.id.Category_textbox);
       fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MealItem MI=new MealItem(null,Name.getText().toString(), getIntent().getExtras().getString("FoodMakerID"), "", Desc.getText().toString(),Double.parseDouble(Price.getText().toString()),"", 4.5);
                MD.save(MI, MealItemDao.GetInstance().GetNewKey(), new TaskListener() {
                    @Override
                    public void OnSuccess() {
                        Toast.makeText(getApplicationContext(),"Meal Added Successfully",Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void OnFail() {
                        Toast.makeText(getApplicationContext(),"Failed To Add Meal",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                ImageView imageView = findViewById(R.id.imageView2);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


