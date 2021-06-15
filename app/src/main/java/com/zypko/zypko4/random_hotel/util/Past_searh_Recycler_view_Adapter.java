package com.zypko.zypko4.random_hotel.util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zypko.zypko4.R;
import com.zypko.zypko4.activity.ui.RandomHotelActivity;
import com.zypko.zypko4.activity.ui.SearchedStationResultHotelsActivity;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.server.HTTP_Get;


public class Past_searh_Recycler_view_Adapter extends RecyclerView.Adapter<Past_searh_Recycler_view_Adapter.Past_search_View_holder> {

    //private ArrayList stations;
    private Context context;
    private String[] stations;
    private int history_or_search;

    private ClickListener clickListener;

    public Past_searh_Recycler_view_Adapter(Context context,String[] stations,int history_or_search){
        this.context = context;
        this.stations = stations;
        this.history_or_search = history_or_search;

    }


    @NonNull
    @Override
    public Past_searh_Recycler_view_Adapter.Past_search_View_holder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int position) {

        final View root = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_for_past_search,viewGroup,false);

        return new Past_search_View_holder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull Past_searh_Recycler_view_Adapter.Past_search_View_holder view_holder, int position) {

        view_holder.past_search_textview.setText(""+stations[position]);
        //Toast.makeText(context, ""+stations.get(position), Toast.LENGTH_SHORT).show();

        if (history_or_search == 0){
            view_holder.imageView_for_past_search.setImageResource(R.drawable.ic_history_black);
        }else {
            view_holder.imageView_for_past_search.setImageResource(R.drawable.ic_search_button);
        }


    }

    @Override
    public int getItemCount() {
        return stations.length;
    }




    class Past_search_View_holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View view;
        TextView past_search_textview;
        ImageView imageView_for_past_search;
        TextView textview_for_error_message;

        Past_search_View_holder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            past_search_textview = itemView.findViewById(R.id.textview_for_past_search);
            imageView_for_past_search = itemView.findViewById(R.id.imageview_of_past_search);
            textview_for_error_message = itemView.findViewById(R.id.textview_for_error_message_in_past_search);

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
