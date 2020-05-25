package com.example.demo.ui.target;

import android.app.Activity;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.demo.data.model.Aim;
import com.example.demo.data.model.Task;
import com.example.demo.data.repository.TargetRepository;

import java.util.List;

public class TargetViewModel extends ViewModel {

    private TargetRepository targetRepository;
    public TargetViewModel(@NonNull Activity application) {
        super();
        targetRepository=new TargetRepository(application);

    }

    public TargetViewModel(TargetRepository NoteLabelRepository) {
        this.targetRepository = NoteLabelRepository;
    }
    public void setCacheDataSource(Activity activity) {
        targetRepository=TargetRepository.getInstance(activity);
        targetRepository.setCacheDataSource(activity);
    }

    public void addAim(Aim ... aims) {
       targetRepository.insertAim(aims);

    }

    public TargetRepository getTargetRepository() {
        return targetRepository;
    }

    public LiveData<List<Aim>> getAimList() {
        return targetRepository.getAimList();
    }

    public void addTask(Task task, Handler handler) {

        targetRepository.insertTask(task);
       // targetRepository.netSYCNotSycTask(handler, );

    }

    public void updateTask(Task obj) {
        targetRepository.updateTask(obj);
    }

    public void deleteTaskByStatus(Task obj) {
        targetRepository.setStatusDelete(obj);
    }


    public void updateAim(Aim obj) {
        targetRepository.updateAim(obj);
    }

    public void deleteAim(Aim obj) {
        targetRepository.deleteAim(obj);
    }
}