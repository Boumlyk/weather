package com.test.metio.ui.main.homeFragment;

import static com.test.metio.ui.BaseActivity.ACTION_FINISH_LOADING;
import static com.test.metio.ui.BaseActivity.ACTION_START_LOADING;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.test.metio.R;
import com.test.metio.databinding.FragmentHomeBinding;
import com.test.metio.tools.Utils;
import com.test.metio.ui.BaseActivity;
import com.test.metio.ui.BaseFragment;

import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FHome extends BaseFragment {

    FragmentHomeBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        initView();
        initObservable();

        return binding.getRoot();
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void initObservable() {
        ((FHomeViewModel) viewModel).currentWeatherMutableLiveData.observe(getViewLifecycleOwner(),entity -> {
            if (entity!=null){
                binding.layout.cityName.setText(entity.getName());
                binding.layout.temp.setText(String.format(Locale.getDefault(), "%.0f°", entity.getTemp()));
                binding.layout.minMax.setText(String.format(Locale.getDefault(), "%.0f°", entity.getTempMin())+
                        " / "+String.format(Locale.getDefault(), "%.0f°",
                        entity.getTempMax())+"\n"+ String.format(Locale.getDefault(),
                        getResources().getString(R.string.wind), entity.getSpeed()));
                binding.layout.humidity.setText(String.format(Locale.ENGLISH,getResources().getString(R.string.humidity) , entity.getHumidity())+"\n" +Utils.formatDateAndTime(entity.getDt()));
                Glide.with(requireActivity()).load(Utils.getIconUrl(entity.getIcon())).centerCrop().into(binding.layout.image);
            }
        });
        viewModel.actions.observe(requireActivity(), actions -> {
            for (String action : actions) {
                switch (action) {
                    case ACTION_START_LOADING:
                        onStartLoading();
                        break;
                    case ACTION_FINISH_LOADING:
                        onFinishLoading();
                        break;
                }
            }
        });
        binding.swipeContainer.setOnRefreshListener(() -> {
            viewModel.cookies.setDownloadedTime(System.currentTimeMillis()-60000);
            ((FHomeViewModel) viewModel).onRefresh((BaseActivity) requireActivity());
        });
    }

    private void onFinishLoading() {
        binding.swipeContainer.setRefreshing(false);
    }

    private void onStartLoading() {
        binding.swipeContainer.setRefreshing(true);
    }

    private void initView() {
        viewModel = new ViewModelProvider(this).get(FHomeViewModel.class);
        binding.setViewModel((FHomeViewModel) viewModel);
        binding.setLifecycleOwner(this);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(((FHomeViewModel) viewModel).getAdapter(requireActivity()));
        ((FHomeViewModel) viewModel).initiateViewModel((BaseActivity) requireActivity());

    }

}
