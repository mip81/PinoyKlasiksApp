package pk.nz.pinoyklasiks.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import pk.nz.pinoyklasiks.R;
import pk.nz.pinoyklasiks.beans.SubOrder;
import pk.nz.pinoyklasiks.db.DBManager;
import pk.nz.pinoyklasiks.db.IDAOManager;
import utils.AppConst;
import utils.SubOrderProductAdapter;

/**
 * Activity represent the suborder (cart)
 * in the app.
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class SubOrderActivity extends AppCompatActivity {

    ListView lvSubOrders;                   // Data of order
    TextView tvSubOrderTotal;               // TextView for present the total cost
    SubOrderProductAdapter adapter;         // Populate the data to ListView
    Handler handler;
    SubOrder subOrder;                      // suborder of order
    IDAOManager dbManager;                  // DB helper;
    DecimalFormat df = new DecimalFormat("$##.00"); // Format for price output

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_order);

        // handler to with UI
        handler = new Handler();

        //find list view and bind the adapter for popuating data
        lvSubOrders = (ListView)findViewById(R.id.lvSubOrder);
        tvSubOrderTotal = (TextView)findViewById(R.id.tvSubOrderTotal);

        //Start thread which fetch the data from DB and fill ListView
        SubOrderDataThread sodt = new SubOrderDataThread();
        sodt.start();

        // Add toolbar to activity
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_suborder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }

    /**
     *  Add menu configuration
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_sub_order, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.tmenu_clear_cart){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (AppConst.DEBUG) Log.d(AppConst.LOGD, "Option 'Clear' was clicked");

                    //Get id of order with open status and delete the records in tb_suborder
                    // with this id (clean cart)
                    int order_id = dbManager.getIdOpenOrder();
                    dbManager.cleanSubOrder(order_id);

                    handler.post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    lvSubOrders.setAdapter(null); // clear listview with suborders
                                }
                            });

                }
            }).start();
        }
        return super.onOptionsItemSelected(item);
    }

    class SubOrderDataThread extends Thread{
        @Override
        public void run() {
            // get the id of order
            dbManager = new DBManager(getApplicationContext());
                int orderId = dbManager.getIdOpenOrder();


            // get the suborder of order (Cart)
            subOrder = dbManager.getSubOrderByOrderId(orderId);


            if(subOrder == null){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "You cart is empty.", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                // Assign the adapter to listview

                final List listProducts = Arrays.asList(subOrder.getMapProducts().entrySet().toArray());
                adapter = new SubOrderProductAdapter(getApplicationContext(), listProducts);

                // add data to UI
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        lvSubOrders.setAdapter(adapter);
                        tvSubOrderTotal.setText(df.format( subOrder.getTotalPrice()) );
                    }

                });

            }

        }
    }
}
