package com.example.todolist;

import androidx.annotation.NonNull;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Aktivita zobrazuje údaje o danej úlohe {podúlohy, názov, popis}.
 * Podúlohy sú uložené v zozname. Položka sa po posunutí do strany vymaže.
 * Menu s možnosťou vymazať všetky položky (podúlohy).
 * FloatingActionButton, ktorý otvorí fragment, na pridanie novej podúlohy.
 * Dva buttony, ktoré otvoria buttom sheety, kde si môžeme pozrieť a zmeniť
 * celý názov a popis parent úlohy.
 */
public class SubItemsActivity extends AppCompatActivity {
    private int parentId;
    private String parentTextOfActivity;
    private String parentDescription;
    private boolean parentDone;

    private RecyclerView recyclerView;
    private SubItemAdapter recAdapter;
    private Button showActivityTextButton;
    private Button showDescriptionButton;
    private FloatingActionButton addButton;
    private ToDoSubListViewModel toDoSubListViewModel;
    private BottomSheetBehavior descriptionBsb;
    private BottomSheetBehavior activityTextBsb;
    private EditText description;
    private EditText activityText;

    private Intent result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_items);

        setTitle("To do sublist");
        this.getDataFromIntent();

        this.createTextChangedListeners();
        this.createRecyclerView();
        this.createAnimation();

        this.toDoSubListViewModel = new ViewModelProvider(this).get(ToDoSubListViewModel.class);
        this.toDoSubListViewModel.getParentsSubItems(this.parentId).observe(this, new Observer<List<ToDoSubItem>>() {
            @Override
            public void onChanged(List<ToDoSubItem> toDoSubItems) {
                recAdapter.setItems(toDoSubItems);
            }
        });

        this.createTouchHelper();

        this.addButton = findViewById((R.id.addSubItemButton));
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fragment sa otvorí len ak sú oba bottom sheety skryté.
                if (activityTextBsb.getState() == BottomSheetBehavior.STATE_COLLAPSED && descriptionBsb.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    openAddSubItemFragment();
                }
            }
        });

        this.createBottomSheets();
    }

    /**
     * Pridá novú položku (podúlohu) do zoznamu.
     * @param activity
     */
    public void addSubItem(String activity) {
        toDoSubListViewModel.insertToDoSubItem(new ToDoSubItem(this.parentId, activity));
    }

    /**
     * Zapnutie buttonov.
     */
    public void enableAddButtons() {
        this.addButton.setEnabled(true);
        this.showActivityTextButton.setEnabled(true);
        this.showDescriptionButton.setEnabled(true);
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

    private void createTextChangedListeners() {
        this.result = new Intent();
        this.activityText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                //ak zmením názov úlohy, uložím ho do intentu.
                parentTextOfActivity = activityText.getText().toString();
                result.putExtra(ActivityConstants.PARENT_TEXT_OF_ACTIVITY, parentTextOfActivity);
                setResult(RESULT_OK, result);
            }
        });

        this.description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                //ak zmením popis úlohy, uložím ho do intentu.
                parentDescription = description.getText().toString();
                result.putExtra(ActivityConstants.PARENT_DESCRIPTION, parentDescription);
                setResult(RESULT_OK, result);
            }
        });
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        this.parentId = intent.getIntExtra(ActivityConstants.PARENT_ID, -1);
        this.parentDone = intent.getBooleanExtra(ActivityConstants.PARENT_CHECKED, false);
        this.parentTextOfActivity = intent.getStringExtra(ActivityConstants.PARENT_TEXT_OF_ACTIVITY);
        this.activityText = findViewById(R.id.item_text);
        this.activityText.setText(this.parentTextOfActivity);
        this.parentDescription = intent.getStringExtra(ActivityConstants.PARENT_DESCRIPTION);
        this.description = findViewById(R.id.item_desc);
        this.description.setText(this.parentDescription);
    }

    private void createBottomSheets() {
        final View descriptionBottomSheet = findViewById(R.id.bottom_sheet_desc);
        this.descriptionBsb = BottomSheetBehavior.from(descriptionBottomSheet);

        this.showDescriptionButton = findViewById(R.id.desc_button);
        showDescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //otvorí sa len, ak je druhý skrytý.
                if (activityTextBsb.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    descriptionBsb.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        View activityTextBottomSheet = findViewById(R.id.bottom_sheet_activity_text);
        this.activityTextBsb = BottomSheetBehavior.from(activityTextBottomSheet);

        this.showActivityTextButton = findViewById(R.id.item_text_button);
        showActivityTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //otvorí sa len, ak je druhý skrytý.
                if (descriptionBsb.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    activityTextBsb.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
    }

    private void createRecyclerView() {
        this.recyclerView = findViewById(R.id.recViewSubItems);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.recAdapter = new SubItemAdapter();
        this.recyclerView.setAdapter(this.recAdapter);

        this.recAdapter.setOnSubItemClickListener(new SubItemAdapter.OnSubItemClickListener() {

            @Override
            public void onCheckBoxClick(int position) {
                recAdapter.getItems().get(position).makeDone();
                toDoSubListViewModel.updateToDoSubItem(recAdapter.getItems().get(position));

                //po zakliknutí kontrolujem počet zakliknutých voči všetkým. Podľa toho
                //si bude vedieť parent úloha zaškrtnúť, al má všetky splnené alebo odškrtnúť,
                //ak nebudú všetky splnené.
                int checked = 0;
                for (ToDoSubItem item : recAdapter.getItems()) {
                    if (item.isDone()) {
                        checked++;
                    }
                }
                result.putExtra(ActivityConstants.CHECKED_SUB_ITEMS_COUNT, checked);
                result.putExtra(ActivityConstants.SUB_ITEMS_COUNT, recAdapter.getItemCount());
                setResult(RESULT_OK, result);
            }
        });
    }

    private void createAnimation() {
        CoordinatorLayout coordinatorLayout = findViewById(R.id.sub_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) coordinatorLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }

    private void openAddSubItemFragment() {
        AddSubItemFragment addSubItemFragment = AddSubItemFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_right, R.anim.slide_from_right, R.anim.slide_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.add_sub_item_fragment_container, addSubItemFragment, "ADD_SUB_ITEM_FRAGMENT").commit();
        //vypnutie buttonov, aby sa nedali zakliknúť, keď otvorím fragment.
        this.addButton.setEnabled(false);
        this.showActivityTextButton.setEnabled(false);
        this.showDescriptionButton.setEnabled(false);
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
        AlertDialog.Builder alertDialogue = new AlertDialog.Builder(SubItemsActivity.this);
        alertDialogue.setTitle("Delete item").setMessage("Are you sure you want to delete this item?").setIcon(R.drawable.ic_delete);
        alertDialogue.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toDoSubListViewModel.deleteToDoSubItem(recAdapter.getToDoSubItemAt(viewHolder.getAdapterPosition()));
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
        AlertDialog.Builder alertDialogue = new AlertDialog.Builder(SubItemsActivity.this);
        alertDialogue.setTitle("Delete all items").setMessage("Are you sure you want to delete all items?").setIcon(R.drawable.ic_warning);
        alertDialogue.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toDoSubListViewModel.deleteAllParentsSubItems(parentId);
                Toast.makeText(SubItemsActivity.this, "all items deleted", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogue.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        alertDialogue.show();
    }
}
