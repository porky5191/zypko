package com.zypko.zypko4.show_searched_station_result_hotels.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zypko.zypko4.R;

public class Adapter_for_searching_food_category_Hotel extends RecyclerView.Adapter<Adapter_for_searching_food_category_Hotel.View_holder> {

    private String[] history;
    private int history_or_search;

    private ClickListener clickListener;

    public Adapter_for_searching_food_category_Hotel(String[] history, int history_or_search){
        this.history = history;
        this.history_or_search = history_or_search;
    }

    @NonNull
    @Override
    public View_holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_for_past_search,viewGroup,false);


        return new View_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_holder view_holder, int position) {
        view_holder.textView_for_search.setText(""+history[position]);
        //Toast.makeText(context, ""+stations.get(position), Toast.LENGTH_SHORT).show();

        if (history_or_search == 0){
            view_holder.imageView_for_past_search.setImageResource(R.drawable.ic_history_black);
        }else {
            view_holder.imageView_for_past_search.setImageResource(R.drawable.ic_search_button);
        }
    }

    @Override
    public int getItemCount() {
        return history.length;
    }

    class View_holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        View view;
        TextView textView_for_search;
        ImageView imageView_for_past_search;

        View_holder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            textView_for_search = view.findViewById(R.id.textview_for_past_search);
            imageView_for_past_search = view.findViewById(R.id.imageview_of_past_search);

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
        this.clickListener = clickListener;
    }


    public interface ClickListener{
        void itemClicked(View view, int position);
    }

}
