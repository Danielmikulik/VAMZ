package com.example.todolist;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * Poskytuje priamy prístup k databáze
 */
@Database(entities = { ToDoItem.class, ToDoSubItem.class}, version = 1)
public abstract class ToDoListRoomDatabase extends RoomDatabase {

    private static ToDoListRoomDatabase database;

    public abstract ToDoListDao toDoItemDao();

    /**
     * Vráti dazabázu. Ak žiadna neexistuje, vytvorí novú a naplní ju defaultnými údajmi.
     * @param context
     * @return inštancia databázy.
     */
    public static synchronized ToDoListRoomDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(),
                    ToDoListRoomDatabase.class, "to_do_item_database.db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback).build();
        }
        return database;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new FillDBAsyncTask(database).execute();
        }
    };

    private static class FillDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private ToDoListDao toDoListDao;

        private FillDBAsyncTask(ToDoListRoomDatabase db) {
            toDoListDao = db.toDoItemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            toDoListDao.insert(new ToDoItem(R.drawable.important, 1, "Spravit VAMZ", "Nechces hadam opakovat"));
            toDoListDao.insert(new ToDoItem(R.drawable.advised, 2, "Upratat doma", "a poriadne"));

            toDoListDao.insert(new ToDoItem(R.drawable.can_wait, 3, "Vyvetrat sa", "aj to je dolezite"));
            toDoListDao.insert(new ToDoSubItem(3, "Zabehat si"));
            toDoListDao.insert(new ToDoSubItem(3, "Ist na bicykel"));
            toDoListDao.insert(new ToDoSubItem(3, "Zahrat futbal"));

            toDoListDao.insert(new ToDoItem(R.drawable.advised, 2, "Urob nakup", "rozklikni a napis si, co mas kupit"));
            toDoListDao.insert(new ToDoSubItem(4, "Chleba"));
            toDoListDao.insert(new ToDoSubItem(4, "Rozky"));
            toDoListDao.insert(new ToDoSubItem(4, "Sunka"));
            toDoListDao.insert(new ToDoSubItem(4, "Slanina"));

            toDoListDao.insert(new ToDoItem(R.drawable.important, 1, "Skusky", "bolo by dobre neopakovať skusky nie? Komu by sa chcelo, robit to buduci rok znova?"));
            toDoListDao.insert(new ToDoSubItem(5, "Diskretna Optimalizacia"));
            toDoListDao.insert(new ToDoSubItem(5, "Algoritmy a Udajove Struktury"));
            toDoListDao.insert(new ToDoSubItem(5, "Pravdepodobnost a Statistika"));
            toDoListDao.insert(new ToDoSubItem(5, "Databazove Systemy"));

            toDoListDao.insert(new ToDoItem(R.drawable.can_wait, 3, "Toto je nazov, kde je vela textu. Vela textu tu je preto, aby sa dalo vidiet, ako funguje moj nested scrollView." +
                    "Samozrejme, ze to nema nijaky vyznam, ale nejako to ukazay treba. A kedze je tu celkom dost miesta, tak musim aj celkom dost pisat, aby vobec bolo co scrollovat." +
                    "Toto je nazov, kde je vela textu. Vela textu tu je preto, aby sa dalo vidiet, ako funguje moj nested scrollView." +
                    "Samozrejme, ze to nema nijaky vyznam, ale nejako to ukazay treba. A kedze je tu celkom dost miesta, tak musim aj celkom dost pisat, aby vobec bolo co scrollovat." +
                    "Toto je nazov, kde je vela textu. Vela textu tu je preto, aby sa dalo vidiet, ako funguje moj nested scrollView." +
                    "Samozrejme, ze to nema nijaky vyznam, ale nejako to ukazay treba. A kedze je tu celkom dost miesta, tak musim aj celkom dost pisat, aby vobec bolo co scrollovat.",
                    "Toto je popis, kde je vela textu. Vela textu tu je preto, aby sa dalo vidiet, ako funguje moj nested scrollView." +
                    "Samozrejme, ze to nema nijaky vyznam, ale nejako to ukazay treba. A kedze je tu celkom dost miesta, tak musim aj celkom dost pisat, aby vobec bolo co scrollovat." +
                    "Toto je popis, kde je vela textu. Vela textu tu je preto, aby sa dalo vidiet, ako funguje moj nested scrollView." +
                    "Samozrejme, ze to nema nijaky vyznam, ale nejako to ukazay treba. A kedze je tu celkom dost miesta, tak musim aj celkom dost pisat, aby vobec bolo co scrollovat." +
                    "Toto je popis, kde je vela textu. Vela textu tu je preto, aby sa dalo vidiet, ako funguje moj nested scrollView." +
                    "Samozrejme, ze to nema nijaky vyznam, ale nejako to ukazay treba. A kedze je tu celkom dost miesta, tak musim aj celkom dost pisat, aby vobec bolo co scrollovat."));


            return null;
        }
    }
}
