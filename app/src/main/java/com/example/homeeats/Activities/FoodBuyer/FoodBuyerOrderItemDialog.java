package com.example.homeeats.Activities.FoodBuyer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.firbasedao.Listeners.TaskListener;
import com.example.homeeats.Dao.CartOrderItemDao;
import com.example.homeeats.Dao.MealItemDao;
import com.example.homeeats.Dao.OrderDao;
import com.example.homeeats.Models.CartOrderItem;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.Models.Order;
import com.example.homeeats.Models.OrderItem;
import com.example.homeeats.R;

import org.w3c.dom.Text;

public class FoodBuyerOrderItemDialog extends DialogFragment {
    private String mealID;
    private TextView textView;
    private EditText instructions;
    private EditText quantity;
    private TextView cancelButton;
    private TextView confirmButton;
    private Order currentOrder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.foodbuyer_order_item_dialog, container, false);
        textView = view.findViewById(R.id.heading);
        this.instructions = view.findViewById(R.id.foodbuyer_dialog_notes_input);
        this.quantity = view.findViewById(R.id.foodbuyer_dialog_qty_input);
        this.cancelButton = view.findViewById(R.id.foodbuyer_action_cancel);
        this.confirmButton = view.findViewById(R.id.foodbuyer_action_add_order_item);
        MealItemDao.GetInstance().get(mealID, new RetrievalEventListener<MealItem>() {
            @Override
            public void OnDataRetrieved(MealItem mealItem) {
                textView.setText(mealItem.name);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final CartOrderItem cartOrderItem = new CartOrderItem("", getActivity().getIntent().getExtras()
                        .getString("FoodBuyerID"), mealID, Integer.parseInt(quantity.getText().toString()),
                        instructions.getText().toString(), 3, 0.0);
                cartOrderItem.getCurrentTotalPrice(new RetrievalEventListener<Double>() {
                    @Override
                    public void OnDataRetrieved(Double aDouble) {
                        cartOrderItem.totalPrice = aDouble;

                        CartOrderItemDao.GetInstance().Add(cartOrderItem, new TaskListener() {
                            @Override
                            public void OnSuccess() {
                                getDialog().dismiss();
                                Toast.makeText(v.getContext(), "Added successfully", Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void OnFail() {

                            }
                        });
                    }
                });
            }
        });

        return view;
    }

    public FoodBuyerOrderItemDialog(String mealID, Order currentOrder) {
        super();
        this.mealID = mealID;
        this.currentOrder = currentOrder;
    }
}
