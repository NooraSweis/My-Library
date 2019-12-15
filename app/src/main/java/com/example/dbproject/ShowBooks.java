package com.example.dbproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbproject.data.DBconnections;
import com.example.dbproject.data.LibraryContract.BooksEntry;

import java.util.ArrayList;


public class ShowBooks extends AppCompatActivity {

    DBconnections BookDBHelper;
    SQLiteDatabase db;

    ListView books_list;
    Cursor cursor;

    ArrayList<String> listItem;
    ArrayAdapter adapter;

    EditText search_edtxt;
    public static String selected_book_id = "";
    public static String selected_book_title = "";
    public static String selected_book_pub_date = "";
    public static String selected_book_pub_house = "";
    public static String selected_book_author = "";
    public static String selected_book_category = "";
    public static String selected_book_branch_id = "";
    public static String selected_book_number_of_copies = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_books);
        search_edtxt = findViewById(R.id.edtxt_search_book);
        books_list = findViewById(R.id.listView_show_books);

        BookDBHelper = new DBconnections(this);
        db = BookDBHelper.getReadableDatabase();

        listItem = new ArrayList<>();
        viewData();

        books_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String select_books_query = "SELECT * FROM " + BooksEntry.TABLE_NAME + " ORDER BY " + BooksEntry.COLUMN_BOOK_TITLE + " ASC";
                cursor = db.rawQuery(select_books_query, null);
                cursor.moveToPosition(position);
                selected_book_id = cursor.getInt(0) + "";
                selected_book_title = cursor.getString(1);
                selected_book_pub_date = cursor.getString(2);
                selected_book_pub_house = cursor.getString(3);
                selected_book_author = cursor.getString(4);
                selected_book_category = cursor.getString(5);
                selected_book_branch_id = cursor.getInt(6) + "";
                selected_book_number_of_copies = cursor.getInt(7) + "";

                String info = "رقم الكتاب :" + selected_book_id + "\n"
                        + "اسم الكتاب : " + selected_book_title + "\n "
                        + "تاريخ النشر :" + selected_book_pub_date + "\n"
                        + "دار النشر : " + selected_book_pub_house + "\n"
                        + "المؤلف : " + selected_book_author + "\n"
                        + "فئة الكتاب : " + selected_book_category + "\n"
                        + "الفرع :" + selected_book_branch_id + "\n"
                        + "عدد النسخ :" + selected_book_number_of_copies + "\n";
                System.out.println(info);
                openDialog(info);
                cursor.close();
            }
        });

        search_edtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!search_edtxt.getText().toString().isEmpty()) {
                    String select_where = "SELECT * FROM " + BooksEntry.TABLE_NAME + " WHERE " + BooksEntry.COLUMN_BOOK_TITLE + " LIKE '%" + search_edtxt.getText().toString() + "%'";
                    Cursor search_cursor = db.rawQuery(select_where, null);
                    ArrayList<String> listItem_search = new ArrayList<>();
                    ArrayAdapter adapter_search = new ArrayAdapter(ShowBooks.this, android.R.layout.simple_list_item_1, listItem_search);
                    while (search_cursor.moveToNext()) {
                        listItem_search.add(search_cursor.getString(1));
                    }
                    books_list.setAdapter(adapter_search);
                    search_cursor.close();
                    System.out.println(search_cursor.getCount() + "\t" + listItem_search.size());
                } else {
                    books_list.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void openDialog(String info) {
        final Dialog selected_book_dialog = new Dialog(this);
        selected_book_dialog.setContentView(R.layout.reader_dialog);

        // set the custom dialog components - text, image and button
        TextView tv_ino = (TextView) selected_book_dialog.findViewById(R.id.btn_edit_reader_info);
        tv_ino.setText(info);

        selected_book_dialog.show();
    }

    private void viewData() {
        SQLiteDatabase db = BookDBHelper.getReadableDatabase();
        String query = "select * from " + BooksEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            listItem.add(cursor.getString(1));
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
        books_list.setAdapter(adapter);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Books until now", Toast.LENGTH_LONG).show();
        }
        cursor.close();
    }

    public void showBooksToAddBooks(View view) {
        startActivity(new Intent(this, NewBookActivity.class));
    }

}