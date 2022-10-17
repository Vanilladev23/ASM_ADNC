package com.example.asm_adnc.models;

import android.database.Observable;

import retrofit2.http.GET;

public interface ServiceAPI {
    String BASE_Service = "https://apis.dinhnt.com/";

    @GET("sample1.json")
    Observable<ListUser> GetListUser();
}
