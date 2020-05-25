package com.example.todolist;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Fragment, slúžiaci na pridanie novej podúlohy. Obsahuje
 * EditText na zadanie názvu a dva buttony
 * na pridanie podúlohy alebo zrušenie.
 */
public class AddSubItemFragment extends Fragment {

    private EditText itemText;
    private Button addButton;
    private Button cancelButton;

    public AddSubItemFragment() {
        // Required empty public constructor
    }

    /**
     * Vytvorí nový fragment
     * @return nová inštancia fragmentu AddItemFragment.
     */
    public static AddSubItemFragment newInstance() {
        AddSubItemFragment fragment = new AddSubItemFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_sub_item, container, false);
        this.addButton = view.findViewById(R.id.new_sub_add_btn);
        this.cancelButton = view.findViewById(R.id.new_sub_cancel_btn);
        this.itemText = view.findViewById(R.id.new_sub_item_activity);
        this.itemText.requestFocus();

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
     * Povolenie tlačidiel v SubItemsActivity po vrátení späť.
     */
    @Override
    public void onPause() {
        super.onPause();
        SubItemsActivity parentActivity = (SubItemsActivity) getActivity();
        parentActivity.enableAddButtons();
    }

    private void sendNewItemBack() {
        String activity = this.itemText.getText().toString();
        if (activity.trim().isEmpty()) {
            Toast toast = Toast.makeText(this.getContext(), "You need to insert activity name.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        this.hideKeyboard();

        SubItemsActivity parentActivity = (SubItemsActivity) getActivity();
        parentActivity.addSubItem(activity);
        parentActivity.onBackPressed();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }
}
