package com.example.homeeats.Adapters;

import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.example.homeeats.Dao.DeliveryBoyDao;
import com.example.homeeats.Dao.FoodBuyerDao;
import com.example.homeeats.Dao.MealItemDao;
import com.example.homeeats.Dao.OrderDao;
import com.example.homeeats.Models.DeliveryBoy;
import com.example.homeeats.Models.FoodBuyer;
import com.example.homeeats.Models.MealItem;
import com.example.homeeats.Models.Order;
import com.example.homeeats.Models.OrderItem;
import com.example.homeeats.Models.OrderStatus;
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
        if(Requests.get(position).orderStatus.toString()=="Pending")
        {
            holder.change_status.setVisibility(View.INVISIBLE);
        }
        else if(Requests.get(position).orderStatus.toString()=="Making")
        {
            holder.change_status.setVisibility(View.VISIBLE);
            holder.Reject.setVisibility(View.INVISIBLE);
            holder.accept.setVisibility(View.INVISIBLE);
        }
        else if(Requests.get(position).orderStatus.toString()=="ReadyForDelivery")
        {
            holder.change_status.setVisibility(View.INVISIBLE);
            holder.Reject.setVisibility(View.INVISIBLE);
            holder.accept.setVisibility(View.INVISIBLE);
        }

        holder.price.setText("EGP" + Requests.get(position).totalPrice.toString());
        holder.status.setText(Requests.get(position).orderStatus.toString());
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

       holder.accept.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               OrderDao.GetInstance().get(Requests.get(position).id, new RetrievalEventListener<Order>() {
                   @Override
                   public void OnDataRetrieved(Order order)
                   {
                       order.orderStatus=OrderStatus.Making;
                       OrderDao.GetInstance().SendOrderNotifications(order.id,"Order Accepted","Maker Accepted Your Order and it is currently in our kitchen");
                       OrderDao.GetInstance().save(order, order.id, new TaskListener() {
                           @Override
                           public void OnSuccess() {
                               Toast.makeText(holder.cardView.getContext(),"Status Updated Successfully", Toast.LENGTH_LONG).show();
                           }

                           @Override
                           public void OnFail() {
                               Toast.makeText(holder.cardView.getContext(),"Failed to Update Status", Toast.LENGTH_LONG).show();
                           }
                       });
                       holder.Reject.setVisibility(View.INVISIBLE);
                       holder.accept.setVisibility(View.INVISIBLE);
                       holder.change_status.setVisibility(View.VISIBLE);
                       holder.status.setText("Making");

                   }
               });
           }
       });
        holder.Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDao.GetInstance().get(Requests.get(position).id, new RetrievalEventListener<Order>() {
                    @Override
                    public void OnDataRetrieved(Order order)
                    {
                        order.orderStatus=OrderStatus.Rejected;
                        OrderDao.GetInstance().save(order, order.id, new TaskListener() {
                            @Override
                            public void OnSuccess()
                            {
                                Toast.makeText(holder.cardView.getContext(),"Status Updated Successfully", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void OnFail()
                            {
                                Toast.makeText(holder.cardView.getContext(),"Failed to Update Status", Toast.LENGTH_LONG).show();
                            }
                        });
                        holder.status.setText("rejected");
                        OrderDao.GetInstance().SendOrderNotifications(order.id,"Order Rejected","Maker Rejected Your Order Please try with another maker");
                    }
                });
            }
        });
        holder.change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDao.GetInstance().get(Requests.get(position).id, new RetrievalEventListener<Order>() {
                    @Override
                    public void OnDataRetrieved(Order order)
                    {
                        order.orderStatus=OrderStatus.ReadyForDelivery;
                        holder.status.setText("Ready to Deliver");
                        OrderDao.GetInstance().save(order, order.id, new TaskListener() {
                            @Override
                            public void OnSuccess() {
                                Toast.makeText(holder.cardView.getContext(),"Status Updated Successfully", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void OnFail() {
                                Toast.makeText(holder.cardView.getContext(),"Failed to Update Status", Toast.LENGTH_LONG).show();
                            }
                        });
                        OrderDao.GetInstance().SendOrderNotifications(order.id,"Order Done","Your Order is Cooked With Love and ready to be delivered");
                    }
                });
                holder.change_status.setVisibility(View.INVISIBLE);
            }
        });
        FoodBuyerDao.GetInstance().get(Requests.get(position).foodBuyerId, new RetrievalEventListener<FoodBuyer>() {
            @Override
            public void OnDataRetrieved(final FoodBuyer foodBuyer) {
                holder.BuyerName.setText(foodBuyer.name);
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
        TextView status;
        TextView BuyerName;
        TextView price;
        TextView DeliveryBoyName;
        TextView Description;
        ImageButton accept;
        ImageButton Reject;
        ImageButton change_status;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.Requests_Card_View);
            status = (TextView) itemView.findViewById(R.id.textViewRequestName);
            BuyerName = (TextView) itemView.findViewById(R.id.textViewRequestBuyerName);
            price = (TextView) itemView.findViewById(R.id.textViewRequestPrice);
            DeliveryBoyName = (TextView) itemView.findViewById(R.id.textViewRequestDeliveryBoyName);
            Description=(TextView)itemView.findViewById(R.id.textViewRequestItems);
            accept=(ImageButton)itemView.findViewById(R.id.FoodMaker_accept_order);
            Reject=(ImageButton)itemView.findViewById(R.id.FoodMaker_Reject_order);
            change_status=(ImageButton)itemView.findViewById(R.id.Ready_to_Deliver);
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
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
