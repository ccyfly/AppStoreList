package c.calvinc.appstorelist.networking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import c.calvinc.appstorelist.db.model.TopFreeApp;

public class AppModel {
    @Expose
    @SerializedName("im:name")
    public NameModel name;

    @Expose
    @SerializedName("im:image")
    public List<ImageModel> imageList;

    @Expose
    @SerializedName("summary")
    public SummaryModel summary;

    @Expose
    @SerializedName("title")
    public TitleModel title;

    @Expose
    @SerializedName("im:artist")
    public ArtistModel artist;

    @Expose
    @SerializedName("rights")
    public RightsModel rights;

    @Expose
    @SerializedName("id")
    public IdModel idModel;

    @Expose
    @SerializedName("category")
    public CategoryModel category;

    @Expose
    @SerializedName("im:releaseDate")
    public ReleaseDateModel releaseDate;

    @Expose
    @SerializedName("im:contentType")
    public ContentTypeModel contentType;

    @Expose
    @SerializedName("im:price")
    public PriceModel price;
}
