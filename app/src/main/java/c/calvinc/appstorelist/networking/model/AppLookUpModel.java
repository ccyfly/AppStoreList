package c.calvinc.appstorelist.networking.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 2018-04-19
 *
 * @author calvinc
 */
public class AppLookUpModel {
    @SerializedName("resultCount")
    public int resultCount;

    @SerializedName("resuls")
    public List<AppDetailModel> results;
}
