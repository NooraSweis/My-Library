package com.example.dbproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dbproject.data.LibraryContract.BookCopiesEntry;
import com.example.dbproject.data.LibraryContract.BooksEntry;
import com.example.dbproject.data.LibraryContract.BranchesEntry;
import com.example.dbproject.data.LibraryContract.EmployeesEntry;

public class DBconnections extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "my_library.db";

    public DBconnections(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_BOOK_COPIES_TABLE = "CREATE TABLE " + BookCopiesEntry.TABLE_NAME + "("
                + BookCopiesEntry.COLUMN_BOOK_COPIES_BOOK_ID + " INTEGER NOT NULL, "
                + BookCopiesEntry.COLUMN_BOOK_COPIES_COPY_ID + " INTEGER NOT NULL, "
                + BookCopiesEntry.COLUMN_BOOK_COPIES_RESERVED + " INTEGER NOT NULL, "
                + "PRIMARY KEY(\"" + BookCopiesEntry.COLUMN_BOOK_COPIES_BOOK_ID
                + "\",\"" + BookCopiesEntry.COLUMN_BOOK_COPIES_COPY_ID + "\"),"
                + "FOREIGN KEY (" + BookCopiesEntry.COLUMN_BOOK_COPIES_BOOK_ID
                + ") REFERENCES " + BooksEntry.TABLE_NAME + " ("
                + BooksEntry.COLUMN_BOOK_ID + ")"
                + ");";
        db.execSQL(SQL_CREATE_BOOK_COPIES_TABLE);

        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BooksEntry.TABLE_NAME + "("
                + BooksEntry.COLUMN_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BooksEntry.COLUMN_BOOK_TITLE + " TEXT NOT NULL, "
                + BooksEntry.COLUMN_BOOK_PUBLICATION_DATE + " TEXT NOT NULL, "
                + BooksEntry.COLUMN_BOOK_PUBLICATION_HOUSE + " TEXT NOT NULL, "
                + BooksEntry.COLUMN_BOOK_AUTHOR + " TEXT NOT NULL, "
                + BooksEntry.COLUMN_BOOK_CATEGORY + " TEXT NOT NULL, "
                + BooksEntry.COLUMN_BOOK_BRANCH_ID + " INTEGER NOT NULL, "
                + BooksEntry.COLUMN_BOOK_NUMBER_OF_COPIES + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + BooksEntry.COLUMN_BOOK_BRANCH_ID
                + ") REFERENCES " + BranchesEntry.TABLE_NAME + " ("
                + BranchesEntry.COLUMN_BRANCH_ID + ")"
                + ");";
        db.execSQL(SQL_CREATE_BOOKS_TABLE);

        String SQL_CREATE_BRANCHES_TABLE = "CREATE TABLE " + BranchesEntry.TABLE_NAME + "("
                + BranchesEntry.COLUMN_BRANCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BranchesEntry.COLUMN_BRANCH_NAME + " TEXT NOT NULL, "
                + BranchesEntry.COLUMN_BRANCH_ADDRESS + " TEXT NOT NULL, "
                + BranchesEntry.COLUMN_BRANCH_PHONE + " INTEGER NOT NULL);";
        db.execSQL(SQL_CREATE_BRANCHES_TABLE);

        String SQL_CREATE_EMPLOYEES_TABLE = "CREATE TABLE " + EmployeesEntry.TABLE_NAME + "("
                + EmployeesEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EmployeesEntry.COLUMN_EMPLOYEE_FIRST_NAME + " TEXT NOT NULL, "
                + EmployeesEntry.COLUMN_EMPLOYEE_LAST_NAME + " TEXT NOT NULL, "
                + EmployeesEntry.COLUMN_EMPLOYEE_ADDRESS + " TEXT NOT NULL, "
                + EmployeesEntry.COLUMN_EMPLOYEE_BRANCH_ID + " INTEGER NOT NULL, "
                + EmployeesEntry.COLUMN_EMPLOYEE_HIRE_DATE + " TEXT NOT NULL, "
                + EmployeesEntry.COLUMN_EMPLOYEE_EMAIL + " TEXT NOT NULL, "
                + EmployeesEntry.COLUMN_EMPLOYEE_PHONE + " INTEGER NOT NULL, "
                + EmployeesEntry.COLUMN_EMPLOYEE_POSITION + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + EmployeesEntry.COLUMN_EMPLOYEE_BRANCH_ID
                + ") REFERENCES " + BranchesEntry.TABLE_NAME + " ("
                + BranchesEntry.COLUMN_BRANCH_ID + ")"
                + ");";
        db.execSQL(SQL_CREATE_EMPLOYEES_TABLE);

        String SQL_CREATE_READER_REQUESTS_TABLE = "CREATE TABLE " + LibraryContract.ReaderRequestEntry.TABLE_NAME + "("
                + LibraryContract.ReaderRequestEntry.COLUMN_Request_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LibraryContract.ReaderRequestEntry.COLUMN_Request_READER_ID + " INTEGER NOT NULL, "
                + LibraryContract.ReaderRequestEntry.COLUMN_Request_BOOK_ID + " INTEGER NOT NULL, "
                + LibraryContract.ReaderRequestEntry.COLUMN_Request_COPY_ID + " INTEGER NOT NULL, "
                + LibraryContract.ReaderRequestEntry.COLUMN_Request_DATE + " TEXT NOT NULL, "
                + LibraryContract.ReaderRequestEntry.COLUMN_Request_RETURN_DATE + " TEXT NOT NULL, "
                + LibraryContract.ReaderRequestEntry.COLUMN_Request_RES_EMPLOYEE + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + LibraryContract.ReaderRequestEntry.COLUMN_Request_READER_ID
                + ") REFERENCES " + LibraryContract.ReadersEntry.TABLE_NAME + " ("
                + LibraryContract.ReadersEntry.COLUMN_READER_ID + "), "
                + "FOREIGN KEY (" + LibraryContract.ReaderRequestEntry.COLUMN_Request_BOOK_ID
                + ") REFERENCES " + LibraryContract.BooksEntry.TABLE_NAME + " ("
                + LibraryContract.BooksEntry.COLUMN_BOOK_ID + ")"
                + ");";
        db.execSQL(SQL_CREATE_READER_REQUESTS_TABLE);

        String SQL_CREATE_READERS_TABLE = "CREATE TABLE " + LibraryContract.ReadersEntry.TABLE_NAME + "("
                + LibraryContract.ReadersEntry.COLUMN_READER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LibraryContract.ReadersEntry.COLUMN_READER_FIRST_NAME + " TEXT NOT NULL, "
                + LibraryContract.ReadersEntry.COLUMN_READER_LAST_NAME + " TEXT NOT NULL, "
                + LibraryContract.ReadersEntry.COLUMN_READER_DATE_OF_BIRTH + " TEXT NOT NULL, "
                + LibraryContract.ReadersEntry.COLUMN_READER_ADDRESS + " TEXT NOT NULL, "
                + LibraryContract.ReadersEntry.COLUMN_READER_GENDER + " TEXT NOT NULL, "
                + LibraryContract.ReadersEntry.COLUMN_READER_PHONE + " INTEGER NOT NULL, "
                + LibraryContract.ReadersEntry.COLUMN_READER_SUB_STATUS + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_READERS_TABLE);

        String SQL_CREATE_SUB_TABLE = "CREATE TABLE " + LibraryContract.SubscriptionsEntry.TABLE_NAME + "("
                + LibraryContract.SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_READER_ID + " INTEGER NOT NULL, "
                + LibraryContract.SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_SUB_DATE + " TEXT NOT NULL, "
                + LibraryContract.SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_END_DATE + " TEXT NOT NULL, "
                + LibraryContract.SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_STATUS + " TEXT NOT NULL, " +
                "PRIMARY KEY(\"" + LibraryContract.SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_READER_ID
                + "\",\"" + LibraryContract.SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_SUB_DATE + "\"),"
                + "FOREIGN KEY (" + LibraryContract.SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_READER_ID
                + ") REFERENCES " + LibraryContract.ReadersEntry.TABLE_NAME + " ("
                + LibraryContract.ReadersEntry.COLUMN_READER_ID + "), " +
                "FOREIGN KEY (" + LibraryContract.SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_READER_ID
                + ") REFERENCES " + LibraryContract.ReadersEntry.TABLE_NAME + " ("
                + LibraryContract.ReadersEntry.COLUMN_READER_ID + ")"
                + ");";
        db.execSQL(SQL_CREATE_SUB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DELETE_BOOK_COPIES = "DROP TABLE IF EXISTS " + LibraryContract.BookCopiesEntry.TABLE_NAME + ";";
        db.execSQL(SQL_DELETE_BOOK_COPIES);
        onCreate(db);

        String SQL_DELETE_BOOKS = "DROP TABLE IF EXISTS " + LibraryContract.BooksEntry.TABLE_NAME + ";";
        db.execSQL(SQL_DELETE_BOOKS);
        onCreate(db);

        String SQL_DELETE_BRANCHES = "DROP TABLE IF EXISTS " + LibraryContract.BranchesEntry.TABLE_NAME + ";";
        db.execSQL(SQL_DELETE_BRANCHES);
        onCreate(db);

        String SQL_DELETE_EMPLOYEES = "DROP TABLE IF EXISTS " + LibraryContract.EmployeesEntry.TABLE_NAME + ";";
        db.execSQL(SQL_DELETE_EMPLOYEES);
        onCreate(db);

        String SQL_DELETE_READER_REQUESTS = "DROP TABLE IF EXISTS " + LibraryContract.ReaderRequestEntry.TABLE_NAME + ";";
        db.execSQL(SQL_DELETE_READER_REQUESTS);
        onCreate(db);

        String SQL_DELETE_READERS = "DROP TABLE IF EXISTS " + LibraryContract.ReadersEntry.TABLE_NAME + ";";
        db.execSQL(SQL_DELETE_READERS);
        onCreate(db);

        String SQL_DELETE_SUBSCRIPTIONS = "DROP TABLE IF EXISTS " + LibraryContract.SubscriptionsEntry.TABLE_NAME + ";";
        db.execSQL(SQL_DELETE_SUBSCRIPTIONS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
