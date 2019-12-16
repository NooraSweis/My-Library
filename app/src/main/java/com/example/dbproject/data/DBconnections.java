package com.example.dbproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dbproject.data.LibraryContract.UpdatedReadersEntry;
import com.example.dbproject.data.LibraryContract.BookCopiesEntry;
import com.example.dbproject.data.LibraryContract.BooksEntry;
import com.example.dbproject.data.LibraryContract.BranchesEntry;
import com.example.dbproject.data.LibraryContract.EmployeesEntry;
import com.example.dbproject.data.LibraryContract.ReadersEntry;
import com.example.dbproject.data.LibraryContract.SubscriptionsEntry;
import com.example.dbproject.data.LibraryContract.ReaderRequestEntry;

import java.util.Calendar;


public class DBconnections extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "my_library.db";

    public DBconnections(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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

        String SQL_CREATE_READER_REQUESTS_TABLE = "CREATE TABLE " + ReaderRequestEntry.TABLE_NAME + "("
                + ReaderRequestEntry.COLUMN_Request_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ReaderRequestEntry.COLUMN_Request_READER_ID + " INTEGER NOT NULL, "
                + ReaderRequestEntry.COLUMN_Request_BOOK_ID + " INTEGER NOT NULL, "
                + ReaderRequestEntry.COLUMN_Request_COPY_ID + " INTEGER NOT NULL, "
                + ReaderRequestEntry.COLUMN_Request_DATE + " TEXT NOT NULL, "
                + ReaderRequestEntry.COLUMN_Request_RETURN_DATE + " TEXT, "
                + ReaderRequestEntry.COLUMN_Request_RES_EMPLOYEE + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + ReaderRequestEntry.COLUMN_Request_READER_ID
                + ") REFERENCES " + ReadersEntry.TABLE_NAME + " ("
                + ReadersEntry.COLUMN_READER_ID + "), "
                + "FOREIGN KEY (" + ReaderRequestEntry.COLUMN_Request_COPY_ID
                + ") REFERENCES " + BookCopiesEntry.TABLE_NAME + " ("
                + BookCopiesEntry.COLUMN_BOOK_COPIES_COPY_ID + "), "
                + "FOREIGN KEY (" + ReaderRequestEntry.COLUMN_Request_BOOK_ID
                + ") REFERENCES " + BooksEntry.TABLE_NAME + " ("
                + BooksEntry.COLUMN_BOOK_ID + ")"
                + ");";
        db.execSQL(SQL_CREATE_READER_REQUESTS_TABLE);

        String SQL_CREATE_READERS_TABLE = "CREATE TABLE " + LibraryContract.ReadersEntry.TABLE_NAME + "("
                + ReadersEntry.COLUMN_READER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ReadersEntry.COLUMN_READER_FIRST_NAME + " TEXT NOT NULL, "
                + ReadersEntry.COLUMN_READER_LAST_NAME + " TEXT NOT NULL, "
                + ReadersEntry.COLUMN_READER_DATE_OF_BIRTH + " TEXT NOT NULL, "
                + ReadersEntry.COLUMN_READER_ADDRESS + " TEXT NOT NULL, "
                + ReadersEntry.COLUMN_READER_GENDER + " TEXT NOT NULL, "
                + ReadersEntry.COLUMN_READER_PHONE + " INTEGER NOT NULL, "
                + ReadersEntry.COLUMN_READER_SUB_STATUS + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_READERS_TABLE);

        String SQL_CREATE_SUB_TABLE = "CREATE TABLE " + SubscriptionsEntry.TABLE_NAME + "("
                + SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_READER_ID + " INTEGER NOT NULL, "
                + SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_SUB_DATE + " TEXT NOT NULL, "
                + SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_END_DATE + " TEXT NOT NULL, "
                + SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_STATUS + " TEXT NOT NULL, " +
                "PRIMARY KEY(\"" + SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_READER_ID
                + "\",\"" + SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_SUB_DATE + "\"),"
                + "FOREIGN KEY (" + SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_READER_ID
                + ") REFERENCES " + ReadersEntry.TABLE_NAME + " ("
                + ReadersEntry.COLUMN_READER_ID + "), " +
                "FOREIGN KEY (" + SubscriptionsEntry.COLUMN_SUBSCRIPTIONS_READER_ID
                + ") REFERENCES " + ReadersEntry.TABLE_NAME + " ("
                + ReadersEntry.COLUMN_READER_ID + ")"
                + ");";
        db.execSQL(SQL_CREATE_SUB_TABLE);

        /*
        Insert initial rows to the database
         */
        insertDefaultRecordsToReaders(db);
        insertDefaultRecordsToBranches(db);
        insertDefaultRecordsToEmployees(db);
        insertDefaultRecordsToBooks(db);
        insertDefaultRecordsToReaderRequests(db);

        create_archive_table(db);
    }

    private void create_archive_table(SQLiteDatabase db) {
        String SQL_CREATE_READERS_archives_TABLE = "CREATE TABLE " + UpdatedReadersEntry.TABLE_NAME + "("
                + UpdatedReadersEntry.UPDATED_READER_ID + " INTEGER NOT NULL, "
                + UpdatedReadersEntry.DATE_OF_UPDATE + " TEXT NOT NULL, "

                + UpdatedReadersEntry.OLD_FIRST_NAME + " TEXT NOT NULL, "
                + UpdatedReadersEntry.OLD_LAST_NAME + " TEXT NOT NULL, "
                + UpdatedReadersEntry.OLD_DATE_OF_BIRTH + " TEXT NOT NULL, "
                + UpdatedReadersEntry.OLD_ADDRESS + " TEXT NOT NULL, "
                + UpdatedReadersEntry.OLD_GENDER + " TEXT NOT NULL, "
                + UpdatedReadersEntry.OLD_PHONE + " INTEGER NOT NULL, "

                + UpdatedReadersEntry.NEW_FIRST_NAME + " TEXT NOT NULL, "
                + UpdatedReadersEntry.NEW_LAST_NAME + " TEXT NOT NULL, "
                + UpdatedReadersEntry.NEW_DATE_OF_BIRTH + " TEXT NOT NULL, "
                + UpdatedReadersEntry.NEW_ADDRESS + " TEXT NOT NULL, "
                + UpdatedReadersEntry.NEW_GENDER + " TEXT NOT NULL, "
                + UpdatedReadersEntry.NEW_PHONE + " INTEGER NOT NULL);";
        db.execSQL(SQL_CREATE_READERS_archives_TABLE);
    }

    private void insertDefaultRecordsToReaderRequests(SQLiteDatabase db) {
        String insert_request = "INSERT INTO " + LibraryContract.ReaderRequestEntry.TABLE_NAME
                + " VALUES (100,1000,1000,1,'2019-12-10', '2019-12-15',1000);";
        db.execSQL(insert_request);
    }

    private void insertDefaultRecordsToBooks(SQLiteDatabase db) {
        String insert_to_books = "INSERT INTO " + BooksEntry.TABLE_NAME + " VALUES (1000, 'أحياء حلب القديمة', '2006-12-03', 'وزارة الثقافة السورية', 'خير الدين الأسدي', 'التاريخ', 1, 1);";
        db.execSQL(insert_to_books);
        String insert_to_copies = "INSERT INTO " + BookCopiesEntry.TABLE_NAME + " VALUES (1000, 1, 0);";
        db.execSQL(insert_to_copies);

        insert_to_books = "INSERT INTO " + BooksEntry.TABLE_NAME + " VALUES (1001, 'أرض زيكولا', '2010-10-13', 'صرح للنشر والتوزيع', 'عمرو عبد الحميد', 'روايات وقصص أدبية', 1, 2);";
        db.execSQL(insert_to_books);
        insert_to_copies = "INSERT INTO " + BookCopiesEntry.TABLE_NAME + " VALUES (1001, 1, 0);";
        db.execSQL(insert_to_copies);
        insert_to_copies = "INSERT INTO " + BookCopiesEntry.TABLE_NAME + " VALUES (1001, 2, 0);";
        db.execSQL(insert_to_copies);

        insert_to_books = "INSERT INTO " + BooksEntry.TABLE_NAME + " VALUES (1002, 'حديث الصباح', '2015-02-11', 'دار كلمات', 'أدهم الشرقاوي', 'روايات وقصص أدبية', 1, 2);";
        db.execSQL(insert_to_books);
        insert_to_copies = "INSERT INTO " + BookCopiesEntry.TABLE_NAME + " VALUES (1002, 1, 0);";
        db.execSQL(insert_to_copies);
        insert_to_copies = "INSERT INTO " + BookCopiesEntry.TABLE_NAME + " VALUES (1002, 2, 0);";
        db.execSQL(insert_to_copies);

        insert_to_books = "INSERT INTO " + BooksEntry.TABLE_NAME + " VALUES (1003, 'أسرار عقل المليونير', '2017-11-23', 'جرير', 'هارف ايكر', 'تنمية', 1, 1);";
        db.execSQL(insert_to_books);
        insert_to_copies = "INSERT INTO " + BookCopiesEntry.TABLE_NAME + " VALUES (1003, 1, 0);";
        db.execSQL(insert_to_copies);
    }

    private void insertDefaultRecordsToEmployees(SQLiteDatabase db) {
        String insert_to_employees = "INSERT INTO " + EmployeesEntry.TABLE_NAME + " VALUES (1000, 'أحمد', 'خالد', 'طولكرم', 1, '2014-02-12', 'ahmad@mylibrary.com', 0598765654, 'مدير');";
        db.execSQL(insert_to_employees);
        insert_to_employees = "INSERT INTO " + EmployeesEntry.TABLE_NAME + " VALUES (1001, 'فاتن', 'محمد', 'طولكرم', 1, '2014-05-23', 'faten@mylibrary.com', 0596326451, 'مساعد');";
        db.execSQL(insert_to_employees);
        insert_to_employees = "INSERT INTO " + EmployeesEntry.TABLE_NAME + " VALUES (1002, 'بانا', 'علي', 'طولكرم', 1, '2014-04-12', 'bana@mylibrary.com', 0596874130, 'مساعد');";
        db.execSQL(insert_to_employees);
        insert_to_employees = "INSERT INTO " + EmployeesEntry.TABLE_NAME + " VALUES (1003, 'محمود', 'مهنا', 'طولكرم', 1, '2015-06-01', 'mahmoud@mylibrary.com', 0985656585, 'مساعد');";
        db.execSQL(insert_to_employees);
        insert_to_employees = "INSERT INTO " + EmployeesEntry.TABLE_NAME + " VALUES (1004, 'أحمد', 'مجدي', 'نابلس',2, '2015-11-23', 'ahmad-m@mylibrary.com', 0236523623, 'مدير');";
        db.execSQL(insert_to_employees);
        insert_to_employees = "INSERT INTO " + EmployeesEntry.TABLE_NAME + " VALUES (1005, 'محمد', 'نور', 'نابلس', 2, '2015-12-14', 'mohammad@mylibrary.com', 0596656523, 'مساعد');";
        db.execSQL(insert_to_employees);
        insert_to_employees = "INSERT INTO " + EmployeesEntry.TABLE_NAME + " VALUES (1006, 'عادل', 'راجح', 'رام الله', 2, '2016-02-28', 'adel@mylibrary.com', 0548779654, 'مساعد');";
        db.execSQL(insert_to_employees);
        insert_to_employees = "INSERT INTO " + EmployeesEntry.TABLE_NAME + " VALUES (1007, 'دينا', 'رائد', 'جنين', 3, '2017-09-10', 'dena@mylibrary.com', 0598595841, 'مدير');";
        db.execSQL(insert_to_employees);
        insert_to_employees = "INSERT INTO " + EmployeesEntry.TABLE_NAME + " VALUES (1008, 'أسيل', 'راشد', 'طولكرم', 3, '2017-09-12', 'aseel@mylibrary.com', 0595252456, 'مساعد');";
        db.execSQL(insert_to_employees);
        insert_to_employees = "INSERT INTO " + EmployeesEntry.TABLE_NAME + " VALUES (1009, 'بهاء', 'فيصل', 'جنين', 3, '2019-10-03', 'bahaa@mylibrary.com', 0595654235, 'مساعد');";
        db.execSQL(insert_to_employees);
    }

    private void insertDefaultRecordsToBranches(SQLiteDatabase db) {
        String insert_to_branches = "INSERT INTO " + BranchesEntry.TABLE_NAME + " VALUES (1, 'مكتبة طولكرم', 'طولكرم', 092632323);";
        db.execSQL(insert_to_branches);
        insert_to_branches = "INSERT INTO " + BranchesEntry.TABLE_NAME + " VALUES (2, 'مكتبة نابلس', 'نابلس', 092687877);";
        db.execSQL(insert_to_branches);
        insert_to_branches = "INSERT INTO " + BranchesEntry.TABLE_NAME + " VALUES (3, 'مكتبة رام الله', 'رام الله', 096968747);";
        db.execSQL(insert_to_branches);
        insert_to_branches = "INSERT INTO " + BranchesEntry.TABLE_NAME + " VALUES (4, 'مكتبة جنين', 'جنين', 096363555);";
        db.execSQL(insert_to_branches);
    }

    private void insertDefaultRecordsToReaders(SQLiteDatabase db) {
        String insert_to_readers = "INSERT INTO " + ReadersEntry.TABLE_NAME + " VALUES (1000, 'نورا', 'صويص', '1999-09-11', 'طولكرم', 'أنثى', 0595483013, 'فعال');";
        db.execSQL(insert_to_readers);
        String insert_to_sub = "INSERT INTO " + SubscriptionsEntry.TABLE_NAME + " VALUES (1000, '2019-12-12', '2020-12-12', 'فعال');";
        db.execSQL(insert_to_sub);

        insert_to_readers = "INSERT INTO " + ReadersEntry.TABLE_NAME + " VALUES (1001, 'علا', 'تيسير', '1994-04-22', 'نابلس', 'أنثى', 0598765485, 'منتهٍ');";
        db.execSQL(insert_to_readers);
        insert_to_sub = "INSERT INTO " + SubscriptionsEntry.TABLE_NAME + " VALUES (1001, '2010-01-03', '2011-01-03', 'منتهٍ');";
        db.execSQL(insert_to_sub);

        insert_to_readers = "INSERT INTO " + ReadersEntry.TABLE_NAME + " VALUES (1002, 'رضا', 'حمدان', '1991-11-03', 'رام الله', 'ذكر', 0598763238, 'فعال');";
        db.execSQL(insert_to_readers);
        insert_to_sub = "INSERT INTO " + SubscriptionsEntry.TABLE_NAME + " VALUES (1002, '2019-08-11', '2020-08-11', 'فعال');";
        db.execSQL(insert_to_sub);

        insert_to_readers = "INSERT INTO " + ReadersEntry.TABLE_NAME + " VALUES (1003, 'نور', 'بدران', '1996-01-12', 'رام الله', 'أنثى', 0598743011, 'فعال');";
        db.execSQL(insert_to_readers);
        insert_to_sub = "INSERT INTO " + SubscriptionsEntry.TABLE_NAME + " VALUES (1003, '2019-11-04', '2020-11-04', 'فعال');";
        db.execSQL(insert_to_sub);

        insert_to_readers = "INSERT INTO " + ReadersEntry.TABLE_NAME + " VALUES (1004, 'أحمد', 'خالد', '2000-06-30', 'نابلس', 'ذكر', 0532163321, 'فعال');";
        db.execSQL(insert_to_readers);
        insert_to_sub = "INSERT INTO " + SubscriptionsEntry.TABLE_NAME + " VALUES (1004, '2017-06-04', '2018-06-04', 'منتهٍ');";
        db.execSQL(insert_to_sub);
        insert_to_sub = "INSERT INTO " + SubscriptionsEntry.TABLE_NAME + " VALUES (1004, '2019-01-22', '2020-01-22', 'فعال');";
        db.execSQL(insert_to_sub);

        insert_to_readers = "INSERT INTO " + ReadersEntry.TABLE_NAME + " VALUES (1005, 'طارق', 'إبراهيم', '2004-12-02', 'جنين', 'ذكر', 0563212325, 'منتهٍ');";
        db.execSQL(insert_to_readers);
        insert_to_sub = "INSERT INTO " + SubscriptionsEntry.TABLE_NAME + " VALUES (1005, '2005-10-16', '2006-10-16', 'منتهٍ');";
        db.execSQL(insert_to_sub);

        insert_to_readers = "INSERT INTO " + ReadersEntry.TABLE_NAME + " VALUES (1006, 'سجى', 'بدير', '2001-10-07', 'طولكرم', 'أنثى', 0598787888, 'فعال');";
        db.execSQL(insert_to_readers);
        insert_to_sub = "INSERT INTO " + SubscriptionsEntry.TABLE_NAME + " VALUES (1006, '2019-02-23', '2020-02-23', 'فعال');";
        db.execSQL(insert_to_sub);

        insert_to_readers = "INSERT INTO " + ReadersEntry.TABLE_NAME + " VALUES (1007, 'حلا', 'يحيى', '1990-03-15', 'طولكرم', 'أنثى', 0153256322, 'فعال');";
        db.execSQL(insert_to_readers);
        insert_to_sub = "INSERT INTO " + SubscriptionsEntry.TABLE_NAME + " VALUES (1007, '2019-05-11', '2020-05-11', 'فعال');";
        db.execSQL(insert_to_sub);

        insert_to_readers = "INSERT INTO " + ReadersEntry.TABLE_NAME + " VALUES (1008, 'سندس', 'ياسر', '1989-07-23', 'طولكرم', 'أنثى', 0569878896, 'منتهٍ');";
        db.execSQL(insert_to_readers);
        insert_to_sub = "INSERT INTO " + SubscriptionsEntry.TABLE_NAME + " VALUES (1008, '2018-07-12', '2019-07-12', 'منتهٍ');";
        db.execSQL(insert_to_sub);

        insert_to_readers = "INSERT INTO " + ReadersEntry.TABLE_NAME + " VALUES (1009, 'رامي', 'عمر', '1985-09-06', 'جنين', 'ذكر', 0596363654, 'فعال');";
        db.execSQL(insert_to_readers);
        insert_to_sub = "INSERT INTO " + SubscriptionsEntry.TABLE_NAME + " VALUES (1009, '2019-09-19', '2020-09-19', 'فعال');";
        db.execSQL(insert_to_sub);
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
