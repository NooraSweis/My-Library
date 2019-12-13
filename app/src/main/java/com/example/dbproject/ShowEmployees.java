package com.example.dbproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbproject.data.LibraryContract.EmployeesEntry;
import com.example.dbproject.data.DBconnections;
import com.example.dbproject.lists.Employee;
import com.example.dbproject.lists.EmployeeAdapter;

import java.util.ArrayList;

public class ShowEmployees extends AppCompatActivity {

    DBconnections dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_employees);

        dbHelper = new DBconnections(this);
        db = dbHelper.getReadableDatabase();
    }

    public void showEmployeesToAddEmployee(View view) {
        startActivity(new Intent(this, NewEmployeeActivity.class));
    }

    public void displayEmployeesFromDatabase() {
        String[] projection = {
                EmployeesEntry.ID,
                EmployeesEntry.COLUMN_EMPLOYEE_FIRST_NAME
        };
        Cursor cursor = db.query(
                EmployeesEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        int id_inx = cursor.getColumnIndex(EmployeesEntry.ID);
        int first_name_inx = cursor.getColumnIndex(EmployeesEntry.COLUMN_EMPLOYEE_FIRST_NAME);
        String values = "";
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(id_inx);
                String first_name = cursor.getString(first_name_inx);
                values += (id + " : " + first_name + "\n");
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "an ERROR was catched", Toast.LENGTH_SHORT).show();
        } finally {
            cursor.close();
        }
        System.out.println(values);
    }
}
