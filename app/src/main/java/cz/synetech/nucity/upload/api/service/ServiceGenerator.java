package cz.synetech.nucity.upload.api.service;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
public class ServiceGenerator {

    private static final String BASE_URL = "http://bbeight.synetech.local:10410/api/user/s3-policy";

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
