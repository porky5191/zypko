package com.zypko.zypko4.activity.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zypko.zypko4.R;
import com.zypko.zypko4.globals.FragmentOpener;
import com.zypko.zypko4.random_hotel.ui.fragment_for_random_hotel;

public class RandomHotelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_hotel);

        new FragmentOpener(this).setManager(getSupportFragmentManager()).openReplace(new fragment_for_random_hotel());

    }


    //*********************************************************************************************///

///*
//
//    private void requeststoragwWritePermission() {
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
//
//            new AlertDialog.Builder(this)
//                    .setTitle("Permission Needed")
//                    .setMessage("Permission is needed to store Files in your memory")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            ActivityCompat.requestPermissions(RandomHotelActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_WRITE_PERMISSION_CODE);
//                        }
//                    })
//                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    })
//                    .create().show();
//        }else {
//            ActivityCompat.requestPermissions(RandomHotelActivity.this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_WRITE_PERMISSION_CODE );
//        }
//    }
//
//    private void requestStoragePermission() {
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
//
//            new AlertDialog.Builder(this)
//                    .setTitle("Permission Needed")
//                    .setMessage("Permission is needed to store your photo in your memory")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            ActivityCompat.requestPermissions(RandomHotelActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
//                        }
//                    })
//                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    })
//                    .create().show();
//        }else {
//            ActivityCompat.requestPermissions(RandomHotelActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE );
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode == STORAGE_PERMISSION_CODE || requestCode == STORAGE_WRITE_PERMISSION_CODE){
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
//            }else {
//                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }*/
}
