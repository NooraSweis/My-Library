package com.example.dbproject.data;

import android.provider.BaseColumns;

public final class LibraryContract {

    private LibraryContract() {
    }

    public static final class EmployeesEntry implements BaseColumns {

        public final static String TABLE_NAME = "employees";

        public final static String ID = "ID";
        public final static String COLUMN_EMPLOYEE_FIRST_NAME = "first_name";
        public final static String COLUMN_EMPLOYEE_LAST_NAME = "last_name";
        public final static String COLUMN_EMPLOYEE_ADDRESS = "address";
        public final static String COLUMN_EMPLOYEE_BRANCH_ID = "branch_id";
        public final static String COLUMN_EMPLOYEE_EMAIL = "email";
        public final static String COLUMN_EMPLOYEE_PHONE = "phone";
        public final static String COLUMN_EMPLOYEE_POSITION = "position";
        public final static String COLUMN_EMPLOYEE_HIRE_DATE = "hire_date";
    }

    public static final class ReadersEntry implements BaseColumns {

        public final static String TABLE_NAME = "readers";

        public final static String COLUMN_READER_ID = "ID";
        public final static String COLUMN_READER_FIRST_NAME = "first_name";
        public final static String COLUMN_READER_LAST_NAME = "last_name";
        public final static String COLUMN_READER_DATE_OF_BIRTH = "date_of_birth";
        public final static String COLUMN_READER_ADDRESS = "address";
        public final static String COLUMN_READER_GENDER = "gender";
        public final static String COLUMN_READER_PHONE = "phone";
        public final static String COLUMN_READER_SUB_STATUS = "sub_status";

        public static final int GENDER_MALE = 0;
        public static final int GENDER_FEMALE = 1;
    }

    public static final class BooksEntry implements BaseColumns {

        public final static String TABLE_NAME = "books";

        public final static String COLUMN_BOOK_ID = "ID";
        public final static String COLUMN_BOOK_TITLE = "title";
        public final static String COLUMN_BOOK_PUBLICATION_DATE = "publication_date";
        public final static String COLUMN_BOOK_PUBLICATION_HOUSE = "publication_house";
        public final static String COLUMN_BOOK_AUTHOR = "author";
        public final static String COLUMN_BOOK_CATEGORY = "category";
        public final static String COLUMN_BOOK_BRANCH_ID = "branch_id";
        public final static String COLUMN_BOOK_NUMBER_OF_COPIES = "number_of_copies";
    }

    public static final class BranchesEntry implements BaseColumns {

        public final static String TABLE_NAME = "branches";

        public final static String COLUMN_BRANCH_ID = "ID";
        public final static String COLUMN_BRANCH_NAME = "name";
        public final static String COLUMN_BRANCH_ADDRESS = "address";
        public final static String COLUMN_BRANCH_PHONE = "phone";
    }

    public static final class ReaderRequestEntry implements BaseColumns {

        public final static String TABLE_NAME = "reader_request";

        public final static String COLUMN_Request_ID = "request_id";
        public final static String COLUMN_Request_READER_ID = "reader_id";
        public final static String COLUMN_Request_BOOK_ID = "book_id";
        public final static String COLUMN_Request_COPY_ID = "copy_id";
        public final static String COLUMN_Request_DATE = "request_date";
        public final static String COLUMN_Request_RETURN_DATE = "return_date";
        public final static String COLUMN_Request_RES_EMPLOYEE = "responsible_employee";
    }

    public static final class BookCopiesEntry implements BaseColumns {

        public final static String TABLE_NAME = "book_copies";

        public final static String COLUMN_BOOK_COPIES_BOOK_ID = "book_id";
        public final static String COLUMN_BOOK_COPIES_COPY_ID = "copy_id";
        public final static String COLUMN_BOOK_COPIES_RESERVED = "reserved";
    }

    public static final class SubscriptionsEntry implements BaseColumns {

        public final static String TABLE_NAME = "subscriptions";

        public final static String COLUMN_SUBSCRIPTIONS_READER_ID = "reader_id";
        public final static String COLUMN_SUBSCRIPTIONS_SUB_DATE = "subscription_date";
        public final static String COLUMN_SUBSCRIPTIONS_END_DATE = "end_date";
        public final static String COLUMN_SUBSCRIPTIONS_STATUS = "status";
    }

    public static final class UpdatedReadersEntry implements BaseColumns {

        public final static String TABLE_NAME = "updated_reader";

        public final static String UPDATED_READER_ID = "updated_reader_id";

        public final static String DATE_OF_UPDATE = "date_of_update";
        public final static String OLD_FIRST_NAME = "old_first_name";
        public final static String OLD_LAST_NAME = "old_last_name";
        public final static String OLD_DATE_OF_BIRTH = "old_date_of_birth";
        public final static String OLD_ADDRESS = "old_address";
        public final static String OLD_GENDER = "old_gender";
        public final static String OLD_PHONE = "old_phone";

        public final static String NEW_FIRST_NAME = "new_first_name";
        public final static String NEW_LAST_NAME = "new_last_name";
        public final static String NEW_DATE_OF_BIRTH = "new_date_of_birth";
        public final static String NEW_ADDRESS = "new_address";
        public final static String NEW_GENDER = "new_gender";
        public final static String NEW_PHONE = "new_phone";
    }

    public static final class UpdatedCopies implements BaseColumns{

        public final static String TABLE_NAME = "updated_copies";

    }
}