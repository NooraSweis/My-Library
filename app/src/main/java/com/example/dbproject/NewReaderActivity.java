package com.example.dbproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbproject.data.DBconnections;
import com.example.dbproject.data.LibraryContract;

public class NewReaderActivity extends AppCompatActivity {

    DBconnections readersDBHelper;

    EditText first_name;
    EditText last_name;
    EditText ID;
    EditText date_of_birth;
    EditText address;
    EditText phone;
    EditText gender;
    EditText sub_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reader);

        readersDBHelper = new DBconnections(this);

        first_name = (EditText) findViewById(R.id.edtxt_reader_first_name);
        last_name = (EditText) findViewById(R.id.edtxt_reader_last_name);
        ID = (EditText) findViewById(R.id.edtxt_reader_id);
        date_of_birth = (EditText) findViewById(R.id.edtxt_reader_date_of_birth);
        address = (EditText) findViewById(R.id.edtxt_reader_address);
        gender = (EditText) findViewById(R.id.edtxt_reader_gender);
        phone = (EditText) findViewById(R.id.edtxt_reader_phone);
        sub_date = (EditText) findViewById(R.id.edtxt_reader_sub_date);
    }

    public void add_reader(View view) {
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
        values.put(LibraryContract.ReadersEntry.COLUMN_READER_ID, Integer.parseInt(ID.getText().toString().trim()));
        values.put(LibraryContract.ReadersEntry.COLUMN_READER_FIRST_NAME, first_name.getText().toString().trim());
        values.put(LibraryContract.ReadersEntry.COLUMN_READER_LAST_NAME, last_name.getText().toString().trim());
        values.put(LibraryContract.ReadersEntry.COLUMN_READER_DATE_OF_BIRTH, date_of_birth.getText().toString().trim());
        values.put(LibraryContract.ReadersEntry.COLUMN_READER_ADDRESS, address.getText().toString().trim());
        values.put(LibraryContract.ReadersEntry.COLUMN_READER_GENDER, gender.getText().toString().trim());
        values.put(LibraryContract.ReadersEntry.COLUMN_READER_PHONE, Integer.parseInt(phone.getText().toString().trim()));
        values.put(LibraryContract.ReadersEntry.COLUMN_READER_SUB_STATUS, "فعال");

        long newRowID = db.insert(LibraryContract.ReadersEntry.TABLE_NAME, null, values);
        Log.v("ReaderActivity", "new row id: " + newRowID);

        ContentValues sub_values = new ContentValues();
        StringBuilder end = new StringBuilder(sub_date.getText().toString().trim());
        int y = Integer.parseInt(end.charAt(3) + "") + 1;
        end.setCharAt(3, Character.forDigit(y, 10));
        sub_values.put(LibraryContract.SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_READER_ID, Integer.parseInt(ID.getText().toString().trim()));
        sub_values.put(LibraryContract.SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_SUB_DATE, sub_date.getText().toString().trim());
        sub_values.put(LibraryContract.SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_END_DATE, end.toString());
        sub_values.put(LibraryContract.SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_STATUS, "فعال");

        long newSubRowID = db.insert(LibraryContract.SubscriptionsEntry.TABLE_NAME, null, sub_values);
        Log.v("SubActivity", "new row id: " + newSubRowID);
    }

}
