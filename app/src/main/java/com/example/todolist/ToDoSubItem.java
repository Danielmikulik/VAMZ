package com.example.todolist;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * Predstavuje jednu podúlohu (to do sub item)
 * a záznam v tabuľke databázy (to_do_sub_item_table),
 * ktorá obsahuje referenciu (cudzé kľúč) na záznam tabuľky to do itemov (to_do_item_table).
 * Ak sa vymaže parent úloha, vymažú sa aj všetky jej podúlohy.
 */
@Entity(tableName = "to_do_sub_item_table", foreignKeys = {
        @ForeignKey(entity = ToDoItem.class,
                parentColumns = "id",
                childColumns = "parentId",
                onDelete = ForeignKey.CASCADE)
})

public class ToDoSubItem {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int parentId;
    private String textOfActivity;
    private boolean done;

    /**
     * Vytvorí a inicializuje podúlohu.
     * @param parentId id parent úlohy.
     * @param textOfActivity názov podúlohy.
     */
    public ToDoSubItem(int parentId, String textOfActivity) {
        this.parentId = parentId;
        this.textOfActivity = textOfActivity;
        this.done = false;
    }

    /**
     * @return id podúlohy.
     */
    public int getId() {
        return id;
    }

    /**
     * @return id parent úlohy.
     */
    public int getParentId() {
        return parentId;
    }

    /**
     * @return názov podaktivity.
     */
    public String getTextOfActivity() {
        return textOfActivity;
    }

    /**
     * @return ukončenie podúlohy.
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Nastaví id (primárny kľúč v tabuľke).
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * @param parentId Nastaví id parenta (cudzi kľúč v tabuľke - referencia na parenta).
     */
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    /**
     * nastaví názov aktivity.
     * @param textOfActivity názov aktivity.
     */
    public void setTextOfActivity(String textOfActivity) {
        this.textOfActivity = textOfActivity;
    }

    /**
     * Nastaví ukončenie podúlohy.
     * @param done  ukončenie podúlohy.
     */
    public void setDone(boolean done) {
        this.done = done;
    }

    /**
     * Zmení ukončenie podúlohy na opačnú hodnotu.
     */
    public void makeDone() {
        this.done = !this.done;
    }
}
