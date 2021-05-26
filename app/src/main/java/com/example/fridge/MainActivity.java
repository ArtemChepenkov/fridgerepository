package com.example.fridge;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    ListView listView;
    EditText prodFilter;
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase db;
    ArrayAdapter<Prod> prodAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textview);
        listView = findViewById(R.id.list);
        prodFilter = findViewById(R.id.prodFilter);

        dataBaseHelper = new DataBaseHelper(this);

        dataBaseHelper.create_db();
//        long DELAY =  10 * 1000;
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                nm = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                Notification.Builder builder = new Notification.Builder(getApplicationContext());
//                Intent intent = new Intent(MainActivity.this,Shell.class);
//                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,0,intent, PendingIntent.FLAG_CANCEL_CURRENT);
//                builder
//                        .setContentIntent(pendingIntent)
//                        .setSmallIcon(R.drawable.ic_launcher_foreground)
//                        .setTicker("Новое уведомление")
//                        .setWhen(System.currentTimeMillis())
//                        .setContentTitle("Уведомление");
//                Notification notification = builder.build();
//                nm.notify(NOTIFICATION_ID,notification);
//            }
//        }, DELAY);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Prod prod = prodAdapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                intent.putExtra("id", prod.getId());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            DataBaseAdapter adapter= new DataBaseAdapter(this);
            adapter.open();
            db = dataBaseHelper.open();
            List<Prod> prods = adapter.getProds();
            prodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,prods);

            if (!prodFilter.getText().toString().isEmpty())
                prodAdapter.getFilter().filter(prodFilter.getText().toString());
            prodFilter.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    prodAdapter.getFilter().filter(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listView.setAdapter(prodAdapter);
            adapter.close();
        } catch (SQLException e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        //   userCursor.close();
    }

    public void add(View view) {
        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
        startActivity(intent);

    }
}