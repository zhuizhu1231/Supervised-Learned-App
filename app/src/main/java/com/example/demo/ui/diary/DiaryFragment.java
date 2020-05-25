package com.example.demo.ui.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.demo.R;
import com.example.demo.ui.diary.model.create.DiaryCreateTodayActivity;
import com.example.demo.ui.diary.model.history.DiaryHistoryActivity;
import com.example.demo.ui.diary.model.search.ScheduleSearchActivity;
import com.example.demo.ui.diary.model.tag.ScheduleTagActivity;

public class DiaryFragment extends Fragment {

    private ImageView diary_new_image,diary_history_image,diary_label_image,diary_search_image;
    private View view;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_diary, container, false);
        m_findViewById();
        setClickListener();
        return view;

    }

    private void setClickListener() {
        diary_new_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),DiaryCreateTodayActivity.class);
                startActivity(intent);
            }
        });
        diary_history_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), DiaryHistoryActivity.class);
                startActivity(intent);
            }
        });

        diary_label_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ScheduleTagActivity.class);
                startActivity(intent);
            }
        });

        diary_search_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ScheduleSearchActivity.class);
                startActivity(intent);
            }
        });
    }

    private void m_findViewById() {
        diary_new_image=view.findViewById(R.id.diary_new_image);
        diary_history_image=view.findViewById(R.id.diary_histiry_image);
        diary_label_image=view.findViewById(R.id.diary_label_image);
        diary_search_image=view.findViewById(R.id.diary_search_image);
    }
}