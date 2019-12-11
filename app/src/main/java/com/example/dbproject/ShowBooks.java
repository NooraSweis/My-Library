package com.example.dbproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ShowBooks extends AppCompatActivity {

    ListView books_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_books);

        books_list = (ListView) findViewById(R.id.listView_show_books);


//        connection.getData();

//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
//
//        getData();
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
//        books_list.setAdapter(adapter);
    }

//    private void getData(){
//        try {
//            URL url = new URL(address);
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("GET");
//            is = new BufferedInputStream(con.getInputStream());
//        } catch (Exception e) {
//            System.out.println("URL connection error");
//            e.printStackTrace();
//        }
//
//        try {
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//            StringBuilder sb = new StringBuilder();
//
//            while ((line = br.readLine()) != null){
//                sb.append(line + "\n");
//            }
//
//            is.close();
//            result = sb.toString();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        try{
//            JSONArray ja = new JSONArray(result);
//            JSONObject jo = null;
//
//            data = new String[ja.length()];
//            for(int i = 0; i<ja.length(); i++){
//                jo = ja.getJSONObject(i);
//                data[i] = jo.getString("book_title");
//            }
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}