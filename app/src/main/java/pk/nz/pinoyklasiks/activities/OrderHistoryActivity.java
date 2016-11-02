package pk.nz.pinoyklasiks.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import pk.nz.pinoyklasiks.R;
import pk.nz.pinoyklasiks.beans.Order;
import pk.nz.pinoyklasiks.db.DBManager;
import pk.nz.pinoyklasiks.db.IDAOManager;
import utils.OrderAdapter;


/**<pre>
 *
 * Title       : OrderHistoryActivity class
 * Purpose     : Show the history of products the customer ordered earlier
 * Date        : 19.10.2016
 * Input       : List of Orders
 * Proccessing : ListView will be shown to user with information
 *               about previous orders
 *
 *
 * Output      : orders
 *
 * </pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public class OrderHistoryActivity extends AppCompatActivity {

    private ListView lvOrderHistory; // ListView which show the orders
    private IDAOManager dbManager;   // Manager for access to DB objects
    private Handler handler;         // Helper
    private List<Order> listOrders;      // List with Order objects
    private OrderAdapter adapter;       // Adapter for ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // Databease manager that works with DB
        dbManager = new DBManager(this);

        // Help us to work with UI
        handler = new Handler();

        // Get ListView which represent every order
        lvOrderHistory = (ListView)findViewById(R.id.lvOh);
        lvOrderHistory.setOnItemClickListener(new ClickOnOrder());

            // Start thread that connected to DB and get the List of Orders
            // and populate it to lvOrderHistory
                new LoadOrderHistory().start();


        // Define toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_order_history);
        setSupportActionBar(toolbar);

        setTitle("History of orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        // customize back home button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);


    }



    /**
     * Title       : ClickOnOrder class
     * Purpose     : Class listener for ListView object
     * Date        : 01.10.2016
     * Input       : Adapter with List of Orders
     * Proccessing : Get the clicked Item with Order
     *             : and passed it to new intent.
     * Output      : Open new activity "SuborderActivity"
     *
     * @author Mikhail PASTUSHKOV
     * @author Melchor RELATADO
     */
    class ClickOnOrder implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int item, long l) {

            // Create suborder intent and pass there Order object
            // intent get from filter that means use activity
            // passed Order object not Order with Open status
            Intent subOrderIntent = new Intent("pk.nz.pinoyklasiks.history_suborder");
            subOrderIntent.putExtra("order", listOrders.get(item));

            startActivity(subOrderIntent);

        }
    }

    // TODO TASK 1. 8) Thread
    /**
     * Title       : LoadOrderHistory class
     * Purpose     : Class Thread
     * Date        : 03.10.2016
     * Input       : ListView
     * Proccessing : Read the Orders from DB
     *             : and assign it to the ListView
     * Output      : ListView with OrderAdapter or Toast message
     *             : if customer does not have any order
     *
     * @author Mikhail PASTUSHKOV
     * @author Melchor RELATADO
     */
    class LoadOrderHistory extends Thread{

        @Override
        public void run() {

            // Get the all Orders from DB
            listOrders = dbManager.getAllOrders();

            if (listOrders.size() <= 0){

                // TODO TASK 1 8) Runnable
                // NO HISTORY ORDERS show message
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "You have no history of orders !", Toast.LENGTH_SHORT).show();
                    }
                });


            }else{ // GET THE ALL ORDERS AND SHOW IT

                adapter = new OrderAdapter(getApplicationContext(), listOrders);

                // TODO TASK 1 8) Runnable
                // Run handler to populate new Data to ListView
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                            lvOrderHistory.setAdapter(adapter);
                    }
                });

            }

        }


    }





}
