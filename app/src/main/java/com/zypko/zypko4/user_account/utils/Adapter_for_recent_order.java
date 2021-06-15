package com.zypko.zypko4.user_account.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zypko.zypko4.R;
import com.zypko.zypko4.activity.ui.TrackingActivity;
import com.zypko.zypko4.cart.util.Adapter_for_selected_food_items;

import java.util.ArrayList;

public class Adapter_for_recent_order extends RecyclerView.Adapter<Adapter_for_recent_order.View_Holder> {

    private Context context;
    private ArrayList<Recent_Order> recent_orders_ArrayList;

    private ClickListener clickListener;

    public Adapter_for_recent_order(Context context,ArrayList<Recent_Order> recent_orders_ArrayList) {
        this.context = context;
        this.recent_orders_ArrayList = recent_orders_ArrayList;
    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_for_recent_order,viewGroup,false);

        return new View_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder view_holder, final int position) {


        view_holder.tv_order_id         .setText(""+recent_orders_ArrayList.get(position).getOrder_table_id());
        view_holder.tv_hotel_name       .setText(recent_orders_ArrayList.get(position).getHotel_name());
        view_holder.tv_hotel_address    .setText(recent_orders_ArrayList.get(position).getHotel_address());
        view_holder.tv_station_name     .setText(recent_orders_ArrayList.get(position).getStation_name());
        view_holder.tv_total_items      .setText(recent_orders_ArrayList.get(position).getTotal_items());
        view_holder.tv_paid_amount      .setText(recent_orders_ArrayList.get(position).getPaid_amount());


        switch (recent_orders_ArrayList.get(position).getPay_by_cash_online()) {
            case "c":

                view_holder.tv_paid_by_cash.setText("Cash");

                view_holder.tv_paid_indicator.setText("Payment through ");
                view_holder.tv_amount_left_to_pay.setText(recent_orders_ArrayList.get(position).getPaid_amount());
                view_holder.linearLayout_paid_amount.setVisibility(View.GONE);
                view_holder.tv_layout_paid_amount.setVisibility(View.GONE);

                break;
            case "o":

                view_holder.tv_paid_by_cash.setText("Online");

                view_holder.tv_paid_indicator.setText("Paid via ");
                view_holder.tv_partially_paid_amount.setText(recent_orders_ArrayList.get(position).getPaid_amount());
                view_holder.tv_amount_left_to_pay.setText("0");
                view_holder.linearLayout_paid_amount.setVisibility(View.VISIBLE);
                view_holder.tv_layout_paid_amount.setVisibility(View.VISIBLE);

                break;
            case "p":

                view_holder.tv_paid_by_cash.setText("Partial");

                view_holder.tv_paid_indicator.setText("Paid via ");
                view_holder.tv_partially_paid_amount.setText(recent_orders_ArrayList.get(position).getPartially_paid_amount());
                view_holder.tv_amount_left_to_pay.setText(recent_orders_ArrayList.get(position).getAmount_left());
                view_holder.linearLayout_paid_amount.setVisibility(View.VISIBLE);
                view_holder.tv_layout_paid_amount.setVisibility(View.VISIBLE);

                break;
        }

        view_holder.btn_track_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                track_order(recent_orders_ArrayList.get(position).getOrder_table_id());
                //Toast.makeText(context, ""+recent_orders_ArrayList.size(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return recent_orders_ArrayList.size();
    }



    class View_Holder extends RecyclerView.ViewHolder{

        TextView tv_order_id,tv_hotel_name,tv_hotel_address,tv_station_name,tv_total_items,tv_paid_amount,tv_paid_by_cash,tv_partially_paid_amount,tv_amount_left_to_pay;
        Button btn_track_order;
        LinearLayout linearLayout_patrially_paid,linearLayout_paid_amount;
        TextView tv_layout_paid_amount,tv_paid_indicator;

        View_Holder(@NonNull View itemView) {
            super(itemView);

            tv_order_id         = itemView.findViewById(R.id.tv_recent_order_table_id);
            tv_hotel_name       = itemView.findViewById(R.id.tv_recent_order_user_account_hotel_name);
            tv_hotel_address    = itemView.findViewById(R.id.tv_recent_order_user_account_hotel_address);
            tv_station_name     = itemView.findViewById(R.id.tv_recent_order_user_account_station_name);
            tv_total_items      = itemView.findViewById(R.id.tv_recent_order_user_account_total_items);
            tv_paid_amount      = itemView.findViewById(R.id.tv_recent_order_user_account_paid_amount);
            tv_paid_by_cash     = itemView.findViewById(R.id.tv_recent_order_user_account_paid_via_cash);
            btn_track_order     = itemView.findViewById(R.id.btn_recent_order_track_your_order);

            tv_partially_paid_amount        = itemView.findViewById(R.id.tv_recent_order_paid_amount);
            tv_amount_left_to_pay           = itemView.findViewById(R.id.tv_recent_order_amount_left_to_pay);
            linearLayout_patrially_paid     = itemView.findViewById(R.id.linear_layout_partial_payment);

            linearLayout_paid_amount        = itemView.findViewById(R.id.linear_layout_paid_amount);
            tv_layout_paid_amount           = itemView.findViewById(R.id.tv_recent_order_layout_paid_amount);
            tv_paid_indicator               = itemView.findViewById(R.id.tv_recent_order_user_account_pay_indicator);


            itemView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                    if (clickListener != null){
                        clickListener.itemClicked(v,getAdapterPosition());
                    }
                }
            });


        }
    }


    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public interface ClickListener{
        void itemClicked(View view,int position);
    }

    ////*************************************************/////////////

    private void track_order(int order_tracking_id) {

        Intent intent = new Intent(context,TrackingActivity.class);
        intent.putExtra("ORDER_TABLE_ID",order_tracking_id);
        intent.putExtra("FRAGMENT","USER_ACTIVITY");
        context.startActivity(intent);

    }

}
