package com.example.demo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.data.model.Notes_label;
import com.example.demo.dialog.LabelEditDialog;
import com.example.demo.ui.note.ui.main.notes_label_fragment.NotesLabelDetailActivity;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.util.ArrayList;
import java.util.List;

public class NotesLabelRecyclerAdapter extends RecyclerView.Adapter<NotesLabelRecyclerAdapter.MyViewHolder> {

    List<Notes_label>  allNotes_labels=new ArrayList<Notes_label>();
    public final static Integer NOTES_LAYOUT=1;
    public final static Integer NOTES_LABEL_LAYOUT=2;
    private Integer now_layout;
    Handler handler;
    Context context;
    public NotesLabelRecyclerAdapter( Context context, Handler handler, int layout_style) {

        this.now_layout=layout_style;

        this.handler=handler;
        this.context=context;
    }

    public void setAll(List<Notes_label> allNotes_labels) {
        this.allNotes_labels = allNotes_labels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView;
        if(now_layout==NOTES_LAYOUT)
            itemView=layoutInflater.inflate(R.layout.labels_recycler_view_item,parent,false);
        else
            itemView=layoutInflater.inflate(R.layout.recycler_tag_item_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
         final Notes_label label = allNotes_labels.get(position);
        holder.note_label_title.setText(label.getTitle());
        if(null!=label.getNotesCount())
            holder.note_label_count.setText(label.getNotesCount().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, NotesLabelDetailActivity.class);
                intent.putExtra("label",label);
                context.startActivity(intent);
            }
        });
        if(now_layout==NOTES_LABEL_LAYOUT){
            holder.image_action_setting=holder.itemView.findViewById(R.id.image_action_setting);
            holder.image_action_delete=holder.itemView.findViewById(R.id.image_action_delete);
            holder.image_action_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tool.alertDialogShow(context,null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Tool.sendMessage(handler, StringUtil.NOTES_LABEL_DELETE, label);
                            dialog.dismiss();
                        }
                    });
                }
            });
            holder.image_action_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LabelEditDialog notesLabelEditDialog=new LabelEditDialog(context,label);
                    notesLabelEditDialog.show();
                    notesLabelEditDialog.getAction_sure().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Notes_label dialogLabel = notesLabelEditDialog.getLabel();
                            if(Tool.testStringNotNULL(dialogLabel.getTitle()))
                                Tool.sendMessage(handler, StringUtil.NOTES_LABEL_UPDATE, dialogLabel);
                            notesLabelEditDialog.dismiss();
                        }
                    });
                    notesLabelEditDialog.getImage_action_delete().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Tool.alertDialogShow(context,null, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Tool.sendMessage(handler, StringUtil.NOTES_LABEL_DELETE, notesLabelEditDialog.getLabel());
                                    dialog.dismiss();
                                }
                            });
                            notesLabelEditDialog.dismiss();
                        }
                    });
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return allNotes_labels.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView note_label_title,note_label_count;
        ImageView image_action_setting,image_action_delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            note_label_title=itemView.findViewById(R.id.tag_title);
            note_label_count=itemView.findViewById(R.id.tag_count);
        }
    }

    public List<Notes_label> getAllNotes_labels() {
        return allNotes_labels;
    }
    public void addOrRemoveNotesLabel(Notes_label label){
        boolean temp=true;
        for (Notes_label i:allNotes_labels){
            if(i.getId().equals(label.getId())){
                allNotes_labels.remove(i);
                temp=false;
                break;
            }
        }
        if(temp) allNotes_labels.add(label);
    }
}
