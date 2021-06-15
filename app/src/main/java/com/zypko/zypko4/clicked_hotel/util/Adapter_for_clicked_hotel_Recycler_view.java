package com.zypko.zypko4.clicked_hotel.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zypko.zypko4.R;
import com.zypko.zypko4.show_searched_station_result_hotels.util.Adapter_for_Searched_hotels;

import java.util.ArrayList;

public class Adapter_for_clicked_hotel_Recycler_view extends RecyclerView.Adapter<Adapter_for_clicked_hotel_Recycler_view.view_Holder> {

    Context context;
    //String[] name,description;
    ArrayList<Hotel> hotels_ArrayList;

        private ClickListener clickListener;

    public Adapter_for_clicked_hotel_Recycler_view(Context context, ArrayList<Hotel> hotels_ArrayList){
        this.context = context;
        this.hotels_ArrayList = hotels_ArrayList;
    }

    @NonNull
    @Override
    public Adapter_for_clicked_hotel_Recycler_view.view_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_for_category_recycler_view,viewGroup,false);



        return new view_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_for_clicked_hotel_Recycler_view.view_Holder view_holder, int position) {

        view_holder.textView_food_Category_name.setText(hotels_ArrayList.get(position).getFood_category_name());
        view_holder.textView_food_Category_description.setText(hotels_ArrayList.get(position).getFood_category_description());

    }

    @Override
    public int getItemCount() {
        return hotels_ArrayList.size();
    }

    class view_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View view;
        TextView textView_food_Category_name,textView_food_Category_description;

        view_Holder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            textView_food_Category_name = view.findViewById(R.id.textView_food_Category_name);
            textView_food_Category_description = view.findViewById(R.id.textView_food_Category_description);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (clickListener != null){
                clickListener.itemClicked(v);
            }
        }
    }


    public void setClickListener(Adapter_for_clicked_hotel_Recycler_view.ClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public interface ClickListener{
        void itemClicked(View view);
    }

}
