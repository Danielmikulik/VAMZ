package com.example.todolist;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


/**
 * Fragment, slúžiaci na pridanie novej úlohy. Obsahuje
 * dva EditTexty na zadanie názvu a popisu
 * a dva buttony na pridanie úlohy alebo zrušenie.
 */
public class AddItemFragment extends Fragment {

    private EditText activity;
    private EditText note;
    private RadioGroup importance;
    private Button addButton;
    private Button cancelButton;

    public AddItemFragment() {
        // Required empty public constructor
    }

    /**
     * Vytvorí nový fragment
     * @return nová inštancia fragmentu AddItemFragment.
     */
    public static AddItemFragment newInstance() {
        AddItemFragment fragment = new AddItemFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    /**
     * Vytvorenie UI fragmentu.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);
        this.activity = view.findViewById(R.id.new_item_activity);
        this.note = view.findViewById(R.id.new_item_note);
        this.addButton = view.findViewById(R.id.new_add_button);
        this.cancelButton = view.findViewById(R.id.new_cancel_button);
        this.importance = view.findViewById(R.id.importanceRadioGroup);

        this.activity.requestFocus();

        this.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNewItemBack();
            }
        });
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    /**
     * Povolenie tlačidiel v MainActivity po vrátení späť.
     */
    @Override
    public void onPause() {
        super.onPause();
        MainActivity main = (MainActivity) getActivity();
        main.enableAddButton();
    }

    private void sendNewItemBack() {
        String activity = this.activity.getText().toString();
        if (activity.trim().isEmpty()) {
            Toast toast = Toast.makeText(this.getContext(), "You need to insert activity name.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        String note = this.note.getText().toString();

        int checkedID = importance.getCheckedRadioButtonId();
        if (checkedID == -1) {
            Toast toast = Toast.makeText(this.getContext(), "You need to select importance of new activity.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        this.hideKeyboard();
        View radioButton = importance.findViewById(checkedID);
        int importanceId = importance.indexOfChild(radioButton);

        MainActivity main = (MainActivity) getActivity();
        main.addItem(importanceId, activity, note);
        main.onBackPressed();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }
}
