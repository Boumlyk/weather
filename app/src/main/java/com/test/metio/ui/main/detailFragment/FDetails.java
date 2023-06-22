package com.test.metio.ui.main.detailFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.test.metio.R;
import com.test.metio.databinding.FragmentDetailsBinding;
import com.test.metio.tools.Utils;
import com.test.metio.ui.BaseActivity;
import com.test.metio.ui.BaseFragment;
import com.test.metio.ui.main.homeFragment.FHomeViewModel;

import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FDetails extends BaseFragment {

    FragmentDetailsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);

        initView();
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        viewModel = new ViewModelProvider(this).get(FDetailsViewModel.class);
        binding.setViewModel((FDetailsViewModel) viewModel);
        binding.setLifecycleOwner(this);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false));
        binding.recyclerView.setAdapter(((FDetailsViewModel) viewModel).getAdapter(requireActivity()));
        ((FDetailsViewModel) viewModel).initiateViewModel((BaseActivity) requireActivity());
        ((FDetailsViewModel) viewModel).currentWeather.observe(getViewLifecycleOwner(), entity -> {
            if (entity!=null){
                binding.swipeContainer.setRefreshing(false);
                binding.cityName.setText(entity.getName());
                binding.pressure.setText(String.format(Locale.getDefault(),
                        getResources().getString(R.string.pressure), entity.getPressure()));
                binding.state.setText(entity.getWeatherStatus());
                binding.temp.setText(String.format(Locale.getDefault(), "%.0f°", entity.getTemp()));
                binding.minMax.setText(String.format(Locale.getDefault(), "%.0f°", entity.getTempMin())+
                        " / "+String.format(Locale.getDefault(), "%.0f°",
                        entity.getTempMax())+"\n"+String.format(Locale.getDefault(),
                        getResources().getString(R.string.wind), entity.getSpeed()));
                binding.humidity.setText(String.format(Locale.ENGLISH,getResources().getString(R.string.humidity) , entity.getHumidity())+"\n" + Utils.formatDateAndTime(entity.getDt()));
                Glide.with(requireActivity()).load(Utils.getIconUrl(entity.getIcon())).centerCrop().into(binding.image);
            }
        });
        binding.swipeContainer.setOnRefreshListener(() -> {
            ((FDetailsViewModel) viewModel).onRefresh();
        });
    }

}
