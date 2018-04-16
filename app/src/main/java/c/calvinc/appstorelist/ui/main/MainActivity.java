package c.calvinc.appstorelist.ui.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import c.calvinc.appstorelist.R;
import c.calvinc.appstorelist.adapters.TopFreeAppAdapter;
import c.calvinc.appstorelist.databinding.ActivityMainBinding;
import c.calvinc.appstorelist.db.model.TopFreeApp;
import c.calvinc.appstorelist.db.model.TopGrossApp;
import c.calvinc.appstorelist.repository.NetworkState;
import c.calvinc.appstorelist.repository.Status;

public class MainActivity extends AppCompatActivity {

    private static final String STATE_KEYWORD = "STATE_KEYWORD";

    MainViewModel viewModel;

    ActivityMainBinding binding;
    TopFreeAppAdapter topFreeAppAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String keyword = null;
        if (savedInstanceState != null) {
            keyword = savedInstanceState.getString(STATE_KEYWORD);
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.setKeyword(keyword);
        setupView();
        setupObservable();

        viewModel.requestFreeAppResponse();
        viewModel.requestTopGrossAppResponse();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_KEYWORD, binding.edtSearch.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        binding.edtSearch.setText(savedInstanceState.getString(STATE_KEYWORD));
    }

    void setupView() {
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.initialTopFreeAppListByKeyword(editable.toString());
            }
        });
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        binding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if(v.getChildAt(v.getChildCount() - 1) != null) {
//                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
//                            scrollY > oldScrollY) {
//
//                        visibleItemCount = linearLayoutManager.getChildCount();
//                        totalItemCount = linearLayoutManager.getItemCount();
//                        pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
//
//                        if (isLoadData()) {
//                            Log.d("TAG", "visibleItemCount: " + visibleItemCount + "/ totalItemCount: " + totalItemCount + "/ pastVisiblesItems: " + pastVisiblesItems);
//                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                                Log.d("TAG", "load more");
//                                if (shouldLoadMore)
//                                    viewModel.getNextTopFreeAppListByKeyword();
//                            }
//                        }
//                    }
//                }
//            }
//        });

//        binding.listView.setAutoLoadMoreThreshold(0);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        binding.recyclerView.addItemDecoration(dividerItemDecoration);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation);
//        binding.recyclerView.setLayoutAnimation(animation);
        topFreeAppAdapter = new TopFreeAppAdapter(this, binding.recyclerView);
        topFreeAppAdapter.setOnLoadMoreListener(new TopFreeAppAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                viewModel.getNextTopFreeAppListByKeyword();
            }
        });
        binding.recyclerView.setAdapter(topFreeAppAdapter);
    }

    void setupObservable() {
        viewModel.getObservableNetworkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                if (networkState.getStatus() == Status.SUCCESS) {
                    viewModel.initialTopFreeAppListByKeyword();
                    binding.loadingLayout.loadingView.setVisibility(View.GONE);
                } else if (networkState.getStatus() == Status.FAILED) {
                    Toast.makeText(MainActivity.this, R.string.error_network_fail, Toast.LENGTH_SHORT).show();
                    viewModel.initialTopFreeAppListByKeyword();
                    binding.loadingLayout.loadingView.setVisibility(View.GONE);
                } else {
                    binding.loadingLayout.loadingView.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.getObservableTopFreeApp().observe(this, new Observer<List<TopFreeApp>>() {
            @Override
            public void onChanged(@Nullable List<TopFreeApp> topFreeApps) {
                initialTopFreeAppToListView(topFreeApps);
            }
        });
        viewModel.getObservableDeltaTopTreeAppData().observe(this, new Observer<List<TopFreeApp>>() {
            @Override
            public void onChanged(@Nullable List<TopFreeApp> topFreeApps) {
                addTopFreeAppToList(topFreeApps);
            }
        });
        viewModel.getObservableTopGrossApp().observe(this, new Observer<List<TopGrossApp>>() {
            @Override
            public void onChanged(@Nullable List<TopGrossApp> topGrossApps) {
                initialTopGrossAppToListView(topGrossApps);
            }
        });
    }

    void initialTopFreeAppToListView(List<TopFreeApp> topFreeApps) {
        topFreeAppAdapter.setList(topFreeApps);
        topFreeAppAdapter.setLoaded();
    }

    void addTopFreeAppToList(List<TopFreeApp> deltaTopFreeApps) {
        topFreeAppAdapter.addList(deltaTopFreeApps);
        topFreeAppAdapter.setLoaded();
    }

    void initialTopGrossAppToListView(List<TopGrossApp> topGrossApps) {
        topFreeAppAdapter.setHeaderList(topGrossApps);
    }
}
