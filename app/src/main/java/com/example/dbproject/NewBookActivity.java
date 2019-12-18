package com.example.dbproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbproject.data.DBconnections;
import com.example.dbproject.data.LibraryContract.BookCopiesEntry;
import com.example.dbproject.data.LibraryContract.BooksEntry;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.dbproject.ShowBooks.selected_book_id;
import static com.example.dbproject.ShowBooks.selected_book_title;
import static com.example.dbproject.ShowBooks.selected_book_pub_date;
import static com.example.dbproject.ShowBooks.selected_book_pub_house;
import static com.example.dbproject.ShowBooks.selected_book_author;
import static com.example.dbproject.ShowBooks.selected_book_category;
import static com.example.dbproject.ShowBooks.selected_book_number_of_copies;
import static com.example.dbproject.ShowReaders.selected_reader_id;

public class NewBookActivity extends AppCompatActivity {

    DBconnections bookDBHelper;

    EditText book_title;
    TextView ID;
    Spinner copy_id;
    EditText publish_date;
    DatePickerDialog.OnDateSetListener datePicker;
    EditText publish_house;
    EditText author;
    Spinner branch_id;
    CheckBox reserved_chB;
    EditText category;
    Button save;
    String selected_copy = "1";
    String selected_branch = "1";
    int reserved = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);

        bookDBHelper = new DBconnections(this);
        book_title = (EditText) findViewById(R.id.edtxt_book_name);
        ID = findViewById(R.id.edtxt_book_id);
        copy_id = findViewById(R.id.spinner_new_copy_id);
        publish_date = (EditText) findViewById(R.id.edtxt_publication_date);
        publish_house = (EditText) findViewById(R.id.edtxt_publication_house);
        author = (EditText) findViewById(R.id.edtxt_author_name);
        branch_id = findViewById(R.id.spinner_new_book_branch_id);
        reserved_chB = findViewById(R.id.checkbox_reserved_new_book);
        category = (EditText) findViewById(R.id.edtxt_book_category);
        save = findViewById(R.id.button_book_save);

        setNewID();
        date_picker();

        //put branches values in spinner
        String b_q = "SELECT ID FROM branches";
        SQLiteDatabase db = bookDBHelper.getReadableDatabase();
        Cursor b_cursor = db.rawQuery(b_q, null);
        ArrayList<String> b_list = new ArrayList<>();
        while (b_cursor.moveToNext()) {
            b_list.add(b_cursor.getString(0));
        }
        b_cursor.close();
        ArrayAdapter b_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, b_list);
        b_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch_id.setPrompt("اختر رقم النسخة");
        branch_id.setAdapter(b_adapter);

        if (!selected_book_id.isEmpty()) {
            ID.setText(selected_book_id);
            book_title.setText(selected_book_title);
            publish_date.setText(selected_book_pub_date);
            publish_house.setText(selected_book_pub_house);
            author.setText(selected_book_author);
            category.setText(selected_book_category);

            //get book copies ID
            String q = "SELECT copy_id FROM book_copies WHERE book_id = " + Integer.parseInt(selected_book_id);
            Cursor cursor = db.rawQuery(q, null);
            ArrayList<String> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                list.add(cursor.getString(0));
            }
            cursor.close();
            System.out.println(list);
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            copy_id.setPrompt("اختر رقم النسخة");
            copy_id.setAdapter(adapter);

            ID.setEnabled(false);
        } else {
            copy_id.setEnabled(false);
        }

        // when save button pressed, check if data will be added or updated
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_book_id.isEmpty()) {
                    add_book();
                } else {
                    update_book();
                    Toast.makeText(getApplicationContext(), "تم التعديل", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewBookActivity.this, ShowBooks.class));
                }
            }
        });

        copy_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                selected_copy = text;
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        branch_id.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                selected_branch = text;
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        reserved_chB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reserved_chB.isChecked()) {
                    reserved = 1;
                } else {
                    reserved = 0;
                }
            }
        });
    }

    private void update_book() {
        SQLiteDatabase db = bookDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BooksEntry.COLUMN_BOOK_TITLE, book_title.getText().toString().trim());
        values.put(BooksEntry.COLUMN_BOOK_PUBLICATION_DATE, publish_date.getText().toString().trim());
        values.put(BooksEntry.COLUMN_BOOK_PUBLICATION_HOUSE, publish_house.getText().toString().trim());
        values.put(BooksEntry.COLUMN_BOOK_AUTHOR, author.getText().toString().trim());
        values.put(BooksEntry.COLUMN_BOOK_CATEGORY, category.getText().toString().trim());

        db.update(BooksEntry.TABLE_NAME, values, "ID = ?", new String[]{selected_book_id});

        ContentValues copy_values = new ContentValues();
        copy_values.put(BookCopiesEntry.COLUMN_BOOK_COPIES_BRANCH_ID, Integer.parseInt(selected_branch));
        copy_values.put(BookCopiesEntry.COLUMN_BOOK_COPIES_RESERVED, reserved);

        String u = "UPDATE book_copies SET branch_id = " + Integer.parseInt(selected_branch)
                + " AND reserved = " + reserved + " WHERE book_id = " + Integer.parseInt(selected_book_id)
                + " AND copy_id = " + selected_copy;
        db.execSQL(u);

        ID.setEnabled(true);
        selected_book_id = "";
        selected_book_title = "";
        selected_book_pub_date = "";
        selected_book_pub_house = "";
        selected_book_author = "";
        selected_book_category = "";
        selected_book_number_of_copies = "";
    }

    private void setNewID() {
        DBconnections dbHelper = new DBconnections(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String select_employees_query = "SELECT " + BooksEntry.COLUMN_BOOK_ID + " FROM " + BooksEntry.TABLE_NAME + " ORDER BY " + BooksEntry.COLUMN_BOOK_ID + " ASC";
        Cursor cursor = db.rawQuery(select_employees_query, null);
        if (cursor.getCount() != 0) {
            cursor.moveToLast();
            ID.setText((cursor.getInt(0) + 1) + "");
        } else {
            ID.setText("1000");
        }
        System.out.println("new ID generated");
        cursor.close();
    }


    public void add_book() {
        if (book_title.getText().toString().isEmpty() || ID.getText().toString().isEmpty()
                || publish_date.getText().toString().isEmpty() || publish_house.getText().toString().isEmpty()
                || author.getText().toString().isEmpty()
                || category.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fill all fields, please!", Toast.LENGTH_SHORT).show();
        } else {
            insertData();
            Toast.makeText(getApplicationContext(), "New Book added successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ShowBooks.class));
        }
    }

    private void insertData() {
        SQLiteDatabase db = bookDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BooksEntry.COLUMN_BOOK_TITLE, book_title.getText().toString().trim());
        values.put(BooksEntry.COLUMN_BOOK_PUBLICATION_HOUSE, publish_house.getText().toString().trim());
        values.put(BooksEntry.COLUMN_BOOK_ID, Integer.parseInt(ID.getText().toString().trim()));
        values.put(BooksEntry.COLUMN_BOOK_AUTHOR, author.getText().toString().trim());
        values.put(BooksEntry.COLUMN_BOOK_PUBLICATION_DATE, publish_date.getText().toString().trim());
        values.put(BooksEntry.COLUMN_BOOK_CATEGORY, category.getText().toString().trim());
        values.put(BooksEntry.COLUMN_BOOK_NUMBER_OF_COPIES, 1);

        db.insert(BooksEntry.TABLE_NAME, null, values);

        //add copies to book_copies table
        ContentValues copy_values = new ContentValues();
        copy_values.put(BookCopiesEntry.COLUMN_BOOK_COPIES_BOOK_ID, Integer.parseInt(ID.getText().toString().trim()));
        copy_values.put(BookCopiesEntry.COLUMN_BOOK_COPIES_COPY_ID, Integer.parseInt(selected_copy));
        copy_values.put(BookCopiesEntry.COLUMN_BOOK_COPIES_BRANCH_ID, Integer.parseInt(selected_branch));
        copy_values.put(BookCopiesEntry.COLUMN_BOOK_COPIES_RESERVED, reserved);

        db.insert(BookCopiesEntry.TABLE_NAME, null, copy_values);
    }

    public void cancel_adding_book(View view) {
        startActivity(new Intent(this, ShowBooks.class));
        Toast.makeText(getApplicationContext(), "Nothing added", Toast.LENGTH_SHORT).show();
    }

    private void date_picker() {
        publish_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewBookActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        datePicker,
                        year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String mm = (month + 1) + "";
                String dd = dayOfMonth + "";

                if (mm.length() == 1) {
                    mm = "0" + mm;
                }
                if (dd.length() == 1) {
                    dd = "0" + dd;
                }
                String date = year + "-" + mm + "-" + dd;
                publish_date.setText(date);
            }
        };
    }
}