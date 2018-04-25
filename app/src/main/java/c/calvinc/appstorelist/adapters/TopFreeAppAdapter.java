package c.calvinc.appstorelist.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import c.calvinc.appstorelist.R;
import c.calvinc.appstorelist.databinding.LayoutEmptyviewBinding;
import c.calvinc.appstorelist.databinding.LayoutHeaderBinding;
import c.calvinc.appstorelist.databinding.ListitemAppBinding;
import c.calvinc.appstorelist.db.model.AppDetail;
import c.calvinc.appstorelist.db.model.TopFreeApp;
import c.calvinc.appstorelist.db.model.TopFreeAppDetail;
import c.calvinc.appstorelist.db.model.TopGrossApp;

/**
 * 2018-04-13
 *
 * @author calvinc
 */
public class TopFreeAppAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_LIST_EMPTY = 2;
    private static final int VIEW_TYPE_LOADING_VIEW = 3;

    private Context context;
    List<TopGrossApp> headerList;
    List<TopFreeAppDetail> list;

    private int lastPosition = -1;
    private boolean isAnimate;

    private int totalItemCount;
    private int lastVisibleItem;
    private boolean isLoading;
    private final int visibleThreshold = 5;
    private OnLoadMoreListener onLoadMoreListener;

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public TopFreeAppAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        list = null;
        headerList = null;
        lastPosition = -1;
        isAnimate = true;
        isLoading = true;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!isLoading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                isLoading = true;
                                showLoading();
                            }
                        }
                    });
        }
    }

    private void showLoading() {
        this.notifyDataSetChanged();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.onLoadMoreListener = listener;
    }

    public void setList(List<TopFreeAppDetail> list) {
        int lastPosition = -1;
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void setHeaderList(List<TopGrossApp> headerList) {
        this.headerList = headerList;
        this.notifyItemChanged(0);
    }

    public void addList(List<TopFreeAppDetail> addList) {
        setLoaded();
        if (addList.size() > 0) {
            int currentListSize = getItemCount();
            int currentPos = this.list.size() + 1;
            int numOfAdd = addList.size();
            this.list.addAll(addList);
            if (currentPos >= 0) {
                this.notifyItemRangeInserted(currentListSize, numOfAdd - 1);
            }
        } else {
            this.notifyDataSetChanged();
        }
    }

    public void setLoaded() {
        isLoading = false;
    }

    private void animateItem(int position, View viewToAnimate)
    {
        if (position > lastPosition && isAnimate)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        } else {
            if (showListEmptyView()) {
                return VIEW_TYPE_LIST_EMPTY;
            } else {
                int realListItemPosition = position - 1;
                if (list != null && realListItemPosition < list.size()) {
                    return VIEW_TYPE_ITEM;
                } else {
                    return VIEW_TYPE_LOADING_VIEW;
                }
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
//        ListitemAppBinding itemBinding =
//                ListitemAppBinding.inflate(layoutInflater, parent, false);
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                LayoutHeaderBinding headerBinding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_header, parent, false);
                return new HeaderViewHolder(headerBinding);
            case VIEW_TYPE_LIST_EMPTY:
                LayoutEmptyviewBinding emptyViewBinding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_emptyview, parent, false);
                return new EmptyViewHolder(emptyViewBinding);
            case VIEW_TYPE_LOADING_VIEW:
                View view = layoutInflater.inflate(R.layout.listitem_loadmore, parent, false);
                return new LoadingViewHolder(view);
            case VIEW_TYPE_ITEM:
            default:
                ListitemAppBinding itemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.listitem_app, parent, false);
                return new AppItemViewHolder(itemBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AppItemViewHolder) {
            int appPosition = position - 1;
            if (appPosition > -1) {
                TopFreeAppDetail app = getItem(appPosition);
                if (app != null) {
                    ((AppItemViewHolder)holder).bindData(appPosition, app);
                    animateItem(position, holder.itemView);
                } else {
                    ((AppItemViewHolder)holder).clear();
                }
            }
        } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder)holder).bindData(headerList);
        }
    }

    @Override
    public int getItemCount() {
        return (list!=null ? list.size() : 0) + 1 + (showListEmptyView() ? 1 : 0) + (isLoading ? 1 : 0);
    }

    private boolean showListEmptyView() {
        return list != null && list.isEmpty();
    }

    TopFreeAppDetail getItem(int position) {
        if (list.size() > position) {
            return list.get(position);
        } else {
            return null;
        }
    }

    @Override
    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder)
    {
        if (holder instanceof AppItemViewHolder) ((AppItemViewHolder)holder).clearAnimation();
    }

    public static final DiffUtil.ItemCallback<TopFreeApp> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<TopFreeApp>() {
            @Override
            public boolean areItemsTheSame(@NonNull TopFreeApp oldUser, @NonNull TopFreeApp newUser) {
                // User properties may have changed if reloaded from the DB, but ID is fixed
                return oldUser.appId.equals(newUser.appId);
            }
            @Override
            public boolean areContentsTheSame(@NonNull TopFreeApp oldUser, @NonNull TopFreeApp newUser) {
                // NOTE: if you use equals, your object must properly override Object#equals()
                // Incorrectly returning false here will result in too many animations.
                return oldUser.appId.equals(newUser.appId);
            }
        };

    public static class AppItemViewHolder extends RecyclerView.ViewHolder {
        private final ListitemAppBinding binding;

        public AppItemViewHolder(ListitemAppBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindData(int position, TopFreeAppDetail topItem) {
            TopFreeApp item = topItem.topFreeApp;
            AppDetail detail = topItem.details;
            this.binding.txtAppName.setText(item.name);
            this.binding.txtPosition.setText(String.valueOf(position+1));
            this.binding.txtCat.setText(item.category);
            android.support.v7.widget.AppCompatRatingBar ratingBar = this.binding.getRoot().findViewById(R.id.rateBar);
            ratingBar.setRating((float)detail.averageUserRating);
            this.binding.numberUserRated.setText("("+detail.userRatingCount+")");
            SimpleDraweeView imgIcon = (SimpleDraweeView)this.binding.getRoot().findViewById(R.id.imgIcon);

//            this.binding.imgIcon.setImageUri(Uri.parse(item.imageUrl));
            if (position % 2 == 0) {
                RoundingParams roundingParams = RoundingParams.fromCornersRadius(7f);
                imgIcon.setHierarchy(new GenericDraweeHierarchyBuilder(binding.getRoot().getResources())
                        .setRoundingParams(roundingParams)
                        .build());
            } else {
                RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                roundingParams.setRoundAsCircle(true);
                imgIcon.getHierarchy().setRoundingParams(roundingParams);
            }
            imgIcon.setImageURI(Uri.parse(item.imageUrl));
        }

        void clear() {

        }

        void clearAnimation() {
            itemView.clearAnimation();
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        LayoutHeaderBinding binding;
        TopGrossAppAdapter appAdapter;
        public HeaderViewHolder(LayoutHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            appAdapter = new TopGrossAppAdapter();
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            binding.horizontalList.setLayoutManager(horizontalLayoutManager);
            this.binding.horizontalList.setAdapter(appAdapter);
        }

        void bindData(List<TopGrossApp> headerList) {
            if (headerList != null) {
                appAdapter.submitList(headerList);
                if (headerList!=null && headerList.isEmpty()) {
                    binding.horizontalList.setVisibility(View.GONE);
                    binding.txtEmptyTopGross.setVisibility(View.VISIBLE);
                } else {
                    binding.horizontalList.setVisibility(View.VISIBLE);
                    binding.txtEmptyTopGross.setVisibility(View.GONE);
                }
            }
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        LayoutEmptyviewBinding binding;

        public EmptyViewHolder(LayoutEmptyviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View view) {
            super(view);
        }
    }
}
