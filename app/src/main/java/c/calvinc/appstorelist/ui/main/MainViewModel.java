package c.calvinc.appstorelist.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import java.util.List;

import c.calvinc.appstorelist.MainApplication;
import c.calvinc.appstorelist.db.model.TopFreeApp;
import c.calvinc.appstorelist.db.model.TopGrossApp;
import c.calvinc.appstorelist.repository.NetworkState;
import c.calvinc.appstorelist.repository.Status;

public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<NetworkState> topFreeAppNetworkState;
    private MutableLiveData<NetworkState> topGrossAppNetworkState;
    private MutableLiveData<List<TopFreeApp>> detraData;
    int currentPage = 0;
    String keyword = null;
    boolean listDataLoaded = false;

    MediatorLiveData<NetworkState> liveDataMerger = new MediatorLiveData<>();

    private NetworkState topAppState;
    private NetworkState topGrossState;
    public MainViewModel(Application application) {
        super(application);
        topFreeAppNetworkState = ((MainApplication)application).getHomeRepository().getObservableTopFreeAppNetworkState();
        detraData = ((MainApplication)application).getHomeRepository().getObservableDeltaTopFreeApp();

        topGrossAppNetworkState = ((MainApplication)application).getHomeRepository().getObservableTopGrossAppNetworkState();

        liveDataMerger.addSource(topFreeAppNetworkState, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                topAppState = networkState;
                switch (networkState.getStatus()) {
                    case SUCCESS:
                    case FAILED:
                        validNetworkState();
                        break;
                    case RUNNING:
                        liveDataMerger.setValue(new NetworkState(Status.RUNNING, "REQUESTING"));
                        break;
                }
            }
        });
        liveDataMerger.addSource(topGrossAppNetworkState, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                topGrossState = networkState;
                switch (networkState.getStatus()) {
                    case SUCCESS:
                    case FAILED:
                        validNetworkState();
                        break;
                    case RUNNING:
                        liveDataMerger.setValue(new NetworkState(Status.RUNNING, "REQUESTING"));
                        break;
                }
            }
        });
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    void validNetworkState() {
        if (topGrossState!=null && topAppState !=null) {
            if (topGrossState.isFinished() && topAppState.isFinished()) {
                if (topAppState.getStatus() == Status.FAILED || topGrossState.getStatus() == Status.FAILED) {
                    liveDataMerger.setValue(new NetworkState(Status.FAILED, "FAILED"));
                } else {
                    liveDataMerger.setValue(new NetworkState(Status.SUCCESS, "SUCCESS"));
                }
            }
        }
    }

    public MutableLiveData<List<TopFreeApp>> getObservableTopFreeApp() {
        return ((MainApplication)getApplication()).getHomeRepository().getObservableTopFreeApp();
    }

    public MutableLiveData<NetworkState> getObservableNetworkState() { return liveDataMerger; }

    public MutableLiveData<List<TopFreeApp>> getObservableDeltaTopTreeAppData() {
        return detraData;
    }

    public MutableLiveData<List<TopGrossApp>> getObservableTopGrossApp() {
        return ((MainApplication)getApplication()).getHomeRepository().getObservableTopGrossApp();
    }

    public void requestFreeAppResponse() {
        ((MainApplication)getApplication()).getHomeRepository().requestFreeAppResponse();
    }

    public void requestTopGrossAppResponse() {
        ((MainApplication)getApplication()).getHomeRepository().requestTopGrossAppResponse();
    }

    public void initialTopFreeAppListByKeyword() {
        currentPage = 0;
        ((MainApplication)getApplication()).getHomeRepository().getTopFreeAppByKeyword(keyword, 0);
        ((MainApplication)getApplication()).getHomeRepository().getTopGrossAppByKeyword(keyword);
    }

    public void initialTopFreeAppListByKeyword(String keyword) {
        currentPage = 0;
        this.keyword = keyword;
        ((MainApplication)getApplication()).getHomeRepository().getTopFreeAppByKeyword(keyword, 0);
        ((MainApplication)getApplication()).getHomeRepository().getTopGrossAppByKeyword(keyword);
    }

    public void getNextTopFreeAppListByKeyword() {
        ((MainApplication)getApplication()).getHomeRepository().getTopFreeAppByKeyword(this.keyword, ++currentPage);
    }


}
