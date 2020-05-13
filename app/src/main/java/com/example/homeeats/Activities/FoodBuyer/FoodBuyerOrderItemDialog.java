package com.example.homeeats.Activities.FoodBuyer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.firbasedao.Listeners.TaskListener;
import com.example.homeeats.Dao.OrderDao;
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
        textView.setText(mealID);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderItem newOrderItem = new OrderItem(mealID, Integer.parseInt(quantity.getText().toString()), instructions.getText().toString(), -1, 432.3);
                currentOrder.orderItems.add(newOrderItem);
                // FIXME: hardcoded code.
                OrderDao.GetInstance().save(currentOrder, "-M710vWwEgbXCnldG3ub", new TaskListener() {
                    @Override
                    public void OnSuccess() {
                        Log.e("200!", "success :)");
                        getDialog().dismiss();
                    }
                    @Override
                    public void OnFail() {
                        getDialog().dismiss();
                    }
                });
            }
        });

        return view;
    }

    public FoodBuyerOrderItemDialog(String mealID, Order currentOrder){
        super();
        this.mealID = mealID;
        this.currentOrder = currentOrder;
    }
}
