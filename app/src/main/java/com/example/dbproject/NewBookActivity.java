package com.example.dbproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbproject.data.DBconnections;
import com.example.dbproject.data.LibraryContract.BooksEntry;

import static com.example.dbproject.ShowBooks.selected_book_id;
import static com.example.dbproject.ShowBooks.selected_book_title;
import static com.example.dbproject.ShowBooks.selected_book_pub_date;
import static com.example.dbproject.ShowBooks.selected_book_pub_house;
import static com.example.dbproject.ShowBooks.selected_book_author;
import static com.example.dbproject.ShowBooks.selected_book_category;
import static com.example.dbproject.ShowBooks.selected_book_branch_id;
import static com.example.dbproject.ShowBooks.selected_book_number_of_copies;

public class NewBookActivity extends AppCompatActivity {

    DBconnections BookDBHelper;

    EditText book_title;
    TextView ID;
    EditText publish_date;
    EditText publish_house;
    EditText author;
    EditText branch_id;
    EditText num_copies;
    EditText category;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);

        BookDBHelper = new DBconnections(this);
        book_title = (EditText) findViewById(R.id.edtxt_book_name);
        ID = findViewById(R.id.edtxt_book_id);
        publish_date = (EditText) findViewById(R.id.edtxt_publication_date);
        publish_house = (EditText) findViewById(R.id.edtxt_publication_house);
        author = (EditText) findViewById(R.id.edtxt_author_name);
        branch_id = (EditText) findViewById(R.id.edtxt_book_branch);
        num_copies = (EditText) findViewById(R.id.edtxt_number_of_copies);
        category = (EditText) findViewById(R.id.edtxt_book_category);
        save = findViewById(R.id.button_book_save);

        setNewID();

        if (!selected_book_id.isEmpty()) {
            ID.setText(selected_book_id);
            book_title.setText(selected_book_title);
            publish_date.setText(selected_book_pub_date);
            publish_house.setText(selected_book_pub_house);
            author.setText(selected_book_author);
            branch_id.setText(selected_book_branch_id);
            num_copies.setText(selected_book_number_of_copies);
            category.setText(selected_book_category);
        }

        // when save button pressed, check if data will be added or updated
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_book_id.isEmpty()) {
                    add_book();
                } else {
                   // update_book();
                    Toast.makeText(getApplicationContext(), "تم التعديل", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewBookActivity.this, ShowBooks.class));
                }
            }
        });
    }

    private void setNewID() {
        DBconnections dbHelper = new DBconnections(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String select_employees_query = "SELECT " + BooksEntry.COLUMN_BOOK_ID + " FROM " + BooksEntry.TABLE_NAME + " ORDER BY " + BooksEntry.COLUMN_BOOK_ID + " ASC";
        Cursor cursor = db.rawQuery(select_employees_query, null);
        if (cursor.getCount() != 0) {
            cursor.moveToLast();
            ID.setText((cursor.getInt(0) + 1) + "");
        }else {
            ID.setText("1000");
        }
        System.out.println("new ID generated");
        cursor.close();
    }


    public void add_book() {
        if (book_title.getText().toString().isEmpty() || ID.getText().toString().isEmpty()
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
        values.put(BooksEntry.COLUMN_BOOK_TITLE, book_title.getText().toString().trim());
        values.put(BooksEntry.COLUMN_BOOK_PUBLICATION_HOUSE, publish_house.getText().toString().trim());
        values.put(BooksEntry.COLUMN_BOOK_ID, Integer.parseInt(ID.getText().toString().trim()));
        values.put(BooksEntry.COLUMN_BOOK_BRANCH_ID, Integer.parseInt(branch_id.getText().toString().trim()));
        values.put(BooksEntry.COLUMN_BOOK_AUTHOR, author.getText().toString().trim());
        values.put(BooksEntry.COLUMN_BOOK_PUBLICATION_DATE, publish_date.getText().toString().trim());
        values.put(BooksEntry.COLUMN_BOOK_NUMBER_OF_COPIES, Integer.parseInt(num_copies.getText().toString().trim()));
        values.put(BooksEntry.COLUMN_BOOK_CATEGORY, category.getText().toString().trim());

        db.insert(BooksEntry.TABLE_NAME, null, values);

        //add copies to book_copies table
    }

    public void cancel_adding_book(View view) {
        startActivity(new Intent(this, ShowBooks.class));
        Toast.makeText(getApplicationContext(), "Nothing added", Toast.LENGTH_SHORT).show();
    }
}