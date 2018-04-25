package c.calvinc.appstorelist.db.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class TopFreeAppDetail {
    @Embedded
    public TopFreeApp topFreeApp;

//    @Relation(parentColumn = "bundleId", entityColumn = "bundleId", entity = AppDetail.class)
    @Embedded
    public AppDetail details;
}
