package com.example.dbproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
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

import com.example.dbproject.data.DBconnections;
import com.example.dbproject.data.LibraryContract;
import com.example.dbproject.data.LibraryContract.ReadersEntry;
import com.example.dbproject.data.LibraryContract.SubscriptionsEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ShowReaders extends AppCompatActivity {

    DBconnections dbHelper;
    SQLiteDatabase db;

    ArrayList<String> listItem;
    ArrayAdapter adapter;

    ListView readers_list;
    String select_readers_query;
    Cursor cursor;

    EditText search_edtxt;
    public static String selected_reader_id = "";
    public static String selected_reader_first_name = "";
    public static String selected_reader_last_name = "";
    public static String selected_reader_address = "";
    public static String selected_reader_gender = "";
    public static String selected_reader_date_of_birth = "";
    public static String selected_reader_phone = "";
    public static String selected_reader_sub_date = "";
    public static String selected_reader_sub_status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_readers);
        readers_list = findViewById(R.id.listView_show_readers);
        search_edtxt = findViewById(R.id.edtxt_search_reader);

        dbHelper = new DBconnections(this);
        db = dbHelper.getReadableDatabase();

        listItem = new ArrayList<>();
        viewData();

        readers_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String info = "";
                if (search_edtxt.getText().toString().trim().isEmpty()) {
                    String select_readers_query = "SELECT * FROM " + ReadersEntry.TABLE_NAME + " ORDER BY " + ReadersEntry.COLUMN_READER_FIRST_NAME + " ASC";
                    cursor = db.rawQuery(select_readers_query, null);
                    cursor.moveToPosition(position);
                    selected_reader_id = cursor.getInt(0) + "";
                    selected_reader_first_name = cursor.getString(1);
                    selected_reader_last_name = cursor.getString(2);
                    selected_reader_date_of_birth = cursor.getString(3);
                    selected_reader_address = cursor.getString(4);
                    selected_reader_gender = cursor.getString(5);
                    selected_reader_phone = cursor.getInt(6) + "";
                    selected_reader_sub_status = cursor.getString(7);

                    info = "الرقم: " + selected_reader_id + "\n"
                            + "الاسم: " + selected_reader_first_name + " " + selected_reader_last_name + "\n"
                            + "تاريخ الميلاد: " + selected_reader_date_of_birth + "\n"
                            + "العنوان: " + selected_reader_address + "\n"
                            + "الجنس: " + selected_reader_gender + "\n"
                            + "الهاتف: " + selected_reader_phone + "\n"
                            + "حالة الاشتراك: " + selected_reader_sub_status;
                } else {
                    String select_readers_query = "SELECT * FROM " + ReadersEntry.TABLE_NAME + " WHERE " + ReadersEntry.COLUMN_READER_FIRST_NAME + " || ' '" + " || " + ReadersEntry.COLUMN_READER_LAST_NAME + " LIKE '%" + search_edtxt.getText().toString() + "%'";
                    cursor = db.rawQuery(select_readers_query, null);
                    cursor.moveToPosition(position);
                    selected_reader_id = cursor.getInt(0) + "";
                    selected_reader_first_name = cursor.getString(1);
                    selected_reader_last_name = cursor.getString(2);
                    selected_reader_date_of_birth = cursor.getString(3);
                    selected_reader_address = cursor.getString(4);
                    selected_reader_gender = cursor.getString(5);
                    selected_reader_phone = cursor.getInt(6) + "";
                    selected_reader_sub_status = cursor.getString(7);

                    info = "الرقم: " + selected_reader_id + "\n"
                            + "الاسم: " + selected_reader_first_name + " " + selected_reader_last_name + "\n"
                            + "تاريخ الميلاد: " + selected_reader_date_of_birth + "\n"
                            + "العنوان: " + selected_reader_address + "\n"
                            + "الجنس: " + selected_reader_gender + "\n"
                            + "الهاتف: " + selected_reader_phone + "\n"
                            + "حالة الاشتراك: " + selected_reader_sub_status;
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
                    String select_where = "SELECT * FROM " + ReadersEntry.TABLE_NAME + " WHERE " + ReadersEntry.COLUMN_READER_FIRST_NAME + " || ' '" + " || " + ReadersEntry.COLUMN_READER_LAST_NAME + " LIKE '%" + search_edtxt.getText().toString() + "%'";
                    Cursor search_cursor = db.rawQuery(select_where, null);
                    ArrayList<String> listItem_search = new ArrayList<>();
                    ArrayAdapter adapter_search = new ArrayAdapter(ShowReaders.this, android.R.layout.simple_list_item_1, listItem_search);
                    while (search_cursor.moveToNext()) {
                        listItem_search.add(search_cursor.getString(1) + " " + search_cursor.getString(2));
                    }
                    readers_list.setAdapter(adapter_search);
                    search_cursor.close();
                    System.out.println(search_cursor.getCount() + "\t" + listItem_search.size());
                } else {
                    readers_list.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void openDialog(String info) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.reader_dialog);
        TextView tv_info = dialog.findViewById(R.id.textView_display_reader_info);
        Button btn_remove = dialog.findViewById(R.id.btn_remove_reader);
        Button btn_renew = dialog.findViewById(R.id.btn_renew_reader_sub);
        Button btn_edit = dialog.findViewById(R.id.btn_edit_reader_info);
        if (selected_reader_sub_status.equals("فعال")) {
            btn_renew.setEnabled(false);
        }
        tv_info.setText(info);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remove_reader_query = "DELETE FROM " + ReadersEntry.TABLE_NAME + " WHERE " + ReadersEntry.COLUMN_READER_ID + " = " + Integer.parseInt(selected_reader_id);
                db.execSQL(remove_reader_query);
                Toast.makeText(getApplicationContext(), "تم حذف المشترك", Toast.LENGTH_SHORT).show();
                listItem.clear();
                viewData();
                dialog.dismiss();
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowReaders.this, NewReaderActivity.class));
            }
        });
        btn_renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renew_subscription();
            }
        });
        dialog.show();
        // clear attributes when dialog dismissed
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                selected_reader_id = "";
                selected_reader_first_name = "";
                selected_reader_last_name = "";
                selected_reader_date_of_birth = "";
                selected_reader_gender = "";
                selected_reader_phone = "";
                selected_reader_address = "";
                selected_reader_sub_date = "";
                selected_reader_sub_status = "";
            }
        });
    }

    private void renew_subscription() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_READER_ID, selected_reader_id);
        //Get current date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        values.put(SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_SUB_DATE, df.format(c));
        //Get next year date
        String[] next = df.format(c).split("-");
        next[0] = (Integer.parseInt(next[0]) + 1) + "";
        values.put(SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_END_DATE, next[0] + "-" + next[1] + "-" + next[2]);
        values.put(SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_STATUS, "فعال");
        //Update subscription status in readers table
        ContentValues up_values = new ContentValues();
        up_values.put(ReadersEntry.COLUMN_READER_SUB_STATUS, "فعال");
        database.update(ReadersEntry.TABLE_NAME, up_values, ReadersEntry.COLUMN_READER_ID + " = ?", new String[]{selected_reader_id});
        //Insert values to subscription table
        database.insert(SubscriptionsEntry.TABLE_NAME, null, values);
        //close dialog
        Toast.makeText(getApplicationContext(), "تم تجديد الاشتراك", Toast.LENGTH_SHORT).show();
        //Update listView
        listItem.clear();
        viewData();
    }

    public void to_add_readers(View view) {
        startActivity(new Intent(this, NewReaderActivity.class));
    }

    public void viewData() {
        String select_readers_query = "SELECT * FROM " + ReadersEntry.TABLE_NAME + " ORDER BY " + ReadersEntry.COLUMN_READER_FIRST_NAME + " ASC";
        cursor = db.rawQuery(select_readers_query, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "NO READERS", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                listItem.add(cursor.getString(1) + " " + cursor.getString(2));
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
            readers_list.setAdapter(adapter);
        }
        cursor.close();
    }
}
