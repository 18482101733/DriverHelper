package com.cduestc.DriverHelper.api;

import android.util.Log;

import com.cduestc.DriverHelper.bean.Coach;
import com.cduestc.DriverHelper.bean.Comment;
import com.cduestc.DriverHelper.bean.GetAllCoachResponseBody;
import com.cduestc.DriverHelper.bean.GetSchoolResponseBody;
import com.cduestc.DriverHelper.bean.LoginResponseBody;
import com.cduestc.DriverHelper.bean.RegisterResponseBody;
import com.cduestc.DriverHelper.bean.ReservationBody;
import com.cduestc.DriverHelper.bean.Student;
import com.cduestc.DriverHelper.bean.User;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ApiService  {

    private static ApiService apiService;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;

    /**
     * 服务器地址
     */
    private static final String API_ADDRESS = "http://119.23.126.79:10648";

    /**
     * 服务器接口路径
     */
    private static final String PATH_LOGIN = "/login/execution";
    private static final String PATH_SIGNUP_COACH = "/signup/staff/{invitecode}";
    private static final String PATH_SIGNuP_STUDENT = "/signup/student/{invitecode}";
    private static final String PATH_GET_STUDENT = "/get/stumeg";
    private static final String PATH_GET_COACH = "/get/teameg";
    private static final String PATH_GET_SCHOOL = "/get/allschoolinfo";
    private static final String PATH_GET_SCHOOL_COACHES = "/get/school/teameg";
    private static final String PATH_SET_RESERVATION = "/set/appointexercise";
    private static final String PATH_GET_COMMENTS ="/teacherId/discusses";
    private static final String PATH_SEND_COMMENT = "/add/discusses";
    private static final String PATH_GET_MY_RESERVATION = "/get/myappoint";
    private static final String PATH_GET_STUDENT_RESERVATION = "/get/appointexercise";
    private static final String PATH_MODIFY_STUDENT = "/set/studentmeg";
    private static final String PATH_MODIFY_COACH = "/set/teachermeg";
    private static final String PATH_EQUAL_ANSWER = "/equal/answer";
    private static final String PATH_SET_ANSWER = "/set/answer";
    private static final String PATH_SET_NEW_PASSWORD = "/change/password";
    private static final String PATH_UPLOAD_STUDENT_ICON = "/student/upload";
    private static final String PATH_UPLOAD_COACH_ICON = "/teacher/upload";
    private static final String PATH_FIND_RESERVATION = "/get/appointexercise/num";
    private static final String PATH_DROP_RESERVATION = "/drop/appointexercise";

    private static final String CONTENT_TYPE_JSON = "Content-Type: application/json";
    private static final String ACCEPT_TYPE_JSON = "Accept: application/json";
    private static final String REQUEST_TYPE= "application/json;charset=utf-8";
    private static final String REQUEST_TYPE_FILE = "multipart/form-data";


    public static final String[] inviteCodes = {"dhasoijklfh","1asd2ewafad","@JH!%hefdsifds"};

    /**
     * 私有构造方法
     */
    private ApiService(){
        okHttpClient = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(API_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    /**
     * 取得实例方法 ,避免在多个地方重复new 此类对象 浪费空间
     * @return
     */
    public static ApiService getInstance(){
        if (apiService == null){
            apiService = new ApiService();
        }
        return apiService;
    }

    /**
     * 登录
     * @param callback 登录接口回调(回调的是向服务器请求数据接口到底是服务器响应请求还是响应请求失败,分别对应onResponse(),和onFailure()方法)
     * @param requestBody 请求体
     */
    public void Login(Callback<LoginResponseBody> callback, String requestBody){
        LoginService loginService = retrofit.create(LoginService.class);
        RequestBody body = RequestBody.create(MediaType.parse(REQUEST_TYPE),requestBody);
        Call<LoginResponseBody> call = loginService.loginRequest(body);
        call.enqueue(callback);
    }


    /**
     *注册
     * @param callback  接口回调
     * @param requestBody 请求体
     * @param isStudent 是否是学生
     * @param invitecode 邀请码
     */
    public void Register(Callback<RegisterResponseBody> callback, String requestBody, boolean isStudent, String invitecode){
        RequestBody body = RequestBody.create(MediaType.parse(REQUEST_TYPE),requestBody);
        if (isStudent){
            RegisterStudentService studentService = retrofit.create(RegisterStudentService.class);
            Call<RegisterResponseBody> call = studentService.registerRequest(body,invitecode);
            call.enqueue(callback);
        }else {
            RegisterCoachService coachService = retrofit.create(RegisterCoachService.class);
            Call<RegisterResponseBody> call = coachService.registerRequest(body,invitecode);
            call.enqueue(callback);
        }
    }


    public void getSchools(Callback<GetSchoolResponseBody> callback,String cityName){
        GetSchoolService getSchool = retrofit.create(GetSchoolService.class);
        Call<GetSchoolResponseBody> call = getSchool.getSchoolsRequest(cityName);
        call.enqueue(callback);
    }


    public void getStudentInfo(Callback<ResponseBody> callback,String uid){

        Retrofit retrofitTemp = new Retrofit.Builder()
                .baseUrl(API_ADDRESS)
                .client(okHttpClient)
                .build();

        GetUserService getStudent = retrofitTemp.create(GetUserService.class);
        Call<ResponseBody> call = getStudent.getStudentRequest(uid);
        call.enqueue(callback);
    }

    public void getCoachInfo(Callback<ResponseBody> callback,String uid){
        GetUserService getCoach = retrofit.create(GetUserService.class);
        Call<ResponseBody> call = getCoach.getCoachRequest(uid);
        call.enqueue(callback);
    }

    public void getAllCoaches(Callback<GetAllCoachResponseBody> callback,String schoolName){
        GetAllCoachesService getAllCoaches = retrofit.create(GetAllCoachesService.class);
        Call<GetAllCoachResponseBody> call = getAllCoaches.getSchoolCoaches(schoolName);
        call.enqueue(callback);
    }

    public void Reservation(Callback<ResponseBody> callback,ReservationBody reservationBody){
        AboutReservationCar reservationCar = retrofit.create(AboutReservationCar.class);
        Call<ResponseBody> call = reservationCar.setReservation(reservationBody);
        call.enqueue(callback);
    }
    public void findReservation(Callback<ResponseBody> callback,String date,String teacherId){
        AboutReservationCar reservationCar = retrofit.create(AboutReservationCar.class);
        String json = "{\n" +
                "  \"appointDate\":\"" +date+ "\"," +
                "  \"teacherId\": \""+teacherId+"\"," +
                "  \"appointTime\": \""+1+"\"" +
                "}";
        RequestBody requestBody = RequestBody.create(MediaType.parse(REQUEST_TYPE),json);
        Call<ResponseBody> call = reservationCar.findReservation(requestBody);
        call.enqueue(callback);
    }

    public void getComments(Callback<ResponseBody> callback,String uid){
        GetComments getComments = retrofit.create(GetComments.class);
        Call<ResponseBody> call = getComments.getComment(uid);
        call.enqueue(callback);
    }

    public void sendComment(Callback<ResponseBody> callback,Comment comment){
        SendCommentService sendCommentService = retrofit.create(SendCommentService.class);
        Call<ResponseBody> call = sendCommentService.sendComment(comment);
        call.enqueue(callback);
    }

    public void getMyReservation(Callback<ResponseBody> callback,String studentId){
        GetMyReservation myReservation = retrofit.create(GetMyReservation.class);
        Call<ResponseBody> call = myReservation.getMyReservation(studentId);
        call.enqueue(callback);
    }

    public void getStudentReservation(Callback<ResponseBody> callback,String coachUid){
        GetStudentReservation studentReservation = retrofit.create(GetStudentReservation.class);
        Call<ResponseBody> call = studentReservation.getStudentReservation(coachUid);
        call.enqueue(callback);
    }

    public void modifyInformation(Callback<ResponseBody> callback, User user){

        if (user instanceof Student){
            Student student = (Student) user;
            RequestBody requestBody = RequestBody.create(MediaType.parse(REQUEST_TYPE),student.toModifyString());
            Log.i("modify", "modifyInformation: " + student.toModifyString());
            ModifyInformation modifyInformation = retrofit.create(ModifyInformation.class);
            Call<ResponseBody> call = modifyInformation.modifyStudent(requestBody);
            call.enqueue(callback);

        }else if (user instanceof Coach){
            Coach coach = (Coach) user;
            RequestBody requestBody = RequestBody.create(MediaType.parse(REQUEST_TYPE),coach.toModifyString());
            Log.i("modify", "modifyInformation: " + coach.toModifyString());
            ModifyInformation modifyInformation = retrofit.create(ModifyInformation.class);
            Call<ResponseBody> call = modifyInformation.modifyCoach(requestBody);
            call.enqueue(callback);
        }
    }

    public void passwordAnswer(Callback<ResponseBody> callback,String uid,String question,String answer){
        String body = "{\n" +
                "  \"myAnswer\":\"" +answer+ "\"," +
                "  \"myQuestion\": \""+question+"\"," +
                "  \"uid\": \""+uid+"\"" +
                "}";
        RequestBody requestBody = RequestBody.create(MediaType.parse(REQUEST_TYPE),body);
        SecretSecurity secretSecurity = retrofit.create(SecretSecurity.class);
        Call<ResponseBody> call = secretSecurity.equalAnswer(requestBody);
        call.enqueue(callback);
    }


    public void setNewPassword(Callback<ResponseBody> callback,String uid,String newPassword){
        SecretSecurity secretSecurity = retrofit.create(SecretSecurity.class);
        Call<ResponseBody> call = secretSecurity.setNewPassword(uid,newPassword);
        call.enqueue(callback);
    }


    public void setAnswer(Callback<ResponseBody> callback,User user,String answer,String question){
        String body = "{\n" +
                "  \"myAnswer\":\"" +answer+ "\"," +
                "  \"myQuestion\": \""+question+"\"," +
                "  \"passWord\": \""+user.getPassword()+"\"," +
                "  \"uid\": \""+user.getUid()+"\"" +
                "}";
        RequestBody requestBody = RequestBody.create(MediaType.parse(REQUEST_TYPE),body);
        SecretSecurity secretSecurity = retrofit.create(SecretSecurity.class);
        Call<ResponseBody> responseBodyCall = secretSecurity.setAnswer(requestBody);
        responseBodyCall.enqueue(callback);
    }

    /**
     * 上传用户头像
     * @param callback
     * @param user
     * @param path
     */
    public void uploadIcon(Callback<ResponseBody> callback, User user, String path) {

        File userIcon = new File(path);
        UploadUserIcon uploadUserIcon = retrofit.create(UploadUserIcon.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse(REQUEST_TYPE_FILE),userIcon);

        MultipartBody.Part part = MultipartBody.Part.createFormData("userIcon","userIcon",requestBody);

        if (user instanceof Student){
            Call<ResponseBody> call = uploadUserIcon.uploadStudentIcon(user.getUid(),part);
            call.enqueue(callback);
        }else if (user instanceof Coach){
            Call<ResponseBody> call = uploadUserIcon.uploadCoachIcon(user.getUid(),part);
            call.enqueue(callback);
        }
    }

    public void dropReservation(Callback<ResponseBody> callback,String appointDate,int appointTime,String studentId ){
        AboutReservationCar reservationCar = retrofit.create(AboutReservationCar.class);

        String body = "{\n" +
                "  \"appointDate\":\"" +appointDate+ "\"," +
                "  \"appointTime\": \""+appointTime+"\"," +
                "  \"studentId\": \""+studentId+"\"" +
                "}";
        RequestBody requestBody = RequestBody.create(MediaType.parse(REQUEST_TYPE),body);
        Call<ResponseBody> call = reservationCar.dropReservation(requestBody);
        call.enqueue(callback);
    }


    /**
     * 登录服务
     */
    public interface LoginService{
        /**
         *
         * @param requestBody 请求体
         * @return
         */
        @Headers({CONTENT_TYPE_JSON,ACCEPT_TYPE_JSON}) //提交服务器的请求体
        @POST(PATH_LOGIN)                              //访问路径
        Call<LoginResponseBody> loginRequest(@Body RequestBody requestBody);
    }

    /**
     * 教练注册服务
     */
    public interface RegisterCoachService{

        /**
         *
         * @param requestBody 请求体
         * @param invitecode 邀请码
         * @return
         */
        @Headers({CONTENT_TYPE_JSON,ACCEPT_TYPE_JSON})
        @POST(PATH_SIGNUP_COACH)
        Call<RegisterResponseBody> registerRequest(@Body RequestBody requestBody,@Path("invitecode") String invitecode);
    }

    /**
     * 学生注册服务
     */
    public interface RegisterStudentService{
        /**
         *
         * @param requestBody 请求body
         * @param invitecode 邀请码
         * @return
         */
        @Headers({CONTENT_TYPE_JSON,ACCEPT_TYPE_JSON})
        @POST(PATH_SIGNuP_STUDENT)
        Call<RegisterResponseBody> registerRequest(@Body RequestBody requestBody, @Path("invitecode") String invitecode);
    }

    /**
     * 获取目标城市学校
     */
    public interface GetSchoolService {
        /**
         * @param city 目标城市
         * @return
         */
        @Headers({CONTENT_TYPE_JSON,ACCEPT_TYPE_JSON})
        @GET(PATH_GET_SCHOOL)
        Call<GetSchoolResponseBody> getSchoolsRequest(@Query("city") String city);
    }

    /**
     * 获取用户信息接口
     */
    public interface GetUserService {
        @Headers({CONTENT_TYPE_JSON,ACCEPT_TYPE_JSON})
        @GET(PATH_GET_STUDENT)
        Call<ResponseBody> getStudentRequest(@Query("studentuid") String uid);

        @Headers({CONTENT_TYPE_JSON,ACCEPT_TYPE_JSON})
        @GET(PATH_GET_COACH)
        Call<ResponseBody> getCoachRequest(@Query("teacheruid") String uid);
    }

    /**
     * 获取该驾校教练接口
     */
    public interface GetAllCoachesService {
        @Headers({CONTENT_TYPE_JSON,ACCEPT_TYPE_JSON})
        @GET(PATH_GET_SCHOOL_COACHES)
        Call<GetAllCoachResponseBody> getSchoolCoaches(@Query("schoolname") String schoolName);
    }

    public interface AboutReservationCar{
        @Headers({CONTENT_TYPE_JSON,ACCEPT_TYPE_JSON})
        @POST(PATH_FIND_RESERVATION)
        Call<ResponseBody> findReservation(@Body RequestBody appoint );

        @Headers({CONTENT_TYPE_JSON,ACCEPT_TYPE_JSON})
        @POST(PATH_SET_RESERVATION)
        Call<ResponseBody> setReservation(@Body ReservationBody body);


        @Headers({CONTENT_TYPE_JSON,ACCEPT_TYPE_JSON})
        @POST(PATH_DROP_RESERVATION)
        Call<ResponseBody> dropReservation(@Body RequestBody dropInfo);

    }

    public interface GetComments{
        @Headers({CONTENT_TYPE_JSON,ACCEPT_TYPE_JSON})
        @GET(PATH_GET_COMMENTS)
        Call<ResponseBody> getComment(@Query("teacherId") String teacherId);
    }

    public interface SendCommentService{
        @Headers({CONTENT_TYPE_JSON,ACCEPT_TYPE_JSON})
        @POST(PATH_SEND_COMMENT)
        Call<ResponseBody> sendComment(@Body Comment comment);
    }

    public interface GetMyReservation{
        @Headers({CONTENT_TYPE_JSON,ACCEPT_TYPE_JSON})
        @GET(PATH_GET_MY_RESERVATION)
        Call<ResponseBody> getMyReservation(@Query("studentId") String studentId);
    }

    public interface GetStudentReservation{
        @Headers({CONTENT_TYPE_JSON,ACCEPT_TYPE_JSON})
        @GET(PATH_GET_STUDENT_RESERVATION)
        Call<ResponseBody> getStudentReservation(@Query("teacherID")String coachUid);
    }


    public interface ModifyInformation{
        @Headers({CONTENT_TYPE_JSON,ACCEPT_TYPE_JSON})
        @POST(PATH_MODIFY_STUDENT)
        Call<ResponseBody>  modifyStudent(@Body RequestBody body);

        @Headers({CONTENT_TYPE_JSON,ACCEPT_TYPE_JSON})
        @POST(PATH_MODIFY_COACH)
        Call<ResponseBody>  modifyCoach(@Body RequestBody body);
    }

    public interface SecretSecurity{
        @Headers({CONTENT_TYPE_JSON,ACCEPT_TYPE_JSON})
        @POST(PATH_EQUAL_ANSWER)
        Call<ResponseBody> equalAnswer(@Body RequestBody body);

        @Headers({CONTENT_TYPE_JSON,ACCEPT_TYPE_JSON})
        @POST(PATH_SET_ANSWER)
        Call<ResponseBody> setAnswer(@Body RequestBody body);


        @POST(PATH_SET_NEW_PASSWORD)
        Call<ResponseBody> setNewPassword(@Query("uid") String uid,@Query("password") String password);

    }


    public interface UploadUserIcon{
        @POST(PATH_UPLOAD_STUDENT_ICON)
        @Multipart
        Call<ResponseBody> uploadStudentIcon(@Query("uid") String _id, @Part() MultipartBody.Part body);

        @POST(PATH_UPLOAD_COACH_ICON)
        @Multipart
        Call<ResponseBody> uploadCoachIcon(@Query("uid") String _id, @Part() MultipartBody.Part body);

    }

}
