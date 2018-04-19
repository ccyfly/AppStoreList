package c.calvinc.appstorelist.utils;

import com.google.gson.annotations.Expose;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import c.calvinc.appstorelist.db.model.TopFreeApp;
import c.calvinc.appstorelist.db.model.TopGrossApp;
import c.calvinc.appstorelist.networking.model.AppModel;
import c.calvinc.appstorelist.networking.model.ImageModel;

/**
 * 2018-04-13
 *
 * @author calvinc
 */
public class MappingModelUtils {

    public static TopFreeApp toTopFreeAppDao(AppModel appModel) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"); //2017-11-28T21:34:35-07:00
        String appId = appModel.idModel.attributes.id;
        String bundleId = appModel.idModel.attributes.bundleId;
        String name = appModel.name.label;
        String title = appModel.title.label;
        String contentType = appModel.contentType.attributes.label;
        String category = appModel.category.attributes.label;
        String artist = appModel.artist.label;
        String strPrice = appModel.price.attributes.amount;
        double dbPrice = Double.parseDouble(strPrice);
        String currency = appModel.price.attributes.currency;
        String strDate = appModel.releaseDate.label;
        Date releaseDate = new Date();
        try {
            releaseDate = sdf.parse(strDate);
        } catch (Exception ex){

        }
        String imageUrl = null;
        List<ImageModel> imageList = appModel.imageList;
        if (imageList.size()>0) {
            ImageModel imageModel = imageList.get(imageList.size() - 1);
            imageUrl = imageModel.label;
        }
        String summary = appModel.summary.label;
        return new TopFreeApp(appId, bundleId, name, title, contentType, category, artist, releaseDate, imageUrl, summary, dbPrice, currency);
    }

    public static TopGrossApp toTopGrossAppDao(AppModel appModel) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"); //2017-11-28T21:34:35-07:00
        String appId = appModel.idModel.attributes.id;
        String bundleId = appModel.idModel.attributes.bundleId;
        String name = appModel.name.label;
        String title = appModel.title.label;
        String contentType = appModel.contentType.attributes.label;
        String category = appModel.category.attributes.label;
        String artist = appModel.artist.label;
        String strPrice = appModel.price.attributes.amount;
        double dbPrice = Double.parseDouble(strPrice);
        String currency = appModel.price.attributes.currency;
        String strDate = appModel.releaseDate.label;
        Date releaseDate = new Date();
        try {
            releaseDate = sdf.parse(strDate);
        } catch (Exception ex){

        }
        String imageUrl = null;
        List<ImageModel> imageList = appModel.imageList;
        if (imageList.size()>0) {
            ImageModel imageModel = imageList.get(imageList.size() - 1);
            imageUrl = imageModel.label;
        }
        String summary = appModel.summary.label;
        return new TopGrossApp(appId, bundleId, name, title, contentType, category, artist, releaseDate, imageUrl, summary, dbPrice, currency);
    }
}
