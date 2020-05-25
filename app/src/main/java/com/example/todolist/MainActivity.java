package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Hlavná aktivita, ktorá sa spustí pri zapnutí aplikácie.
 * Zobrazuje zoznam úloh, ktoré treba vykonať a umožňuje
 * zaškrtnutie už vykonaných úloh. Po kliknutí na danú úlohu
 * sa vytvorí nová aktivita s podúlohami danej úlohy.
 * posunutím úlohy do strany sa úloha vymaže.
 * Obsahuje menu s jednou možnosťou - vymazať všetky položky.
 * Obsahuje FloatingActionButton, ktorý otvorí nový fragment
 * na pridanie úlohy.
 */
public class MainActivity extends AppCompatActivity {

    private int senderPosition;
    private RecyclerView recyclerView;
    private ItemAdapter recAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton addButton;
    private ToDoListViewModel toDoListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.createRecyclerView();
        this.createAnimation();

        this.toDoListViewModel = new ViewModelProvider(this).get(ToDoListViewModel.class);
        this.toDoListViewModel.getToDoItems().observe(this, new Observer<List<ToDoItem>>() {
            @Override
            public void onChanged(List<ToDoItem> toDoItems) {
                recAdapter.submitList(toDoItems);
            }
        });

        this.createTouchHelper();

        this.addButton = findViewById((R.id.addItemButton));
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddItemFragment();
            }
        });
    }

    /**
     * Spracuje výsledok z inej aktivity. Ak je z akttivity podúloh, môžu
     * prísť dáta s upravenými údajmi o názve a popise úlohy a ak sa vykonali
     * všetky podúlohy, nastaví sa úloha ako splnená.
     * @param requestCode kód poslaný pri vytváraní novej aktivity
     * @param resultCode prijatý kód z inej aktivity
     * @param data dáta z inej aktivity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActivityConstants.SUB_ITEMS_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            ToDoItem item = recAdapter.getItems().get(this.senderPosition);
            String textOfActivity = data.getStringExtra(ActivityConstants.PARENT_TEXT_OF_ACTIVITY);

            //Ak sa zmenil názov úlohy v druhej aktivite, tak sa zmení v textView
            if (textOfActivity != null && !textOfActivity.trim().equals("")) {
                item.setTextOfActivity(textOfActivity);
            }

            //Ak sa zmenil popis úlohy v druhej aktivite, tak sa zmení v textView
            String description = data.getStringExtra(ActivityConstants.PARENT_DESCRIPTION);
            if (description != null) {
                item.setDescription(description);
            }

            int checkedSubItemsCount = data.getIntExtra(ActivityConstants.CHECKED_SUB_ITEMS_COUNT, -1);
            int subItemsCount = data.getIntExtra(ActivityConstants.SUB_ITEMS_COUNT, -1);


            if (checkedSubItemsCount != -1 && subItemsCount != -1) {
                //Ak sa zmenil stav podúloh, upraví sa aj parent úloha, ak to je potrebné.
                if ((checkedSubItemsCount == subItemsCount && !item.isDone()) ||
                        (checkedSubItemsCount != subItemsCount && item.isDone())) {
                    item.makeDone();
                }
            }
            toDoListViewModel.updateToDoItem(item);
            recAdapter.notifyItemChanged(this.senderPosition);
        }
    }

    /**
     * Vytvorenie menu.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Spracovanie kliknutí na položky z menu.
     * @param item zakliknutá menu položka
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_items:
                this.deleteAllAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Zapnutie buttonov.
     */
    public void enableAddButton() {
        this.addButton.setEnabled(true);
    }

    /**
     * Pridanie novej úlohy do zoznamu.
     * @param importance
     * @param activity
     * @param note
     */
    public void addItem(int importance, String activity, String note) {
        int icons[] = new int[3];
        icons[0] = R.drawable.important;
        icons[1] = R.drawable.advised;
        icons[2] = R.drawable.can_wait;
        toDoListViewModel.insertToDoItem(new ToDoItem(icons[importance],importance + 1, activity, note));
    }

    private void openAddItemFragment() {
        AddItemFragment addItemFragment = AddItemFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_right, R.anim.slide_from_right, R.anim.slide_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_container, addItemFragment, "addItemFragment").commit();
        this.addButton.setEnabled(false);
    }

    private void itemClicked(int position) {
        this.senderPosition = position;
        ToDoItem parentItem = recAdapter.getItems().get(position);
        Intent intent = new Intent(MainActivity.this, SubItemsActivity.class);
        intent.putExtra(ActivityConstants.PARENT_ID, parentItem.getId());
        intent.putExtra(ActivityConstants.PARENT_TEXT_OF_ACTIVITY, parentItem.getTextOfActivity());
        intent.putExtra(ActivityConstants.PARENT_DESCRIPTION, parentItem.getDescription());
        intent.putExtra(ActivityConstants.PARENT_CHECKED, parentItem.isDone());
        startActivityForResult(intent, ActivityConstants.SUB_ITEMS_ACTIVITY_REQUEST_CODE);
    }

    private void setChecked(int position) {
        recAdapter.getItems().get(position).makeDone();
        toDoListViewModel.updateToDoItem(recAdapter.getItems().get(position));
    }

    private void createAnimation() {
        CoordinatorLayout coordinatorLayout = findViewById(R.id.main_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) coordinatorLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }

    private void createRecyclerView() {
        this.recyclerView = findViewById(R.id.recViewItems);
        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(this);
        this.recAdapter = new ItemAdapter();

        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setAdapter(recAdapter);

        recAdapter.setOnClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                itemClicked(position);
            }

            @Override
            public void onCheckBoxClick(int position) {
                setChecked(position);
            }

        });
    }

    private void createTouchHelper() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteAlert(viewHolder);
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void deleteAlert(final RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder alertDialogue = new AlertDialog.Builder(MainActivity.this);
        alertDialogue.setTitle("Delete item").setMessage("Are you sure you want to delete this item?").setIcon(R.drawable.ic_delete);
        alertDialogue.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toDoListViewModel.deleteToDoItem(recAdapter.getToDoItemAt(viewHolder.getAdapterPosition()));
            }
        });
        alertDialogue.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }
        });
        alertDialogue.setCancelable(false);
        alertDialogue.show();
    }

    private void deleteAllAlert() {
        AlertDialog.Builder alertDialogue = new AlertDialog.Builder(MainActivity.this);
        alertDialogue.setTitle("Delete all checked items").setMessage("Are you sure you want to delete all checked items?").setIcon(R.drawable.ic_warning);
        alertDialogue.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toDoListViewModel.deleteAllCheckedItems();
                Toast.makeText(MainActivity.this, "all checked items deleted", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogue.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        alertDialogue.show();
    }
}
