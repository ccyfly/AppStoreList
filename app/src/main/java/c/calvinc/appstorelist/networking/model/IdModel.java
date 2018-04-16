package c.calvinc.appstorelist.networking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IdModel extends AbstractLabelModel {

    @Expose
    @SerializedName("attributes")
    public Attributes attributes;

    public static class Attributes {
        @Expose
        @SerializedName("im:id")
        public String id;
        @Expose
        @SerializedName("im:bundleId")
        public String bundleId;
    }
}
