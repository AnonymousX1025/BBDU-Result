package com.notanony.bbduresult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ResultApi {
    @FormUrlEncoded
    @POST("bbdu-result/result-student.php")
    Call<String> getResultList(
            @Field("urollno") String rollNumber
    );

    @FormUrlEncoded
    @POST("bbdu-result/result-display.php")
    Call<String> getResultDetails(
            @Field("session") String session,
            @Field("year") String year,
            @Field("urollno") String rollNumber,
            @Field("semester") String semester,
            @Field("examtype") String examType,
            @Field("status") String status
    );
}
