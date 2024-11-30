package kr.pknu.seolmunhyeok201911938.api;

import static kr.pknu.seolmunhyeok201911938.BuildConfig.API_KEY;

import kr.pknu.seolmunhyeok201911938.BuildConfig;
import kr.pknu.seolmunhyeok201911938.model.CurrencyResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyApiService {
    @GET("latest")
    Call<CurrencyResponse> getLatestRates(@Query("apikey") String apikey);
}
