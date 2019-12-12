package com.example.dbproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ShowReaders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_readers);
    }

    public void to_add_readers(View view){
        startActivity(new Intent(this, NewReaderActivity.class));
    }
}
