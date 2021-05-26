package com.example.fridge;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Date;


public class UserActivity extends Activity {
    EditText name, year;
    Button btn_save, btn_delete;

    long prodID = 0;
    DataBaseAdapter adapter;


    public static void createChannelIfNeeded(NotificationManager manager){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("CHANNEL_ID","CHANNEL_ID",NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        name = findViewById(R.id.name);
        year = findViewById(R.id.year);
        btn_delete = findViewById(R.id.btn_delete);
        btn_save = findViewById(R.id.btn_save);
        adapter = new DataBaseAdapter(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            prodID = extras.getLong("id");
        if (prodID > 0) {
            adapter.open();

            //Prod user = adapter.getProd(prodID);

            /*name.setText(user.getName());
            year.setText(String.valueOf(user.getDays()));*/

            adapter.close();
        }
        if (prodID == 0) {
            btn_delete.setVisibility(View.GONE);
        }
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prod_name = name.getText().toString();
                int prod_year = 0;
                try {
                    prod_year = Integer.parseInt(year.getText().toString());
                }
                catch (NumberFormatException e){}
                final Prod prod = new Prod(prodID, prod_name, prod_year,new Date().getTime());
                final Handler handler = new Handler();
                long delay = 1000L * prod_year;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        Intent intent = new Intent(getApplicationContext(), Shell.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),"CHANNEL_ID")
                                .setAutoCancel(false)
                                .setSmallIcon(R.drawable.ic_fridge)
                                .setWhen(System.currentTimeMillis())
                                .setContentIntent(pendingIntent)
                                .setContentText("У "+prod.getName()+" истёк срок годности");
                        createChannelIfNeeded(notificationManager);
                        notificationManager.notify((int)System.currentTimeMillis()/1000,notificationBuilder.build());

                    }
                }, delay);

                adapter.open();

                if (prodID > 0)
                    adapter.update(prod);
                else
                    adapter.insert(prod);
                adapter.close();
                goHome();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.open();
                adapter.delete(prodID);
                adapter.close();
                goHome();
            }
        });

    }

    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
    }

