package c.calvinc.appstorelist.networking.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedModel {
    @SerializedName("author")
    public AuthorModel author;

    @SerializedName("entry")
    public List<AppModel> entry;
}
