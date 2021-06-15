package com.zypko.zypko4.user_account.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zypko.zypko4.R;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;
import com.zypko.zypko4.random_hotel.ui.fragment_for_random_hotel;
import com.zypko.zypko4.user_account.utils.Service_for_Rate_Delivery_Boy;

public class fragment_for_rate_delivery_boy extends Fragment {

    View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {

            root = inflater.inflate(R.layout.layout_for_rating,container,false);

            initialise();

        }

        return root;
    }

    private void initialise() {

        TextView name                   = root.findViewById(R.id.tv_name_delivery_boy_rating);
        final TextView rating_text            = root.findViewById(R.id.rating_text_delivery_rating);
        TextView phone_no               = root.findViewById(R.id.tv_phone_no_delivery_boy_rating);
        final EditText comment          = root.findViewById(R.id.et_comment_delivery_boy_rating);
        ImageView del_boy_img           = root.findViewById(R.id.imageView_delivery_boy_rating);
        final Button btn_submit         = root.findViewById(R.id.btn_submit_delivery_boy_rating);
        final RatingBar ratingBar       = root.findViewById(R.id.rating_delivery_boy_rating);

        name.setText(new PrefEditor(getActivity()).getString(Json_Shared_Preference.DELIVERY_BOY_NAME));
        phone_no.setText(new PrefEditor(getActivity()).getString(Json_Shared_Preference.DELIVERY_BOY_PHONE_NO));
        Picasso.get().load(UrlValues.SERVER+new PrefEditor(getActivity()).getString(Json_Shared_Preference.DELIVERY_BOY_IMAGE)).centerCrop().fit().into(del_boy_img);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rate_delivery_boy(ratingBar.getRating(),comment.getText().toString());
            }
        });


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rate, boolean fromUser) {

                int rating = (int) rate;

                if (rating != 5) {

                    if (rating < rate) {
                        rating = rating + 1;
                    }
                }

                switch (rating){

                    case 1 :
                        rating_text.setText("Very Bad");
                        break;

                    case 2 :
                        rating_text.setText("Bad");
                        break;

                    case 3 :
                        rating_text.setText("Normal");
                        break;

                    case 4 :
                        rating_text.setText("Good");
                        break;

                    case 5 :
                        rating_text.setText("Best");
                        break;

                }

            }
        });




    }


    private void rate_delivery_boy(float rate, String comment) {

        int rating = (int) rate;

        if (rating != 5) {

            if (rating < rate) {
                rating = rating + 1;
            }
        }

        Intent intent = new Intent(getActivity(), Service_for_Rate_Delivery_Boy.class);
        intent.putExtra("RATING",rating);
        intent.putExtra("COMMENT",comment);
        getActivity().startService(intent);


        new PrefEditor(getActivity()).writeData(Json_Shared_Preference.DELIVERED,"n");
        new FragmentOpener(getActivity()).setManager(getFragmentManager()).openReplace(new fragment_for_random_hotel());
    }


}

