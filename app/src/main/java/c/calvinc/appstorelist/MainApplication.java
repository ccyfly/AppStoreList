package c.calvinc.appstorelist;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import c.calvinc.appstorelist.db.AppDatabase;
import c.calvinc.appstorelist.networking.AppStoreListService;
import c.calvinc.appstorelist.repository.MainRepository;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainApplication extends Application {

    private static Retrofit retrofit;

    private AppExecutors appExecutors;

    AppStoreListService appStoreListService;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        appExecutors = new AppExecutors();
    }

    public AppStoreListService getAppStoreListService() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.connectTimeout(60, TimeUnit.SECONDS);
        okHttpClient.readTimeout(60, TimeUnit.SECONDS);
        okHttpClient.writeTimeout(60, TimeUnit.SECONDS);
        okHttpClient.retryOnConnectionFailure(true);
        okHttpClient.addNetworkInterceptor(new StethoInterceptor());

        appStoreListService = new Retrofit.Builder()
                .baseUrl("https://itunes.apple.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build()
                .create(AppStoreListService.class);
        return appStoreListService;
    }

    public AppDatabase getAppDatabase() {
        return AppDatabase.getInstance(this);
    }

    public MainRepository getHomeRepository() {
        return MainRepository.getInstance(getAppDatabase(), appExecutors, getAppStoreListService());
    }
}
