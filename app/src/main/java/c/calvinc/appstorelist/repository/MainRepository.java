package c.calvinc.appstorelist.repository;

import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import c.calvinc.appstorelist.AppExecutors;
import c.calvinc.appstorelist.db.AppDatabase;
import c.calvinc.appstorelist.db.model.AppDetail;
import c.calvinc.appstorelist.db.model.TopFreeApp;
import c.calvinc.appstorelist.db.model.TopFreeAppDetail;
import c.calvinc.appstorelist.db.model.TopGrossApp;
import c.calvinc.appstorelist.networking.AppStoreListService;
import c.calvinc.appstorelist.networking.model.AppDetailModel;
import c.calvinc.appstorelist.networking.model.AppModel;
import c.calvinc.appstorelist.networking.model.FeedModel;
import c.calvinc.appstorelist.networking.response.AppLookUpResponse;
import c.calvinc.appstorelist.networking.response.FeedResponse;
import c.calvinc.appstorelist.utils.MappingModelUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {

    private static MainRepository sInstance;

    private final AppDatabase appDatabase;
    private final AppExecutors appExecutors;
    private final AppStoreListService service;

    private int currentPage;
    private final int PAGE_SIZE = 10;
    MutableLiveData<List<TopFreeAppDetail>> topFreeAppData;
    MutableLiveData<List<TopFreeAppDetail>> deltaTopFreeAppData;
    MutableLiveData<List<TopGrossApp>> topGrossAppData;

    MutableLiveData<NetworkState> topFreeAppNetworkState;
    MutableLiveData<NetworkState> topGrossAppNetworkState;
    MutableLiveData<NetworkState> appDetailNetworkState;

//    private MutableLiveData<List<T>>

    private MainRepository(final AppDatabase database, final AppExecutors executors, final AppStoreListService service) {
        appDatabase = database;
        this.appExecutors = executors;
        this.service = service;
        currentPage = 0;
        topFreeAppNetworkState = new MutableLiveData<>();
        topFreeAppData = new MutableLiveData<>();
        deltaTopFreeAppData = new MutableLiveData<>();
        topGrossAppNetworkState = new MutableLiveData<>();
        appDetailNetworkState = new MutableLiveData<>();
        topGrossAppData = new MutableLiveData<>();
    }

    public static MainRepository getInstance(final AppDatabase database, final AppExecutors executors, final AppStoreListService service) {
        if (sInstance == null) {
            synchronized (MainRepository.class) {
                if (sInstance == null) {
                    sInstance = new MainRepository(database, executors, service);
                }
            }
        }
        return sInstance;
    }

    public MutableLiveData<List<TopFreeAppDetail>> getObservableTopFreeApp() {
        return topFreeAppData;
    }
    public MutableLiveData<List<TopFreeAppDetail>> getObservableDeltaTopFreeApp() {
        return deltaTopFreeAppData;
    }
    public MutableLiveData<NetworkState> getObservableTopFreeAppNetworkState() {
        return topFreeAppNetworkState;
    }

    public MutableLiveData<NetworkState> getObservableTopGrossAppNetworkState() {
        return topGrossAppNetworkState;
    }

    public MutableLiveData<List<TopGrossApp>> getObservableTopGrossApp() {
        return topGrossAppData;
    }

    public void requestFreeAppResponse() {
        topFreeAppNetworkState.setValue(new NetworkState(Status.RUNNING, "Requesting"));
        Call<FeedResponse> freeAppCall = service.getTopFreeApp();
        freeAppCall.enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, final Response<FeedResponse> response) {
                if (response.isSuccessful()) {
                    appExecutors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            FeedModel feed = response.body().feed;
                            List<AppModel> appList = feed.entry;
                            List<TopFreeApp> freeAppList = new ArrayList<>();
                            for (AppModel appModel: appList) {
                                freeAppList.add(MappingModelUtils.toTopFreeAppDao(appModel));
                            }
                            appDatabase.topFreeAppDao().deleteAllApps();
                            TopFreeApp[] topFreeAppsArr = new TopFreeApp[freeAppList.size()];
                            topFreeAppsArr = freeAppList.toArray(topFreeAppsArr);
                            appDatabase.topFreeAppDao().insertApps(topFreeAppsArr);
                            appExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    topFreeAppNetworkState.setValue(new NetworkState(Status.SUCCESS, "SUCCESS"));
                                }
                            });
                        }
                    });
                } else {
                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            topFreeAppNetworkState.setValue(new NetworkState(Status.FAILED, "FAILED"));
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<FeedResponse> call, Throwable t) {
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        topFreeAppNetworkState.setValue(new NetworkState(Status.FAILED, "FAILED"));
                    }
                });
            }
        });
    }
    
    public void requestTopGrossAppResponse() {
        topGrossAppNetworkState.setValue(new NetworkState(Status.RUNNING, "REQUESTING"));
        Call<FeedResponse> call = service.getTopGrossApp();
        call.enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, final Response<FeedResponse> response) {
                if (response.isSuccessful()) {
                    appExecutors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            FeedModel feed = response.body().feed;
                            List<AppModel> appList = feed.entry;
                            List<TopGrossApp> freeAppList = new ArrayList<>();
                            for (AppModel appModel: appList) {
                                freeAppList.add(MappingModelUtils.toTopGrossAppDao(appModel));
                            }
                            appDatabase.topFreeAppDao().deleteAllTopGrossApps();
                            TopGrossApp[] topFreeAppsArr = new TopGrossApp[freeAppList.size()];
                            topFreeAppsArr = freeAppList.toArray(topFreeAppsArr);
                            appDatabase.topFreeAppDao().insertTopGrossApp(topFreeAppsArr);
                            appExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    topGrossAppNetworkState.setValue(new NetworkState(Status.SUCCESS, "SUCCESS"));
                                }
                            });
                        }
                    });
                } else {
                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            topGrossAppNetworkState.setValue(new NetworkState(Status.FAILED, "FAILED"));
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<FeedResponse> call, Throwable t) {
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        topGrossAppNetworkState.setValue(new NetworkState(Status.FAILED, "FAILED"));
                    }
                });
            }
        });
    }

    public void getTopFreeAppByKeyword(final String keyword, final int page) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<TopFreeApp> data;
                if (keyword != null && keyword.length() > 0) {
                    data = appDatabase.topFreeAppDao().allTopFreeAppByKeyword(keyword, page * PAGE_SIZE, PAGE_SIZE);
                } else {
                    data = appDatabase.topFreeAppDao().allTopFreeAppById(page * PAGE_SIZE, PAGE_SIZE);
                }
                final List<TopFreeApp> postData = data;
                boolean isDelta = page != 0;
                requestAppDetails(data, isDelta);
//                appExecutors.mainThread().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (page == 0) {
//                            topFreeAppData.setValue(postData);
//                        } else {
//                            deltaTopFreeAppData.setValue(postData);
//                        }
//                    }
//                });
            }
        });
    }

    public void getTopGrossAppByKeyword(final String keyword) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<TopGrossApp> data;
                if (keyword != null && keyword.length() > 0) {
                    data = appDatabase.topFreeAppDao().allTopGrossAppByKeyword(keyword);
                } else {
                    data = appDatabase.topFreeAppDao().allTopGrossApp();
                }
                final List<TopGrossApp> postData = data;
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        topGrossAppData.setValue(postData);
                    }
                });

            }
        });
    }

    private void requestAppDetails(final List<TopFreeApp> appList, final boolean isDelta) {
        if (appList.size() > 0) {
            appExecutors.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    appDetailNetworkState.setValue(new NetworkState(Status.RUNNING, "Requesting"));
                }
            });

            appExecutors.networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    CountDownLatch countDownLatch = new CountDownLatch(appList.size());
                    for (TopFreeApp app : appList) {
                        requestAppDetail(countDownLatch, app.appId);
                    }
                    try {
                        countDownLatch.await();
                        queryAppDetailList(appList, isDelta);
                    } catch (Exception e) {

                    }
                }
            });
        } else {
            appExecutors.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    appDetailNetworkState.setValue(new NetworkState(Status.SUCCESS, "Success"));
                    if (isDelta) {
                        deltaTopFreeAppData.setValue(new ArrayList<TopFreeAppDetail>());
                    } else {
                        topFreeAppData.setValue(new ArrayList<TopFreeAppDetail>());
                    }
                }
            });
        }
    }

    private void queryAppDetailList(final List<TopFreeApp> appList, final boolean isDelta) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<TopFreeAppDetail> list = new ArrayList<>();
                for (TopFreeApp app : appList) {
                    TopFreeAppDetail detail = appDatabase.topFreeAppDao().getAppDetail(app.bundleId);
                    list.add(detail);
                }
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        appDetailNetworkState.setValue(new NetworkState(Status.SUCCESS, "Success"));
                        if (isDelta) {
                            deltaTopFreeAppData.setValue(list);
                        } else {
                            topFreeAppData.setValue(list);
                        }
                    }
                });
            }
        });
    }

    private void requestAppDetail(final CountDownLatch latch, String appId) {
        Call<AppLookUpResponse> call = service.lookupApp(appId);
        call.enqueue(new Callback<AppLookUpResponse>() {
            @Override
            public void onResponse(Call<AppLookUpResponse> call, Response<AppLookUpResponse> response) {
                if (response.isSuccessful()) {
                    final AppDetailModel model = response.body().results.get(0);
                    appExecutors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            AppDetail detail = MappingModelUtils.toAppDetailDao(model);
                            appDatabase.topFreeAppDao().insertAppDetail(detail);
                            latch.countDown();
                        }
                    });
                } else {
                    latch.countDown();
                }
            }

            @Override
            public void onFailure(Call<AppLookUpResponse> call, Throwable t) {
                latch.countDown();
            }
        });
    }

    private static class RequestAppDetailRunnable implements Runnable {
        CountDownLatch latch;
        String appId;

        RequestAppDetailRunnable(CountDownLatch latch, String appId) {
            this.latch = latch;
            this.appId = appId;
        }

        @Override
        public void run() {

        }
    }
}
