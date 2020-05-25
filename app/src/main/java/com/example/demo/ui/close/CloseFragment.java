package com.example.demo.ui.close;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.example.demo.R;
import com.example.demo.data.model.Task;
import com.example.demo.databinding.FragmentCloseBinding;
import com.example.demo.util.LockUtil;


public class CloseFragment extends Fragment {
    View root;
    ConstraintLayout close_relax_layout,close_work_layout;
    private CloseViewModel closeViewModel;
    private FragmentCloseBinding binding;
    private Task task;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        closeViewModel =
                ViewModelProviders.of(this).get(CloseViewModel.class);
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_close,container,false);
        binding.setClose(closeViewModel);
        binding.setLifecycleOwner(this);
        root = binding.getRoot();
        initView();
        return root;
    }

    private void initView() {
        close_relax_layout=root.findViewById(R.id.close_relax_layout);
        close_work_layout=root.findViewById(R.id.close_work_layout);
        close_relax_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockUtil.lock(getActivity(), task);
            }
        });
        close_work_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockUtil.lock(getActivity(), task);
            }
        });
    }

}