package com.example.homeeats.Activities.FoodMaker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.R;
import com.example.homeeats.RetrievalEventListener;

public class FoodMakerEditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_maker_edit_profile);
        final EditText edited_name=(EditText)findViewById(R.id.FoodMaker_Edit_Name);
        final EditText edited_Email=(EditText)findViewById(R.id.FoodMaker_Edit_Email);
        final EditText edited_Location=(EditText)findViewById(R.id.FoodMaker_Edit_Location);
        final EditText edited_Phone=(EditText)findViewById(R.id.FoodMaker_Edit_Phone);
        final ImageButton edit=(ImageButton) findViewById(R.id.Edit_Food_Maker_Button);
        final String Food_maker_ID= getIntent().getExtras().getString("FoodMakerID");
        FoodMakerDao.GetInstance().get(Food_maker_ID, new RetrievalEventListener<FoodMaker>() {
            @Override
            public void OnDataRetrieved(FoodMaker foodMaker)
            {
                edited_name.setText(foodMaker.name);
                edited_Email.setText(foodMaker.emailAddress);
                edited_Location.setText(foodMaker.location.toString());
                edited_Phone.setText(foodMaker.phone);

            }
        });
        edit.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                FoodMakerDao.GetInstance().get(Food_maker_ID, new RetrievalEventListener<FoodMaker>()
                {
                    @Override
                    public void OnDataRetrieved(FoodMaker foodMaker)
                    {
                        foodMaker.name=edited_name.getText().toString();
                        foodMaker.emailAddress=edited_Email.getText().toString();
                        foodMaker.phone=edited_Phone.getText().toString();
                        Toast.makeText(getApplicationContext(),"Profile Edited Successfully", Toast.LENGTH_LONG).show();

                    }
                });


            }
        });


    }
}
