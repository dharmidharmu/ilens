package com.mikelau.cropme;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.mikelau.croperino.Croperino;
import com.mikelau.croperino.CroperinoConfig;
import com.mikelau.croperino.CroperinoFileUtil;
import com.mikelau.cropme.R;

public class search extends AppCompatActivity {


    private ImageButton btnSummon;
    private ImageView ivMain;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


           btnSummon = findViewById(R.id.searchbut);
        ivMain = findViewById(R.id.iv_main);
        new CroperinoConfig("IMG_" + System.currentTimeMillis() + ".jpg", "/MikeLau/Pictures", "/sdcard/MikeLau/Pictures");
        CroperinoFileUtil.setupDirectory(search.this);


        btnSummon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CroperinoFileUtil.verifyStoragePermissions(search.this)) {
                    prepareChooser();
                }
            }
        });
    }

    private void prepareChooser() {
        Croperino.prepareChooser(search.this, "Capture photo...", ContextCompat.getColor(search.this, android.R.color.background_dark));
    }
    private void prepareCamera() {
        Croperino.prepareCamera(search.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CroperinoConfig.REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Croperino.runCropImage(CroperinoFileUtil.getTempFile(), search.this, true, 1, 1, R.color.gray, R.color.gray_variant);
                }
                break;
            case CroperinoConfig.REQUEST_PICK_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    CroperinoFileUtil.newGalleryFile(data, search.this);
                    Croperino.runCropImage(CroperinoFileUtil.getTempFile(), search.this, true, 1, 1, R.color.gray, R.color.gray_variant);
                }
                break;
            case CroperinoConfig.REQUEST_CROP_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Uri i = Uri.fromFile(CroperinoFileUtil.getTempFile());

                    ivMain.setImageURI(i);
                }
                break;
            default:
                break;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CroperinoFileUtil.REQUEST_CAMERA) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.CAMERA)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        prepareCamera();
                    }
                }
            }
        } else if (requestCode == CroperinoFileUtil.REQUEST_EXTERNAL_STORAGE) {
            boolean wasReadGranted = false;
            boolean wasWriteGranted = false;

            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        wasReadGranted = true;
                    }
                }
                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        wasWriteGranted = true;
                    }
                }
            }

            if (wasReadGranted && wasWriteGranted) {
                prepareChooser();
            }
        }
    }
}
