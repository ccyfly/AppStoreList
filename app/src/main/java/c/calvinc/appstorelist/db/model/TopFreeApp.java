package c.calvinc.appstorelist.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "top_free_app")
public class TopFreeApp {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String appId;

    public String name;
    public String title;
    public String contentType;
    public String category;
    public String artist;
    public Date releaseDate;

    public String imageUrl;

    public String summary;

    public double price;
    public String currency;

    public TopFreeApp(String appId, String name, String title, String contentType, String category, String artist, Date releaseDate, String imageUrl, String summary, double price, String currency) {
        this.appId = appId;
        this.name = name;
        this.title = title;
        this.contentType = contentType;
        this.category = category;
        this.artist = artist;
        this.releaseDate = releaseDate;
        this.imageUrl = imageUrl;
        this.summary = summary;
        this.price = price;
        this.currency = currency;
    }
}