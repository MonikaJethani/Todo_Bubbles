package com.example.todo_bubbles;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;

import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "1001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createBubble();
            }
        });
    }

    private void createBubble() {
        createNotificationChannel();
        // Create bubble intent
        Intent target = new Intent(this, BubbleActivity.class);
        PendingIntent bubbleIntent =
                PendingIntent.getActivity(this, 0, target, 0 /* flags */);

       // Create bubble metadata
        Notification.BubbleMetadata bubbleData =
                new Notification.BubbleMetadata.Builder()
                        .setDesiredHeight(600)
                        .setIcon(Icon.createWithResource(this, R.drawable.ic_launcher_background))
                        .setIntent(bubbleIntent)
                        .setAutoExpandBubble(true)
                        .setSuppressInitialNotification(true)
                        .build();

        Person chatBot = new Person.Builder()
                .setBot(true)
                .setName("BubbleBot")
                .setImportant(true)
                .build();

        Notification.Builder builder =
                new Notification.Builder(this, CHANNEL_ID)
                        .setContentTitle("Your to-do list is here")
                        .setContentText("Click to open to-do list")
                        .setContentIntent(bubbleIntent)
                        .setSmallIcon(R.drawable.ic_todo_list)
                        .setBubbleMetadata(bubbleData)
                        .setAutoCancel(true)
                        .addPerson(chatBot);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1002, builder.build());

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setAllowBubbles(true);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
