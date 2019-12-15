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

import com.example.dbproject.data.LibraryContract.ReadersEntry;
import com.example.dbproject.data.LibraryContract.SubscriptionsEntry;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbproject.data.DBconnections;
import com.example.dbproject.data.LibraryContract;

import static com.example.dbproject.ShowReaders.selected_reader_id;
import static com.example.dbproject.ShowReaders.selected_reader_first_name;
import static com.example.dbproject.ShowReaders.selected_reader_last_name;
import static com.example.dbproject.ShowReaders.selected_reader_address;
import static com.example.dbproject.ShowReaders.selected_reader_date_of_birth;
import static com.example.dbproject.ShowReaders.selected_reader_phone;
import static com.example.dbproject.ShowReaders.selected_reader_gender;
import static com.example.dbproject.ShowReaders.selected_reader_sub_date;

import java.util.Calendar;

public class NewReaderActivity extends AppCompatActivity {

    DBconnections readersDBHelper;

    EditText first_name;
    EditText last_name;
    TextView ID;
    TextView date_of_birth;
    EditText address;
    EditText phone;
    EditText gender;
    TextView sub_date;
    Button save;

    DatePickerDialog.OnDateSetListener datePicker;
    DatePickerDialog.OnDateSetListener datePicker2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reader);

        readersDBHelper = new DBconnections(this);

        first_name = findViewById(R.id.edtxt_reader_first_name);
        last_name = findViewById(R.id.edtxt_reader_last_name);
        ID = findViewById(R.id.edtxt_reader_id);
        date_of_birth = findViewById(R.id.edtxt_reader_date_of_birth);
        address = findViewById(R.id.edtxt_reader_address);
        gender = findViewById(R.id.edtxt_reader_gender);
        phone = findViewById(R.id.edtxt_reader_phone);
        sub_date = findViewById(R.id.edtxt_reader_sub_date);
        save = findViewById(R.id.button_reader_save);

        setNewID();
        datePickers();

        if (!selected_reader_id.isEmpty()) {
            get_sub_date();
            sub_date.setEnabled(false);
            first_name.setText(selected_reader_first_name);
            last_name.setText(selected_reader_last_name);
            ID.setText(selected_reader_id);
            date_of_birth.setText(selected_reader_date_of_birth);
            address.setText(selected_reader_address);
            gender.setText(selected_reader_gender);
            sub_date.setText(selected_reader_sub_date);
            phone.setText(selected_reader_phone);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_reader_id.isEmpty()) {
                    add_reader();
                } else {
                    update_reader();
                    sub_date.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "تم التعديل", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewReaderActivity.this, ShowReaders.class));
                }
            }
        });
    }

    private void get_sub_date() {
        String query = "SELECT " + SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_SUB_DATE +
                " FROM " + SubscriptionsEntry.TABLE_NAME +
                " WHERE " + SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_READER_ID + " = '" + selected_reader_id +
                "' ORDER BY " + SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_SUB_DATE + " ASC;";
        DBconnections dbHelper = new DBconnections(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToLast();
        selected_reader_sub_date = cursor.getString(0);
    }

    private void update_reader() {
        SQLiteDatabase db = readersDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReadersEntry.COLUMN_READER_FIRST_NAME, first_name.getText().toString().trim());
        values.put(ReadersEntry.COLUMN_READER_LAST_NAME, last_name.getText().toString().trim());
        values.put(ReadersEntry.COLUMN_READER_DATE_OF_BIRTH, date_of_birth.getText().toString().trim());
        values.put(ReadersEntry.COLUMN_READER_GENDER, gender.getText().toString().trim());
        values.put(ReadersEntry.COLUMN_READER_ADDRESS, address.getText().toString().trim());
        values.put(ReadersEntry.COLUMN_READER_PHONE, phone.getText().toString().trim());

        db.update(ReadersEntry.TABLE_NAME, values, "ID = ?", new String[]{selected_reader_id});

        System.out.println(selected_reader_id + ":  " + selected_reader_first_name);
        selected_reader_id = "";
        selected_reader_first_name = "";
        selected_reader_last_name = "";
        selected_reader_date_of_birth = "";
        selected_reader_gender = "";
        selected_reader_phone = "";
        selected_reader_address = "";
        selected_reader_sub_date = "";
    }

    private void setNewID() {
        DBconnections dbHelper = new DBconnections(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String select_readers_query = "SELECT " + ReadersEntry.COLUMN_READER_ID + " FROM " + ReadersEntry.TABLE_NAME + " ORDER BY " + LibraryContract.EmployeesEntry.ID + " ASC";
        Cursor cursor = db.rawQuery(select_readers_query, null);
        cursor.moveToLast();
        ID.setText((cursor.getInt(0) + 1) + "");
        cursor.close();
    }

    public void add_reader() {
        if (first_name.getText().toString().isEmpty() || last_name.getText().toString().isEmpty()
                || ID.getText().toString().isEmpty() || date_of_birth.getText().toString().isEmpty()
                || address.getText().toString().isEmpty() || gender.getText().toString().isEmpty()
                || phone.getText().toString().isEmpty() || sub_date.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fill all fields, please!", Toast.LENGTH_SHORT).show();
        } else {
            insertData();
            Toast.makeText(getApplicationContext(), "New reader added successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ShowReaders.class));
        }
    }

    public void cancel_adding_reader(View view) {
        startActivity(new Intent(this, ShowReaders.class));
        Toast.makeText(getApplicationContext(), "Nothing added", Toast.LENGTH_SHORT).show();
    }

    private void insertData() {
        SQLiteDatabase db = readersDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReadersEntry.COLUMN_READER_ID, Integer.parseInt(ID.getText().toString().trim()));
        values.put(ReadersEntry.COLUMN_READER_FIRST_NAME, first_name.getText().toString().trim());
        values.put(ReadersEntry.COLUMN_READER_LAST_NAME, last_name.getText().toString().trim());
        values.put(ReadersEntry.COLUMN_READER_DATE_OF_BIRTH, date_of_birth.getText().toString().trim());
        values.put(ReadersEntry.COLUMN_READER_ADDRESS, address.getText().toString().trim());
        values.put(ReadersEntry.COLUMN_READER_GENDER, gender.getText().toString().trim());
        values.put(ReadersEntry.COLUMN_READER_PHONE, Integer.parseInt(phone.getText().toString().trim()));
        values.put(ReadersEntry.COLUMN_READER_SUB_STATUS, "فعال");

        long newRowID = db.insert(LibraryContract.ReadersEntry.TABLE_NAME, null, values);

        ContentValues sub_values = new ContentValues();
        // Get next year date
        String[] end = sub_date.getText().toString().split("-");
        end[0] = (Integer.parseInt(end[0]) + 1) + "";
        sub_values.put(SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_READER_ID, Integer.parseInt(ID.getText().toString().trim()));
        sub_values.put(SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_SUB_DATE, sub_date.getText().toString().trim());
        sub_values.put(SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_END_DATE, end[0] + "-" + end[1] + "-" + end[2]);
        sub_values.put(SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_STATUS, "فعال");

        long newSubRowID = db.insert(SubscriptionsEntry.TABLE_NAME, null, sub_values);

        if (newRowID == -1 || newSubRowID == -1) {
            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    private void datePickers() {
        date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewReaderActivity.this,
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
                month = month + 1;
                String date = year + "-" + month + "-" + dayOfMonth;
                date_of_birth.setText(date);
            }
        };

        sub_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewReaderActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        datePicker2,
                        year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        datePicker2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "-" + month + "-" + dayOfMonth;
                sub_date.setText(date);
            }
        };
    }

}
