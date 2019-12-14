package com.example.dbproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbproject.data.DBconnections;
import com.example.dbproject.data.LibraryContract;

import java.util.ArrayList;


public class ShowBooks extends AppCompatActivity {

    DBconnections BookDBHelper;
    ListView books_list;
    ArrayList<String> listItem;
    ArrayAdapter adapter;
    Cursor cursor;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_books);
        BookDBHelper = new DBconnections(this);
        listItem = new ArrayList<>();
        db = BookDBHelper.getReadableDatabase();
        books_list = findViewById(R.id.listView_show_books);

        viewData();

        books_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String select_books_query = "SELECT * FROM " + LibraryContract.BooksEntry.TABLE_NAME + " ORDER BY " + LibraryContract.BooksEntry.COLUMN_BOOK_TITLE + " ASC";
                cursor = db.rawQuery(select_books_query, null);
                cursor.moveToPosition(position);
                String info = "رقم الكتاب :" + cursor.getInt(0) + "\n"
                        + "اسم الكتاب : " + cursor.getString(1) + "\n "
                        + "تاريخ النشر :" + cursor.getString(2) + "\n"
                        + "دار النشر : " + cursor.getString(3) + "\n"
                        + "المؤلف : " + cursor.getString(4) + "\n"
                        + "فئة الكتاب : " + cursor.getInt(5) + "\n"
                        + "الفرع :" + cursor.getString(6) + "\n"
                        + "عدد النسخ :" + cursor.getInt(7) + "\n";
                openDialog(info);
                cursor.close();
            }
        });
    }

    private void viewData() {
        SQLiteDatabase db = BookDBHelper.getReadableDatabase();
        String query = "select * from " + LibraryContract.BooksEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0)
            Toast.makeText(this, "No Books until now", Toast.LENGTH_LONG).show();
        else {
            while (cursor.moveToNext()) {
                listItem.add(cursor.getString(1));

            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
            books_list.setAdapter(adapter);
        }
    }

    public void showBooksToAddBooke(View view) {
        startActivity(new Intent(this, NewBookActivity.class));
    }

    private void openDialog(String info) {
        AlertDialog dialog = new AlertDialog.Builder(this).setMessage("معلومات الكتاب : \n\n" + info).show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(20);
    }

}