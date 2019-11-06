package com.example.dbproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
