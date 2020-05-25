package com.example.demo.adapter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.data.model.Notes_label;
import com.example.demo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class DialogNotesLabelRecyclerAdapter extends RecyclerView.Adapter<DialogNotesLabelRecyclerAdapter.MyViewHolder> {
    private MyViewHolder viewHolder;
    List<Notes_label>  allNotes_labels=new ArrayList<Notes_label>();
    Handler handler;
    Activity mActivity;
    public DialogNotesLabelRecyclerAdapter(Activity activity, Handler handler) {
        mActivity=activity;
        this.handler=handler;

    }

    public void setAll(List<Notes_label> allNotes_labels) {
        this.allNotes_labels = allNotes_labels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.dialog_recycler_label_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
         final Notes_label label = allNotes_labels.get(position);
          holder.text_note_label_title.setText(label.getTitle());

          holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Message message = Message.obtain();
                    message.what = StringUtil.NOTE_ADD_LABEL_DIALOG_MESSAGE;
                    message.obj = label;
                    handler.sendMessage(message);

            }
        });

    }

    @Override
    public int getItemCount() {
        return allNotes_labels.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView text_note_label_title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text_note_label_title=itemView.findViewById(R.id.text_label_title);
        }
    }

    public List<Notes_label> getAllNotes_labels() {
        return allNotes_labels;
    }
}
