package com.example.demo.ui.note.ui.main.notes_fragment.edit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.DialogNotesLabelRecyclerAdapter;
import com.example.demo.adapter.NotesLabelRecyclerAdapter;
import com.example.demo.data.model.Notes;
import com.example.demo.data.model.Notes_in_label;
import com.example.demo.data.model.Notes_label;
import com.example.demo.databinding.ActivityNoteEditBinding;
import com.example.demo.dialog.LabelAddDialog;
import com.example.demo.util.StringUtil;
import com.example.demo.util.TimeUtils;
import com.example.demo.util.Tool;

import java.util.ArrayList;
import java.util.List;




public class NoteEditActivity extends AppCompatActivity {
    NoteEditViewModel noteEditViewModel;
    ImageView action_sure,image_note_label,image_action_delete;
    ActivityNoteEditBinding binding;
    Notes notes;
    RecyclerView note_labels_recyclerView;
    NotesLabelRecyclerAdapter adapter;
    EditText note_content_edit;
    int activity_status;
    LabelAddDialog dialog;
    List<Notes_label> initNotesLabelList;
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case StringUtil.NOTE_ADD_LABEL_DIALOG_MESSAGE:
                    adapter.addOrRemoveNotesLabel((Notes_label) msg.obj);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //TODO:默认为INSERT_STATUS,需要根据传进activity的值判断；
//        Intent intent = getIntent();
//        Serializable note = intent.getSerializableExtra("note");
//        if(note==null)activity_status=StringUtil.INSERT_STATUS;
//        else  activity_status=StringUtil.UPDATE_STATUS;

        setContentView(R.layout.activity_note_edit);
        androidx.appcompat.widget.Toolbar toolbar= findViewById(R.id.note_edit_toolbar);
        setSupportActionBar(toolbar);
        noteEditViewModel = ViewModelProviders.of(this,new NotesEditViewModelFactory()).get(NoteEditViewModel.class);
       // noteViewModel= ViewModelProviders.of(this).get(NoteViewModel.class);
        noteEditViewModel.getRepository().setNotesCacheDataSource(getApplicationContext());
        binding= DataBindingUtil.setContentView(this,R.layout.activity_note_edit);
        binding.setModel(noteEditViewModel);
        binding.setDate(TimeUtils.getDateNow(getResources().getString(R.string.date_format),getResources().getString(R.string.timezone)));
        binding.setLifecycleOwner(this);
        note_content_edit=findViewById(R.id.note_content_edit);
        image_action_delete=findViewById(R.id.image_action_delete);
        image_note_label=findViewById(R.id.image_note_label);
       // note_content_edit.setText("");//从传输通道中取值
        //TextView note_date=findViewById(R.id.note_date);
       // note_date.setText(TimeUtils.getDateNow(getResources().getString(R.string.date_format),getResources().getString(R.string.timezone)));
        initRecyclerView();
        initData();
        setAction_sureAction();
        setDialog_noteLabel();
        setActionDeleteListener();

    }

    private void setActionDeleteListener() {
        image_action_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Notes_label label:initNotesLabelList){
                    noteEditViewModel.statusDeleteRelationByNotesIdLabelIdAsync(notes.getId(),label.getId());
                }
                noteEditViewModel.statusDeleteNotesAsync(notes);
                finish();
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        notes = (Notes) intent.getSerializableExtra("note");
        if(notes!=null) {
            activity_status = StringUtil.UPDATE_STATUS;
            note_content_edit.setText(notes.getContent());
            List<Notes_label> labelList=noteEditViewModel.getNotesLabelByNotesStatic(notes);
            adapter.setAll(labelList);
            adapter.notifyDataSetChanged();
            initNotesLabelList=new ArrayList<>();
            for(Notes_label label:labelList){
                initNotesLabelList.add(label);
            }
        } else {
            image_action_delete.setVisibility(View.GONE);
            activity_status = StringUtil.INSERT_STATUS;
        }
    }

    private void setDialog_noteLabel() {

        image_note_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new LabelAddDialog(NoteEditActivity.this,new DialogNotesLabelRecyclerAdapter(NoteEditActivity.this,handler));
                DialogNotesLabelRecyclerAdapter dialogAdapter = (DialogNotesLabelRecyclerAdapter) dialog.getAdapter();
                List<Notes_label> allNoteLabelStatic = noteEditViewModel.getAllNoteLabelStatic();
                dialogAdapter.setAll(allNoteLabelStatic);
                noteEditViewModel.getAllNoteLabel().observe(NoteEditActivity.this, new Observer<List<Notes_label>>() {
                    @Override
                    public void onChanged(List<Notes_label> notes_labels) {
                       // dialog.updateView(notes_labels);
                        dialogAdapter.setAll(notes_labels);
                        dialogAdapter.notifyDataSetChanged();
                    }
                });
                dialog.show();

            }
        });
    }

    private void initRecyclerView() {
        note_labels_recyclerView=findViewById(R.id.note_labels_recyclerView);
        adapter = new NotesLabelRecyclerAdapter(this,handler, NotesLabelRecyclerAdapter.NOTES_LAYOUT);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        note_labels_recyclerView.setLayoutManager(linearLayoutManager);
        note_labels_recyclerView.setAdapter(adapter);

    }

    private void setAction_sureAction(){
        action_sure=findViewById(R.id.text_action_sure);
        action_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Notes_label> allNotes_labels = adapter.getAllNotes_labels();
                if(StringUtil.INSERT_STATUS==activity_status) {
                    notes = new Notes();
                    notes.setContent(note_content_edit.getText().toString());
                    if (null != notes.getContent() && !"".equals(notes.getContent().trim())) {
                        Long note_id=noteEditViewModel.insertNoteMain(notes);
                        for(Notes_label label:allNotes_labels){
                            Notes_in_label relation = new Notes_in_label();
                            relation.setNotesLabelId(label.getId());
                            relation.setNotesId(note_id.intValue());
                            noteEditViewModel.insertRelationAsync(relation);//repository中会更新label count数
                        }
                    }
                    finish();
                }else {
                    notes.setContent(note_content_edit.getText().toString());
                    if (null != notes.getContent() && !"".equals(notes.getContent().trim())) {
                        notes.setLastTime(Tool.createNewTimeStamp());
                        noteEditViewModel.updateNotes(notes);
                        for(Notes_label label:initNotesLabelList){
                            if(allNotes_labels.contains(label)) {
                                allNotes_labels.remove(label);
                                initNotesLabelList.remove(label);
                            }
                        }
                        for(Notes_label label:initNotesLabelList){
                            noteEditViewModel.statusDeleteRelationByNotesIdLabelIdAsync(notes.getId(),label.getId());//repository中会更新label count数
                        }
                        for(Notes_label label:allNotes_labels){
                            Notes_in_label relation = new Notes_in_label();
                            relation.setNotesLabelId(label.getId());
                            relation.setNotesId(notes.getId());
                            noteEditViewModel.insertRelationAsync(relation);//repository中会更新label count数
                        }
                    }
                    finish();
                    
                }
            }
        });
    }


}
