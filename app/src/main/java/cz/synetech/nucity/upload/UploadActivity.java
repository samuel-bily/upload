package cz.synetech.nucity.upload;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;

import cz.synetech.nucity.upload.api.service.FileUploadService;
import cz.synetech.nucity.upload.api.service.ServiceGenerator;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Project upload
 * Copyright (C) SYNETECH s.r.o. - All Rights Reserved
 * <p>
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * <p>
 * Proprietary and confidential
 * <p>
 * Written by Samuel Bilý <samuel.bily@synetech.cz>, 05 2017
 */
public class UploadActivity extends AppCompatActivity {

    private final int IMAGE_REQUEST = 1000;
    private final String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpZCI6MSwiZW1haWwiOiJpbmZvQHN5bmV0ZWNoLmN6IiwibmFtZSI6IlN5bmV0ZWNoIiwiZXhwaXJlc0luIjoiMzAgZGF5cyIsImlhdCI6MTQ5NTYzMjU0Nn0.qtEsh3xnRK9nOJuBPvuOtOYeSzBtUbxCzWJA0R6kn9j00PKdvh_WmAOFoU0i8daGCf_vkUx1uD4RojX_tydsE_iS3_nlCyuop5PKBrIkQYclV0aYLPl8VFBDnjTwwhclsjawCG2zau26DwxfUtb8Mp03pCPmeEnFGLxQPDRJkNs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case IMAGE_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createImagePicker();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uploadFile(data.getData());
        }
    }

    public void onUploadClicked(View view) {
        if (!checkIfHavePermission()) {
            requestForReadStoragePermission();
        } else {
            createImagePicker();
        }
    }

    private boolean checkIfHavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestForReadStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, IMAGE_REQUEST);
    }

    private void createImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST);
    }

    private void uploadFile(Uri fileUri) {
        FileUploadService service = ServiceGenerator.createService(FileUploadService.class);

        File file = new File(fileUri.getPath());

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        file
                );
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
        String descriptionString = "hello, this is description speaking";
        RequestBody headers =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);

        Call<ResponseBody> call = service.uploadImage(headers, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
}
