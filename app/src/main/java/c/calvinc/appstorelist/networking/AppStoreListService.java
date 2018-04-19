package c.calvinc.appstorelist.networking;

import c.calvinc.appstorelist.networking.response.AppLookUpResponse;
import c.calvinc.appstorelist.networking.response.FeedResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AppStoreListService {
    @GET("hk/rss/topfreeapplications/limit=100/json")
    Call<FeedResponse> getTopFreeApp();

    @GET("hk/rss/topgrossingapplications/limit=10/json")
    Call<FeedResponse> getTopGrossApp();

    @GET("hk/lookup")
    Call<AppLookUpResponse> lookupApp(@Query("id") String appId);
}
