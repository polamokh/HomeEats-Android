package com.example.homeeats.Activities.FoodMaker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.firbasedao.Listeners.TaskListener;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Dao.MealItemDao;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.R;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public class FoodMakerEditMealActivity extends AppCompatActivity {
    ImageButton edit;
    EditText edited_name;
    EditText edited_desc;
    EditText edited_price;
    EditText Edited_Category;
    private int PICK_IMAGE_REQUEST = 1;
    ImageView image;
    String Foodmaker_ID;
    Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_maker_edit_meal);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        edited_name = findViewById(R.id.FoodMaker_Edit_Meal_Name);
        edited_desc = findViewById(R.id.FoodMaker_Edit_Meal_Desc);
        final Button selectImage = (Button) findViewById(R.id.Food_Maker_Edit_Meal_Image_btn);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        edited_price = findViewById(R.id.FoodMaker_Edit_Meal_Price);
        Edited_Category = findViewById(R.id.FoodMaker_Edit_Meal_Category);
        image = findViewById(R.id.Food_maker_Edit_Meal_Photo);
        edit = findViewById(R.id.Edit_Food_Maker_Meal_btn);
        final String Meal_ID = getIntent().getExtras().getString("MealID");

        // selectImage=MealItemDao.GetInstance().get(Meal);
        MealItemDao.GetInstance().get(Meal_ID, new RetrievalEventListener<MealItem>()
        {
            @Override
            public void OnDataRetrieved(MealItem mealItem)
            {
                edited_name.setText(mealItem.name);
                edited_desc.setText(mealItem.description);
                edited_price.setText(mealItem.price.toString());
                Edited_Category.setText(mealItem.mealCategory);
                Picasso.get().load(mealItem.photo).into(image);
                Foodmaker_ID = mealItem.foodMakerId;
                //selectedImage = ((BitmapDrawable)image.getDrawable()).getBitmap();

            }
        });
        edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (selectedImage == null)
                    selectedImage = ((BitmapDrawable) image.getDrawable()).getBitmap();
                final MealItem MI = new MealItem(Meal_ID, edited_name.getText().toString(),
                        Foodmaker_ID,
                        "",
                        edited_desc.getText().toString(),
                        Double.parseDouble(edited_price.getText().toString()), Edited_Category.getText().toString(), 4.5, true);
                         MealItemDao.GetInstance().save(MI, Meal_ID, selectedImage, new TaskListener()
                            {
                                @Override
                                public void OnSuccess() {
                                    Toast.makeText(getApplicationContext(), "Meal Edited Successfully", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void OnFail() {
                                    Toast.makeText(getApplicationContext(), "Failed To Edit Meal", Toast.LENGTH_LONG).show();
                                }
                            }
                    );
                }

        });
    }

    public void chooseImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            Uri uri = data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                selectedImage = bitmap;
                image.setImageBitmap(selectedImage);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
