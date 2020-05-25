package com.example.demo.ui.milepost;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.MilepostRecyclerAdapter;
import com.example.demo.data.model.Milepost;
import com.example.demo.dialog.MilepostEditDialog;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.util.List;

public class MilepostActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MilepostViewModel milepostViewModel;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StringUtil.MILEPOST_UPDATE:
                    milepostViewModel.updateMilepost((Milepost)msg.obj);
                    break;
                case StringUtil.MILEPOST_DELETE:
                    milepostViewModel.statusDeleteMilepost((Milepost)msg.obj);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milepost);
        androidx.appcompat.widget.Toolbar toolbar=(androidx.appcompat.widget.Toolbar) findViewById(R.id.milepost_toolbar);
        setSupportActionBar(toolbar);
        milepostViewModel= ViewModelProviders.of(this).get(MilepostViewModel.class);
        initView();
    }

    private void initView() {
        recyclerView=findViewById(R.id.recycle_view_milepost);
        MilepostRecyclerAdapter adapter=new MilepostRecyclerAdapter(this,handler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        milepostViewModel.getAllMilepostList().observe(this, new Observer<List<Milepost>>() {
            @Override
            public void onChanged(List<Milepost> mileposts) {
                adapter.setAll(mileposts);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                MilepostEditDialog dialog=new MilepostEditDialog(MilepostActivity.this,null);
                dialog.show();
                dialog.getImage_action_sure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Milepost milepost = dialog.getMilepost();
                        if(Tool.testStringNotNULL(milepost.getTitle())){
                            milepostViewModel.insertMilepost(milepost);;
                        }
                        dialog.dismiss();
                    }
                });
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
