package com.zypko.zypko4.cart.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zypko.zypko4.R;
import com.zypko.zypko4.clicked_hotel.util.Adapter_for_clicked_hotel_Recycler_view;
import com.zypko.zypko4.clicked_hotel.util.Food_Sqlite;
import com.zypko.zypko4.globals.Database;

import java.util.ArrayList;

public class Adapter_for_selected_food_items extends RecyclerView.Adapter<Adapter_for_selected_food_items.View_Holder> {

    private ArrayList<Food_Sqlite> cart_food_details_ArrayList;
    private Context context;

    private int track_individual_food_items = 0, food_made_amount = 0;
    private Database myDB;

    private ClickListener clickListener;

    public Adapter_for_selected_food_items(Context context, ArrayList<Food_Sqlite> cart_food_details_ArrayList) {
        this.context = context;
        this.cart_food_details_ArrayList = cart_food_details_ArrayList;

        myDB = new Database(context);
    }


    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_for_cart_food_items, viewGroup, false);

        return new View_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final View_Holder view_holder, final int position) {

        view_holder.selected_item_name.setText(cart_food_details_ArrayList.get(position).getFood_name());


        if (cart_food_details_ArrayList.get(position).gettotal_items() > 0) {
            view_holder.textView_individual_food_items.setText("" + cart_food_details_ArrayList.get(position).gettotal_items());

        } else {
            view_holder.minus_items.setVisibility(View.GONE);
            view_holder.textView_individual_food_items.setVisibility(View.GONE);
        }

        if (cart_food_details_ArrayList.get(position).getVeg().equals("y")) {
            view_holder.imageView_veg.setImageResource(R.drawable.veg);
        } else {
            view_holder.imageView_veg.setImageResource(R.drawable.non_veg);
        }

        view_holder.textView_food_individual_total_price.setText("" + cart_food_details_ArrayList.get(position).getFood_total_price());


        view_holder.add_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                track_individual_food_items = cart_food_details_ArrayList.get(position).gettotal_items();
                add_button_clicked(view_holder, position);
                //Toast.makeText(context, "" + track_individual_food_items, Toast.LENGTH_SHORT).show();
            }
        });

        view_holder.minus_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                track_individual_food_items = cart_food_details_ArrayList.get(position).gettotal_items();
                minus_button_clicked(view_holder, position);
                //Toast.makeText(context, "" + track_individual_food_items, Toast.LENGTH_SHORT).show();

            }
        });


        // old price

        int old_price = ( cart_food_details_ArrayList.get(position).getFood_made_price()
                + (( cart_food_details_ArrayList.get(position).getFood_actual_price() * cart_food_details_ArrayList.get(position).getFood_discount() )/100) ) * Integer.parseInt(view_holder.textView_individual_food_items.getText().toString());

        Log.e("OLD_PRICE",""+old_price);
        Log.e("OLD_PRICE",""+cart_food_details_ArrayList.get(position).getFood_actual_price());
        Log.e("OLD_PRICE",""+cart_food_details_ArrayList.get(position).getFood_made_price());
        Log.e("OLD_PRICE",""+cart_food_details_ArrayList.get(position).getFood_discount());
        view_holder.selected_item_old_price.setText(String.valueOf(old_price));

        if (cart_food_details_ArrayList.get(position).getFood_discount() == 0) {

            view_holder.linearLayout_old_price.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return cart_food_details_ArrayList.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {

        View view;
        TextView selected_item_name, selected_item_old_price, textView_individual_food_items, textView_food_individual_total_price;
        ImageView imageView_veg, add_items, minus_items;
        LinearLayout linearLayout_old_price;

        View_Holder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            selected_item_name              = view.findViewById(R.id.text_view_cart_selected_item_name);
            selected_item_old_price         = view.findViewById(R.id.text_view_cart_selected_item_old_price);
            selected_item_old_price         .setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            linearLayout_old_price          = view.findViewById(R.id.linear_layout_old_price);

            textView_individual_food_items  = view.findViewById(R.id.textView_individual_food_items);
            //textView_total_food_price_to_pay    = view.findViewById(R.id.textView_cart_total_food_price_to_pay);

            add_items                       = view.findViewById(R.id.imageView_add_item);
            minus_items                     = view.findViewById(R.id.imageView_minus_item);
            imageView_veg                   = view.findViewById(R.id.imageView_cart_veg_or_non_veg);
            textView_food_individual_total_price = view.findViewById(R.id.textView_total_individual_food_amount);

            //itemView.setOnClickListener(this);
            itemView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    //Toast.makeText(context, "layout change", Toast.LENGTH_SHORT).show();
                    if (clickListener != null){
                        clickListener.itemClicked(v,getAdapterPosition());
                    }
                }
            });


        }

        /*
        @Override
        public void onClick(View v) {
            if (clickListener != null){
                clickListener.itemClicked(v,getAdapterPosition());
            }
        }
        */

    }


    private void add_button_clicked(View_Holder viewHolder, int position) {

        if (track_individual_food_items == 0) {
            viewHolder.minus_items.setVisibility(View.VISIBLE);
            viewHolder.textView_individual_food_items.setVisibility(View.VISIBLE);

        }

        track_individual_food_items++;

        food_made_amount = cart_food_details_ArrayList.get(position).getFood_made_price() ;


        int individual_total_price = food_made_amount * track_individual_food_items;
        int total_packaging_charge  = cart_food_details_ArrayList.get(0).getFood_packing_charge() * track_individual_food_items;
        int total_food_taxes        = cart_food_details_ArrayList.get(0).getFood_taxes() * track_individual_food_items;

        myDB.check_selected_item(cart_food_details_ArrayList.get(position).getFood_id(),
                cart_food_details_ArrayList.get(position).getFood_name(),
                cart_food_details_ArrayList.get(position).getVeg(),
                cart_food_details_ArrayList.get(position).getFood_actual_price(),
                food_made_amount,
                individual_total_price,
                track_individual_food_items,
                cart_food_details_ArrayList.get(position).getFood_discount(),
                total_food_taxes,
                total_packaging_charge,
                cart_food_details_ArrayList.get(position).getHotel_name(),
                cart_food_details_ArrayList.get(position).getHotel_id());

        cart_food_details_ArrayList.get(position).settotal_items(track_individual_food_items);
        viewHolder.textView_individual_food_items.setText("" + track_individual_food_items);
        viewHolder.textView_food_individual_total_price.setText("" + individual_total_price);

        int old_price = ( cart_food_details_ArrayList.get(position).getFood_made_price()
                + (( cart_food_details_ArrayList.get(position).getFood_actual_price() * cart_food_details_ArrayList.get(position).getFood_discount() )/100)) * Integer.parseInt(viewHolder.textView_individual_food_items.getText().toString());

        viewHolder.selected_item_old_price.setText(String.valueOf(old_price));

    }


    private void minus_button_clicked(View_Holder viewHolder, int position) {


        if (track_individual_food_items > 1) {
            track_individual_food_items--;

            food_made_amount = cart_food_details_ArrayList.get(position).getFood_made_price() ;

            int individual_total_price = food_made_amount * track_individual_food_items;
            int total_packaging_charge  = cart_food_details_ArrayList.get(0).getFood_packing_charge() * track_individual_food_items;
            int total_food_taxes        = cart_food_details_ArrayList.get(0).getFood_taxes() * track_individual_food_items;

            myDB.check_selected_item(cart_food_details_ArrayList.get(position).getFood_id(),
                    cart_food_details_ArrayList.get(position).getFood_name(),
                    cart_food_details_ArrayList.get(position).getVeg(),
                    cart_food_details_ArrayList.get(position).getFood_actual_price(),
                    food_made_amount,
                    individual_total_price,
                    track_individual_food_items,
                    cart_food_details_ArrayList.get(position).getFood_discount(),
                    total_food_taxes,
                    total_packaging_charge,
                    cart_food_details_ArrayList.get(position).getHotel_name(),
                    cart_food_details_ArrayList.get(position).getHotel_id());


            cart_food_details_ArrayList.get(position).settotal_items(track_individual_food_items);
            viewHolder.textView_individual_food_items.setText("" + track_individual_food_items);
            viewHolder.textView_food_individual_total_price.setText("" + individual_total_price);

            int old_price = ( cart_food_details_ArrayList.get(position).getFood_made_price()
                    + (( cart_food_details_ArrayList.get(position).getFood_actual_price() * cart_food_details_ArrayList.get(position).getFood_discount() )/100) ) * Integer.parseInt(viewHolder.textView_individual_food_items.getText().toString());

            viewHolder.selected_item_old_price.setText(String.valueOf(old_price));


        } else {

            track_individual_food_items--;

            food_made_amount = cart_food_details_ArrayList.get(position).getFood_made_price() ;

            int individual_total_price = food_made_amount * track_individual_food_items ;
            int total_packaging_charge  = cart_food_details_ArrayList.get(0).getFood_packing_charge() * track_individual_food_items;
            int total_food_taxes        = cart_food_details_ArrayList.get(0).getFood_taxes() * track_individual_food_items;

            myDB.check_selected_item(cart_food_details_ArrayList.get(position).getFood_id(),
                    cart_food_details_ArrayList.get(position).getFood_name(),
                    cart_food_details_ArrayList.get(position).getVeg(),
                    cart_food_details_ArrayList.get(position).getFood_actual_price(),
                    food_made_amount,
                    individual_total_price,
                    track_individual_food_items,
                    cart_food_details_ArrayList.get(position).getFood_discount(),
                    total_food_taxes,
                    total_packaging_charge,
                    cart_food_details_ArrayList.get(position).getHotel_name(),
                    cart_food_details_ArrayList.get(position).getHotel_id());

            cart_food_details_ArrayList.get(position).settotal_items(track_individual_food_items);

            viewHolder.minus_items.setVisibility(View.GONE);
            viewHolder.textView_individual_food_items.setVisibility(View.GONE);

            int old_price = ( cart_food_details_ArrayList.get(position).getFood_made_price()
                    + (( cart_food_details_ArrayList.get(position).getFood_actual_price() * cart_food_details_ArrayList.get(position).getFood_discount() )/100) ) * Integer.parseInt(viewHolder.textView_individual_food_items.getText().toString());

            viewHolder.selected_item_old_price.setText(String.valueOf(old_price));

            myDB.Delete_sqlite_one_selected_row(cart_food_details_ArrayList.get(position).getFood_id());

            viewHolder.textView_individual_food_items.setText("" + track_individual_food_items);
            viewHolder.textView_food_individual_total_price.setText("" + individual_total_price);

            cart_food_details_ArrayList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,cart_food_details_ArrayList.size());

            if (cart_food_details_ArrayList.size() == 0){
                ((Activity)context).onBackPressed();
            }

        }


    }




    public void setClickListener(Adapter_for_selected_food_items.ClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public interface ClickListener{
        void itemClicked(View view,int position);
    }

}
