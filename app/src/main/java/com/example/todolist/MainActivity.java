package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText edtEntry;
    private Button btnAdd;
    private RecyclerView recyclerView;
    private ToDoAdapter adapter;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        DbHelper dbHelper = new DbHelper(this);
        database = dbHelper.getWritableDatabase();

        adapter = new ToDoAdapter(this, getAllItems());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                 removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItems();
            }
        });
    }

    private void removeItem(long id) {
        database.delete(Constants.TABLE_NAME,
                Constants._ID + "=" + id, null);
        adapter.swapCursor(getAllItems());
    }

    private void addItems() {

        String entry = edtEntry.getText().toString();
        if(entry.trim().equals("")) {
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(Constants.COLUMN_NAME, entry);

        database.insert(Constants.TABLE_NAME, null, cv);
        adapter.swapCursor(getAllItems());

        edtEntry.getText().clear();
    }

    private void initViews() {
        edtEntry = findViewById(R.id.edtEntry);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
    }

    public Cursor getAllItems() {
        return database.query(
                Constants.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Constants.COLUMN_TIMESTAMP + " DESC"
        );
    }
}
