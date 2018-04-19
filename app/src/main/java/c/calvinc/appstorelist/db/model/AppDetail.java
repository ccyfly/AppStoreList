package c.calvinc.appstorelist.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * 2018-04-19
 *
 * @author calvinc
 */
@Entity(tableName = "app_detail")
public class AppDetail {
    @PrimaryKey
    @NonNull
    public String bundleId;

    public double averageUserRating;

    public int userRatingCount;
}
