package c.calvinc.appstorelist.networking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PriceModel extends AbstractLabelModel {
    @Expose
    @SerializedName("attributes")
    public Attributes attributes;

    public static class Attributes {
        @Expose
        @SerializedName("currency")
        public String currency;
        @Expose
        @SerializedName("amount")
        public String amount;
    }
}
