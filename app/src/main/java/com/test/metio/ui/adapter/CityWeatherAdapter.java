package com.test.metio.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.test.metio.R;
import com.test.metio.databinding.ItemWeatherBinding;
import com.test.metio.module.weather.CurrentWeatherEntity;
import com.test.metio.tools.Utils;
import com.test.metio.ui.BaseViewModel;
import com.test.metio.ui.main.homeFragment.FHomeViewModel;
import com.test.metio.ui.main.moreFragment.FMoreViewModel;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CityWeatherAdapter extends RecyclerView.Adapter<CityWeatherAdapter.ViewHolder> implements Filterable {
    Context context;
    BaseViewModel viewModel;
    List<CurrentWeatherEntity> list;
    private final List<CurrentWeatherEntity> ListCopy;
    private final Filter operationFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CurrentWeatherEntity> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList = ListCopy;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (CurrentWeatherEntity item : ListCopy) {
                    if (item != null && stripAccents(item.getName().toLowerCase()).contains(stripAccents(filterPattern))) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list = (ArrayList<CurrentWeatherEntity>) results.values;
            notifyDataSetChanged();
        }
    };

    public CityWeatherAdapter(Context context, BaseViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
        this.list = new ArrayList<>();
        this.ListCopy = new ArrayList<>();
    }

    public static String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CityWeatherAdapter.ViewHolder(ItemWeatherBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CurrentWeatherEntity entity = list.get(position);
        holder.binding.temp.setText(String.format(Locale.getDefault(), "%.0f°", entity.getTemp()));
        holder.binding.cityName.setText(entity.getName());
        holder.binding.state.setText(entity.getWeatherStatus());
        Glide.with(context).load(Utils.getIconUrl(entity.getIcon())).centerCrop().into(holder.binding.imageWeather);
    }

    public void feedList(List<CurrentWeatherEntity> strings) {
        list.clear();
        list.addAll(strings);
        ListCopy.clear();
        ListCopy.addAll(strings);
        notifyDataSetChanged();
    }
    public void addItemToList(CurrentWeatherEntity entity) {
        list.add(entity);
        ListCopy.add(entity);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return operationFilter;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemWeatherBinding binding;

        public ViewHolder(ItemWeatherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                if (viewModel != null) {
                    if (viewModel instanceof FHomeViewModel){
                        ((FHomeViewModel)viewModel).onItemClicked(list.get(getAdapterPosition()));
                    }else if (viewModel instanceof FMoreViewModel){
                        ((FMoreViewModel)viewModel).onItemClicked(list.get(getAdapterPosition()));
                    }
                }
            });
            binding.getRoot().setOnLongClickListener(v -> {
                Dialog dialog=new Dialog(context, R.style.AlertDialog);
                dialog.setContentView(R.layout.dialog_delete_city);
                dialog.setCancelable(false);
                dialog.findViewById(R.id.yes).setOnClickListener(v1 -> {
                    viewModel.onItemDeleted(list.get(getAdapterPosition()));
                    list.remove(list.get(getAdapterPosition()));
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();
                });
                dialog.findViewById(R.id.no).setOnClickListener(v1 -> {
                    dialog.dismiss();
                });
                dialog.create();
                dialog.show();
                return false;
            });

        }
    }
}
