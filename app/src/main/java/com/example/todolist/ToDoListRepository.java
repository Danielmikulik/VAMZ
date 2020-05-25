package com.example.todolist;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;

/**
 * Vytvára API pre viewModel, pre zjednodušenie používania.
 * Vykonáva akcie na pozadí.
 */
public class ToDoListRepository {
    private static final int INSERT_TO_DO_ITEM = 0;
    private static final int UPDATE_TO_DO_ITEM = 1;
    private static final int DELETE_TO_DO_ITEM = 2;
    private static final int DELETE_ALL_TO_DO_ITEMS = 3;

    private static final int INSERT_TO_DO_SUB_ITEM = 4;
    private static final int UPDATE_TO_DO_SUB_ITEM = 5;
    private static final int DELETE_TO_DO_SUB_ITEM = 6;

    private ToDoListDao toDoListDao;
    private LiveData<List<ToDoItem>> allItems;

    /**
     * Vytvorí a inicializuje nový repozirár.
     * @param application
     */
    public ToDoListRepository(Application application) {
        ToDoListRoomDatabase database = ToDoListRoomDatabase.getInstance(application);
        toDoListDao = database.toDoItemDao();
        allItems = toDoListDao.getAllItems();
    }

    /**
     * Vloží úlohu do databázy.
     * @param item
     */
    public void insertToDoItem(ToDoItem item) {
        new ToDoItemAsyncTask(toDoListDao, INSERT_TO_DO_ITEM).execute(item);
    }

    /**
     * Upraví úlohu v databáze.
     * @param item
     */
    public void updateToDoItem(ToDoItem item) {
        new ToDoItemAsyncTask(toDoListDao, UPDATE_TO_DO_ITEM).execute(item);
    }

    /**
     * Zmaže úlohu z databázy.
     * @param item
     */
    public void deleteToDoItem(ToDoItem item) {
        new ToDoItemAsyncTask(toDoListDao, DELETE_TO_DO_ITEM).execute(item);
    }

    /**
     * Zmaže všetky úlohy z databázy.
     */
    public void deleteAllCheckedItems () {
        new ToDoItemAsyncTask(toDoListDao, DELETE_ALL_TO_DO_ITEMS).execute();
    }

    /**
     * Vráti všetky úlohy z databázy.
     * @return všetky úlohy z databázy.
     */
    public LiveData<List<ToDoItem>> getAllItems() {
        return allItems;
    }


    /**
     * Vloží podúlohu do databázy.
     * @param item
     */
    public void insertToDoSubItem(ToDoSubItem item) {
        new ToDoSubItemAsyncTask(toDoListDao, INSERT_TO_DO_SUB_ITEM).execute(item);
    }

    /**
     * Upraví podúlohu v databáze.
     * @param item
     */
    public void updateToDoSubItem(ToDoSubItem item) {
        new ToDoSubItemAsyncTask(toDoListDao, UPDATE_TO_DO_SUB_ITEM).execute(item);
    }

    /**
     * Zmaže podúlohu z databázy.
     * @param item
     */
    public void deleteToDoSubItem(ToDoSubItem item) {
        new ToDoSubItemAsyncTask(toDoListDao, DELETE_TO_DO_SUB_ITEM).execute(item);
    }

    /**
     * Zmaže všetky podúlohy patriace parent úlohe s daným id.
     * @param parentId id parent úlohy.
     */
    public void deleteAllCheckedParentsSubItems(int parentId) {
        new DeleteCheckedSubItemsAsyncTask(toDoListDao, parentId).execute();
    }

    /**
     * Vráti všetky podúlohy patriace parent úlohe s daným id.
     * @param parentId id parent úlohy.
     * @return všetky podúlohy patriace parent úlohe s daným id.
     */
    public LiveData<List<ToDoSubItem>> getAllParentsSubItems(int parentId) {
        return this.toDoListDao.getAllParentsSubItems(parentId);
    }



    private static class DeleteCheckedSubItemsAsyncTask extends AsyncTask<ToDoSubItem, Void, Void> {
        private ToDoListDao toDoListDao;
        private int parentId;

        private DeleteCheckedSubItemsAsyncTask(ToDoListDao toDoListDao, int parentId) {
            this.toDoListDao = toDoListDao;
            this.parentId = parentId;
        }

        @Override
        protected Void doInBackground(ToDoSubItem... toDoSubItems) {
            toDoListDao.deleteAllMyCheckedSubItems(this.parentId);
            return null;
        }
    }


    private static class ToDoItemAsyncTask extends AsyncTask<ToDoItem, Void, Void> {
        private ToDoListDao toDoListDao;
        private int operation;

        private ToDoItemAsyncTask(ToDoListDao toDoListDao, int operation) {
            this.toDoListDao = toDoListDao;
            this.operation = operation;
        }

        @Override
        protected Void doInBackground(ToDoItem... toDoItems) {
            switch (this.operation) {
                case INSERT_TO_DO_ITEM:
                    toDoListDao.insert(toDoItems[0]);
                    break;
                case UPDATE_TO_DO_ITEM:
                    toDoListDao.update(toDoItems[0]);
                    break;
                case DELETE_TO_DO_ITEM:
                    toDoListDao.delete(toDoItems[0]);
                    break;
                case DELETE_ALL_TO_DO_ITEMS:
                    toDoListDao.deleteAllCheckedToDoItems();
                    break;
            }

            return null;
        }
    }

    private static class ToDoSubItemAsyncTask extends AsyncTask<ToDoSubItem, Void, Void> {
        private ToDoListDao toDoListDao;
        private int operation;

        private ToDoSubItemAsyncTask(ToDoListDao toDoListDao, int operation) {
            this.toDoListDao = toDoListDao;
            this.operation = operation;
        }

        @Override
        protected Void doInBackground(ToDoSubItem... toDoSubItems) {
            switch (this.operation) {
                case INSERT_TO_DO_SUB_ITEM:
                    toDoListDao.insert(toDoSubItems[0]);
                    break;
                case UPDATE_TO_DO_SUB_ITEM:
                    toDoListDao.update(toDoSubItems[0]);
                    break;
                case DELETE_TO_DO_SUB_ITEM:
                    toDoListDao.delete(toDoSubItems[0]);
                    break;
            }

            return null;
        }
    }


}
