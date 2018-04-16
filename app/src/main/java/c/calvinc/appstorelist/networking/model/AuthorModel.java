package c.calvinc.appstorelist.networking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthorModel {
    @Expose
    @SerializedName("uri")
    public UriEntity uri;
    @Expose
    @SerializedName("name")
    public NameEntity name;

    public static class UriEntity {
        @Expose
        @SerializedName("label")
        public String label;
    }

    public static class NameEntity {
        @Expose
        @SerializedName("label")
        public String label;
    }
}
