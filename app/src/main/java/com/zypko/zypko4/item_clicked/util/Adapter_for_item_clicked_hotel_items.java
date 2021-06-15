package com.zypko.zypko4.item_clicked.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zypko.zypko4.R;
import com.zypko.zypko4.clicked_random_food.utils.Food_of_Clicked_Category;
import com.zypko.zypko4.globals.UrlValues;

import java.util.ArrayList;

public class Adapter_for_item_clicked_hotel_items extends RecyclerView.Adapter<Adapter_for_item_clicked_hotel_items.View_holder> {

    private ArrayList<Food_of_Clicked_Category> food_category_ArrayList;
    private String station_name,hotel_name;

    public Adapter_for_item_clicked_hotel_items(ArrayList<Food_of_Clicked_Category> food_category_ArrayList,String station_name, String hotel_name){
        this.food_category_ArrayList = food_category_ArrayList;
        this.hotel_name = hotel_name;
        this.station_name = station_name;
    }

    private static Adapter_for_item_clicked_hotel_items.ClickListener clickListener;

    @NonNull
    @Override
    public Adapter_for_item_clicked_hotel_items.View_holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View root = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_for_one_image,viewGroup,false);

        return new Adapter_for_item_clicked_hotel_items.View_holder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_for_item_clicked_hotel_items.View_holder viewHolder, int position) {

        viewHolder.textview_name_of_food.setText(food_category_ArrayList.get(position).getFood_name());
        viewHolder.textView_id          .setText(""+food_category_ArrayList.get(position).getFood_id());

        // check the url correct or not
        String url = UrlValues.HOTEL_CATEGORY_IMAGE_URL+station_name+"/"+hotel_name
                +"/category/"+food_category_ArrayList.get(position).getFood_id()+".jpg";

        Picasso.get().load(url).centerCrop().fit().into(viewHolder.imageview_of_single_image);

        if (food_category_ArrayList.get(position).getVeg().equals("y")) {
            viewHolder.imageview_veg_or_non_veg.setImageResource(R.drawable.veg);
        }else {
            viewHolder.imageview_veg_or_non_veg.setImageResource(R.drawable.non_veg);
        }


    }

    @Override
    public int getItemCount() {
        return food_category_ArrayList.size();
    }

    static class View_holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View view;
        ImageView imageview_of_single_image,imageview_veg_or_non_veg;
        TextView textview_name_of_food,textView_id;

        View_holder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            imageview_of_single_image   = view.findViewById(R.id.imageview_of_one_photo);
            imageview_veg_or_non_veg    = view.findViewById(R.id.imageView_one_image_veg_or_non_veg);
            textview_name_of_food       = view.findViewById(R.id.textview_name_of_the_photo);
            textView_id                 = view.findViewById(R.id.textView_id);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(context, "clicked "+past_search_textview.getText(), Toast.LENGTH_SHORT).show();


            if (clickListener != null){
                clickListener.itemClicked(v,getAdapterPosition());
            }

        }
    }


    public void setClickListener(ClickListener clickListener) {
        Adapter_for_item_clicked_hotel_items.clickListener = clickListener;
    }


    public interface ClickListener{
        void itemClicked(View view,int position);
    }


}
