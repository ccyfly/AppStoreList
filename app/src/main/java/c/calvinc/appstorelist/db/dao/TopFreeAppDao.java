package c.calvinc.appstorelist.db.dao;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListProvider;
import android.arch.paging.PagedList;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import c.calvinc.appstorelist.db.model.TopFreeApp;
import c.calvinc.appstorelist.db.model.TopGrossApp;

@Dao
public interface TopFreeAppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertApps(TopFreeApp... apps);

    @Query("SELECT * FROM top_free_app ORDER BY id ASC LIMIT :limit OFFSET :offset")
    public abstract List<TopFreeApp> allTopFreeAppById(int offset, int limit);

    @Query("SELECT * FROM top_free_app WHERE title LIKE '%' || :keyword || '%' OR artist LIKE '%' || :keyword || '%' OR summary LIKE '%' || :keyword || '%' OR name LIKE '%' || :keyword || '%' OR category LIKE '%' || :keyword || '%' ORDER BY id ASC  LIMIT :limit OFFSET :offset")
    public abstract List<TopFreeApp> allTopFreeAppByKeyword(String keyword, int offset, int limit);

    @Query("DELETE FROM top_free_app")
    public abstract void deleteAllApps();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTopGrossApp(TopGrossApp... apps);

    @Query("SELECT * FROM top_gross_app ORDER BY id ASC")
    public abstract List<TopGrossApp> allTopGrossApp();

    @Query("SELECT * FROM top_gross_app WHERE title LIKE '%' || :keyword || '%' OR artist LIKE '%' || :keyword || '%' OR summary LIKE '%' || :keyword || '%' OR name LIKE '%' || :keyword || '%' OR category LIKE '%' || :keyword || '%' ORDER BY id ASC")
    public abstract List<TopGrossApp> allTopGrossAppByKeyword(String keyword);

    @Query("DELETE FROM top_gross_app")
    public abstract void deleteAllTopGrossApps();
}
