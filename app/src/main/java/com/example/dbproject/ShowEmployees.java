package com.example.dbproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    public static String selected_employee_id = "";
    public static String selected_employee_first_name = "";
    public static String selected_employee_last_name = "";
    public static String selected_employee_address = "";
    public static String selected_employee_branch_id = "";
    public static String selected_employee_hire_date = "";
    public static String selected_employee_phone = "";
    public static String selected_employee_email = "";
    public static String selected_employee_position = "";

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
                String info = "";
                if (search_edtxt.getText().toString().isEmpty()) {
                    String select_readers_query = "SELECT * FROM " + EmployeesEntry.TABLE_NAME + " ORDER BY " + EmployeesEntry.COLUMN_EMPLOYEE_FIRST_NAME + " ASC";
                    cursor = db.rawQuery(select_readers_query, null);
                    cursor.moveToPosition(position);
                    //Give values to static attributes to use them if edit button clicked
                    selected_employee_id = cursor.getInt(0) + "";
                    selected_employee_first_name = cursor.getString(1);
                    selected_employee_last_name = cursor.getString(2);
                    selected_employee_address = cursor.getString(3);
                    selected_employee_branch_id = cursor.getInt(4) + "";
                    selected_employee_hire_date = cursor.getString(5);
                    selected_employee_email = cursor.getString(6);
                    selected_employee_phone = cursor.getInt(7) + "";
                    selected_employee_position = cursor.getString(8);

                    info = "رقم الموظف: " + selected_employee_id + "\n"
                            + "اسم الموظف: " + selected_employee_first_name + " " + selected_employee_last_name + "\n"
                            + "العنوان: " + selected_employee_address + "\n"
                            + "رقم الفرع: " + selected_employee_branch_id + "\n"
                            + "تاريخ التوظيف: " + selected_employee_hire_date + "\n"
                            + "البريد اﻹلكتروني: " + selected_employee_email + "\n"
                            + "الهاتف: " + selected_employee_phone + "\n"
                            + "المنصب: " + selected_employee_position + "\n";
                } else {
                    String select_readers_query = "SELECT * FROM " + EmployeesEntry.TABLE_NAME + " WHERE " + EmployeesEntry.COLUMN_EMPLOYEE_FIRST_NAME + " || ' '" + " || " + EmployeesEntry.COLUMN_EMPLOYEE_LAST_NAME + " LIKE '%" + search_edtxt.getText().toString() + "%'";
                    cursor = db.rawQuery(select_readers_query, null);
                    cursor.moveToPosition(position);
                    //Give values to static attributes to use them if edit button clicked
                    selected_employee_id = cursor.getInt(0) + "";
                    selected_employee_first_name = cursor.getString(1);
                    selected_employee_last_name = cursor.getString(2);
                    selected_employee_address = cursor.getString(3);
                    selected_employee_branch_id = cursor.getInt(4) + "";
                    selected_employee_hire_date = cursor.getString(5);
                    selected_employee_email = cursor.getString(6);
                    selected_employee_phone = cursor.getInt(7) + "";
                    selected_employee_position = cursor.getString(8);

                    info = "رقم الموظف: " + selected_employee_id + "\n"
                            + "اسم الموظف: " + selected_employee_first_name + " " + selected_employee_last_name + "\n"
                            + "العنوان: " + selected_employee_address + "\n"
                            + "رقم الفرع: " + selected_employee_branch_id + "\n"
                            + "تاريخ التوظيف: " + selected_employee_hire_date + "\n"
                            + "البريد اﻹلكتروني: " + selected_employee_email + "\n"
                            + "الهاتف: " + selected_employee_phone + "\n"
                            + "المنصب: " + selected_employee_position + "\n";
                }
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
                if (!search_edtxt.getText().toString().isEmpty()) {
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
                } else {
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
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.employee_dialog);
        TextView tv_info = dialog.findViewById(R.id.textView_display_employee_info);
        Button btn_remove = dialog.findViewById(R.id.btn_remove_employee);
        Button btn_edit = dialog.findViewById(R.id.btn_edit_employee_info);
        //display data
        tv_info.setText(info);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remove_employee_query = "DELETE FROM " + EmployeesEntry.TABLE_NAME + " WHERE " + EmployeesEntry.ID + " = " + Integer.parseInt(selected_employee_id);
                db.execSQL(remove_employee_query);
                Toast.makeText(getApplicationContext(), "تم حذف الموظف", Toast.LENGTH_SHORT).show();
                listItem.clear();
                viewData();
                dialog.dismiss();
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowEmployees.this, NewEmployeeActivity.class));
            }
        });
        dialog.show();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
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
        });
    }

    public void viewData() {
        String select_employees_query = "SELECT * FROM " + EmployeesEntry.TABLE_NAME + " ORDER BY " + EmployeesEntry.COLUMN_EMPLOYEE_FIRST_NAME + " ASC";
        cursor = db.rawQuery(select_employees_query, null);
        while (cursor.moveToNext()) {
            listItem.add(cursor.getString(1) + " " + cursor.getString(2));
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
        employees_list.setAdapter(adapter);
        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "NO EMPLOYEES", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }
}
