package com.zypko.zypko4.globals;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zypko.zypko4.R;


public class FragmentOpener extends ContextWrapper {

    Context context;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public FragmentOpener(Context base) {
        super(base);
        this.context = base;
    }

    public FragmentOpener setManager(FragmentManager fm){
        this.fragmentManager = fm;

        return this;
    }

    public void open(Fragment fragment){
        String backStack = fragment.getClass().getName().toUpperCase();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.f_container,fragment);
        fragmentTransaction.addToBackStack(backStack);
        fragmentTransaction.commit();
    }

    public void openReplace(Fragment fragment){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.f_container, fragment);
        fragmentTransaction.commit();
    }

    public void openReplace(Fragment fragment, Bundle bundle){
        fragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.f_container, fragment);
        fragmentTransaction.commit();
    }

    public void open_Replace_Backstack(Fragment fragment){
        String backStack = fragment.getClass().getName().toUpperCase();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.f_container,fragment);
        fragmentTransaction.addToBackStack(backStack);
        fragmentTransaction.commit();
    }

    public void open_Replace_Backstack(Fragment fragment, Bundle bundle){
        String backStack = fragment.getClass().getName().toUpperCase();
        fragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.f_container,fragment);
        fragmentTransaction.addToBackStack(backStack);
        fragmentTransaction.commit();
    }

    public void remove_fragment(Fragment fragment){

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
        fragmentManager.popBackStack();
    }

}