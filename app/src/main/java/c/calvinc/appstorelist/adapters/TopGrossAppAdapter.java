package c.calvinc.appstorelist.adapters;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import c.calvinc.appstorelist.R;
import c.calvinc.appstorelist.databinding.ListitemHorizontalAppBinding;
import c.calvinc.appstorelist.db.model.TopGrossApp;

public class TopGrossAppAdapter extends RecyclerView.Adapter<TopGrossAppAdapter.ViewHolder> {

    List<TopGrossApp> list;
    public TopGrossAppAdapter() {
        list = new ArrayList<>();
    }

    public void submitList(List<TopGrossApp> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
//        ListitemAppBinding itemBinding =
//                ListitemAppBinding.inflate(layoutInflater, parent, false);
        ListitemHorizontalAppBinding itemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.listitem_horizontal_app, parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TopGrossApp app = getItem(position);
        if (app != null) {
            holder.bindData(position, app);
        } else {
            holder.clear();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    TopGrossApp getItem(int position) {
        if (list.size() > position) {
            return list.get(position);
        } else {
            return null;
        }
    }

    public static final DiffUtil.ItemCallback<TopGrossApp> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<TopGrossApp>() {
                @Override
                public boolean areItemsTheSame(@NonNull TopGrossApp oldUser, @NonNull TopGrossApp newUser) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return oldUser.appId.equals(newUser.appId);
                }
                @Override
                public boolean areContentsTheSame(@NonNull TopGrossApp oldUser, @NonNull TopGrossApp newUser) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldUser.appId.equals(newUser.appId);
                }
            };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ListitemHorizontalAppBinding binding;

        public ViewHolder(ListitemHorizontalAppBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindData(int position, TopGrossApp item) {
            this.binding.txtAppName.setText(item.name);
            this.binding.txtCat.setText(item.category);
            SimpleDraweeView imgIcon = (SimpleDraweeView)this.binding.getRoot().findViewById(R.id.imgIcon);

//            this.binding.imgIcon.setImageUri(Uri.parse(item.imageUrl));
            if (position % 2 == 0 || true) {
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
    }
}
