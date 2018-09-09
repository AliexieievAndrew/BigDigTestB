package comw.example.user.testbigdigappb.AlarmNotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import comw.example.user.testbigdigappb.DataBase.DataBaseManager;

public class NotificationPublisher extends BroadcastReceiver {
    public final String NOTIFICATION_ID = "notification-id";
    public final String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);

        deleteLink(context,new Integer(intent.getStringExtra("id")));
        notificationManager.notify(id, notification);
    }
    private void deleteLink(Context context,Integer id){
        DataBaseManager manager = new DataBaseManager(context);
        manager.deleteValueDataBase(Integer.parseInt(id.toString()));
    }
}
