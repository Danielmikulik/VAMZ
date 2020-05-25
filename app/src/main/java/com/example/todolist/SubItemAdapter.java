package com.example.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/**
 * Spája dáta (podúlohy) s recyclerView
 */
public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.SubItemViewHolder> {
    private OnSubItemClickListener mListener;
    private List<ToDoSubItem> items = new ArrayList<>();

    /**
     * obsahuje metódu, ktorá sa vykoná pri kliknutí na checkBox.
     */
    public interface OnSubItemClickListener {
        void onCheckBoxClick(int position);
    }

    /**
     * Nastavenie onSubItemClickListeneru.
     * @param listener
     */
    public void setOnSubItemClickListener(OnSubItemClickListener listener) {
        this.mListener = listener;
    }

    /**
     * Vráti item (podúlohu) na danej pozícii v recyclerView.
     * @param position pozícia kliknutia.
     * @return item na pozícii.
     */
    public ToDoSubItem getToDoSubItemAt(int position) {
        return this.items.get(position);
    }

    /**
     * Vráti všetky itemy z recyclerView.
     * @return všetky itemy z recyclerView.
     */
    public List<ToDoSubItem> getItems() {
        return this.items;
    }

    /**
     * Vráti počet itemov (podúloh) v recyclerView.
     * @return počet itemov (podúloh) v recyclerView.
     */
    @Override
    public int getItemCount() {
        return this.items.size();
    }

    /**
     * Nastavenie zoznamu itemov (podúloh) do recyclerView a upozorní na zmenu.
     * @param items zoznamu itemov (podúloh)
     */
    public void setItems(List<ToDoSubItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    /**
     * Popisuje jeden item (podúlohu) v recyclerView.
     */
    public static class SubItemViewHolder extends RecyclerView.ViewHolder {
        private TextView subItemActivityText;
        private CheckBox subDone;

        /**
         * Priradenie listeneru.
         * @param itemView
         * @param listener
         */
        public SubItemViewHolder(@NonNull View itemView, final SubItemAdapter.OnSubItemClickListener listener) {
            super(itemView);
            this.subItemActivityText = itemView.findViewById(R.id.to_do_sub_item);
            this.subDone = itemView.findViewById(R.id.sub_done);

            subDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onCheckBoxClick(position);
                        }
                    }
                }
            });
        }
    }

    /**
     * Vytvorenie nového SubItemHolderu.
     * @param parent
     * @param viewType
     * @return nový SubItemHolder.
     */
    @NonNull
    @Override
    public SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_sub_item, parent, false);
        SubItemViewHolder svh = new SubItemViewHolder(itemView, this.mListener);
        return svh;
    }

    /**
     * Pracuje s itemom podľa pozície.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull SubItemViewHolder holder, int position) {
        ToDoSubItem currentItem = this.items.get(position);
        holder.subItemActivityText.setText(currentItem.getTextOfActivity());
        holder.subDone.setChecked(currentItem.isDone());
    }
}
