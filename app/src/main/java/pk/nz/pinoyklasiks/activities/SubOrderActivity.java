package pk.nz.pinoyklasiks.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import pk.nz.pinoyklasiks.R;
import pk.nz.pinoyklasiks.beans.AbstractProduct;
import pk.nz.pinoyklasiks.beans.Order;
import pk.nz.pinoyklasiks.beans.SubOrder;
import pk.nz.pinoyklasiks.db.DBManager;
import pk.nz.pinoyklasiks.db.IDAOManager;
import utils.AppConst;
import utils.SubOrderProductAdapter;

/**<pre>
 *
 * Title       : SubOrderActivity class
 * Purpose     : Class Activity represent the Cart
 *               and List of order from history
 * Date        : 28.09.2016
 * Input       : Order object
 * Proccessing : Has two aims if the that defined by action in
 *               the intent
 *               1) Cart: proccess checkout of order and calulate the total sum
 *               2) History show the order from history and allow customer to reorder
 * Output      :  ListView with suborder
 *
 * </pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public class SubOrderActivity extends AppCompatActivity {

    // constant for context menu describe
    // action should be accomplished
    private final int CTX_MENU_CLEAR = 1;
    private final int CTX_MENU_CHECKOUT = 2;

    // Constant for cheking action of intent
    private final String OPEN_SUBORDER = "pk.nz.pinoyklasiks.open_suborder";
    private final String HISTORY_SUBORDER = "pk.nz.pinoyklasiks.history_suborder";

    // current action from intent
    private String intentAction;



    private int orderId;                            // Represent id of order

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


        handler = new Handler();                    // handler to work with UI
        intentAction = getIntent().getAction();     // get intent action




        //find list view and bind the adapter for popuating data
        lvSubOrders = (ListView)findViewById(R.id.lvSubOrder);


                // get the view for the price
                tvSubOrderTotal = (TextView)findViewById(R.id.tvSubOrderTotal);

        // Checkout button use listener described at the bottom of this activity
        btnCheckout = (Button)findViewById(R.id.subOrderCheckOut);
            btnCheckout.setOnClickListener(new CheckoutListener());

        // Change behaviour of the button "Checkout"
        // if Action is HISTORY_SUBORDER and chamge the
        // text on the button to "Reorder"
        if(intentAction == HISTORY_SUBORDER){
            btnCheckout.setOnClickListener(new ReorderListener());
            btnCheckout.setText("Reorder");

            // register context menu for the list view
            registerForContextMenu(lvSubOrders);
        }
        // Add toolbar
        addToolbar();

        //Start thread which fetch the data from DB and fill ListView
        SubOrderDataThread sodt = new SubOrderDataThread();
        sodt.start();



    }

    /**
     * Add toolbar and decribe it settings
     */
    private void addToolbar(){
        // Add toolbar to activity
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_suborder);
           setSupportActionBar(toolbar);
                setTitle(R.string.cartTitle);
                    getSupportActionBar().setIcon(R.drawable.ic_cart);


        // Change name of the toolbar if action is history
        if(intentAction == HISTORY_SUBORDER){
            setTitle(R.string.sub_act_history_toolbar_title);
        }
                // Turn on the button home
                getSupportActionBar().setHomeButtonEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                // customize back home button
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

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
            menu.add(0,CTX_MENU_CHECKOUT,0, "Checkout the order");
        }
    }

    /**
     * Procceed the context menu actions
     *
     * @param item
     * @return boolean
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // Check the button was clicked
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

        // Hiide the button clear if showing history
       if(intentAction == HISTORY_SUBORDER) menu.findItem(R.id.tmenu_clear_cart).setVisible(false);

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

        // If no product in the cart and presssed CLEAN Cart
        // show message 'Cart is empty'
        // otherwise clean it
        if(item.getItemId() == R.id.tmenu_clear_cart  && subOrder != null){
           clearCart();
        }else

        if(item.getItemId() == R.id.tmenu_clear_cart) {
            Toast.makeText(this, AppConst.CART_IS_EMPTY, Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Clear cart method
     * show the approval dialog
     * and then clean the cart
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
    * Title       : ReorderListener class
    * Purpose     : Add product from current order to new order
    * Date        : 31.10.2016
    * Input       : Order object
    * Proccessing : Get the order object and copy
    *               it to new order in the diffrent Thread
    *
    * Output      :  Create new record with order or add to opened
    *
    * @author Mikhail PASTUSHKOV
    * @author Melchor RELATADO
    */
    class ReorderListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {

            new Thread(new Runnable() {

            // Take SubOrder from Order
            SubOrder suborder = order.getSuborder();
            IDAOManager dbManager = new DBManager(getApplicationContext());

            // Get the Map of product
            Map<AbstractProduct, Integer> mapProducts = suborder.getMapProducts();

                @Override
                public void run() {
                    // Go through map of prooduct
                    // and add them to the new order
                    for(AbstractProduct product : mapProducts.keySet()){
                        new Thread();
                        dbManager.addProductToOrder(product, mapProducts.get(product), true);
                        dbManager.close();

                        //Move customer to cart
                        Intent intentSuborder = new Intent("pk.nz.pinoyklasiks.open_suborder");
                        startActivity(intentSuborder);

                    }

                }
            }).start();

        }
    }


    /**
     * Title       : CheckoutListener class
     * Purpose     : Listener class show the choice of type order
     *               and move customer to the order form
     *
     * Date        : 15.09.2016
     * Input       : Order
     * Proccessing : Display the dialog with type of order and put the current
     *               order to intent and pass it to CompleteOrderActivity
     * Output      : none
     *
     * @author Mikhail PASTUSHKOV
     * @author Melchor RELATADO
     */
    class CheckoutListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {


            // TODO: 10/19/16  read ordersTypes from db and populate in the dialog (sponsor not ready to work with delivery)
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
     * Title       : SubOrderDataThread class
     * Purpose     : Thread class get the Order and populate it into ListView
     * Date        : 01.10.2016
     * Input       : Action of SubOrder intent
     * Proccessing : Check the Action and if thereis open_suborder action
     *             : receive it from Database or if it History get from Inten
     *             : and using SubOrderProduct adapter fill ListView
     * Output      : none
     *
     * @author Mikhail PASTUSHKOV
     * @author Melchor RELATADO
     */
    class SubOrderDataThread extends Thread{
        @Override
        public void run() {

            // If "Cart" was choosen to open activity
            // the read open order from DN
          if(intentAction == OPEN_SUBORDER){
              // get the id of order
              dbManager = new DBManager(getApplicationContext());
              // Get opened order
              orderId = dbManager.getIdOpenOrder();

              // Receive the order
              order = dbManager.getOrderById(orderId);

              // get the suborder of order (Cart)
              subOrder = dbManager.getSubOrderByOrderId(orderId);

              // Close resources
              dbManager.close();
          }

          if(intentAction == HISTORY_SUBORDER){

              // get the order from intent
              order = (Order)getIntent().getExtras().getSerializable("order");

              // get suborder from order
              subOrder = order.getSuborder();


          }

            // If there are no products in the suborder clear the ListView
            if(subOrder == null){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), AppConst.CART_IS_EMPTY, Toast.LENGTH_SHORT).show();
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

                            if(intentAction == HISTORY_SUBORDER) {
                                adapter = new SubOrderProductAdapter(SubOrderActivity.this, listProducts, orderId, false );
                            }else{
                                adapter = new SubOrderProductAdapter(SubOrderActivity.this, listProducts, orderId, true);
                            }

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
