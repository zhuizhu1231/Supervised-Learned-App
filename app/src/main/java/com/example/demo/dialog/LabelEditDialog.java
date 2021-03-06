package com.example.demo.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.demo.R;
import com.example.demo.data.model.Notes_label;

public class LabelEditDialog extends AlertDialog {
    EditText edit_text_note_label_title ;
    ImageView action_sure,image_action_delete;
    Notes_label label;
    public static Integer NOTE_LABEL_EDIT_CATEGORY_SAVE=0;
    public static Integer NOTE_LABEL_EDIT_CATEGORY_UPDATE=1;
    //private Integer category;
    public LabelEditDialog(@NonNull Context context, Notes_label label) {
        super(context);
        this.label=label;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_note_label_edit);
        edit_text_note_label_title = findViewById(R.id.edit_text_label_title);
        action_sure=findViewById(R.id.text_action_sure);
        image_action_delete=findViewById(R.id.image_action_delete);
        if(null!=label){
            edit_text_note_label_title.setText(label.getTitle());
          //  category=NOTE_LABEL_EDIT_CATEGORY_UPDATE;
        }else {
            label=new Notes_label();
            label.setNotesCount(0);
            image_action_delete.setVisibility(View.GONE);
         //   category=NOTE_LABEL_EDIT_CATEGORY_SAVE;
        }
        edit_text_note_label_title.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                label.setTitle(edit_text_note_label_title.getText().toString());
            }
        });
    }

    public ImageView getAction_sure() {
        return action_sure;
    }

   // public Integer getCategory() {
//        return category;
//    }

    public ImageView getImage_action_delete() {
        return image_action_delete;
    }

    public Notes_label getLabel() {
        return label;
    }
}
