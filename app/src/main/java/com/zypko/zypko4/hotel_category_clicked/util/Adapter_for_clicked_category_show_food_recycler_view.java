package com.zypko.zypko4.hotel_category_clicked.util;

import android.content.Context;
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
import com.zypko.zypko4.clicked_random_food.utils.Food_of_Clicked_Category;
import com.zypko.zypko4.globals.UrlValues;

import java.util.ArrayList;


public class Adapter_for_clicked_category_show_food_recycler_view extends RecyclerView.Adapter<Adapter_for_clicked_category_show_food_recycler_view.View_Holder> {

    private ClickListener clickListener;
    private ArrayList<Food_of_Clicked_Category> food_ArrayList;
    Food_of_Clicked_Category foods;
    private Context context;

    public Adapter_for_clicked_category_show_food_recycler_view(Context context, ArrayList<Food_of_Clicked_Category> food_ArrayList) {
        this.food_ArrayList = food_ArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_for_clicked_category_recycler_view,viewGroup,false);

        return new View_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder view_holder, int position) {

        String url = UrlValues.HOTEL_CATEGORY_IMAGE_URL+food_ArrayList.get(position).getStation_name()+"/"+food_ArrayList.get(position).getHotel_name()
                +"/category/"+food_ArrayList.get(position).getFood_id()+".jpg";

        Picasso.get().load(url).fit().centerCrop().into(view_holder.image_of_food);

        //Log.e("URL",url);

        view_holder.textView_food_name.setText(food_ArrayList.get(position).getFood_name());
        view_holder.textView_food_id.setText(""+food_ArrayList.get(position).getFood_id());


        if (food_ArrayList.get(position).getVeg().equals("y")) {
            view_holder.image_view_veg_or_non_veg.setImageResource(R.drawable.veg);
        }else {
            view_holder.image_view_veg_or_non_veg.setImageResource(R.drawable.non_veg);
        }

//        int food_price = food_ArrayList.get(position).getFood_price() + food_ArrayList.get(position).getFood_taxes()
//                + food_ArrayList.get(position).getFood_packing_charge() - ((food_ArrayList.get(position).getFood_price() * food_ArrayList.get(position).getFood_discount()) / 100);

        int food_price = food_ArrayList.get(position).getFood_price() - ((food_ArrayList.get(position).getFood_price() * food_ArrayList.get(position).getFood_discount()) / 100);

        view_holder.tv_price.setText(""+food_price);

        switch (food_ArrayList.get(position).getOffer_by_us()) {
            case "n":
                view_holder.image_view_offer_by_us.setVisibility(View.GONE);

                break;
            case "b":
                view_holder.image_view_offer_by_us.setImageResource(R.drawable.best_seller);

                break;
            case "s":
                view_holder.image_view_offer_by_us.setImageResource(R.drawable.spcial_offer);
                break;
        }

        if (food_ArrayList.get(position).getText_above_image().equals("null")) {
            view_holder.relativeLayout.setVisibility(View.GONE);

        }else {
            view_holder.tv_text_above_image.setText(""+food_ArrayList.get(position).getText_above_image());
        }


    }

    @Override
    public int getItemCount() {
        return food_ArrayList.size();
    }

    public class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image_of_food,image_view_veg_or_non_veg,image_view_offer_by_us;
        TextView textView_food_name,textView_food_id,tv_text_above_image,tv_price;
        RelativeLayout relativeLayout;

        View_Holder(@NonNull View itemView) {
            super(itemView);

            image_of_food               = itemView.findViewById(R.id.imageView_of_clicked_category_food);
            image_view_veg_or_non_veg   = itemView.findViewById(R.id.imageView_Veg_or_non_veg_clicked_category);
            textView_food_name          = itemView.findViewById(R.id.textview_name_of_clicked_category_food_name);
            textView_food_id            = itemView.findViewById(R.id.textView_food_id);
            tv_text_above_image         = itemView.findViewById(R.id.tv_text_above_image_clicked_hotel_category);
            image_view_offer_by_us      = itemView.findViewById(R.id.imageView_offer_by_us_clicked_hotel_category);
            relativeLayout              = itemView.findViewById(R.id.relative_layout_text_above_image_clicked_category);
            tv_price                    = itemView.findViewById(R.id.textView_price_of_food_category);

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

    public void setClickListener(Adapter_for_clicked_category_show_food_recycler_view.ClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public interface ClickListener{
        void itemClicked(View view);
    }
}
