package tw.sspr.seoulsafepublicrestroom.Service;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tw.sspr.seoulsafepublicrestroom.Object.ReportItem;
import tw.sspr.seoulsafepublicrestroom.RequestBody.DeleteReportDTO;
import tw.sspr.seoulsafepublicrestroom.RequestBody.NFCReportDTO;
import tw.sspr.seoulsafepublicrestroom.ResponseBody.ReportGET;
import tw.sspr.seoulsafepublicrestroom.ResponseBody.RestroomGET;

public interface RetroBaseApiService {
//    final String Base_URL = "http://172.30.1.2:8080";
final String Base_URL = "http://devsim.cafe24.com/";

    @POST("restroom/update")
    Call<ResponseBody> updateRestroom(@Body NFCReportDTO report);

    @GET("restroom/get")
    Call<RestroomGET> readRestroom(@Query("id") String id);

    @Multipart
    @POST("report/add")
    Call<ResponseBody> uploadForm(@Part MultipartBody.Part file,
                                  @Query("restroom_id") String restroom_id,
                                  @Query("writer") String writer,
                                  @Query("pwd") String pwd,
                                  @Query("msg") String msg);

    @GET("report/get")
    Call<List<ReportGET>> readReports(@Query("restroom_id") String restroom_id);


    @POST("report/delete")
    Call<ResponseBody> deleteReport(@Body DeleteReportDTO report);
}
