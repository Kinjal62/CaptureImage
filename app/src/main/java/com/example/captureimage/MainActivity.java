package com.example.captureimage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.captureimage.Adapter.ImageAdapter;
import com.example.captureimage.Model.ImageModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imageview;
    Button btn;
    ImageButton cancel;
    private int SELECT_FROM_GALLERY = 0;
    private int REQUEST_CAMERA = 1;
    public static final int RequestPermissionCode = 1;
    public static final int cameraRequestPermissionCode = 2;
    String currentPhotoPath;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ImageAdapter imageAdapter;
    ArrayList<String> image = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button)findViewById(R.id.btn);
        recyclerView = findViewById(R.id.rv_image);
        cancel = (ImageButton)findViewById(R.id.cancel);
        btn.setOnClickListener(this);
}

    public void ImageChooser() {
        final CharSequence[] items = {"Take Photo", "Choose from library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    CamaraIntent();
                } else if (items[item].equals("Choose from library")) {
                    GalleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public void CamaraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager())!=null);
        {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }catch (Exception e){
            }if(photoFile != null){
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(Intent.createChooser(intent,"Select Camara"),REQUEST_CAMERA);
            }
        }
    }
    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",
                /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    public void GalleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FROM_GALLERY);
    }

    @Override
    public void onClick(View v) {
            if (checkPermission()) {
                if (camaraCheckPermission()) {
                    ImageChooser();
                } else {
                    camaraRequestPermission();
                }
            } else {
                requestPermission();
            }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (resultCode == RESULT_OK) {
                String path = null;
                if (requestCode == SELECT_FROM_GALLERY) {
                    path = data.getData().toString();
                } else if (requestCode == REQUEST_CAMERA) {
                    path = "file://" + currentPhotoPath;
                }
                if (path != null)
                    Log.d("IMAGE URI :", path);
//                imageview.setImageURI(Uri.parse(path));
//                 startActivity(new Intent(this, ImageAdapter.class).putExtra("uriImage", path));
                    image.add(path);
                loadingimagedata();
            }
        }

    }
    public void loadingimagedata() {
//        if (image.size() > 0) {
            mLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(mLayoutManager);
            imageAdapter = new ImageAdapter(MainActivity.this, image);
            recyclerView.setAdapter(imageAdapter);
//        }
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},RequestPermissionCode);
    }
    public boolean checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int result = ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }else {
            return true;
        }
    }
    private void camaraRequestPermission(){
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},cameraRequestPermissionCode);
    }
    public boolean camaraCheckPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int result = ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA);
            return result == PackageManager.PERMISSION_GRANTED;
        }else {
            return true;
        }
    }
}