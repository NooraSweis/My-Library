package com.example.dbproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dbproject.data.DBconnections;
import com.example.dbproject.data.LibraryContract;

public class NewBookActivity extends AppCompatActivity {

    DBconnections BookDBHelper;

    EditText Book_name;
    EditText ID;
    EditText publish_date;
    EditText publish_house;
    EditText author;
    EditText branch_id;
    EditText num_copies;
    EditText category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);

        BookDBHelper = new DBconnections(this);
        Book_name = (EditText) findViewById(R.id.book_name);
        ID = (EditText) findViewById(R.id._ID);
        publish_date = (EditText) findViewById(R.id.publish_date);
        publish_house = (EditText) findViewById(R.id.publish_house);
        author = (EditText) findViewById(R.id.author);
        branch_id = (EditText) findViewById(R.id.branch_id);
        num_copies = (EditText) findViewById(R.id.num_copies);
        category = (EditText) findViewById(R.id.category);
    }


    public void add_book(View view) {
        if (Book_name.getText().toString().isEmpty() || ID.getText().toString().isEmpty()
                || publish_date.getText().toString().isEmpty() || publish_house.getText().toString().isEmpty()
                || author.getText().toString().isEmpty() || branch_id.getText().toString().isEmpty()
                || num_copies.getText().toString().isEmpty() || category.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fill all fields, please!", Toast.LENGTH_SHORT).show();
        } else {
            insertData();
            Toast.makeText(getApplicationContext(), "New Book added successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ShowBooks.class));
        }
    }

    private void insertData() {
        SQLiteDatabase db = BookDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LibraryContract.BooksEntry.COLUMN_BOOK_TITLE, Book_name.getText().toString().trim());
        values.put(LibraryContract.BooksEntry.COLUMN_BOOK_PUBLICATION_HOUSE, publish_house.getText().toString().trim());
        values.put(LibraryContract.BooksEntry.COLUMN_BOOK_ID, Integer.parseInt(ID.getText().toString().trim()));
        values.put(LibraryContract.BooksEntry.COLUMN_BOOK_BRANCH_ID, Integer.parseInt(branch_id.getText().toString().trim()));
        values.put(LibraryContract.BooksEntry.COLUMN_BOOK_AUTHOR, author.getText().toString().trim());
        values.put(LibraryContract.BooksEntry.COLUMN_BOOK_PUBLICATION_DATE, publish_date.getText().toString().trim());
        values.put(LibraryContract.BooksEntry.COLUMN_BOOK_NUMBER_OF_COPIES, Integer.parseInt(num_copies.getText().toString().trim()));
        values.put(LibraryContract.BooksEntry.COLUMN_BOOK_CATEGORY, category.getText().toString().trim());

        long newRowID = db.insert(LibraryContract.BooksEntry.TABLE_NAME, null, values);
        Log.v("BooksActivity", "new row id: " + newRowID);
    }

    public void cancel_book(View view) {
        startActivity(new Intent(this, ShowBooks.class));
        Toast.makeText(getApplicationContext(), "Nothing added", Toast.LENGTH_SHORT).show();
    }
}