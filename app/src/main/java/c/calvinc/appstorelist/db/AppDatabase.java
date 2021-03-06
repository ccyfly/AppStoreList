package c.calvinc.appstorelist.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import c.calvinc.appstorelist.AppExecutors;
import c.calvinc.appstorelist.db.converter.DateConverter;
import c.calvinc.appstorelist.db.dao.TopFreeAppDao;
import c.calvinc.appstorelist.db.model.AppDetail;
import c.calvinc.appstorelist.db.model.TopFreeApp;
import c.calvinc.appstorelist.db.model.TopGrossApp;

/**
 * 2018-04-13
 *
 * @author calvinc
 */
@Database(entities = {TopFreeApp.class, TopGrossApp.class, AppDetail.class}, version = 2, exportSchema = true)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase _instance;
    @VisibleForTesting
    public static final String DATABASE_NAME = "app-db";

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE top_free_app "
                    + " ADD COLUMN bundleId TEXT");
            database.execSQL("ALTER TABLE top_gross_app "
                    + " ADD COLUMN bundleId TEXT");
        }
    };

    public abstract TopFreeAppDao topFreeAppDao();

    public static AppDatabase getInstance(final Context context) {
        if (_instance == null) {
            synchronized (AppDatabase.class) {
                if (_instance == null) {
                    _instance = Room.databaseBuilder(context,
                            AppDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return _instance;
    }
}
