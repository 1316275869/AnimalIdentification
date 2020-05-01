package com.example.myapplication.Detaile;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDetail {
    @GET(" ")
    Call<ResponseBody> getCall();
}
