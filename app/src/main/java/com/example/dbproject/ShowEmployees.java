package com.example.dbproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dbproject.data.DBconnections;
import com.example.dbproject.data.LibraryContract;

public class ShowEmployees extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_employees);
        //displayDatabaseInfo();
    }

    public void showEmployeesToAddEmployee(View view) {
        startActivity(new Intent(this, NewEmployeeActivity.class));
    }

}
