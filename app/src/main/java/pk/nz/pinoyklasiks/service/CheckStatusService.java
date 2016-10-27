package pk.nz.pinoyklasiks.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import pk.nz.pinoyklasiks.R;
import pk.nz.pinoyklasiks.beans.Order;
import pk.nz.pinoyklasiks.beans.SubOrder;
import utils.AppConst;

/**
 *
 * Service check the status id using globalid
 * value of order object if there is changes
 * it will send notification
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class CheckStatusService extends Service{
    // still has no finished order
    private boolean isNotFinished = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Order order =  intent.getExtras().getSerializable("order");
        //while (isNotFinished){



       // }

        if(AppConst.DEBUG) Log.d(AppConst.LOGD , "<<< Start Service >>>");
        // Intent that leads to the suborder
        Intent suborderIntent = new Intent(this, SubOrder.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, suborderIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
        nb.setSmallIcon(R.drawable.ic_cart);
        nb.setContentTitle("Your order was approved.");
        nb.setContentText(" Information about order");
        nb.setContentIntent(pi);

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(1, nb.build());

        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }
}
