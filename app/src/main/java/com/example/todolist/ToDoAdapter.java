package com.example.todolist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public ToDoAdapter(Context mContext, Cursor mCursor) {
        this.mContext = mContext;
        this.mCursor = mCursor;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.recyclerview, parent, false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
         if(!mCursor.moveToPosition(position)) {
             return;
         }
         String entry = mCursor.getString(mCursor.getColumnIndex(Constants.COLUMN_NAME));
         long id = mCursor.getLong(mCursor.getColumnIndex(Constants._ID));

         holder.txtEntry.setText(entry);
         holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder{
        public TextView txtEntry;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);

            txtEntry = itemView.findViewById(R.id.txtEntry);
        }
    }

    public void swapCursor(Cursor newCursor) {
        if(mCursor!=null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if(mCursor!=null) {
            notifyDataSetChanged();
        }
    }
}
