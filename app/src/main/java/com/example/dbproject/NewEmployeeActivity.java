package com.example.dbproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.example.dbproject.data.LibraryContract.EmployeesEntry;
import android.widget.Toast;
import com.example.dbproject.data.DBconnections;

public class NewEmployeeActivity extends AppCompatActivity {

    DBconnections employeesDBHelper;

    EditText first_name;
    EditText last_name;
    EditText ID;
    EditText branch_id;
    EditText address;
    EditText email;
    EditText phone;
    EditText hire_date;
    EditText position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_employee);

        employeesDBHelper = new DBconnections(this);

        first_name = (EditText) findViewById(R.id.edtxt_first_name);
        last_name = (EditText) findViewById(R.id.edtxt_last_name);
        ID = (EditText) findViewById(R.id.edtxt_ID);
        branch_id = (EditText) findViewById(R.id.edtxt_branch_id);
        address = (EditText) findViewById(R.id.edtxt_address);
        email = (EditText) findViewById(R.id.edtxt_email);
        phone = (EditText) findViewById(R.id.edtxt_phone);
        hire_date = (EditText) findViewById(R.id.edtxt_hire_date);
        position = (EditText) findViewById(R.id.edtxt_position);
    }

    public void add_employee(View view) {
        if (first_name.getText().toString().isEmpty() || last_name.getText().toString().isEmpty()
                ||ID.getText().toString().isEmpty() || branch_id.getText().toString().isEmpty()
                ||address.getText().toString().isEmpty() ||email.getText().toString().isEmpty()
                ||phone.getText().toString().isEmpty() ||hire_date.getText().toString().isEmpty()
                ||position.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fill all fields, please!", Toast.LENGTH_SHORT).show();
        } else {
            insertData();
            Toast.makeText(getApplicationContext(), "New employee added successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ShowEmployees.class));
        }
    }

    private void insertData() {
        SQLiteDatabase db = employeesDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EmployeesEntry.COLUMN_EMPLOYEE_FIRST_NAME, first_name.getText().toString().trim());
        values.put(EmployeesEntry.COLUMN_EMPLOYEE_LAST_NAME, last_name.getText().toString().trim());
        values.put(EmployeesEntry.ID, Integer.parseInt(ID.getText().toString().trim()));
        values.put(EmployeesEntry.COLUMN_EMPLOYEE_BRANCH_ID, Integer.parseInt(branch_id.getText().toString().trim()));
        values.put(EmployeesEntry.COLUMN_EMPLOYEE_ADDRESS, address.getText().toString().trim());
        values.put(EmployeesEntry.COLUMN_EMPLOYEE_EMAIL, email.getText().toString().trim());
        values.put(EmployeesEntry.COLUMN_EMPLOYEE_PHONE, Integer.parseInt(phone.getText().toString().trim()));
        values.put(EmployeesEntry.COLUMN_EMPLOYEE_HIRE_DATE, hire_date.getText().toString().trim());
        values.put(EmployeesEntry.COLUMN_EMPLOYEE_POSITION, position.getText().toString().trim());

        long newRowID = db.insert(EmployeesEntry.TABLE_NAME, null, values);
        Log.v("EmployeesActivity", "new row id: " + newRowID);
    }

    public void cancel_adding(View view) {
        startActivity(new Intent(this, ShowEmployees.class));
        Toast.makeText(getApplicationContext(), "Nothing added", Toast.LENGTH_SHORT).show();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        displayDatabaseInfo();
//    }
//
//    private void displayDatabaseInfo() {
//        // To access our database, we instantiate our subclass of SQLiteOpenHelper
//        // and pass the context, which is the current activity.
//        DBconnections mDbHelper = new DBconnections(this);
//
//        // Create and/or open a database to read from it
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//        // Perform this raw SQL query "SELECT * FROM pets"
//        // to get a Cursor that contains all rows from the pets table.
//        Cursor cursor = db.rawQuery("SELECT * FROM " + LibraryContract.EmployeesEntry.TABLE_NAME, null);
//        try {
//            // Display the number of rows in the Cursor (which reflects the number of rows in the
//            // pets table in the database).
//            TextView displayView = (TextView) findViewById(R.id.textView);
//            displayView.setText("Number of rows in pets database table: " + cursor.getCount());
//        } finally {
//            // Always close the cursor when you're done reading from it. This releases all its
//            // resources and makes it invalid.
//            cursor.close();
//        }
//    }
}
