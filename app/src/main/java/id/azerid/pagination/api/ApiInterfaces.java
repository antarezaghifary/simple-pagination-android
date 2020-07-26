package id.azerid.pagination.api;

import java.util.List;

import id.azerid.pagination.api.model.ApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterfaces {
    @GET("/99c279bb173a6e28359c/data")
    Call<List<ApiResponse>> getData();
}
