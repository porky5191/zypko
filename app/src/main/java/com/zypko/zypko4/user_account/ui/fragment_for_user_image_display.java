package com.zypko.zypko4.user_account.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.zypko.zypko4.R;
import com.zypko.zypko4.activity.ui.UserAccountActivity;
import com.zypko.zypko4.globals.AlertDialog_For_NoInternet;
import com.zypko.zypko4.globals.CheckInternet;
import com.zypko.zypko4.globals.PrefEditor;
import com.zypko.zypko4.globals.Prefs;
import com.zypko.zypko4.globals.UrlValues;
import com.zypko.zypko4.json_Objects.Json_Objects;
import com.zypko.zypko4.json_Objects.Json_Shared_Preference;
import com.zypko.zypko4.json_Objects.Json_User_Details;
import com.zypko.zypko4.server.HTTP_Post_Callback;
import com.zypko.zypko4.server.NetRequest;
import com.zypko.zypko4.server.Post_to_Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class fragment_for_user_image_display extends Fragment {

    View root;
    public static final int IMG_REQUEST = 1;

    ImageView imageView;
    private Bitmap bitmap;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (root == null) {

            root = inflater.inflate(R.layout.fragment_for_user_image_display,container,false);

            setHasOptionsMenu(true);

            root.setFocusableInTouchMode(true);
            root.requestFocus();

            initialise();

        }

        return root;
    }



    private void initialise() {

        imageView           = root.findViewById(R.id.display_user_image);

        Toolbar toolbar = root.findViewById(R.id.toolbar_user_account_display_image);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        display_the_image();

    }



    private void display_the_image() {

        int user_id = (int) new PrefEditor(getActivity()).getLong(Json_Shared_Preference.USER_ID);

        Picasso.get()
                .load("file:/storage/emulated/0/Android/obb/com.zypko.zypko4/zypko/"+user_id+".jpg")
                .placeholder(R.drawable.edit_user)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .fit().centerCrop()
                .into(imageView);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_for_edit_user_image, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (R.id.menu_edit_user_image == item.getItemId()) {

            new MaterialDialog.Builder(getActivity())
                    .title("Set your Image")
                    .items(R.array.uploadImages)
                    .itemsIds(R.array.itemIds)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                            switch (position) {
                                case 0:
                                    Intent intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(intent, IMG_REQUEST);
                                    break;

                                case 1:
                                    imageView.setImageResource(R.drawable.edit_user);
                                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.edit_user);

                                    Save_Image_to_Internal_Storage();
                                    getActivity().onBackPressed();

                                    break;
                            }
                        }
                    }).show();

        }

        return super.onOptionsItemSelected(item);
    }


    private void Save_Image_to_Internal_Storage() {

        int user_id = (int) new PrefEditor(getActivity()).getLong(Json_Shared_Preference.USER_ID);


        File dir = new File(getActivity().getObbDir(),"zypko");
        dir.mkdir();

        File file = new File(dir, user_id+".jpg");

        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        rotate_bitmap(""+getActivity().getObbDir().getAbsolutePath()+"/zypko/"+user_id+".jpg");

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (new CheckInternet(getActivity()).Internet()) {
            if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {

                try {
                    // Rotate the bitmap to original orientation
                    Uri selectedImage = data.getData();
                    String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
                    Cursor cur = getActivity().managedQuery(selectedImage, orientationColumn, null, null, null);
                    int orientation = -1;
                    if (cur != null && cur.moveToFirst()) {
                        orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
                    }
                    InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                    bitmap = BitmapFactory.decodeStream(imageStream);
                    switch(orientation) {
                        case 90:
                            bitmap = rotateImage(bitmap, 90);
                            break;
                        case 180:
                            bitmap = rotateImage(bitmap, 180);
                            break;
                        case 270:
                            bitmap = rotateImage(bitmap, 270);
                            break;
                        default:
                            break;
                    }

                    imageView.setImageBitmap(bitmap );

                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setTitle("Uploading");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(true);
                    progressDialog.show();

                    Upload_Image_to_server();

                    ///// for offline uses..

                    Save_Image_to_Internal_Storage();

                    progressDialog.dismiss();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "error image", Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            new AlertDialog_For_NoInternet(getActivity()).AlertDialog();
        }
    }


    private void Upload_Image_to_server() {

        String url = UrlValues.SEND_USER_IMAGE;

        Map<String, String> map = new HashMap<>();

        map.put("ENCODED_STRING", Bitmap_to_String(bitmap));
        map.put("USER_ID", ""+new PrefEditor(getActivity()).getLong(Json_Shared_Preference.USER_ID));

        Post_to_Server pst = new Post_to_Server(getActivity(), map);
        pst.getJson(url, new PostCall());

    }

    private String Bitmap_to_String(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }


    private class PostCall implements HTTP_Post_Callback {
        @Override
        public void onSuccess(String string) {

            try {
                JSONObject object = new JSONObject(string);

                int success = object.getInt(Json_Objects.SUCCESS);

                if (success > 0) {

                    Toast.makeText(getActivity(), "Profile Photo Updated Successfully", Toast.LENGTH_SHORT).show();

                    Save_Image_to_Internal_Storage();

                    progressDialog.dismiss();

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Catch View teacher image >" + e, Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
            }
        }
        @Override
        public void onError(VolleyError error) {
            progressDialog.hide();
        }
    }


    private void rotate_bitmap(String path){

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(path);

        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);


        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                bitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                bitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                bitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                bitmap = bitmap;
        }

    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    @Override
    public void onPause() {
        super.onPause();

        NetRequest.getInstance(getActivity()).getRequestQueue().cancelAll(com.zypko.zypko4.server.NetRequest.TAG);

    }

}
