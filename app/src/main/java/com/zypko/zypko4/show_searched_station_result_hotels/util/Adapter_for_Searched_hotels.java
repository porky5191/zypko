package com.zypko.zypko4.show_searched_station_result_hotels.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zypko.zypko4.R;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.random_hotel.util.Past_searh_Recycler_view_Adapter;

import java.util.ArrayList;

public class Adapter_for_Searched_hotels extends RecyclerView.Adapter<Adapter_for_Searched_hotels.Searched_hotels_View_Holder> {

    private ArrayList<Hotels_of_Searched_Station> hotels_ArrayList;
    private Context context;
    private ClickListener clickListener;

    public Adapter_for_Searched_hotels(Context context,ArrayList<Hotels_of_Searched_Station> hotels_ArrayList){
        this.context =context;
        this.hotels_ArrayList = hotels_ArrayList;

    }

    @NonNull
    @Override
    public Searched_hotels_View_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_for_searched_hotel,viewGroup,false);

        Searched_hotels_View_Holder viewHolder = new Searched_hotels_View_Holder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Searched_hotels_View_Holder view_holder, int position) {

        String url = UrlValues.HOTEL_IMAGE_URL
                + hotels_ArrayList.get(position).getHotel_station() + "/" + hotels_ArrayList.get(position).getHotel_name()
                + "/" +hotels_ArrayList.get(position).getHotel_id() + ".jpg";

        Log.e("URL",url);

        Picasso.get().load(url).fit().centerCrop().into(view_holder.imageView_of_single_photo);

        view_holder.textView_name_of_hotels.setText(hotels_ArrayList.get(position).getHotel_name());
        view_holder.textView_category_of_hotels.setText(hotels_ArrayList.get(position).getHotel_category());
        view_holder.textView_station_of_hotels.setText(hotels_ArrayList.get(position).getHotel_station());
        view_holder.textView_hotel_id.setText(""+hotels_ArrayList.get(position).getHotel_id());

    }

    @Override
    public int getItemCount() {
         return hotels_ArrayList.size();
    }

    class Searched_hotels_View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        View view;
        ImageView imageView_of_single_photo;
        TextView textView_name_of_hotels,textView_category_of_hotels,textView_station_of_hotels,textView_hotel_id;

        Searched_hotels_View_Holder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            imageView_of_single_photo   = view.findViewById(R.id.imageview_of_one_searched_hotel);
            textView_name_of_hotels     = view.findViewById(R.id.textview_name_of_searched_hotel);
            textView_category_of_hotels     = view.findViewById(R.id.textview_category_of_searched_hotel);
            textView_station_of_hotels     = view.findViewById(R.id.textview_station_of_searched_hotel);
            textView_hotel_id     = view.findViewById(R.id.textView_hotel_id);

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
