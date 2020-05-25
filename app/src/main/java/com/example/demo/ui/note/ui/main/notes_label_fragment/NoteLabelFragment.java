package com.example.demo.ui.note.ui.main.notes_label_fragment;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.NotesLabelRecyclerAdapter;
import com.example.demo.data.model.Notes_label;
import com.example.demo.databinding.FragmentNoteLabelBinding;
import com.example.demo.util.StringUtil;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteLabelFragment extends Fragment {

    NoteLabelViewModel noteLabelViewModel;
    RecyclerView recycler_view_notes_labels;
    FragmentNoteLabelBinding binding;
    View view;
    NotesLabelRecyclerAdapter adapter;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StringUtil.NOTES_LABEL_UPDATE:
                    noteLabelViewModel.updateLabel((Notes_label)msg.obj);
                    break;
                case StringUtil.NOTES_LABEL_DELETE:
                    noteLabelViewModel.statusDeleteLabel((Notes_label)msg.obj);
                    break;
            }
        }
    };
    public NoteLabelFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        noteLabelViewModel= ViewModelProviders.of(this,new NoteLabelViewModelFactory()).get(NoteLabelViewModel.class);
        noteLabelViewModel.getRepository().setNotesCacheDataSource(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_note_label, container, false);
        view=binding.getRoot();
        initView();
        return view;
    }

    private void initView() {

        recycler_view_notes_labels=view.findViewById(R.id.recycler_view_notes_labels);
        recycler_view_notes_labels.setLayoutManager(new LinearLayoutManager(view.getContext()));
         adapter=new NotesLabelRecyclerAdapter(getContext(),handler,NotesLabelRecyclerAdapter.NOTES_LABEL_LAYOUT);
        recycler_view_notes_labels.setAdapter(adapter);
        noteLabelViewModel.getNoteLabelList().observe(getViewLifecycleOwner(), new Observer<List<Notes_label>>() {
            @Override
            public void onChanged(List<Notes_label> tasks) {
                adapter.setAll(tasks);
                adapter.notifyDataSetChanged();
            }
        });
    }

}
