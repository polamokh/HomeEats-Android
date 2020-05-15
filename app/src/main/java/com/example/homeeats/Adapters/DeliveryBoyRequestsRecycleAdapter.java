package com.example.homeeats.Adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.example.firbasedao.Listeners.RetrievalEventListener;
import com.example.firbasedao.Listeners.TaskListener;
import com.example.homeeats.Activities.DeliveryBoy.DeliveryBoyActivity;
import com.example.homeeats.Activities.DeliveryBoy.DeliveryBoyViewOrderDetailsActivity;
import com.example.homeeats.Activities.MainActivity;
import com.example.homeeats.Dao.DeliveryBoyDao;
import com.example.homeeats.Dao.FoodBuyerDao;
import com.example.homeeats.Dao.FoodMakerDao;
import com.example.homeeats.Dao.MealItemDao;
import com.example.homeeats.Dao.OrderDao;
import com.example.homeeats.Models.DeliveryBoy;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.Models.FoodMaker;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.Models.Order;
import com.example.homeeats.Models.OrderStatus;
import com.example.homeeats.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class DeliveryBoyRequestsRecycleAdapter extends RecyclerView.Adapter<DeliveryBoyRequestsRecycleAdapter.RequestViewHolder>

{
    List<Order> Requests;
    public DeliveryBoyRequestsRecycleAdapter(List<Order> Requests)
    {
    this.Requests = Requests;

}
    @NonNull
    @Override
    public DeliveryBoyRequestsRecycleAdapter.RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_boy_order_card, parent,
                false);
        DeliveryBoyRequestsRecycleAdapter.RequestViewHolder RequestViewHolder = new DeliveryBoyRequestsRecycleAdapter.RequestViewHolder(view);
        return RequestViewHolder;
    }
    @Override
    public void onBindViewHolder(final RequestViewHolder holder, final int position) {
        if(Requests.get(position).orderStatus==OrderStatus.getValue("Delivered"))
        {
            holder.View_details.setVisibility(View.INVISIBLE);
        }
        holder.status.setText(Requests.get(position).orderStatus.toString());
        FoodMakerDao.GetInstance().get(Requests.get(position).foodMakerId, new RetrievalEventListener<FoodMaker>() {
            @Override
            public void OnDataRetrieved(FoodMaker foodMaker) {
                holder.MakerName.setText(foodMaker.name);
            }
        });
        FoodBuyerDao.GetInstance().get(Requests.get(position).foodBuyerId, new RetrievalEventListener<FoodBuyer>() {
            @Override
            public void OnDataRetrieved(FoodBuyer foodBuyer) {
                holder.BuyerName.setText(foodBuyer.name);
            }
        });
        holder.View_details.setOnClickListener(new View.OnClickListener()
                                               {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(holder.View_details.getContext(), DeliveryBoyViewOrderDetailsActivity.class);
                intent.putExtra("OrderID",Requests.get(position).id );
                view.getContext().startActivity(intent);
            }
                                               }
        );


    }

    @Override
    public int getItemCount() {
        return Requests.size();

    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView status;
        TextView BuyerName;
        TextView MakerName;
        ImageView View_details;
        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.DeliveryBoyOrdersCardView);
            status = (TextView) itemView.findViewById(R.id.DeliveryBoyOrdersViewStatus);
            MakerName = (TextView) itemView.findViewById(R.id.DeliveryBoyOrdersViewMakerName);
            BuyerName = (TextView) itemView.findViewById(R.id.DeliveryBoyOrdersViewBuyerName);
            View_details=(ImageView) itemView.findViewById(R.id.View_Order_Details);
        }
    }
}
