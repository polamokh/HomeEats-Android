package com.example.homeeats.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.homeeats.Dao.DeliveryBoyDao;
import com.example.homeeats.Dao.FoodBuyerDao;
import com.example.homeeats.Dao.MealItemDao;
import com.example.homeeats.Models.DeliveryBoy;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.Models.Order;
import com.example.homeeats.Models.OrderItem;
import com.example.homeeats.R;

import java.util.List;

public class FoodMakerRequestsRecycleAdapter extends RecyclerView.Adapter<FoodMakerRequestsRecycleAdapter.RequestViewHolder> {
    List<Order> Requests;
    String desc=" ";

    public FoodMakerRequestsRecycleAdapter(List<Order> Requests) {
        this.Requests = Requests;

    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_requests, parent,
                false);
        FoodMakerRequestsRecycleAdapter.RequestViewHolder RequestViewHolder = new FoodMakerRequestsRecycleAdapter.RequestViewHolder(view);
        return RequestViewHolder;


    }

    @Override
    public void onBindViewHolder(final RequestViewHolder holder, final int position) {
        holder.price.setText("EGP" + Requests.get(position).totalPrice.toString());
        holder.BuyerName.setText(Requests.get(position).orderStatus.toString());
        for(int i=0;i<Requests.get(position).orderItems.size();i++)
        {
            final int finalI = i;
            MealItemDao.GetInstance().get(Requests.get(position).orderItems.get(i).mealItemId, new RetrievalEventListener<MealItem>() {
                @Override
                public void OnDataRetrieved(MealItem mealItem) {
                    desc+=(Requests.get(position).orderItems.get(finalI).quantity.toString()+" "+mealItem.name+" "+";"+Requests.get(position).orderItems.get(finalI).notes+"\n"+" ");
                    holder.Description.setText(desc);
                }
            });

        }


        FoodBuyerDao.GetInstance().get(Requests.get(position).foodBuyerId, new RetrievalEventListener<FoodBuyer>() {
            @Override
            public void OnDataRetrieved(final FoodBuyer foodBuyer) {
                holder.name.setText(foodBuyer.name);
            }
        });
        DeliveryBoyDao.GetInstance().get(Requests.get(position).deliveryBoyId, new RetrievalEventListener<DeliveryBoy>() {
            @Override
            public void OnDataRetrieved(DeliveryBoy deliveryBoy) {
                holder.DeliveryBoyName.setText(deliveryBoy.name);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Requests.size();

    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView name;
        TextView BuyerName;
        TextView price;
        TextView DeliveryBoyName;
        TextView Description;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.Requests_Card_View);
            name = (TextView) itemView.findViewById(R.id.textViewRequestName);
            BuyerName = (TextView) itemView.findViewById(R.id.textViewRequestBuyerName);
            price = (TextView) itemView.findViewById(R.id.textViewRequestPrice);
            DeliveryBoyName = (TextView) itemView.findViewById(R.id.textViewRequestDeliveryBoyName);
            Description=(TextView)itemView.findViewById(R.id.textViewRequestItems);

            // START OF HABD
            final ConstraintLayout expandableView = itemView.findViewById(R.id.requests_expandableView);
            final Button arrowBtn = itemView.findViewById(R.id.arrowBtn_Requests);
            final CardView cardView = itemView.findViewById(R.id.Requests_Card_View);
            arrowBtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    if (expandableView.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.VISIBLE);
                        //arrowBtn.setBackgroundResource(R.drawable.ic_expand);
                    } else {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.GONE);
                        //arrowBtn.setBackgroundResource(R.drawable.ic_expand);
                    }
                }
            });
        }
    }
}
