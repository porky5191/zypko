package com.zypko.zypko4.clicked_random_food.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zypko.zypko4.R;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.random_hotel.util.Food_category;

import java.util.ArrayList;

public class Adapter_for_clicked_random_food extends RecyclerView.Adapter<Adapter_for_clicked_random_food.View_holder> {

    private int size;
    private ArrayList<Food_of_Clicked_Category> food_category_ArrayList;

    public Adapter_for_clicked_random_food(ArrayList<Food_of_Clicked_Category> food_category_ArrayList){
        this.food_category_ArrayList = food_category_ArrayList;
    }

    private static ClickListener clickListener;

    @NonNull
    @Override
    public Adapter_for_clicked_random_food.View_holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View root = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_for_image_of_food_category,viewGroup,false);

        return new Adapter_for_clicked_random_food.View_holder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_for_clicked_random_food.View_holder viewHolder, int position) {

        String url = UrlValues.HOTEL_CATEGORY_IMAGE_URL+food_category_ArrayList.get(position).getStation_name()+"/"+food_category_ArrayList.get(position).getHotel_name()
                +"/category/"+food_category_ArrayList.get(position).getFood_id()+".jpg";


        Picasso.get().load(url).fit().centerCrop().into(viewHolder.imageview_of_single_image);
        //Log.d("URRRL",UrlValues.SERVER+food_category_ArrayList.get(position).getPhoto_of_food()+".jpg");

        //Picasso.get().load(R.drawable.image).fit().into(viewHolder.imageview_of_single_image);
        viewHolder.textview_name_of_food.setText(food_category_ArrayList.get(position).getFood_name());
        viewHolder.textView_id          .setText(""+food_category_ArrayList.get(position).getFood_id());

        if (food_category_ArrayList.get(position).getVeg().equals("y")) {
            viewHolder.imageview_veg_or_non_veg.setImageResource(R.drawable.veg);
        }else {
            viewHolder.imageview_veg_or_non_veg.setImageResource(R.drawable.non_veg);
        }

//        int food_price = food_category_ArrayList.get(position).getFood_price() + food_category_ArrayList.get(position).getFood_taxes()
//                + food_category_ArrayList.get(position).getFood_packing_charge() - ((food_category_ArrayList.get(position).getFood_price() * food_category_ArrayList.get(position).getFood_discount()) / 100);

        int food_price = food_category_ArrayList.get(position).getFood_price() - ((food_category_ArrayList.get(position).getFood_price() * food_category_ArrayList.get(position).getFood_discount()) / 100);

        viewHolder.tv_price.setText(""+food_price);

        switch (food_category_ArrayList.get(position).getOffer_by_us()) {
            case "n":
                viewHolder.image_view_offer_by_us.setVisibility(View.GONE);
                break;
            case "b":
                viewHolder.image_view_offer_by_us.setImageResource(R.drawable.best_seller);
                break;
            case "s":
                viewHolder.image_view_offer_by_us.setImageResource(R.drawable.spcial_offer);
                break;
            default:
                viewHolder.image_view_offer_by_us.setVisibility(View.GONE);
        }

        if (food_category_ArrayList.get(position).getText_above_image().equals("null")) {
            viewHolder.relativeLayout.setVisibility(View.GONE);

        }else {
            viewHolder.tv_text_above_image.setText(""+food_category_ArrayList.get(position).getText_above_image());
        }


    }

    @Override
    public int getItemCount() {
        return food_category_ArrayList.size();
    }

    static class View_holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View view;
        ImageView imageview_of_single_image,imageview_veg_or_non_veg,image_view_offer_by_us;
        TextView textview_name_of_food,textView_id,tv_price,tv_text_above_image;
        RelativeLayout relativeLayout;

        View_holder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            imageview_of_single_image   = view.findViewById(R.id.imageView_of_food_category);
            imageview_veg_or_non_veg    = view.findViewById(R.id.imageView_of_food_category_veg_or_non_veg);
            textview_name_of_food       = view.findViewById(R.id.textView_name_of_food_category);
            textView_id                 = view.findViewById(R.id.textView_id);
            tv_price                    = view.findViewById(R.id.textView_price_of_food_category);
            tv_text_above_image         = itemView.findViewById(R.id.tv_text_above_image_clicked_hotel_category);
            image_view_offer_by_us      = itemView.findViewById(R.id.imageView_offer_by_us_clicked_hotel_category);
            relativeLayout              = itemView.findViewById(R.id.relative_layout_text_above_image_clicked_category);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (clickListener != null){
                clickListener.itemClicked(v);
            }

        }
    }


    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public interface ClickListener{
        void itemClicked(View view);
    }


}
