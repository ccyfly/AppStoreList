package c.calvinc.appstorelist.networking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AbstractLabelModel {
    @Expose
    @SerializedName("label")
    public String label;
}
