package com.zypko.zypko4.bottom_sheet_for_location;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zypko.zypko4.R;

public class fragment_for_Bottom_sheet_for_asking_location extends BottomSheetDialogFragment {

    View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {
            root = inflater.inflate(R.layout.bottom_shetet_layout_for_asking_location,container,false);
        }

        return root;
    }
}
