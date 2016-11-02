package pk.nz.pinoyklasiks.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.icu.util.Calendar;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import pk.nz.pinoyklasiks.R;
import pk.nz.pinoyklasiks.activities.OrderHistoryActivity;
import pk.nz.pinoyklasiks.beans.Order;
import pk.nz.pinoyklasiks.beans.Status;
import pk.nz.pinoyklasiks.beans.SubOrder;
import pk.nz.pinoyklasiks.db.DBManager;
import pk.nz.pinoyklasiks.db.IDAOManager;
import pk.nz.pinoyklasiks.db.IWebService;
import pk.nz.pinoyklasiks.db.WebService;
import utils.AppConst;

/**<pre>
 *
 * Title       : CheckStatus class
 * Purpose     : Class service for checking current
 *             : status order from the server
 * Date        : 01.10.2016
 * Input       : Order object
 * Proccessing : Via webservice service check id and if the id changed
 *             : become higher then notify customer end stop service
 * Output      : Change order status
 *
 * </pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class CheckStatusService extends Service{
    // The status id not finished
    private boolean isNotFinished = true;
    private IWebService webService;    // WebService manager
    private IDAOManager dbManager;      // DAO Manager
    private int currStatus;             // current status of order
    private int serverStatus;           // status of order on the server
    private Order order;                // current order


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(AppConst.DEBUG) Log.d(AppConst.LOGD , "<<< Start Service >>>");

        webService = new WebService(this);
        dbManager = new DBManager(this);


        // get the order and current id of status
        order =  (Order) intent.getExtras().getSerializable("order");

        currStatus = order.getStatus().getId();

        notifyCustomer(AppConst.APP_NAME, "Orders has been already sent.");
               new Thread(new Runnable() {
                @Override
                public void run() {




                    // loop will check until status will not be changed
                    while (isNotFinished){

                        if(webService.isOnline()){

                            serverStatus = webService.checkStatusOrder(order.getGlobalId(), null);

                            if(serverStatus > 2){

                                order.setStatus(dbManager.getStatusById(serverStatus));

                                dbManager.saveOrder(order);

                                notifyCustomer(AppConst.APP_NAME, "Status of your order was changed on '"+(order.getStatus().getStatusName())+"'");

                                if(AppConst.DEBUG) Log.d(AppConst.LOGD,"<<< CheckStatusService >>> ::: status was changed exit from service");

                                // Notify user by sound
                                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.notify_service);
                                mp.start();

                                isNotFinished = false;

                                stopSelf();

                            }
                        }

                        // Wait next checking
                        try{
                            if(AppConst.DEBUG) Log.d(AppConst.LOGD,"<<< CheckStatusService >>> ::: Wait next checking");

                            Thread.sleep(AppConst.TIME_CHECK_STATUS);


                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * Show notification to user
     * wit given title and message
     * @param title String
     * @param msg   String
     */
    private void notifyCustomer(String title, String msg){

        Intent suborderIntent = new Intent(this, OrderHistoryActivity.class);

        // Intent that leads to the suborder
        PendingIntent pi = PendingIntent.getActivity(this, 0, suborderIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create notification
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);

        // Set notification variables
        // Icon, Title, Message and Link to Intent
        nb.setSmallIcon(R.drawable.ic_cart);

        nb.setContentTitle(title);
            nb.setContentText(msg);
                nb.setContentIntent(pi);

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Run notification
        mNotifyMgr.notify(1, nb.build());

    }



}
