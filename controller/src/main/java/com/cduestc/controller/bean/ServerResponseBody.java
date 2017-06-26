package com.cduestc.controller.bean;

import android.util.Log;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by c on 2017/4/26.
 */
public class ServerResponseBody<T> {
    private String message;
    private T data;
    private boolean success;

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public static String praseJson(Response<ResponseBody> response){
        String json = "";
        try{
            if (!response.isSuccessful()){
                json = "{\n" +
                        "  \"success\":\"" +false+ "\"," +
                        "  \"message\": \""+"获取信息失败"+"\"," +
                        "  \"data\": \""+null+"\"" +
                        "}";
                Log.i("errorBody", "praseJson: " + response.errorBody().string());
            }else {
                json = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

}
