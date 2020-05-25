package com.example.todolist;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Predstavuje jednu úlohu (to do item)
 * a záznam v tabuľke databázy (to_do_item_table).
 */
@Entity(tableName = "to_do_item_table")
public class ToDoItem {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int imageResource;
    private int priority;
    private String textOfActivity;
    private String description;
    private boolean done;

    /**
     * Vytvorenie a inicializáčia úlohy.
     * @param imageResource ikona, ktorá vyjadruje dôležitosť úlohy.
     * @param textOfActivity názov úlohy.
     * @param description popis úlohy.
     */
    public ToDoItem(int imageResource, int priority, String textOfActivity, String description){
        this.imageResource = imageResource;
        this.priority = priority;
        this.textOfActivity = textOfActivity;
        this.description = description;
        this.done = false;
    }

    /**
     * Nastaví ikou úlohy.
     * @param imageResource ikona, ktorá vyjadruje dôležitosť úlohy.
     */
    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    /**
     * Nastaví prioritu úlohy.
     * @param priority priorita.
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Nastaví názov úlohy.
     * @param textOfActivity názov úlohy.
     */
    public void setTextOfActivity(String textOfActivity) {
        this.textOfActivity = textOfActivity;
    }

    /**
     * Nastaví popis úlohy.
     * @param description popis úlohy.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Nastaví, ukončenie úlohy.
     * @param done ukončenie úlohy.
     */
    public void setDone(boolean done) {
        this.done = done;
    }

    /**
     * Nastaví id úlohy (primárny kľúč v tabuľke).
     * @param id id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return id úlohy.
     */
    public int getId() {
        return id;
    }

    /**
     * @return čiselná hodnota ikony.
     */
    public int getImageResource() {
        return this.imageResource;
    }

    /**
     * @return priorita úlohy.
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @return text aktivity.
     */
    public String getTextOfActivity() {
        return this.textOfActivity;
    }

    /**
     * @return popis úlohy.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @return ukončenie úlohy.
     */
    public boolean isDone() {
        return this.done;
    }

    /**
     * Zmení ukončenie úlohy na opačnú hodnotu.
     */
    public void makeDone() {
        this.done = !this.done;
    }

}
