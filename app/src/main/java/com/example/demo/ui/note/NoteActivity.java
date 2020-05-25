package com.example.demo.ui.note;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.demo.R;
import com.example.demo.dialog.LabelEditDialog;
import com.example.demo.ui.note.ui.main.SectionsPagerAdapter;
import com.example.demo.ui.note.ui.main.notes_fragment.edit.NoteEditActivity;
import com.google.android.material.tabs.TabLayout;

public class NoteActivity extends AppCompatActivity {
    ImageView action_add;
    SectionsPagerAdapter sectionsPagerAdapter;
    NoteViewModel noteViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        androidx.appcompat.widget.Toolbar toolbar= findViewById(R.id.note_toolbar);
        setSupportActionBar(toolbar);
        noteViewModel= ViewModelProviders.of(this,new NoteViewModelFactory()).get(NoteViewModel.class);
        noteViewModel.getRepository().setNotesCacheDataSource(this);

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        initView();
    }

    private void initView() {
        action_add=findViewById(R.id.action_add);
        action_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //    sectionsPagerAdapter.isViewFromObject(R.layout.fragment_note,sectionsPagerAdapter)

                if(sectionsPagerAdapter.ifFragmentEquals(sectionsPagerAdapter.getmFragments().get(0), sectionsPagerAdapter.getmCurrentPrimaryItem())){
                    Intent intent=new Intent(NoteActivity.this, NoteEditActivity.class);
                    startActivity(intent);
                }else {
                    final LabelEditDialog dialog=new LabelEditDialog(NoteActivity.this, null);
                    dialog.show();
                    dialog.getAction_sure().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            if(NOTE_LABEL_EDIT_CATEGORY_SAVE==dialog.getCategory())
//                            {
                                String title = dialog.getLabel().getTitle();
                                if(null!=title&&!"".equals(title.trim()))
                                    noteViewModel.insertNoteLabelAsync(dialog.getLabel());
//                            }else{
//                                //TODO:更新label或是在title为“”删除label
//                            }
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
    }
}