package cz.synetech.nucity.upload.api.service;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
public interface FileUploadService {

    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadImage(
            @Part("headers") RequestBody headers,
            @Part MultipartBody.Part photo
    );

}
