package com.example.todolist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Slúži na definovanie operácií v databáze.
 * Komunikuje s dazabázou.
 */
@Dao
public interface ToDoListDao {

    /**
     * Vloží záznam do tabuľky to_do_sub_item_table.
     * @param item
     */
    @Insert
    void insert(ToDoSubItem item);

    /**
     * Upraví záznam v tabuľke to_do_sub_item_table.
     * @param item
     */
    @Update
    void update(ToDoSubItem item);

    /**
     * Zmaže záznam z tabuľky to_do_sub_item_table.
     * @param item
     */
    @Delete
    void delete(ToDoSubItem item);

    /**
     * @param parentId id parent úlohy
     * @return zoznam podúloh patriacich parent úlohe.
     */
    @Query("SELECT * FROM to_do_sub_item_table WHERE parentId = :parentId ORDER BY done")
    LiveData<List<ToDoSubItem>> getAllParentsSubItems(int parentId);

    /**
     * Zmaže všetky záznamy z tabuľky to_do_sub_item_table patriacich parent úlohe.
     * @param parentId parentId
     */
    @Query("DELETE FROM to_do_sub_item_table WHERE parentId = :parentId")
    void deleteAllMySubItems(int parentId);



    /**
     * Vloží záznam do tabuľky to_do_item_table.
     * @param item
     */
    @Insert
    void insert(ToDoItem item);

    /**
     * Upraví záznam v tabuľke to_do_item_table.
     * @param item
     */
    @Update
    void update(ToDoItem item);

    /**
     * Zmaže záznam z tabuľky to_do_item_table.
     * @param item
     */
    @Delete
    void delete(ToDoItem item);

     /**
     * Zmaže všetky záznamy z tabuľky to_do_item_table.
     */
    @Query("DELETE FROM to_do_item_table")
    void deleteAllToDoItems();

    /**
     * @return všetky záznamy z tabuľky to_do_item_table.
     */
    @Query("SELECT * FROM to_do_item_table ORDER BY done, priority")
    LiveData<List<ToDoItem>> getAllItems();
}
