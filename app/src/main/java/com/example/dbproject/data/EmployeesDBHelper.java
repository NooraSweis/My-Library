package com.example.dbproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EmployeesDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "employees.db";

    public EmployeesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_EMPLOYEES_TABLE = "CREATE TABLE " + EmployeesContract.EmployeesEntry.TABLE_NAME + "("
                + EmployeesContract.EmployeesEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EmployeesContract.EmployeesEntry.COLUMN_EMPLOYEE_FIRST_NAME + " TEXT NOT NULL, "
                + EmployeesContract.EmployeesEntry.COLUMN_EMPLOYEE_LAST_NAME + " TEXT NOT NULL, "
                + EmployeesContract.EmployeesEntry.COLUMN_EMPLOYEE_ADDRESS + " TEXT NOT NULL, "
                + EmployeesContract.EmployeesEntry.COLUMN_EMPLOYEE_BRANCH_ID + " INTEGER NOT NULL, "
                + EmployeesContract.EmployeesEntry.COLUMN_EMPLOYEE_HIRE_DATE + " TEXT NOT NULL, "
                + EmployeesContract.EmployeesEntry.COLUMN_EMPLOYEE_EMAIL + " TEXT NOT NULL, "
                + EmployeesContract.EmployeesEntry.COLUMN_EMPLOYEE_PHONE + " INTEGER NOT NULL, "
                + EmployeesContract.EmployeesEntry.COLUMN_EMPLOYEE_POSITION + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_EMPLOYEES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DELETE_EMPLOYEES = "DROP TABLE IF EXISTS " + EmployeesContract.EmployeesEntry.TABLE_NAME + ";";
        db.execSQL(SQL_DELETE_EMPLOYEES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}