package com.example.dbproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dbproject.data.DBconnections;
import com.example.dbproject.data.LibraryContract.BooksEntry;
import com.example.dbproject.data.LibraryContract.ReaderRequestEntry;
import com.example.dbproject.data.LibraryContract.ReadersEntry;

import java.util.ArrayList;
import java.util.Calendar;

public class ShowDetails extends AppCompatActivity {

    DBconnections dbConnections;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        dbConnections = new DBconnections(this);

        subtract_two_weeks("2020-01-06");
    }

    public void show_books_must_return(View view) {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.details_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        d.show();
        d.getWindow().setAttributes(lp);

        ArrayList<String> listItem = new ArrayList<>();
        ArrayAdapter adapter;

        ListView books_list = d.findViewById(R.id.listView_details_dialog);
        Button ok = d.findViewById(R.id.btn_ok_details_dialog);
        Cursor cursor = display_list(listItem, books_list);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }

    private Cursor display_list(ArrayList listItem, ListView books_list) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String current_date = year + "-" + month + "-" + day;

        SQLiteDatabase db = dbConnections.getReadableDatabase();

        String query = "SELECT * FROM books, readers, reader_request "
                + "WHERE reader_request.book_id = books.ID AND readers.ID = reader_request.reader_id" +
                " AND return_date NOT NULL AND request_date = '" + subtract_two_weeks(current_date) + "';";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            listItem.add(cursor.getString(1));
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
        books_list.setAdapter(adapter);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Books are borrowed", Toast.LENGTH_LONG).show();
        }
        return cursor;
    }

    private String subtract_two_weeks(String d) {
        String first_date[] = d.split("-");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(first_date[0]), Integer.parseInt(first_date[1]) - 1, Integer.parseInt(first_date[2]));
        calendar.add(Calendar.DAY_OF_YEAR, -14);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        System.out.println(year + "-" + month + "-" + day);
        return year + "-" + month + "-" + day;
    }
}
