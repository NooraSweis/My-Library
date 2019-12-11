package com.example.dbproject.data;

import android.provider.BaseColumns;

public final class EmployeesContract {

    private EmployeesContract() {
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

}
