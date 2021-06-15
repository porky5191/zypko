package com.zypko.zypko4.activity.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zypko.zypko4.R;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.random_hotel.util.Food_category;
import com.zypko.zypko4.random_hotel.util.Past_searh_Recycler_view_Adapter;

import java.util.ArrayList;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.one_photo_viewHolder> {

    private int size;
    private String[] dataset;
    private ArrayList<Food_category> food_category_ArrayList;

    public GridAdapter(ArrayList<Food_category> food_category_ArrayList){
        this.food_category_ArrayList = food_category_ArrayList;
    }

    private static ClickListener clickListener;

    @NonNull
    @Override
    public one_photo_viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View root = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_for_one_image,viewGroup,false);

        one_photo_viewHolder viewHolder = new one_photo_viewHolder(root);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull one_photo_viewHolder viewHolder, int position) {

        Picasso.get().load(UrlValues.FOOD_CATEGORY_IMAGE_URL+food_category_ArrayList.get(position).getId()+".jpg").fit().centerCrop().into(viewHolder.imageview_of_single_image);

        viewHolder.textview_name_of_food.setText(food_category_ArrayList.get(position).getName());
        viewHolder.img_veg.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return food_category_ArrayList.size();
    }

    static class one_photo_viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View view;
        ImageView imageview_of_single_image,img_veg;
        TextView textview_name_of_food;

        one_photo_viewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            imageview_of_single_image   = view.findViewById(R.id.imageview_of_one_photo);
            textview_name_of_food       = view.findViewById(R.id.textview_name_of_the_photo);
            img_veg                     = view.findViewById(R.id.imageView_one_image_veg_or_non_veg);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(context, "clicked "+past_search_textview.getText(), Toast.LENGTH_SHORT).show();


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
