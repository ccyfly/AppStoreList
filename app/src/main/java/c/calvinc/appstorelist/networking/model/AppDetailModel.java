package c.calvinc.appstorelist.networking.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 2018-04-19
 *
 * @author calvinc
 */
public class AppDetailModel {
    @SerializedName("bundleId")
    public String bundleId;

    @SerializedName("userRatingCount")
    public int userRatingCount;

    @SerializedName("averageUserRating")
    public double averageUserRating;
}
