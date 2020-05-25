package com.example.demo.ui.target;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.TargetRecyclerAdapter;
import com.example.demo.data.model.Aim;
import com.example.demo.data.model.Task;
import com.example.demo.dialog.TargetEditDialog;
import com.example.demo.util.StringUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TargetFragment extends Fragment {

    private TargetViewModel targetViewModel;
 //   private FragmentTargetBinding binding;
    TargetRecyclerAdapter adapter;
    RecyclerView recyclerView;

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case StringUtil.TASK_INSERT:
                    targetViewModel.addTask((Task)msg.obj,handler);
                    break;
                case StringUtil.TASK_UPDATE:
                    targetViewModel.updateTask((Task)msg.obj);
                    break;

                case StringUtil.TASK_DELETE:
                    targetViewModel.deleteTaskByStatus((Task)msg.obj);
                    break;
                case StringUtil.FAIL_REQUEST:
                    Toast.makeText(getContext(),getString(R.string.fail_request),Toast.LENGTH_SHORT).show();
                    break;
                case StringUtil.TARGET_UPDATE:
                    targetViewModel.updateAim((Aim)msg.obj);
                    break;
                case StringUtil.TARGET_DELETE:
                    targetViewModel.deleteAim((Aim)msg.obj);
                    break;
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        targetViewModel =
                ViewModelProviders.of(this,new TargetViewModelFactory()).get(TargetViewModel.class);
        targetViewModel.setCacheDataSource(getActivity());
        //binding= DataBindingUtil.inflate(inflater,R.layout.fragment_target,container,false);
       // binding.setLifecycleOwner(this);
       View view=inflater.inflate(R.layout.fragment_target, container, false);
     //   View view=binding.getRoot();
        initRecycleAim(view);
        initFloatingActionButton(view);
        return view;
    }

    private void initFloatingActionButton(View view) {
        FloatingActionButton fab = view.findViewById(R.id.fab_target_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TargetEditDialog dialog=new TargetEditDialog(getActivity(),null);
                dialog.show();
                dialog.getImage_action_sure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Aim aim=dialog.getAim();
                        targetViewModel.addAim(aim);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void initRecycleAim(View view) {
        recyclerView=view.findViewById(R.id.recycler_target);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter=new TargetRecyclerAdapter(getContext(),getActivity(),handler);
        adapter.setTaskRepository(targetViewModel.getTargetRepository());
        recyclerView.setAdapter(adapter);
        targetViewModel.getAimList().observe(this.getViewLifecycleOwner(), new Observer<List<Aim>>() {
            @Override
            public void onChanged(List<Aim> aims) {
                adapter.setAll(aims);
                adapter.notifyDataSetChanged();
            }
        });
    }
}