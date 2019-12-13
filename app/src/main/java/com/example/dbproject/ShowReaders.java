package com.example.dbproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbproject.data.DBconnections;
import com.example.dbproject.data.LibraryContract.ReadersEntry;

import java.util.ArrayList;

public class ShowReaders extends AppCompatActivity {

    DBconnections dbHelper;
    SQLiteDatabase db;

    ArrayList<String> listItem;
    ArrayAdapter adapter;

    ListView readers_list;
    String select_readers_query;
    Cursor cursor;

    EditText search_edtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_readers);
        readers_list = findViewById(R.id.listView_show_readers);
        search_edtxt = findViewById(R.id.edtxt_search_reader);

        dbHelper = new DBconnections(this);
        db = dbHelper.getReadableDatabase();

        listItem = new ArrayList<>();
        viewData();

        readers_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                String select_readers_query = "SELECT * FROM " + ReadersEntry.TABLE_NAME + " ORDER BY " + ReadersEntry.COLUMN_READER_FIRST_NAME + " ASC";
                cursor = db.rawQuery(select_readers_query, null);
                cursor.moveToPosition(position);
                String info = "رقم المشترك: " + cursor.getInt(0) + "\n"
                        + "اسم المشترك: " + cursor.getString(1) + " " + cursor.getString(2) + "\n"
                        + "تاريخ الميلاد: " + cursor.getString(3) + "\n"
                        + "العنوان: " + cursor.getString(4) + "\n"
                        + "الجنس: " + cursor.getString(5) + "\n"
                        + "الهاتف: " + cursor.getInt(6) + "\n"
                        + "حالة الاشتراك: " + cursor.getString(7);
                openDialog(info);
            }
        });
        cursor.close();

        search_edtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!search_edtxt.getText().toString().isEmpty()) {
                    String select_where = "SELECT * FROM " + ReadersEntry.TABLE_NAME + " WHERE " + ReadersEntry.COLUMN_READER_FIRST_NAME + " || ' '" + " || " + ReadersEntry.COLUMN_READER_LAST_NAME + " LIKE '%" + search_edtxt.getText().toString() + "%'";
                    Cursor search_cursor = db.rawQuery(select_where, null);
                    ArrayList<String> listItem_search = new ArrayList<>();
                    ArrayAdapter adapter_search = new ArrayAdapter(ShowReaders.this, android.R.layout.simple_list_item_1, listItem_search);
                    while (search_cursor.moveToNext()) {
                        listItem_search.add(search_cursor.getString(1) + " " + search_cursor.getString(2));
                    }
                    readers_list.setAdapter(adapter_search);
                    search_cursor.close();
                    System.out.println(search_cursor.getCount() + "\t" + listItem_search.size());
                } else {
                    readers_list.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void openDialog(String info) {
        AlertDialog dialog = new AlertDialog.Builder(this).setMessage("معلومات المشترك: \n\n" + info).show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(20);
    }

    public void to_add_readers(View view) {
        startActivity(new Intent(this, NewReaderActivity.class));
    }

    public void viewData() {
        String select_readers_query = "SELECT * FROM " + ReadersEntry.TABLE_NAME + " ORDER BY " + ReadersEntry.COLUMN_READER_FIRST_NAME + " ASC";
        cursor = db.rawQuery(select_readers_query, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "NO READERS", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                listItem.add(cursor.getString(1) + " " + cursor.getString(2));
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
            readers_list.setAdapter(adapter);
        }
        cursor.close();
    }
}
