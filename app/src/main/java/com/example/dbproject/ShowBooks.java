package com.example.dbproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbproject.data.DBconnections;
import com.example.dbproject.data.LibraryContract.ReadersEntry;
import com.example.dbproject.data.LibraryContract.ReaderRequestEntry;
import com.example.dbproject.data.LibraryContract.SubscriptionsEntry;
import com.example.dbproject.data.LibraryContract.BookCopiesEntry;
import com.example.dbproject.data.LibraryContract.BooksEntry;

import java.util.ArrayList;
import java.util.Calendar;


public class ShowBooks extends AppCompatActivity {

    DBconnections bookDBHelper;
    SQLiteDatabase db;

    ListView books_list;
    Cursor cursor;

    ArrayList<String> listItem;
    ArrayAdapter adapter;

    EditText search_edtxt;
    public static String selected_book_id = "";
    public static String selected_book_title = "";
    public static String selected_book_pub_date = "";
    public static String selected_book_pub_house = "";
    public static String selected_book_author = "";
    public static String selected_book_category = "";
    public static String selected_book_branch_id = "";
    public static String selected_book_number_of_copies = "";
    int reserved = 0;
    int book_id = 0;
    public int b = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_books);
        search_edtxt = findViewById(R.id.edtxt_search_book);
        books_list = findViewById(R.id.listView_show_books);

        bookDBHelper = new DBconnections(this);
        db = bookDBHelper.getReadableDatabase();

        listItem = new ArrayList<>();
        viewData();

        books_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String select_books_query = "SELECT * FROM " + BooksEntry.TABLE_NAME + " ORDER BY " + BooksEntry.COLUMN_BOOK_TITLE + " ASC";
                cursor = db.rawQuery(select_books_query, null);
                cursor.moveToPosition(position);
                selected_book_id = cursor.getInt(0) + "";
                selected_book_title = cursor.getString(1);
                selected_book_pub_date = cursor.getString(2);
                selected_book_pub_house = cursor.getString(3);
                selected_book_author = cursor.getString(4);
                selected_book_category = cursor.getString(5);
                selected_book_number_of_copies = cursor.getInt(6) + "";

                String info = "رقم الكتاب :" + selected_book_id + "\n"
                        + "اسم الكتاب : " + selected_book_title + "\n "
                        + "تاريخ النشر :" + selected_book_pub_date + "\n"
                        + "دار النشر : " + selected_book_pub_house + "\n"
                        + "المؤلف : " + selected_book_author + "\n"
                        + "فئة الكتاب : " + selected_book_category + "\n"
                        + "عدد النسخ المتوفرة للاستعارة :" +
                        (Integer.parseInt(selected_book_number_of_copies) - get_number_of_borrowed_copies() -
                                get_number_of_reserved_copies()) + "\n";
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
                    String select_where = "SELECT * FROM " + BooksEntry.TABLE_NAME + " WHERE " + BooksEntry.COLUMN_BOOK_TITLE + " || " + BooksEntry.COLUMN_BOOK_AUTHOR + " LIKE '%" + search_edtxt.getText().toString() + "%' ORDER BY " + BooksEntry.COLUMN_BOOK_TITLE + " ASC;";
                    Cursor search_cursor = db.rawQuery(select_where, null);
                    ArrayList<String> listItem_search = new ArrayList<>();
                    ArrayAdapter adapter_search = new ArrayAdapter(ShowBooks.this, android.R.layout.simple_list_item_1, listItem_search);
                    while (search_cursor.moveToNext()) {
                        listItem_search.add(search_cursor.getString(1));
                    }
                    books_list.setAdapter(adapter_search);
                    search_cursor.close();
                    System.out.println(search_cursor.getCount() + "\t" + listItem_search.size());
                } else {
                    books_list.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private int get_number_of_reserved_copies() {
        String query = "SELECT count(" + BookCopiesEntry.COLUMN_BOOK_COPIES_BOOK_ID
                + ") FROM " + BookCopiesEntry.TABLE_NAME
                + " WHERE " + BookCopiesEntry.COLUMN_BOOK_COPIES_BOOK_ID + " = " + Integer.parseInt(selected_book_id)
                + " AND " + BookCopiesEntry.COLUMN_BOOK_COPIES_RESERVED + " = 1";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        System.out.println(cursor.getInt(0));
        return cursor.getInt(0);
    }

    private int get_number_of_borrowed_copies() {
        String query = "SELECT count(" + ReaderRequestEntry.COLUMN_Request_BOOK_ID
                + ") FROM " + ReaderRequestEntry.TABLE_NAME
                + " WHERE " + ReaderRequestEntry.COLUMN_Request_BOOK_ID + " = " + Integer.parseInt(selected_book_id)
                + " AND " + ReaderRequestEntry.COLUMN_Request_RETURN_DATE + " IS NULL ";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        System.out.println(cursor.getInt(0));
        return cursor.getInt(0);
    }

    private void openDialog(String info) {
        final Dialog selected_book_dialog = new Dialog(this);
        selected_book_dialog.setContentView(R.layout.book_dialog);

        // set the custom dialog components - text, image and button
        TextView tv_ino = (TextView) selected_book_dialog.findViewById(R.id.textView_display_book_info);
        tv_ino.setText(info);
        Button edit_book = selected_book_dialog.findViewById(R.id.btn_edit_book_info);
        Button borrow = selected_book_dialog.findViewById(R.id.btn_book_borrow);
        Button add_copy = selected_book_dialog.findViewById(R.id.btn_add_book_copy);
        Button remove_copy = selected_book_dialog.findViewById(R.id.btn_remove_book_copy);
        Button return_copy = selected_book_dialog.findViewById(R.id.btn_book_return);

        if (Integer.parseInt(selected_book_number_of_copies) - get_number_of_borrowed_copies() - get_number_of_reserved_copies() <= 0) {
            borrow.setEnabled(false);
        }
        if (Integer.parseInt(selected_book_number_of_copies) - get_number_of_borrowed_copies() <= 0) {
            remove_copy.setEnabled(false);
        }

        edit_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowBooks.this, NewBookActivity.class));
            }
        });

        add_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_new_copy(selected_book_dialog);
                selected_book_dialog.dismiss();
            }
        });

        remove_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove_one_copy(selected_book_dialog);
            }
        });

        borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_borrow_dialog(selected_book_dialog);
            }
        });

        return_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(ShowBooks.this);
                // رح نستخدم نفس ديالوج الحذف لأنه ما بدنا غير رقم النسخة
                d.setContentView(R.layout.remove_book_dialog);
                final EditText copy_id = d.findViewById(R.id.edtxt_remove_copy_id);
                Button ok = d.findViewById(R.id.btn_remove_ok);
                d.show();
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (copy_is_borrowed(copy_id.getText().toString().trim())) {
                            String query = "UPDATE reader_request SET return_date = '" + getCurrentDate() + "' WHERE book_id = "
                                    + selected_book_id + " AND copy_id = " + copy_id.getText().toString().trim();
                            SQLiteDatabase database = bookDBHelper.getWritableDatabase();
                            database.execSQL(query);
                            Toast.makeText(getApplicationContext(), "تمت إعادة الكتاب", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "تأكد من الرقم المدخل", Toast.LENGTH_LONG).show();
                        }
                        d.dismiss();
                        selected_book_dialog.dismiss();
                    }
                });
            }
        });

        selected_book_dialog.show();

        // clear attributes when dialog dismissed
        selected_book_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                selected_book_id = "";
                selected_book_title = "";
                selected_book_number_of_copies = "";
                selected_book_category = "";
                selected_book_pub_house = "";
                selected_book_author = "";
                selected_book_pub_date = "";
            }
        });
    }

    private void open_borrow_dialog(final Dialog selected_book_dialog) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.borrow_dialog);
        final EditText reader_id = dialog.findViewById(R.id.edtxt_reader_borrowing_id);
        final EditText copy_id = dialog.findViewById(R.id.edtxt_borrowed_copy_id);
        final EditText res_employee = dialog.findViewById(R.id.edtxt_responsible_employee);
        Button ok = dialog.findViewById(R.id.btn_borrow_ok);

        dialog.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (copy_available(Integer.parseInt(copy_id.getText().toString().trim()))) {
                    insert_into_reader_request(reader_id.getText().toString().trim(),
                            copy_id.getText().toString().trim(), res_employee.getText().toString().trim(), selected_book_dialog);
                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "something wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void insert_into_reader_request(String reader_id, String copy_id, String res_employee, Dialog selected_book_dialog) {
        SQLiteDatabase database = bookDBHelper.getWritableDatabase();
        //get current date
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        ContentValues values = new ContentValues();
        values.put(ReaderRequestEntry.COLUMN_Request_READER_ID, Integer.parseInt(reader_id));
        values.put(ReaderRequestEntry.COLUMN_Request_COPY_ID, Integer.parseInt(copy_id));
        values.put(ReaderRequestEntry.COLUMN_Request_RES_EMPLOYEE, Integer.parseInt(res_employee));
        values.put(ReaderRequestEntry.COLUMN_Request_BOOK_ID, Integer.parseInt(selected_book_id));
        values.put(ReaderRequestEntry.COLUMN_Request_DATE, year + "-" + month + "-" + day);

        if (reader_sub_ended(reader_id)) {
            Toast.makeText(getApplicationContext(), "يجب تجديد الاشتراك", Toast.LENGTH_LONG).show();
            return;
        }

        if (copy_available(Integer.parseInt(copy_id))) {
            try {
                database.insert(ReaderRequestEntry.TABLE_NAME, null, values);
                Toast.makeText(getApplicationContext(), "تمت استعارة الكتاب", Toast.LENGTH_LONG).show();
                selected_book_dialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "حدث خطأ ما", Toast.LENGTH_LONG).show();
                System.out.println("EXC OCCUR");
            }
        } else {
            Toast.makeText(getApplicationContext(), "الكتاب غير متوفر", Toast.LENGTH_LONG).show();
        }
    }

    private boolean reader_sub_ended(String id) {
        String query = "SELECT " + ReadersEntry.COLUMN_READER_SUB_STATUS + " FROM " + ReadersEntry.TABLE_NAME
                + " WHERE " + ReadersEntry.COLUMN_READER_ID + " = " + id;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0 || cursor.getString(0).equals("منتهٍ")) {
            return true;
        }
        return false;
    }

    private boolean copy_available(int copy_id) {
        String query = "SELECT " + ReaderRequestEntry.COLUMN_Request_COPY_ID + " FROM " + ReaderRequestEntry.TABLE_NAME
                + " WHERE " + ReaderRequestEntry.COLUMN_Request_BOOK_ID + " = " + selected_book_id
                + " AND " + ReaderRequestEntry.COLUMN_Request_COPY_ID + " = " + copy_id
                + " AND " + ReaderRequestEntry.COLUMN_Request_RETURN_DATE + " IS NULL";
        Cursor cursor = db.rawQuery(query, null);

        String q2 = "SELECT copy_id FROM book_copies WHERE book_id = " + Integer.parseInt(selected_book_id)
                + " AND copy_id = " + copy_id + " AND reserved = 0";
        Cursor c2 = db.rawQuery(q2, null);

        if (cursor.getCount() == 0 || c2.getCount() != 0) {
            System.out.println("COPY AVAILABLE");
            return true;
        }
        System.out.println("COPY NOT AVAILABLE");
        return false;
    }


    private void remove_one_copy(final Dialog selected_book_dialog) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.remove_book_dialog);
        final EditText copy_id = dialog.findViewById(R.id.edtxt_remove_copy_id);
        Button ok = dialog.findViewById(R.id.btn_remove_ok);
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (copy_exists(copy_id.getText().toString().trim())) {
                    SQLiteDatabase database = bookDBHelper.getWritableDatabase();
                    String delete_query = "DELETE FROM " + BookCopiesEntry.TABLE_NAME + " WHERE " + BookCopiesEntry.COLUMN_BOOK_COPIES_BOOK_ID
                            + " = " + Integer.parseInt(selected_book_id) + " AND " + BookCopiesEntry.COLUMN_BOOK_COPIES_COPY_ID
                            + " = " + Integer.parseInt(copy_id.getText().toString().trim()) + ";";
                    database.execSQL(delete_query);
                    selected_book_number_of_copies = (Integer.parseInt(selected_book_number_of_copies) - 1) + "";
                    String update_query = "UPDATE " + BooksEntry.TABLE_NAME + " SET " + BooksEntry.COLUMN_BOOK_NUMBER_OF_COPIES
                            + " = " + Integer.parseInt(selected_book_number_of_copies) + ";";
                    database.execSQL(update_query);
                    Toast.makeText(getApplicationContext(), "تم حذف نسخة", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "النسخة غير موجودة", Toast.LENGTH_LONG).show();
                }
                selected_book_dialog.dismiss();
                dialog.dismiss();
            }
        });
    }

    private boolean copy_exists(String copy_id) {
        String query = "SELECT copy_id FROM book_copies WHERE book_id = " + selected_book_id + " AND copy_id = " + copy_id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            return false;
        }
        return true;
    }

    private void add_new_copy(Dialog dialog) {
        // 1) update number of copies in books table
        add_one_to_number_of_copies();
        // 2) ask if new copy is reserved or not
        book_id = Integer.parseInt(selected_book_id);
//        dialog.dismiss();
        open_reserved_dialog();
        // 3) add new copy to the book_copies table
    }

    private void viewData() {
        SQLiteDatabase db = bookDBHelper.getReadableDatabase();
        String query = "select * from " + BooksEntry.TABLE_NAME + " ORDER BY " + BooksEntry.COLUMN_BOOK_TITLE + " ASC;";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            listItem.add(cursor.getString(1));
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
        books_list.setAdapter(adapter);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Books until now", Toast.LENGTH_LONG).show();
        }
        cursor.close();
    }

    public void showBooksToAddBooks(View view) {
        startActivity(new Intent(this, NewBookActivity.class));
    }

    private void open_reserved_dialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.reserved_dialog);
        final Spinner branch = dialog.findViewById(R.id.edtxt_new_copy_branch_id);
        final CheckBox reserved_chB = dialog.findViewById(R.id.checkbox_reserved);
        Button ok = dialog.findViewById(R.id.btn_ok_reserved_dialog);

        //put branches values in spinner
        String b_q = "SELECT ID FROM branches";
        SQLiteDatabase db = bookDBHelper.getReadableDatabase();
        Cursor b_cursor = db.rawQuery(b_q, null);
        ArrayList<String> b_list = new ArrayList<>();
        while (b_cursor.moveToNext()) {
            b_list.add(b_cursor.getString(0));
        }
        b_cursor.close();
        ArrayAdapter b_adapter = new ArrayAdapter(ShowBooks.this, android.R.layout.simple_spinner_item, b_list);
        b_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch.setPrompt("اختر رقم النسخة");
        branch.setAdapter(b_adapter);

        reserved_chB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reserved_chB.isChecked()) {
                    reserved = 1;
                } else {
                    reserved = 0;
                }
            }
        });

        branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                b = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reserved_chB.isChecked()) {
                    reserved = 1;
                } else {
                    reserved = 0;
                }

                SQLiteDatabase database = bookDBHelper.getWritableDatabase();
                String insert_query = "INSERT INTO " + BookCopiesEntry.TABLE_NAME + " VALUES ("
                        + book_id + ", " + get_new_copy_id(book_id) + ", " + b + ", " + reserved + ");";
                database.execSQL(insert_query);
                System.out.println(reserved);
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "أُضيفت نسخة جديدة", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void add_one_to_number_of_copies() {
        SQLiteDatabase database = bookDBHelper.getWritableDatabase();
        int new_number_of_copies = Integer.parseInt(selected_book_number_of_copies) + 1;
        String update_query = "UPDATE " + BooksEntry.TABLE_NAME + " SET " + BooksEntry.COLUMN_BOOK_NUMBER_OF_COPIES + " = "
                + new_number_of_copies;
        database.execSQL(update_query);
    }

    private int get_new_copy_id(int book_id) {
        String select_query = "SELECT " + BookCopiesEntry.COLUMN_BOOK_COPIES_COPY_ID + " FROM " + BookCopiesEntry.TABLE_NAME
                + " WHERE " + BookCopiesEntry.COLUMN_BOOK_COPIES_BOOK_ID + " = " + book_id +
                " ORDER BY " + BookCopiesEntry.COLUMN_BOOK_COPIES_COPY_ID + " ASC;";
        Cursor cursor = db.rawQuery(select_query, null);
        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "لا يوجد نُسخ لحذفها", Toast.LENGTH_SHORT).show();
            return 1;
        }
        cursor.moveToLast();
        return cursor.getInt(0) + 1;
    }

    private boolean copy_is_borrowed(String copy_id) {
        String query = "SELECT * from reader_request WHERE book_id = " + selected_book_id + " AND copy_id = " + copy_id
                + " AND return_date IS NULL";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            return false;
        }
        return true;
    }

    private String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return year + "-" + month + "-" + day;
    }
}