package pk.nz.pinoyklasiks.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pk.nz.pinoyklasiks.MainActivity;
import pk.nz.pinoyklasiks.R;
import pk.nz.pinoyklasiks.beans.Order;
import pk.nz.pinoyklasiks.beans.SubOrder;
import pk.nz.pinoyklasiks.beans.TypeOrder;
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

    private final int CTX_MENU_CLEAR = 1;
    private final int CTX_MENU_CHECKOUT = 2;

    private int orderId;

    private ListView lvSubOrders;                   // Data of order
    private TextView tvSubOrderTotal;               // TextView for present the total cost
    private Button btnCheckout;
    private SubOrderProductAdapter adapter;         // Populate the data to ListView
    private Handler handler;                        // Work with UI
    private SubOrder subOrder;                      // suborder of order
    private IDAOManager dbManager;                  // DB helper;
    private DecimalFormat df = new DecimalFormat("$##.00"); // Format for price output
    private Order order;                            // Order object




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_order);

        // handler to with UI
        handler = new Handler();

        // Checkout button use listener described at the bottom of this activity
        btnCheckout = (Button)findViewById(R.id.subOrderCheckOut);
            btnCheckout.setOnClickListener(new CheckoutListener());

        //find list view and bind the adapter for popuating data
        lvSubOrders = (ListView)findViewById(R.id.lvSubOrder);

            registerForContextMenu(lvSubOrders); // register context menu for the list view

                // get the view for the price
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
     * Create the context menu for the ListView
     * will be called using long click
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(v.getId() == R.id.lvSubOrder){
            menu.add(0,CTX_MENU_CLEAR,0,"Clear the order");
            menu.add(0,CTX_MENU_CHECKOUT,0, "Proceed the order");
        }
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case CTX_MENU_CLEAR:
                    clearCart();
                break;
            case CTX_MENU_CHECKOUT:
                    new CheckoutListener().onClick(btnCheckout);
                break;
        }


        return super.onContextItemSelected(item);
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
     *  define action for button "Clear"
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.tmenu_clear_cart) {

           clearCart();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Clear cart method
     */
    protected void clearCart(){

        //  Ask user to confirm deletion
        new AlertDialog.Builder(this).setTitle(R.string.dialog_confirm_title)
                .setMessage(R.string.dialog_confirm_message)
                .setNegativeButton(R.string.no, null) //Do nothing when answer NO
                //Otherwise clean the cart in the another thread
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Get the last order Id and and clean the product in the tb_suborder with this ID
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (AppConst.DEBUG)
                                    Log.d(AppConst.LOGD, "Option 'Clear' was clicked");

                                //Get id of order with open status and delete the records in tb_suborder
                                // with this id (clean cart)
                                int order_id = dbManager.getIdOpenOrder();
                                dbManager.cleanSubOrder(order_id);

                                handler.post(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                lvSubOrders.setAdapter(null);  // clear listview with suborders
                                                btnCheckout.setEnabled(false); // disable button checkout
                                                tvSubOrderTotal.setText("$0"); // set price label to $0

                                            }
                                        });
                            }
                        }).start();
                    }
                }).show();
    }


    /**
     * Start SubOrderDataThread
     * (read the data from tb_suborder and populate it to ListView thru custom adapter)
     */
    public void updateSubOrder(){
        new SubOrderDataThread().start();
    }


    /**
     * class listener for checkout btn
     * run Dialog with choice of type order
     * and then change Activity to fill customer form
     *
     */
    class CheckoutListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {


            // TODO: 10/19/16  read types from db and populate in the dialog

            final String strOrderTypes[]  = {"Dine-in", "Takeaway"};

            //Create and show the dialog with TYPE ORDER choice
            // then this choice will be pass to CompleteOrderActivity

            AlertDialog.Builder dialogChooseType = new AlertDialog.Builder(SubOrderActivity.this);
            dialogChooseType.setTitle(R.string.suborderTypeOrderTitle)
                    .setSingleChoiceItems(strOrderTypes, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(AppConst.DEBUG)  Log.d(AppConst.LOGD , " SubOrder Activity ::: Clicked 'CHECKOUT' choosen type : "+strOrderTypes[which]);

                            Intent coIntent = new Intent(getApplicationContext(), CompleteOrderActivity.class);

                            coIntent.putExtra("typeOrder", strOrderTypes[which]);
                            coIntent.putExtra("order", (Serializable) order );
                            startActivity(coIntent);
                            dialog.dismiss();
                        }
                    }).show();

        }
    }

    /**
     * Thread read the data from tb_suborder and populate it to ListView thru custom adapter
     */
    class SubOrderDataThread extends Thread{
        @Override
        public void run() {
            // get the id of order
            dbManager = new DBManager(getApplicationContext());
                // Get opened order
                orderId = dbManager.getIdOpenOrder();

                order = dbManager.getOrderById(orderId);


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
                        btnCheckout.setEnabled(false);

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
                        btnCheckout.setEnabled(true);

                    }

                });

            }

        }
    }





}
