package com.example.final_project2;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentHome = new Intent(context, History.class);
        Toast.makeText(context, "Image clicked", Toast.LENGTH_SHORT).show();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(1);
        context.startActivity(intentHome);
    }
}
