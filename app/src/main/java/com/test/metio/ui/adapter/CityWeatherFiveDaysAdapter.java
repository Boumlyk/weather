package com.test.metio.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.test.metio.databinding.ItemFiveDayBinding;
import com.test.metio.module.weather.CurrentWeatherEntity;
import com.test.metio.tools.Utils;
import com.test.metio.ui.BaseViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CityWeatherFiveDaysAdapter extends RecyclerView.Adapter<CityWeatherFiveDaysAdapter.ViewHolder>{
    Context context;
    BaseViewModel viewModel;
    List<CurrentWeatherEntity> list;

    public CityWeatherFiveDaysAdapter(Context context, BaseViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CityWeatherFiveDaysAdapter.ViewHolder(ItemFiveDayBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CurrentWeatherEntity entity = list.get(position);
        holder.binding.temp.setText(String.format(Locale.getDefault(), "%.0f°", entity.getTemp()));
        holder.binding.date.setText(Utils.formatDateString(entity.getDtTxt()));
        holder.binding.state.setText(entity.getWeatherStatus());
        Glide.with(context).load(Utils.getIconUrl(entity.getIcon())).centerCrop().into(holder.binding.imageWeather);
    }

    public void feedList(List<CurrentWeatherEntity> strings) {
        list.clear();
        list.addAll(strings);
        notifyDataSetChanged();
    }
    public void addItemToList(CurrentWeatherEntity entity) {
        list.add(entity);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemFiveDayBinding binding;

        public ViewHolder(ItemFiveDayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
