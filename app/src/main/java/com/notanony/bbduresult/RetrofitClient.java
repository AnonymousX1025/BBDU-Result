package com.notanony.bbduresult;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL =
            "https://examcell.bbdu.ac.in/";

    private static Retrofit retrofit;

    public static ResultApi getApi() {

        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }

        return retrofit.create(ResultApi.class);
    }
}
