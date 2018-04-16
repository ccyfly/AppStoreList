package c.calvinc.appstorelist.networking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArtistModel extends AbstractLabelModel {
    @Expose
    @SerializedName("attributes")
    public Attributes attributes;

    public static class Attributes {
        @Expose
        @SerializedName("href")
        public String href;
    }
}
