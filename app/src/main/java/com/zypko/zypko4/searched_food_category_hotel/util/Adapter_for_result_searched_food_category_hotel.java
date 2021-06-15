package com.zypko.zypko4.searched_food_category_hotel.util;

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
import com.zypko.zypko4.show_searched_station_result_hotels.util.Adapter_for_Searched_hotels;

public class Adapter_for_result_searched_food_category_hotel extends RecyclerView.Adapter<Adapter_for_result_searched_food_category_hotel.View_holder> {

    private String[] data;

    private ClickListener clickListener;

    public Adapter_for_result_searched_food_category_hotel(String[] data){

        this.data = data;
    }

    @NonNull
    @Override
    public View_holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_for_searched_food_category_hotel,viewGroup,false);


        return new View_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_holder view_holder, int position) {
        Picasso.get().load(UrlValues.IMAGE_URL+data[position]).fit().centerCrop().into(view_holder.imageView_for_search);

        view_holder.textView_for_search.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class View_holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View view;
        TextView textView_for_search;
        ImageView imageView_for_search;

        View_holder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            textView_for_search     = view.findViewById(R.id.textview_name_of_searched_item);
            imageView_for_search    = view.findViewById(R.id.imageview_of_searched_item);

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
