package kr.pknu.seolmunhyeok201911938.api;

import kr.pknu.seolmunhyeok201911938.BuildConfig;
import kr.pknu.seolmunhyeok201911938.model.CurrencyResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyApiService {
    @GET("v1/latest")
    Call<CurrencyResponse> getLatestRates(@Query(BuildConfig.API_KEY) String apiKey);
}
