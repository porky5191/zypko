package com.zypko.zypko4.cart.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
import com.zypko.zypko4.R;
import com.zypko.zypko4.globals.UrlValues;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static com.zypko.zypko4.cart.util.Seter_Geter.ONE_TYPE;
import static com.zypko.zypko4.cart.util.Seter_Geter.TWO_TYPE;

public class Adapter_for_nested_recycler_view_for_cart extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Seter_Geter> list;
    Context context;

    public Adapter_for_nested_recycler_view_for_cart(Context context, ArrayList<Seter_Geter> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {

        Seter_Geter seter_geter = list.get(position);
        if (seter_geter != null) {
            return seter_geter.getType();
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view;

        switch (viewType) {
            case ONE_TYPE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_for_cart_recycler_view_one, viewGroup, false);
                return new first_view_holder(view);
            case TWO_TYPE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_for_cart_recycler_view_two, viewGroup, false);
                return new second_view_holder(view);
            /*case LIST_TYPE :
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_2,viewGroup,false);
                return new ListViewHOler(view); */

        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        Seter_Geter seter_geter = list.get(i);
        switch (seter_geter.getType()) {
            case ONE_TYPE:
                ((first_view_holder) viewHolder).hotel_name.setText("Hotel Dynasti");
                ((first_view_holder) viewHolder).hotel_address.setText("Gandhi Basti, Saraniya Hills");

                Picasso.get().load(UrlValues.IMAGE_URL+"10.jpg").fit().into(((first_view_holder) viewHolder).image_of_hotel);


                String[] str = new String[]{"Momo", "Pani Puri", "Chow", "Panir Butter Masala"};
                //ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,str);
                //((OneViewHOler)viewHolder).listView.setAdapter(adapter);
                ((first_view_holder) viewHolder).recyclerView_selected_items.setLayoutManager(new LinearLayoutManager(context));
                //Adapter_for_selected_food_items adapter2 = new Adapter_for_selected_food_items(str);
                //((first_view_holder) viewHolder).recyclerView_selected_items.setAdapter(adapter2);

                break;
            case TWO_TYPE:
                ((second_view_holder) viewHolder).subtotal.setText("90");
                ((second_view_holder) viewHolder).delivery_charge.setText("30");
                ((second_view_holder) viewHolder).total_amount.setText("120");
                break;
        }
    }



    // Decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 200;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class first_view_holder extends RecyclerView.ViewHolder {

        TextView hotel_name, hotel_address;
        ImageView image_of_hotel;
        RecyclerView recyclerView_selected_items;

        public first_view_holder(@NonNull View itemView) {
            super(itemView);
            hotel_name = itemView.findViewById(R.id.textView_cart_hotel_name);
            hotel_address = itemView.findViewById(R.id.textView_cart_hotel_address);
            image_of_hotel = itemView.findViewById(R.id.imageView_cart_hotel_photo);
            recyclerView_selected_items = itemView.findViewById(R.id.recycler_view_for_selected_items);
        }
    }


    class second_view_holder extends RecyclerView.ViewHolder {

        TextView subtotal, delivery_charge, total_amount;

        public second_view_holder(@NonNull View itemView) {
            super(itemView);
            subtotal = itemView.findViewById(R.id.textView_cart_subtotal);
            delivery_charge = itemView.findViewById(R.id.textView_cart_delivery_charge);
            total_amount = itemView.findViewById(R.id.textView_cart_total_amount);
        }
    }


}
