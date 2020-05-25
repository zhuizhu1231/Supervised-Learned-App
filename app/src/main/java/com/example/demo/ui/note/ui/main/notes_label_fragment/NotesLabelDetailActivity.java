package com.example.demo.ui.note.ui.main.notes_label_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.NotesRecyclerAdapter;
import com.example.demo.data.model.Notes;
import com.example.demo.data.model.Notes_label;
import com.example.demo.data.repository.NotesRepository;

import java.util.List;

public class NotesLabelDetailActivity extends AppCompatActivity {

    NotesRepository notesRepository;
    TextView text_schedule_tag_title;
    RecyclerView recycler_list;
    NotesRecyclerAdapter adapter;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_detail);
        androidx.appcompat.widget.Toolbar toolbar=(androidx.appcompat.widget.Toolbar) findViewById(R.id.tag_detail_toolbar);
        setSupportActionBar(toolbar);
        notesRepository=NotesRepository.getInstance(getApplication());
        initView();
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        Notes_label label= (Notes_label) intent.getSerializableExtra("label");
        text_schedule_tag_title.setText(label.getTitle());

   //     List<Notes> scheduleListByTagStatic = notesRepository.getNotesListByTagStatic(label);
//        adapter.setAll(scheduleListByTagStatic);
//        adapter.notifyDataSetChanged();
        notesRepository.getNotesListByTag(label).observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> notes) {
                adapter.setAll(notes);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        text_schedule_tag_title=findViewById(R.id.text_tag_title);
        recycler_list=findViewById(R.id.recycler_list);
        adapter=new NotesRecyclerAdapter(this,this,handler);
        recycler_list.setLayoutManager(new LinearLayoutManager(this));
        recycler_list.setAdapter(adapter);
    }
}
