package com.test.metio.ui.main.moreFragment;

import static com.test.metio.ui.BaseActivity.ACTION_FINISH_LOADING;
import static com.test.metio.ui.BaseActivity.ACTION_FINISH_LOADING_DIALOG;
import static com.test.metio.ui.BaseActivity.ACTION_START_LOADING;
import static com.test.metio.ui.BaseActivity.ACTION_START_LOADING_DIALOG;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.test.metio.R;
import com.test.metio.databinding.DialogAddCityBinding;
import com.test.metio.databinding.FragmentMoreBinding;
import com.test.metio.ui.BaseActivity;
import com.test.metio.ui.BaseFragment;
import com.test.metio.ui.tools.UITools;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FMore extends BaseFragment {

    FragmentMoreBinding binding;

    Dialog dialogAddCity;
    DialogAddCityBinding bindingD;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);
        initView();
        initObservable();
        return binding.getRoot();
    }

    private void initObservable() {
        viewModel.actions.observe(requireActivity(), actions -> {
            for (String action : actions) {
                switch (action) {
                    case ACTION_START_LOADING:
                        onStartLoading();
                        break;
                    case ACTION_FINISH_LOADING:
                        onFinishLoading();
                        break;
                    case ACTION_START_LOADING_DIALOG:
                        onStartLoadingDialog();
                        break;
                    case ACTION_FINISH_LOADING_DIALOG:
                        onFinishLoadingDialog();
                        onFinishLoading();
                        break;
                }
            }
        });
    }

    private void onFinishLoadingDialog() {
        if (dialogAddCity!=null){
            if (dialogAddCity.isShowing())
                dialogAddCity.dismiss();
        }
    }

    private void onStartLoadingDialog() {
        if (dialogAddCity!=null){
            dialogAddCity.show();
        }else{
            displayDialogAddCity();
            dialogAddCity.show();
        }
    }

    private void initView() {
        viewModel = new ViewModelProvider(this).get(FMoreViewModel.class);
        binding.setViewModel((FMoreViewModel) viewModel);
        binding.setLifecycleOwner(this);
        binding.recyclerView.setHasFixedSize(true);
        displayDialogAddCity();
        binding.recyclerView.setAdapter(((FMoreViewModel) viewModel).getAdapter(requireActivity()));
        ((FMoreViewModel) viewModel).initiateViewModel((BaseActivity) requireActivity());

        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                binding.swipeContainer.setRefreshing(false);
            }

        });
    }

    private void onStartLoading() {
        UITools.setOnLoadingState(true, requireActivity(), bindingD.oui, R.id.btn_progress, R.id.btn_next);
    }

    private void onFinishLoading() {
        UITools.setOnLoadingState(false, requireActivity(), bindingD.oui, R.id.btn_progress, R.id.btn_next);
    }

    public void displayDialogAddCity() {
        dialogAddCity = new Dialog(requireContext(), R.style.AlertDialog);
         bindingD = DataBindingUtil.inflate(LayoutInflater.from(requireContext()),
                R.layout.dialog_add_city, null, false);
        dialogAddCity.setContentView(bindingD.getRoot());
        dialogAddCity.setCancelable(false);
        bindingD.setViewModel((FMoreViewModel) viewModel);
        dialogAddCity.create();
    }


}
