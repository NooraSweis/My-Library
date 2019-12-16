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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.dbproject.data.LibraryContract.BranchesEntry;
import com.example.dbproject.data.LibraryContract.EmployeesEntry;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbproject.data.DBconnections;

import java.util.Calendar;

import static com.example.dbproject.ShowEmployees.selected_employee_id;
import static com.example.dbproject.ShowEmployees.selected_employee_first_name;
import static com.example.dbproject.ShowEmployees.selected_employee_last_name;
import static com.example.dbproject.ShowEmployees.selected_employee_address;
import static com.example.dbproject.ShowEmployees.selected_employee_branch_id;
import static com.example.dbproject.ShowEmployees.selected_employee_hire_date;
import static com.example.dbproject.ShowEmployees.selected_employee_phone;
import static com.example.dbproject.ShowEmployees.selected_employee_email;
import static com.example.dbproject.ShowEmployees.selected_employee_position;

public class NewEmployeeActivity extends AppCompatActivity {

    DBconnections employeesDBHelper;

    EditText first_name;
    EditText last_name;
    TextView ID;
    EditText branch_id;
    EditText address;
    EditText email;
    EditText phone;
    TextView hire_date;
    EditText position;
    Button save;

    DatePickerDialog.OnDateSetListener datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_employee);

        employeesDBHelper = new DBconnections(this);

        first_name = findViewById(R.id.edtxt_employee_first_name);
        last_name = findViewById(R.id.edtxt_employee_last_name);
        ID = findViewById(R.id.edtxt_employee_id);
        branch_id = findViewById(R.id.edtxt_employee_branch_id);
        address = findViewById(R.id.edtxt_employee_address);
        email = findViewById(R.id.edtxt_employee_email);
        phone = findViewById(R.id.edtxt_employee_phone);
        hire_date = findViewById(R.id.edtxt_employee_hire_date);
        position = findViewById(R.id.edtxt_employee_position);
        save = findViewById(R.id.button_employee_save);

        setNewID();
        datePickers();

        if (!selected_employee_id.isEmpty()) {
            first_name.setText(selected_employee_first_name);
            last_name.setText(selected_employee_last_name);
            ID.setText(selected_employee_id);
            hire_date.setText(selected_employee_hire_date);
            address.setText(selected_employee_address);
            email.setText(selected_employee_email);
            position.setText(selected_employee_position);
            phone.setText(selected_employee_phone);
            branch_id.setText(selected_employee_branch_id);
        }
        // when save button pressed, check if data will be added or updated
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected_employee_id.isEmpty()) {
                    add_employee();
                } else {
                    update_employee();
                    Toast.makeText(getApplicationContext(), "تم التعديل", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewEmployeeActivity.this, ShowEmployees.class));
                }
            }
        });
    }

    private void update_employee() {
        SQLiteDatabase db = employeesDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EmployeesEntry.COLUMN_EMPLOYEE_FIRST_NAME, first_name.getText().toString().trim());
        values.put(EmployeesEntry.COLUMN_EMPLOYEE_LAST_NAME, last_name.getText().toString().trim());
        values.put(EmployeesEntry.COLUMN_EMPLOYEE_BRANCH_ID, Integer.parseInt(branch_id.getText().toString().trim()));
        values.put(EmployeesEntry.COLUMN_EMPLOYEE_ADDRESS, address.getText().toString().trim());
        values.put(EmployeesEntry.COLUMN_EMPLOYEE_PHONE, Integer.parseInt(phone.getText().toString().trim()));
        values.put(EmployeesEntry.COLUMN_EMPLOYEE_EMAIL, email.getText().toString().trim());
        values.put(EmployeesEntry.COLUMN_EMPLOYEE_POSITION, position.getText().toString().trim());

        db.update(EmployeesEntry.TABLE_NAME, values, "ID = ?", new String[]{selected_employee_id});

        System.out.println(selected_employee_id + ":  " + selected_employee_first_name);
        selected_employee_id = "";
        selected_employee_first_name = "";
        selected_employee_last_name = "";
        selected_employee_address = "";
        selected_employee_branch_id = "";
        selected_employee_phone = "";
        selected_employee_position = "";
        selected_employee_email = "";
        selected_employee_hire_date = "";
    }

    private void setNewID() {
        DBconnections dbHelper = new DBconnections(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String select_employees_query = "SELECT " + EmployeesEntry.ID + " FROM " + EmployeesEntry.TABLE_NAME + " ORDER BY " + EmployeesEntry.ID + " ASC";
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

    public void add_employee() {
        if (first_name.getText().toString().isEmpty() || last_name.getText().toString().isEmpty()
                || ID.getText().toString().isEmpty() || branch_id.getText().toString().isEmpty()
                || address.getText().toString().isEmpty() || email.getText().toString().isEmpty()
                || phone.getText().toString().isEmpty() || hire_date.getText().toString().isEmpty()
                || position.getText().toString().isEmpty()) {
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

    private void datePickers() {
        hire_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewEmployeeActivity.this,
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
                String date = year + "-" + month+1 + "-" + dayOfMonth;
                hire_date.setText(date);
            }
        };
    }
}
