package com.example.demo.ui.note.ui.main.notes_fragment;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo.R;
import com.example.demo.adapter.NotesRecyclerAdapter;
import com.example.demo.data.model.Notes;
import com.example.demo.databinding.FragmentNoteBinding;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment {
    NoteViewModel noteViewModel;
    FragmentNoteBinding binding;
    View view;
    RecyclerView recycler_view_notes;
    NotesRecyclerAdapter adapter;
    Handler handler=new Handler(){};
    public NoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //noteViewModel= ViewModelProviders.of(this).get(NoteViewModel.class);
       noteViewModel = ViewModelProviders.of(this,new NotesViewModelFactory()).get(NoteViewModel.class);
       noteViewModel.getRepository().setNotesCacheDataSource(getActivity());
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_note, container, false);
        binding.setLifecycleOwner(this);
        view=binding.getRoot();
//        View view=inflater.inflate(R.layout.fragment_note, container, false);
        initView();
        return view;
    }

    private void initView() {
        recycler_view_notes=view.findViewById(R.id.recycler_view_notes);
        recycler_view_notes.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter=new NotesRecyclerAdapter(getActivity(),getContext(),handler);
        recycler_view_notes.setAdapter(adapter);

        noteViewModel.getNotesList().observe(getViewLifecycleOwner(), new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> tasks) {
                adapter.setAll(tasks);
                adapter.notifyDataSetChanged();
            }
        });
    }

}
