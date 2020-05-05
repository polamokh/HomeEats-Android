package com.example.homeeats.Activities.FoodMaker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.R;
import com.example.homeeats.RetrievalEventListener;
import com.example.homeeats.TaskListener;

public class FoodMakerEditProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_food_maker_edit_profile, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        final EditText edited_name;
        edited_name = getView().findViewById(R.id.FoodMaker_Edit_Name);
        final EditText edited_Email=getView().findViewById(R.id.FoodMaker_Edit_Email);
        final EditText edited_Location=getView().findViewById(R.id.FoodMaker_Edit_Location);
        final EditText edited_Phone=getView().findViewById(R.id.FoodMaker_Edit_Phone);
        final ImageButton edit=getView().findViewById(R.id.Edit_Food_Maker_Button);
        final String Food_maker_ID= getActivity().getIntent().getExtras().getString("FoodMakerID");
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
                        FoodMakerDao.GetInstance().save(foodMaker, Food_maker_ID, new TaskListener() {
                            @Override
                            public void OnSuccess() {
                                Toast.makeText(getActivity(),"Profile Edited Successfully", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void OnFail()
                            {
                                Toast.makeText(getActivity(),"Failed to Update Profile", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });


            }
        });
    }
}
