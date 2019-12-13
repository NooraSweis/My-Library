package com.example.dbproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbproject.data.LibraryContract.EmployeesEntry;
import com.example.dbproject.data.DBconnections;

import java.util.ArrayList;

public class ShowEmployees extends AppCompatActivity {

    DBconnections dbHelper;
    SQLiteDatabase db;

    ArrayList<String> listItem;
    ArrayAdapter adapter;

    ListView employees_list;
    String select_employees_query;
    Cursor cursor;

    EditText search_edtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_employees);
        employees_list = findViewById(R.id.listView_show_employees);
        search_edtxt = findViewById(R.id.edtxt_search_employee);

        dbHelper = new DBconnections(this);
        db = dbHelper.getReadableDatabase();

        listItem = new ArrayList<>();
        viewData();

        employees_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                String select_readers_query = "SELECT * FROM " + EmployeesEntry.TABLE_NAME + " ORDER BY " + EmployeesEntry.COLUMN_EMPLOYEE_FIRST_NAME + " ASC";
                cursor = db.rawQuery(select_readers_query, null);
                cursor.moveToPosition(position);
                String info = "رقم الموظف: " + cursor.getInt(0) + "\n"
                        + "اسم الموظف: " + cursor.getString(1) + " " + cursor.getString(2) + "\n"
                        + "العنوان: " + cursor.getString(3) + "\n"
                        + "رقم الفرع: " + cursor.getInt(4) + "\n"
                        + "تاريخ التوظيف: " + cursor.getString(5) + "\n"
                        + "البريد اﻹلكتروني: " + cursor.getString(6) + "\n"
                        + "الهاتف: " + cursor.getInt(7) + "\n"
                        + "المنصب: " + cursor.getString(8) + "\n";
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
                if(!search_edtxt.getText().toString().isEmpty()){
                    String select_where = "SELECT * FROM " + EmployeesEntry.TABLE_NAME + " WHERE " + EmployeesEntry.COLUMN_EMPLOYEE_FIRST_NAME + " || ' '" + " || " + EmployeesEntry.COLUMN_EMPLOYEE_LAST_NAME + " LIKE '%" + search_edtxt.getText().toString() + "%'";
                    Cursor search_cursor = db.rawQuery(select_where, null);
                    ArrayList<String> listItem_search = new ArrayList<>();
                    ArrayAdapter adapter_search = new ArrayAdapter(ShowEmployees.this, android.R.layout.simple_list_item_1, listItem_search);
                    while (search_cursor.moveToNext()) {
                        listItem_search.add(search_cursor.getString(1) + " " + search_cursor.getString(2));
                    }
                    employees_list.setAdapter(adapter_search);
                    search_cursor.close();
                    System.out.println(search_cursor.getCount() + "\t" + listItem_search.size());
                }else{
                    employees_list.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void showEmployeesToAddEmployee(View view) {
        startActivity(new Intent(this, NewEmployeeActivity.class));
    }

    private void openDialog(String info) {
        AlertDialog dialog = new AlertDialog.Builder(this).setMessage("معلومات الموظف: \n\n" + info).show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(20);
    }

    public void viewData() {
        String select_employees_query = "SELECT * FROM " + EmployeesEntry.TABLE_NAME + " ORDER BY " + EmployeesEntry.COLUMN_EMPLOYEE_FIRST_NAME + " ASC";
        cursor = db.rawQuery(select_employees_query, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "NO EMPLOYEES", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                listItem.add(cursor.getString(1) + " " + cursor.getString(2));
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
            employees_list.setAdapter(adapter);
        }
        cursor.close();
    }
}
