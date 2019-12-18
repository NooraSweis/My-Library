package com.example.dbproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dbproject.data.DBconnections;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.dbproject.MainActivity.get_current_date;

public class ShowDetails extends AppCompatActivity {

    DBconnections dbConnections;
    Cursor cursor;
    EditText reader_id_edtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        dbConnections = new DBconnections(this);

        reader_id_edtxt = findViewById(R.id.edtxt_reader_id_details);
    }

    public void show_borrowed_books(View view) {
        String query = "SELECT title, book_id, copy_id, reader_id, first_name || \" \" || last_name FROM readers, books, reader_request WHERE readers.ID = reader_request.reader_id AND books.ID = reader_request.book_id AND return_date IS NULL;";

        ListView list = display_list(query);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                String info = "عنوان الكتاب :" + cursor.getString(0) + "\n"
                        + "رقم الكتاب : " + cursor.getInt(1) + "\n "
                        + "رقم النسخة :" + cursor.getInt(2) + "\n"
                        + "رقم القارئ : " + cursor.getInt(3) + "\n"
                        + "اسم القارئ : " + cursor.getString(4) + "\n";
                openDialog(info);
            }
        });
    }

    public void show_books_must_return(View view) {
        String query = "SELECT title, book_id, copy_id, reader_id, first_name || \" \" || last_name " +
                "FROM readers, books, reader_request " +
                "WHERE readers.ID = reader_request.reader_id AND books.ID = reader_request.book_id " +
                "AND return_date IS NULL AND request_date = '" + subtract_two_weeks(get_current_date()) + "';";

        ListView list = display_list(query);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                String info = "عنوان الكتاب :" + cursor.getString(0) + "\n"
                        + "رقم الكتاب : " + cursor.getInt(1) + "\n "
                        + "رقم النسخة :" + cursor.getInt(2) + "\n"
                        + "رقم القارئ : " + cursor.getInt(3) + "\n"
                        + "اسم القارئ : " + cursor.getString(4) + "\n";
                openDialog(info);
            }
        });
    }

    public void show_late_books(View view) {
        String query = "SELECT title, book_id, copy_id, reader_id, first_name || \" \" || last_name " +
                "FROM readers, books, reader_request " +
                "WHERE readers.ID = reader_request.reader_id AND books.ID = reader_request.book_id " +
                "AND return_date IS NULL AND date(request_date) < date('" + subtract_two_weeks(get_current_date()) + "');";

        ListView list = display_list(query);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                String info = "عنوان الكتاب :" + cursor.getString(0) + "\n"
                        + "رقم الكتاب : " + cursor.getInt(1) + "\n "
                        + "رقم النسخة :" + cursor.getInt(2) + "\n"
                        + "رقم المشترك : " + cursor.getInt(3) + "\n"
                        + "اسم المشترك : " + cursor.getString(4) + "\n";
                openDialog(info);
            }
        });
    }

    public void show_one_reader_books(View view) {
        String query = "SELECT first_name || \" \" || last_name ,count(request_id)" +
                " FROM readers,reader_request" +
                " WHERE readers.ID = reader_request.reader_id AND readers.ID = " + reader_id_edtxt.getText().toString().trim() +
                " GROUP BY reader_id;";
        SQLiteDatabase db = dbConnections.getReadableDatabase();
        String info = "";
        try {
            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();
            info = "اسم المشترك: " + c.getString(0) + "\n"
                    + "عدد الكتب المستعارة: " + c.getInt(1);
        } catch (Exception e) {
            info = "تأكد من رقم المشترك";
        }
        openDialog(info);
    }

    public void show_end_this_month(View view) {
        String query = "SELECT first_name, last_name " +
                "FROM readers, subscriptions " +
                "WHERE readers.ID = subscriptions.reader_id " +
                "AND strftime('%m', end_date) = strftime('%m','now') " +
                "AND strftime('%Y', end_date) = strftime('%Y','now')";

        ListView list = display_list(query);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                String info = cursor.getString(0) + "\n";
                openDialog(info);
            }
        });
    }

    private void openDialog(String info) {
        AlertDialog alertDialog = new AlertDialog.Builder(ShowDetails.this).create();
        alertDialog.setMessage(info);
        alertDialog.show();

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                reader_id_edtxt.setText("");
            }
        });
    }

    private ListView display_list(String query) {

        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.details_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        d.show();
        d.getWindow().setAttributes(lp);

        ListView list = d.findViewById(R.id.listView_details_dialog);
        ArrayList<String> listItem = new ArrayList<>();
        ArrayAdapter adapter;

        Button ok = d.findViewById(R.id.btn_ok_details_dialog);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                cursor.close();
            }
        });

        SQLiteDatabase db = dbConnections.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            listItem.add(cursor.getString(0));
        }
        System.out.println(listItem);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
        list.setAdapter(adapter);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Data", Toast.LENGTH_LONG).show();
        }
        return list;
    }

    private String subtract_two_weeks(String d) {
        String first_date[] = d.split("-");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(first_date[0]), Integer.parseInt(first_date[1]) - 1, Integer.parseInt(first_date[2]));
        calendar.add(Calendar.DAY_OF_YEAR, -14);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String mm = month + "";
        String dd = day + "";

        if (mm.length() == 1) {
            mm = "0" + mm;
        }
        if (dd.length() == 1) {
            dd = "0" + dd;
        }

        System.out.println(year + "-" + month + "-" + day);
        return year + "-" + mm + "-" + dd;
    }
}
