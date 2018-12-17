package tw.sspr.seoulsafepublicrestroom.Retrofit;

import android.content.Context;
import android.util.Log;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import tw.sspr.seoulsafepublicrestroom.Object.ReportItem;
import tw.sspr.seoulsafepublicrestroom.RequestBody.DeleteReportDTO;
import tw.sspr.seoulsafepublicrestroom.RequestBody.NFCReportDTO;
import tw.sspr.seoulsafepublicrestroom.ResponseBody.ReportGET;
import tw.sspr.seoulsafepublicrestroom.ResponseBody.RestroomGET;
import tw.sspr.seoulsafepublicrestroom.Service.RetroBaseApiService;

public class RetroClient {
    private RetroBaseApiService apiService;
    public static String baseUrl = RetroBaseApiService.Base_URL;
    private static Context mContext;
    private static Retrofit retrofit;


    private static class SingletonHolder {
        private static RetroClient INSTANCE = new RetroClient(mContext);
    }

    public static RetroClient getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return SingletonHolder.INSTANCE;
    }

    private RetroClient(Context context) {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    public RetroClient createBaseApi() {
        apiService = create(RetroBaseApiService.class);
        return this;
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public  <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    public void updateRestroom(NFCReportDTO report, final RetroCallback callback) {
        apiService.updateRestroom(report).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void readRestroom(String id, final RetroCallback callback) {
        apiService.readRestroom(id).enqueue(new Callback<RestroomGET>() {

            @Override
            public void onResponse(Call<RestroomGET> call, Response<RestroomGET> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<RestroomGET> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void uploadForm(MultipartBody.Part file,
                           String restroom_id,
                           String writer,
                           String pwd,
                           String msg, final RetroCallback callback) {
        apiService.uploadForm(file,restroom_id,writer,pwd,msg).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void readReports(String restroom_id, final RetroCallback callback) {
        apiService.readReports(restroom_id).enqueue(new Callback<List<ReportGET>>() {

            @Override
            public void onResponse(Call<List<ReportGET>> call, Response<List<ReportGET>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ReportGET>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void deleteReport(DeleteReportDTO report, final RetroCallback callback) {
        apiService.deleteReport(report).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(t);
            }
        });
    }




}
