package com.example.demo.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;

public class LabelAddDialog extends AlertDialog {
    RecyclerView recycler_view_dialog_label;
    RecyclerView.Adapter adapter;
    Activity mActivity;


    public LabelAddDialog(@NonNull Activity context,RecyclerView.Adapter adapter) {
        super(context);
        mActivity=context;
        this.adapter=adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_label_add);
        getWindow().setGravity(Gravity.BOTTOM);
        recycler_view_dialog_label=findViewById(R.id.recycler_view_dialog_label);
        recycler_view_dialog_label.setLayoutManager(new LinearLayoutManager(mActivity));
        recycler_view_dialog_label.setAdapter(adapter);
    }


    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

//    public void updateView(List<Notes_label> notes_labels){
//        adapter.setAll(notes_labels);
//        adapter.notifyDataSetChanged();
//    }
}
