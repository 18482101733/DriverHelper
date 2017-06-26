package com.cduestc.controller.api;

import android.util.Log;

import com.cduestc.controller.bean.School;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by c on 2017/4/26.
 */
public class ApiService {

    private static  ApiService apiService = null;
    private Retrofit retrofit;
    private OkHttpClient client;
    private DoApiCall doApiCall;

    private static final String BASE_URL= "http://119.23.126.79:10648";
    private static final String PATH_LOGIN = "/login/execution";

    private static final String PATH_GET_ALL_STUDENT = "/get/allstumeg";
    private static final String PATH_GET_ALL_COACH = "/get/allteameg";
    private static final String PATH_GET_ALL_USER = "/get/alluser";
    private static final String PATH_SET_STATES = "/set/states";
    private static final String PATH_DELETE_STATES = "/delete/states";
    private static final String PATH_GET_ALL_SCHOOLS = "/get/allschool";
    private static final String PATH_DELETE_SCHOOL = "/delete/school";
    private static final String PATH_ADD_SCHOOL = "/add/school";

    private static final String CONTENT_TYPE_JSON = "Content-Type: application/json";
    private static final String ACCEPT_TYPE_JSON = "Accept: application/json";
    private static final String REQUEST_TYPE= "application/json;charset=utf-8";

    private ApiService(){
        client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS).build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .build();


        doApiCall = retrofit.create(DoApiCall.class);
    }

    public static ApiService getInstance(){
        if (apiService == null){
            apiService = new ApiService();
        }
        return apiService;
    }

    public void login(Callback<ResponseBody> callback,String password,String username){

        String json = "{\n" +
                "  \"password\":\"" +password+ "\"," +
                "  \"userName\": \""+username+"\"" +
                "}";
        Log.i("username", "login: " + json);

        RequestBody requestBody = RequestBody.create(MediaType.parse(REQUEST_TYPE),json);
        Call<ResponseBody> call = doApiCall.loginService(requestBody);
        call.enqueue(callback);
    }

    public void getUser(Callback<ResponseBody> callback){
        Call<ResponseBody> call = doApiCall.getAllUser();
        call.enqueue(callback);
    }

    public void getStudent(Callback<ResponseBody> callback){
        Call<ResponseBody> call = doApiCall.getAllStudent();
        call.enqueue(callback);
    }

    public void getCoach(Callback<ResponseBody> callback){
        Call<ResponseBody> call = doApiCall.getAllCoach();
        call.enqueue(callback);
    }

    public void setStates(Callback<ResponseBody> callback,String uid){
        Call<ResponseBody> call = doApiCall.setStates(uid);
        call.enqueue(callback);
    }

    public void deleteStates(Callback<ResponseBody> callback,String uid){
        Call<ResponseBody> call = doApiCall.deleteStates(uid);
        call.enqueue(callback);
    }

    public void getAllSchool(Callback<ResponseBody> callback){
        Call<ResponseBody> call = doApiCall.getAllSchool();
        call.enqueue(callback);
    }

    public void deleteSchool(Callback<ResponseBody> callback,String uid){
        Call<ResponseBody> call = doApiCall.deleteSchool(uid);
        call.enqueue(callback);
    }

    public void addSchool(Callback<ResponseBody> callback, School school){
        RequestBody requestBody = RequestBody.create(MediaType.parse(REQUEST_TYPE),school.toString());
        Call<ResponseBody> call = doApiCall.addSchool(requestBody);
        call.enqueue(callback);
    }

    private interface DoApiCall{
        @Headers({CONTENT_TYPE_JSON,ACCEPT_TYPE_JSON})
        @POST(PATH_LOGIN)
        Call<ResponseBody> loginService(@Body RequestBody body);

        @GET(PATH_GET_ALL_STUDENT)
        Call<ResponseBody> getAllStudent();

        @GET(PATH_GET_ALL_COACH)
        Call<ResponseBody> getAllCoach();

        @GET(PATH_GET_ALL_USER)
        Call<ResponseBody> getAllUser();

        @POST(PATH_SET_STATES)
        Call<ResponseBody> setStates(@Query("uid") String uid);

        @POST(PATH_DELETE_STATES)
        Call<ResponseBody> deleteStates(@Query("uid") String uid);

        @GET(PATH_GET_ALL_SCHOOLS)
        Call<ResponseBody> getAllSchool();

        @POST(PATH_DELETE_SCHOOL)
        Call<ResponseBody> deleteSchool(@Query("uid")String uid);

        @POST(PATH_ADD_SCHOOL)
        Call<ResponseBody> addSchool(@Body RequestBody body);
    }

}
