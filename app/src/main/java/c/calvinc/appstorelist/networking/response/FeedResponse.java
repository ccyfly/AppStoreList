package c.calvinc.appstorelist.networking.response;

import com.google.gson.annotations.SerializedName;

import c.calvinc.appstorelist.networking.model.FeedModel;

public class FeedResponse {
    @SerializedName("feed")
    public FeedModel feed;
}
