package comw.example.user.testbigdigappb.AlarmNotification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import comw.example.user.testbigdigappb.MainActivity;
import comw.example.user.testbigdigappb.R;

public class AlarmNotification {
    private Context context;

    public static final String NOTIFICATION_ID = "notification-id";
    public static final String NOTIFICATION = "notification";
    public static final String MESSAGE_TITLE = "Important message";
    public static final String MESSAGE_DESCRIPTION = "link successfully deleted";
    public static final String CHANNEL_ID = "my_channel_01";
    public static  final CharSequence NAME = "my_channel";

    public AlarmNotification(Context context) {
        this.context = context;
    }

    public void scheduleNotification(Notification notification, int delay,int id) {

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NOTIFICATION, notification);
        notificationIntent.putExtra("id",String.valueOf(id));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public Notification getNotification() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, NAME, importance);
            mChannel.enableLights(true);
            mChannel.setShowBadge(false);

            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(MESSAGE_TITLE)
                .setContentText(MESSAGE_DESCRIPTION);

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        return builder.build();
    }
}