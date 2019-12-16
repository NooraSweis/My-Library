package com.example.dbproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.dbproject.data.DBconnections;
import com.example.dbproject.data.LibraryContract.SubscriptionsEntry;
import com.example.dbproject.data.LibraryContract.ReadersEntry;

import java.util.Calendar;

import static com.example.dbproject.ShowBooks.selected_book_id;
import static com.example.dbproject.ShowBooks.selected_book_title;
import static com.example.dbproject.ShowBooks.selected_book_branch_id;
import static com.example.dbproject.ShowBooks.selected_book_number_of_copies;
import static com.example.dbproject.ShowBooks.selected_book_category;
import static com.example.dbproject.ShowBooks.selected_book_pub_house;
import static com.example.dbproject.ShowBooks.selected_book_pub_date;
import static com.example.dbproject.ShowBooks.selected_book_author;
import static com.example.dbproject.ShowEmployees.selected_employee_id;
import static com.example.dbproject.ShowEmployees.selected_employee_first_name;
import static com.example.dbproject.ShowEmployees.selected_employee_last_name;
import static com.example.dbproject.ShowEmployees.selected_employee_address;
import static com.example.dbproject.ShowEmployees.selected_employee_branch_id;
import static com.example.dbproject.ShowEmployees.selected_employee_email;
import static com.example.dbproject.ShowEmployees.selected_employee_hire_date;
import static com.example.dbproject.ShowEmployees.selected_employee_phone;
import static com.example.dbproject.ShowEmployees.selected_employee_position;
import static com.example.dbproject.ShowReaders.selected_reader_id;
import static com.example.dbproject.ShowReaders.selected_reader_sub_date;
import static com.example.dbproject.ShowReaders.selected_reader_gender;
import static com.example.dbproject.ShowReaders.selected_reader_phone;
import static com.example.dbproject.ShowReaders.selected_reader_date_of_birth;
import static com.example.dbproject.ShowReaders.selected_reader_address;
import static com.example.dbproject.ShowReaders.selected_reader_last_name;
import static com.example.dbproject.ShowReaders.selected_reader_first_name;
import static com.example.dbproject.ShowReaders.selected_reader_sub_status;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDatabase();
        check_end_subscriptions();

        if (!selected_book_id.isEmpty() || !selected_employee_id.isEmpty() || !selected_reader_id.isEmpty()) {
            clear_selected_attributes();
        }
    }

    private void check_end_subscriptions() {
        DBconnections dBconnections = new DBconnections(this);
        SQLiteDatabase update_db = dBconnections.getWritableDatabase();
        SQLiteDatabase select_db = dBconnections.getReadableDatabase();

        //get current date
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) +1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String current_date = year + "-" + month + "-" + day;
        System.out.println(current_date);

        String select_query = "SELECT " + SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_READER_ID + " FROM " + SubscriptionsEntry.TABLE_NAME
                + " WHERE " + SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_END_DATE + " = '" + current_date + "';";
        Cursor cursor = select_db.rawQuery(select_query, null);

        String update_query = "UPDATE " + SubscriptionsEntry.TABLE_NAME + " SET " + SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_STATUS + " = 'منتهٍ' "
                + "WHERE " + SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_END_DATE + " = '" + current_date + "';";
        update_db.execSQL(update_query);

        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String update_reader = "UPDATE " + ReadersEntry.TABLE_NAME + " SET " + ReadersEntry.COLUMN_READER_SUB_STATUS
                    + " = 'منتهٍ' " + "WHERE " + ReadersEntry.COLUMN_READER_ID + " = " + id + ";";
            update_db.execSQL(update_reader);
        }
    }

    public void main_to_books(View view) {
        startActivity(new Intent(this, ShowBooks.class));
    }

    public void main_to_readers(View view) {
        startActivity(new Intent(this, ShowReaders.class));
    }

    public void main_to_employees(View view) {
        startActivity(new Intent(this, ShowEmployees.class));
    }

    public void main_to_details(View view) {
        startActivity(new Intent(this, ShowDetails.class));
    }

    private void createDatabase() {
        DBconnections dBconnections = new DBconnections(this);
        SQLiteDatabase db = dBconnections.getReadableDatabase();
    }

    private void clear_selected_attributes() {
        //book data
        selected_book_id = "";
        selected_book_title = "";
        selected_book_branch_id = "";
        selected_book_number_of_copies = "";
        selected_book_category = "";
        selected_book_pub_house = "";
        selected_book_author = "";
        selected_book_pub_date = "";
        //employee_data
        selected_employee_id = "";
        selected_employee_first_name = "";
        selected_employee_last_name = "";
        selected_employee_address = "";
        selected_employee_branch_id = "";
        selected_employee_phone = "";
        selected_employee_position = "";
        selected_employee_email = "";
        selected_employee_hire_date = "";
        //reader data
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
}
