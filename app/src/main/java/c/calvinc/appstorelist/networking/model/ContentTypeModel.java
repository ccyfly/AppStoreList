package c.calvinc.appstorelist.networking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 2018-04-13
 *
 * @author calvinc
 */
public class ContentTypeModel {
    @Expose
    @SerializedName("attributes")
    public Attributes attributes;

    public static class Attributes {
        @Expose
        @SerializedName("term")
        public String term;
        @Expose
        @SerializedName("label")
        public String label;
    }
}
