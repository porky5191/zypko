package com.zypko.zypko4.user_account.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zypko.zypko4.R;

import java.util.ArrayList;

public class Adapter_for_past_orders extends RecyclerView.Adapter<Adapter_for_past_orders.View_Holder> {

    private Context context;
    private ArrayList<Past_Order> past_orders_ArrayList;


    public Adapter_for_past_orders(Context context,ArrayList<Past_Order> past_orders_ArrayList) {
        this.context = context;
        this.past_orders_ArrayList = past_orders_ArrayList;

    }

    @NonNull
    @Override
    public Adapter_for_past_orders.View_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_for_past_order,viewGroup,false);

        return new View_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_for_past_orders.View_Holder view_holder, final int position) {

        view_holder.tv_order_id.setText(""+past_orders_ArrayList.get(position).getOrder_table_id());
        view_holder.tv_hotel_name.setText(past_orders_ArrayList.get(position).getHotel_name());
        view_holder.tv_hotel_address.setText(past_orders_ArrayList.get(position).getHotel_address());
        view_holder.tv_station_name.setText(past_orders_ArrayList.get(position).getStation_name());
        view_holder.tv_delivered_date.setText(past_orders_ArrayList.get(position).getOrder_date());
        view_holder.tv_delivered_time.setText(past_orders_ArrayList.get(position).getDelivered_time());
        view_holder.tv_total_items.setText(past_orders_ArrayList.get(position).getTotal_items());
        view_holder.tv_paid_amount.setText(past_orders_ArrayList.get(position).getPaid_amount());


        if (past_orders_ArrayList.get(position).getPay_by_cash_online().equals("c")) {

            view_holder.tv_paid_by_cash.setText("Cash");
        }else if (past_orders_ArrayList.get(position).getPay_by_cash_online().equals("o")) {

            view_holder.tv_paid_by_cash.setText("Online");

        } else if (past_orders_ArrayList.get(position).getPay_by_cash_online().equals("p")) {

            view_holder.tv_paid_by_cash.setText("Partial");
        }


        if (past_orders_ArrayList.get(position).getOrder_accepted().equals("r")) {

            view_holder.tv_delivered_status.setText("Order Cancel");
            view_holder.tv_delivered_status.setTextColor(Color.RED);

        }else if (past_orders_ArrayList.get(position).getDelivered().equals("y")) {

            view_holder.tv_delivered_status.setText("Delivered");
            view_holder.tv_delivered_status.setTextColor(Color.GREEN);
        }else {

            view_holder.tv_delivered_status.setText("Not Delivered");
            view_holder.tv_delivered_status.setTextColor(Color.RED);
        }


        view_holder.delete_imageView_past_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_box(position);

            }
        });

    }

    private void dialog_box(final int position) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

        builder.setTitle("Delete !!");
        builder.setMessage("Are you sure want to delete your past order history ?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                delete_history(position);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorBlue_tez));
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorBlue_tez));

    }

    private void delete_history(int position) {

        Intent intent = new Intent(context,Service_for_remove_past_order.class);
        intent.putExtra("ORDER_TABLE_ID",past_orders_ArrayList.get(position).getOrder_table_id());
        context.startService(intent);

        past_orders_ArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, past_orders_ArrayList.size());

    }

    @Override
    public int getItemCount() {
        return past_orders_ArrayList.size();
    }


    class View_Holder extends RecyclerView.ViewHolder{

        ImageView delete_imageView_past_order;
        TextView tv_order_id,tv_hotel_name,tv_hotel_address,tv_station_name,tv_delivered_status,tv_delivered_time,tv_delivered_date,tv_total_items,tv_paid_amount,tv_paid_by_cash;

        View_Holder(@NonNull View itemView) {
            super(itemView);
            tv_order_id                 = itemView.findViewById(R.id.tv_order_table_id);
            tv_hotel_name               = itemView.findViewById(R.id.tv_user_account_hotel_name);
            tv_hotel_address            = itemView.findViewById(R.id.tv_user_account_hotel_address);
            tv_station_name             = itemView.findViewById(R.id.tv_user_account_station_name);
            tv_delivered_status         = itemView.findViewById(R.id.tv_user_account_delivered_status);
            tv_delivered_time           = itemView.findViewById(R.id.tv_user_account_delivered_time);
            tv_delivered_date           = itemView.findViewById(R.id.tv_user_account_delivered_date);
            tv_total_items              = itemView.findViewById(R.id.tv_user_account_total_items);
            tv_paid_amount              = itemView.findViewById(R.id.tv_user_account_paid_amount);
            tv_paid_by_cash             = itemView.findViewById(R.id.tv_user_account_paid_via_cash);
            delete_imageView_past_order = itemView.findViewById(R.id.delete_imageView_past_order);


        }
    }
}
