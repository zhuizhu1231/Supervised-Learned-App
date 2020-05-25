package com.example.demo.ui.task;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.demo.R;
import com.example.demo.adapter.TaskRecyclerAdapter;
import com.example.demo.data.model.Task;
import com.example.demo.databinding.FragmentTaskBinding;
import com.example.demo.dialog.TaskEditDialog;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TaskFragment extends Fragment {

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StringUtil.TASK_UPDATE:
                    taskViewModel.UpdateTask((Task)msg.obj);
                    break;

                case StringUtil.TASK_DELETE:
                    taskViewModel.deleteTaskByStatus((Task)msg.obj);
                    break;
            }
        }
    };


    private TaskViewModel taskViewModel;
    FragmentTaskBinding binding;
    RecyclerView recyclerView;
    TaskRecyclerAdapter adapter;
    FloatingActionButton fab_task_add;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        taskViewModel =
                ViewModelProviders.of(this).get(TaskViewModel.class);

        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false);

        binding.setLifecycleOwner(this);
        View view=binding.getRoot();
        initRecycleTask(view);
        initTaskAddButton(view);
        taskViewModel.getTaskAloneList().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.setAll(tasks);
                adapter.notifyDataSetChanged();
            }
        });
        actionBarDrawerStart();

        return view;
    }

    private void initTaskAddButton(View view) {
        fab_task_add=view.findViewById(R.id.fab_task_add);
        fab_task_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TaskEditDialog dialog=new TaskEditDialog(getActivity(),null);
                dialog.show();
                dialog.getImage_action_sure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Task task = dialog.getTask();
                        if(Tool.testStringNotNULL(task.getTitle())){
                            //TOdo:
                           taskViewModel.insertTask(task);
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void actionBarDrawerStart() {
        ActionBar actionBar= getActivity().getActionBar();
        if(actionBar!=null){
            // TODO: 底部导航栏对toolbar的返回  存在问题  需改正
            setHasOptionsMenu(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer_start_white_24dp);
        }
    }
    private void initRecycleTask(View view) {
        recyclerView=view.findViewById(R.id.recycle_task);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter=new TaskRecyclerAdapter(getActivity(),TaskRecyclerAdapter.BIG_LAYOUT,handler);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}