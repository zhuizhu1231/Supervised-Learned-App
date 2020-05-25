package com.example.demo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.data.dao.Notes_labelDao;
import com.example.demo.data.database.MyDataBase;
import com.example.demo.data.model.Notes;
import com.example.demo.data.model.Notes_label;
import com.example.demo.ui.note.ui.main.notes_fragment.edit.NoteEditActivity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.MyViewHolder> {
    private MyViewHolder viewHolder;
    List<Notes> allNotesses =new ArrayList<Notes>();
    private Notes_labelDao dao;
    Context context;
    Activity mActivity;
    NotesLabelRecyclerAdapter adapter;
    Handler handler;
    public NotesRecyclerAdapter(Activity activity, Context context,Handler handler) {
        mActivity=activity;
        dao= MyDataBase.getInstance(activity).getNotes_labelDao();
        this.context=context;
        this.handler=handler;


    }

    public void setAll(List<Notes> allNotesses) {
        this.allNotesses = allNotesses;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.recycler_note_item_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
         final Notes notes = allNotesses.get(position);
        holder.note_content.setText(notes.getContent());
        Timestamp last_time = notes.getLastTime();
        if(last_time!=null)
            holder.note_notify_time.setText(last_time.toString());
        else
            holder.note_notify_time.setText("");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, NoteEditActivity.class);
                intent.putExtra("note",notes);
                context.startActivity(intent);
            }
        });
        adapter = new NotesLabelRecyclerAdapter( context,handler,NotesLabelRecyclerAdapter.NOTES_LAYOUT);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.itemView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.note_labels_recyclerView.setLayoutManager(linearLayoutManager);
        holder.note_labels_recyclerView.setAdapter(adapter);
        List<Notes_label> allNotesLabelByNoteIdStatic = dao.getAllNotesLabelByNoteIdStatic(notes.getId());
        adapter.setAll(allNotesLabelByNoteIdStatic);
    }

    @Override
    public int getItemCount() {
        return allNotesses.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView note_content,note_notify_time;
        RecyclerView note_labels_recyclerView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            note_content=itemView.findViewById(R.id.note_content);
            note_notify_time=itemView.findViewById(R.id.note_notify_time);
            note_labels_recyclerView=itemView.findViewById(R.id.note_labels_recyclerView);
        }
    }
}
