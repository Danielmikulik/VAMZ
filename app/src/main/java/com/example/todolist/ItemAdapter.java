package com.example.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Spája dáta (úlohy) s recyclerView.
 * Extenduje ListAdapter - defaulné animácie pri pridávaní/mazaní
 */
public class ItemAdapter extends ListAdapter<ToDoItem, ItemAdapter.ItemsViewHolder> {

    private OnItemClickListener iListener;

    /**
     * Vytvorenie adaptera.
     */
    public ItemAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<ToDoItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<ToDoItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull ToDoItem oldItem, @NonNull ToDoItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ToDoItem oldItem, @NonNull ToDoItem newItem) {
            return oldItem.isDone() == newItem.isDone() &&
                    oldItem.getImageResource() == newItem.getImageResource() &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getTextOfActivity().equals(newItem.getTextOfActivity());
        }
    };

    /**
     * Vráti item (úlohu) na danej pozícii v recyclerView.
     * @param position pozícia kliknutia.
     * @return item na pozícii.
     */
    public ToDoItem getToDoItemAt(int position) {
        return getItem(position);
    }

    /**
     * Vráti všetky itemy z recyclerView.
     * @return všetky itemy z recyclerView.
     */
    public List<ToDoItem> getItems() {
        return this.getCurrentList();
    }

    /**
     * obsahuje metódy, ktoré sa vykonajú pri kliknutí na checkBox a item (úlohu) celkovo.
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onCheckBoxClick(int position);
    }

    /**
     * Nastavenie onClickListeneru.
     * @param listener
     */
    public void setOnClickListener(OnItemClickListener listener) {
        this.iListener = listener;
    }

    /**
     * Popisuje jeden item (úlohu) v recyclerView.
     */
    public static class ItemsViewHolder extends RecyclerView.ViewHolder {
        private ImageView importanceSign;
        private TextView activityText;
        private TextView note;
        private CheckBox done;

        /**
         * Priradenie listeneru.
         * @param itemView
         * @param listener
         */
        public ItemsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            this.importanceSign = itemView.findViewById(R.id.importanceSign);
            this.activityText = itemView.findViewById(R.id.toDoItem);
            this.note = itemView.findViewById(R.id.note);
            this.done = itemView.findViewById(R.id.done);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            done.setOnClickListener(new View.OnClickListener() {
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
     * Vytvorenie nového ItemHolderu.
     * @param parent
     * @param viewType
     * @return nový ItemHolder.
     */
    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_item, parent, false);
        ItemsViewHolder ivh = new ItemsViewHolder(itemView, this.iListener);
        return ivh;
    }

    /**
     * Pracuje s itemom podľa pozície.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        ToDoItem currentItem = getItem(position);

        holder.importanceSign.setImageResource(currentItem.getImageResource());
        holder.activityText.setText(currentItem.getTextOfActivity());
        holder.note.setText(currentItem.getDescription());
        holder.done.setChecked(currentItem.isDone());
    }
}
