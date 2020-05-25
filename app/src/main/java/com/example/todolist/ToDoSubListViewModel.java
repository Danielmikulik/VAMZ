package com.example.todolist;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

/**
 * Udržiava dáta (podúlohy) pre UI v SubItemsActivity ako LiveData.
 */
public class ToDoSubListViewModel extends AndroidViewModel {

    private ToDoListRepository repository;

    /**
     * Vytvorí a inicializuje ViewModel.
     * @param application
     */
    public ToDoSubListViewModel(@NonNull Application application) {
        super(application);

        repository = new ToDoListRepository(application);
    }

    /**
     * Vloží podúlohu do databázy.
     * @param item
     */
    public void insertToDoSubItem(ToDoSubItem item) {
        this.repository.insertToDoSubItem(item);
    }

    /**
     * Upraví podúlohu v databáze.
     * @param item
     */
    public void updateToDoSubItem(ToDoSubItem item) {
        this.repository.updateToDoSubItem(item);
    }

    /**
     * Zmaže podúlohu z databázy.
     * @param item
     */
    public void deleteToDoSubItem(ToDoSubItem item) {
        this.repository.deleteToDoSubItem(item);
    }

    /**
     * Vráti všetky podúlohy patriace parent úlohe s daným id.
     * @param parentId id parent úlohy.
     * @return všetky podúlohy patriace parent úlohe s daným id.
     */
    public LiveData<List<ToDoSubItem>> getParentsSubItems(int parentId) {
        return this.repository.getAllParentsSubItems(parentId);
    }

    /**
     * Zmaže všetky podúlohy patriace parent úlohe s daným id.
     * @param parentId id parent úlohy.
     */
    public void deleteAllParentsSubItems(int parentId) {
        this.repository.deleteAllParentsSubItems(parentId);
    }
}
