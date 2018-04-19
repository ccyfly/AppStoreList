package c.calvinc.appstorelist.networking.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import c.calvinc.appstorelist.networking.model.AppDetailModel;

/**
 * 2018-04-19
 *
 * @author calvinc
 */
public class AppLookUpResponse {
    @SerializedName("resultCount")
    public int resultCount;

    @SerializedName("results")
    public List<AppDetailModel> results;
}
