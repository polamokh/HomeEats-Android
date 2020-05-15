package com.example.homeeats.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.homeeats.Activities.FoodBuyer.FoodBuyerOrderDetailedActivity;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.Models.Order;
import com.example.homeeats.R;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class FoodBuyerOrderRecycleAdapter extends RecyclerView.Adapter<FoodBuyerOrderRecycleAdapter.OrderViewHolder> {
    List<Order> orders;
    public FoodBuyerOrderRecycleAdapter(List<Order> orders){
        this.orders = orders;
    }
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodbuyer_order_card, parent, false);
        OrderViewHolder OrderViewHolder = new OrderViewHolder(view);
        return OrderViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, int position) {
        holder.name.setText(orders.get(position).foodMakerId);  //TODO get name
        holder.orderId.setText(orders.get(position).id);
        holder.orderStatus.setText(orders.get(position).orderStatus.toString());
        FoodMakerDao.GetInstance().get(orders.get(position).foodMakerId, new RetrievalEventListener<FoodMaker>() {
            @Override
            public void OnDataRetrieved(FoodMaker foodMaker) {
                holder.name.setText(foodMaker.name);
                Picasso.get()
                        .load(foodMaker.photo)
                        .into(holder.profileImage);
            }
        });
    }
    @Override
    public int getItemCount() {
        return orders.size();
    }
    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView profileImage;
        TextView name;
        TextView orderId;
        TextView orderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
             cardView = itemView.findViewById(R.id.foodBuyerMealOrderView);
             profileImage = itemView.findViewById(R.id.foodBuyerOrderMakerImage);
             name = itemView.findViewById(R.id.foodBuyerOrderMakerName);
             orderId = itemView.findViewById(R.id.foodBuyerOrderID);
             orderStatus = itemView.findViewById(R.id.foodBuyerOrderStatus);
             cardView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     cardClick(v);
                 }
             });
        }

        void cardClick(View view) {
            Intent intent = new Intent(view.getContext(), FoodBuyerOrderDetailedActivity.class);
            intent.putExtra("orderID", orderId.getText());
            view.getContext().startActivity(intent);
        }
    }
}
