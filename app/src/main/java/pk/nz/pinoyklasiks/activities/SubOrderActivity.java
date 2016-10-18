package pk.nz.pinoyklasiks.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
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
    Handler handler;                        // Work with UI
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
        lvSubOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(AppConst.LOGD, "CLICKED IN THE ACTIVITY  "+view.getClass());
            }
        });




        tvSubOrderTotal = (TextView)findViewById(R.id.tvSubOrderTotal);

        //Start thread which fetch the data from DB and fill ListView
        SubOrderDataThread sodt = new SubOrderDataThread();
        sodt.start();

        // Add toolbar to activity
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_suborder);
            setSupportActionBar(toolbar);
                setTitle(R.string.cartTitle);
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


    /**
     *  Work with ActionBar menu
     *  procees button "Clear"
     * @param item
     * @return
     */
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


    /**
     * Start SubOrderDataThread
     * (read the data from tb_suborder and populate it to ListView thru custom adapter)
     */
    public void updateSubOrder(){
        new SubOrderDataThread().start();
    }



    /**
     * Thread read the data from tb_suborder and populate it to ListView thru custom adapter
     */
    class SubOrderDataThread extends Thread{
        @Override
        public void run() {
            // get the id of order
            dbManager = new DBManager(getApplicationContext());
                final int orderId = dbManager.getIdOpenOrder();


            // get the suborder of order (Cart)
            subOrder = dbManager.getSubOrderByOrderId(orderId);


            // If there are no products in the suborder clear the ListView
            if(subOrder == null){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "You cart is empty.", Toast.LENGTH_SHORT).show();
                        lvSubOrders.setAdapter(null);
                        tvSubOrderTotal.setText("$0");

                    }
                });
            }else{
                // Create and assign the adapter with products and populate the total price
                // and notify the listView that data was changed
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        List listProducts = Arrays.asList(subOrder.getMapProducts().entrySet().toArray());
                            adapter = new SubOrderProductAdapter(SubOrderActivity.this, listProducts, orderId);
                                lvSubOrders.setAdapter(adapter);
                                    lvSubOrders.deferNotifyDataSetChanged();

                        tvSubOrderTotal.setText(df.format( subOrder.getTotalPrice()) );

                    }

                });

            }

        }
    }
}
