package c.calvinc.appstorelist.networking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryModel {
    @Expose
    @SerializedName("attributes")
    public Attributes attributes;

    public static class Attributes extends AbstractLabelModel {
        @Expose
        @SerializedName("im:id")
        public String id;

        @SerializedName("term")
        public String term;

        @SerializedName("scheme")
        public String scheme;
    }
}
