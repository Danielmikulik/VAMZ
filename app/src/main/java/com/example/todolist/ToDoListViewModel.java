package com.example.todolist;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

/**
 * Udržiava dáta (úlohy) pre UI v MainActivity ako LiveData.
 */
public class ToDoListViewModel extends AndroidViewModel {

    private ToDoListRepository repository;
    private LiveData<List<ToDoItem>> toDoItems;

    /**
     * Vytvorí a inicializuje ViewModel.
     * @param application
     */
    public ToDoListViewModel(@NonNull Application application) {
        super(application);

        repository = new ToDoListRepository(application);
        toDoItems = this.repository.getAllItems();
    }

    /**
     * Vloží úlohu do databázy.
     * @param item
     */
    public void insertToDoItem(ToDoItem item) {
        this.repository.insertToDoItem(item);
    }

    /**
     * Upraví úlohu v databáze.
     * @param item
     */
    public void updateToDoItem(ToDoItem item) {
        this.repository.updateToDoItem(item);
    }

    /**
     * Zmaže úlohu z databázy.
     * @param item
     */
    public void deleteToDoItem(ToDoItem item) {
        this.repository.deleteToDoItem(item);
    }

    /**
     * Zmaže všetky úlohy v databáze.
     */
    public void deleteAllItems() {
        this.repository.deleteAllItems();
    }

    /**
     * Vráti všetky úlohy z databázy.
     * @return
     */
    public LiveData<List<ToDoItem>> getToDoItems() {
        return this.toDoItems;
    }


}
