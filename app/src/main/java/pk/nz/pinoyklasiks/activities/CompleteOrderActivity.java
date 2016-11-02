package pk.nz.pinoyklasiks.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pk.nz.pinoyklasiks.MainActivity;
import pk.nz.pinoyklasiks.R;
import pk.nz.pinoyklasiks.beans.Address;
import pk.nz.pinoyklasiks.beans.Customer;
import pk.nz.pinoyklasiks.beans.District;
import pk.nz.pinoyklasiks.beans.Order;
import pk.nz.pinoyklasiks.beans.Status;
import pk.nz.pinoyklasiks.beans.Suburb;
import pk.nz.pinoyklasiks.beans.TypeOrder;
import pk.nz.pinoyklasiks.db.DBManager;
import pk.nz.pinoyklasiks.db.IDAOManager;
import pk.nz.pinoyklasiks.db.IDBInfo;
import pk.nz.pinoyklasiks.db.IWebService;
import pk.nz.pinoyklasiks.db.WebService;
import pk.nz.pinoyklasiks.service.CheckStatusService;
import utils.AppConst;


/**
 * <pre>
 * Title       : CompleteOrderActivity class
 * Purpose     : To finish the the order with product in the cart
 * Date        : 15.10.2016
 * Input       : order with open status
 * Proccessing : Customer will fill the fields with personal inforemation
 *               and after complenting the system will send the order to the
 *               serever and it will turn the service with checking status.
 *               In case customer has no internet connection will be offered
 *               to send order by SMS or make a Call to the restaurant.
 *
 * Output      : order
 *</pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public class CompleteOrderActivity extends AppCompatActivity {

    private final int DIALOG_DATE = 1;          // Dialog const. show DateDialog
    private final int DIALOG_TIME = 2;          // Dialog const. show TimeDialog
    private final int DIALOG_MSG  = 3;          // Dialog show the message that order was successfull
    private final int DIALOG_SEND_SMS = 4;      // Dialog offer user to send sms with ORDER
    private final int DIALOG_PHONE_AND_SMS = 5;      // Dialog offer user to send sms with ORDER or to call

    // FOR SENDING SMS
    private final String SENT_SMS = "SENT_SMS";
    private final String DELIVERED_SMS = "DELIVERED_SMS";
    private Intent sent_intent = new Intent(SENT_SMS);             //  To show SMS was sent
    private Intent delivered_intent = new Intent(DELIVERED_SMS);   //  and delivered
    private PendingIntent sentPi, deliveredPi;



    private Customer customer;          // Object will be recieved from DB to fill the field
    private EditText etCoCustomerName;  // Name of the customer
    private EditText etCoPhoneNumber;   // Phone of the customer
    private EditText etCoEmail;         // Email of the customer
    private TextView etCoDate;          // Order for this date
    private EditText etCoTime;          // Order for this time
    private TextView tvCoQuantityPerson;// Quantity of the persons
    private Button btnCoPlaceOrder;     // Button to submit the order
    private ImageButton btnCoMinus;     // Button to decrease the number of persons
    private ImageButton btnCoPlus;      // Button to increase the number of persons
    private EditText etCoComment;       // Addirional comments to the order

    private Calendar calendar;          // calendar tor work with date and time of order

    private int qCounter = 1;               // Quantity counter for number persons

    private String typeOrder;              // Type of object
    private Order order;                   // Order object passed from SubOrderActivity
    IDAOManager dbManager;                 // Work with DB
    IWebService webservice;                // Methods to use webservice





    // Formatter for DATETIME
    private SimpleDateFormat sdf = new SimpleDateFormat(IDBInfo.MYSQL_DATETIME_PATTERN);

    /*  This fields will be used in the next versions
    private Spinner spSuburb;
    private Spinner spCoDistrict;
    private EditText etCoAddress;
    */


    // TODO:TASK 1. 6)  4. onCreate
    /**
     *  Start activity CompleteOrderActivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order_activity);

        // Define vars
        assignViewsToVars();

        // get the VARS from intent
        typeOrder = getIntent().getExtras().getString("typeOrder");
        order = (Order)getIntent().getExtras().getSerializable("order");


    }


    // TODO:TASK 1. 6)  3. onResume
    /**
     * Method will define current date variable
     * (special for assignment outline)
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Register the broadcasts
        registerReceiver(sentReciever, new IntentFilter(SENT_SMS));
        registerReceiver(deliveredReciever, new IntentFilter(DELIVERED_SMS));


        if (AppConst.DEBUG) Log.d(AppConst.LOGD, " <<< onResume >>> :: define calendar");

       // get the current date
        calendar = Calendar.getInstance();

    }

    // TODO:TASK 1. 6)  2. onStop
    @Override
    protected void onStop() {
        super.onStop();

        // Unregister the broadcasts
        unregisterReceiver(sentReciever);
        unregisterReceiver(deliveredReciever);

    }

    /**
     * Assign the variables to views
     */
    private void assignViewsToVars(){


        // get the instance DB Mannager
        dbManager = new DBManager(this);

        // get the class to work with webservices
        webservice = new WebService(this);


        // Define Pending intents
        sentPi = PendingIntent.getBroadcast(CompleteOrderActivity.this, 0, sent_intent, 0);
        deliveredPi = PendingIntent.getBroadcast(CompleteOrderActivity.this, 0, delivered_intent, 0);

        // Add toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_complete_order);
            setSupportActionBar(toolbar);

            getSupportActionBar().setTitle("Complete the order");

                // Show the back button that leads to Cart back
                // back home page is SubOrderAcivity

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // customize back home button
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

            // The title of ActionBar
            getSupportActionBar().setTitle("Complete the order");

        // Get the fiels of activity
        etCoCustomerName = (EditText)findViewById(R.id.etCoCustomerName);
            etCoPhoneNumber = (EditText)findViewById(R.id.etCoPhoneNumber);
                etCoEmail = (EditText)findViewById(R.id.etCoEmail);
                    tvCoQuantityPerson = (TextView)findViewById(R.id.tvCoQuantityPerson);

        btnCoPlaceOrder = (Button)findViewById(R.id.btnCoPlaceOrder);
            btnCoPlaceOrder.setOnClickListener(new ClickListener());


        // Get button + and - and fields with tima and date
        // add listeners on click method
        etCoDate = (EditText) findViewById(R.id.etCoDate);
            etCoDate.setOnClickListener(new ClickListener());

        etCoTime = (EditText)findViewById(R.id.etCoTime);
            etCoTime.setOnClickListener(new ClickListener());

        btnCoMinus = (ImageButton) findViewById(R.id.btnCoMinus);
            btnCoMinus.setOnClickListener(new ClickListener());

        btnCoPlus = (ImageButton) findViewById(R.id.btnCoPlus);
            btnCoPlus.setOnClickListener(new ClickListener());

        etCoComment = (EditText)findViewById(R.id.etCoComment);

        // get the last customer and fill fields
        new TaskFillTheLastCustomer().execute();

    }


    /**
     * Add the top menu to ActionBar
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add menu from suborder (has the same needed item)
        getMenuInflater().inflate(R.menu.activity_complete_order, menu);

        return super.onCreateOptionsMenu(menu);

    }

    /**
     * Procceed the item menu actions
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       if(item.getItemId() == R.id.tmenu_co_clear_form){
           // clear the form
           clearForm();
       }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Clear fields in the form
     */
    private void clearForm(){
        etCoCustomerName.setText("");
        etCoPhoneNumber.setText("");
        etCoComment.setText("");
        etCoCustomerName.setText("");
        etCoDate.setText("");
        etCoTime.setText("");
        etCoEmail.setText("");
        etCoComment.setText("");

        tvCoQuantityPerson.setText("1");
        this.qCounter = 1;
    }

    // TODO: TASK 4. 1) Perfom input validation
    /**
     * Check if all required field is not empty
     * and valid to proceed
     * @return boolean
     */
    private boolean isFormValid(){
       boolean isValid = true;

        // get the messages from res
        String requredFieldMsg = getResources().getString(R.string.co_valform_required);
        String lengthLimitNameMsg = getResources().getString(R.string.co_valform_legth_limit_cust_name);
        String lengthLimitPhoneMsg = getResources().getString(R.string.co_valform_legth_limit_phone);
        String checkEmailMsg = getResources().getString(R.string.co_valform_check_email);

        // Check if the fields are empty show the error and return false
        if(etCoCustomerName.getText().toString().isEmpty()) {
            etCoCustomerName.setError(requredFieldMsg);
            isValid = false;
        }

        // Check the length of customer name
        if(etCoCustomerName.getText().toString().length() > 25) {
            etCoPhoneNumber.setError(lengthLimitNameMsg);
            isValid = false;
        }

            // checking the CUSTOMER NAME field
        if(etCoPhoneNumber.getText().toString().isEmpty()){
            etCoPhoneNumber.setError(requredFieldMsg);
            isValid = false;
        }

        // Check the length of phone number name
        if(etCoPhoneNumber.getText().toString().length() > 25) {
            etCoPhoneNumber.setError(lengthLimitPhoneMsg);
            isValid = false;
        }
            // checking the DATE field
        if(etCoDate.getText().toString().isEmpty()){
            etCoDate.setError(requredFieldMsg);
            isValid = false;
        }

        // check the Time field
        if(etCoTime.getText().toString().isEmpty()){
            etCoTime.setError(requredFieldMsg);
            isValid = false;
        }

        // check if the email valid
        // email in not required
        if(!isValidEmail(etCoEmail.getText())){
            etCoEmail.setError(checkEmailMsg);
            isValid = false;
        }





        if(AppConst.DEBUG) Log.d(AppConst.LOGD, "<<< CompleteOrderActivity >>> ::: isFormValid() ::: return : "+isValid);

        return isValid;
    }


    /**
     *  Show dialog
     *  DIALOG_SEND_SMS        ask send SMS with order
     *  DIALOG_PHONE_AND_SMS   ask send SMS or make a CALL
     *  DIALOG_MSG             tell that order was sent
     *  DIALOG_DATE            ask input date
     *  DIALOG_TIME            ask  input type
     *
     * @param idDialog int the type of dialog
     */
    private void openDialog(int idDialog){

        // SHOW SMS SEND DIALOG
        if(idDialog == DIALOG_SEND_SMS){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.co_sms_dialog_title)
            .setMessage(R.string.co_sms_dialog_msg)
                    .setNegativeButton(R.string.co_sms_dialog_no, null)
                    .setPositiveButton(R.string.co_sms_dialog_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            sendOrderViaSMS(order);

                        }
                    }).show();
        }

        // SHOW DIALOG WITH PHONE AND SMS OFFER
        if(idDialog == DIALOG_PHONE_AND_SMS){

                new AlertDialog.Builder(this)
                        .setTitle(R.string.co_sms_phone_dialog_title)
                        .setMessage(R.string.co_sms_phone_dialog_msg)
                        .setNegativeButton(R.string.co_sms_phone_dialog_call, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                // Set the new status to order and save it
                                order.setStatus(dbManager.getStatusById(7));
                                dbManager.saveOrder(order);

                                //close resource
                                dbManager.close();

                                // CALL TO MANAGER
                                Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+IDBInfo.PHONE_OF_RESTAURANT));
                                intentCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                try{
                                    startActivity(intentCall);
                                }catch (Exception e){
                                    Toast.makeText(getApplication(), "Sorry this function doesn't work :(", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setPositiveButton(R.string.co_sms_phone_dialog_sms, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // SEND SMS WITH ORDER
                                sendOrderViaSMS(order);

                            }
                        })
                        .setNeutralButton(R.string.co_sms_phone_dialog_later, null).show();

        }

        // SHOW MESSAGE ABOUT APPROVAL THE ORDER
        if(idDialog == DIALOG_MSG){
            new AlertDialog.Builder(this)
                .setTitle(R.string.co_title_dialog_msg)
                .setMessage(R.string.co_msg_dialog_msg)
                    .setPositiveButton(R.string.co_ok_btn_dialog_msg, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // TODO: 10/27/16 Start service to check status
                            Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(mainActivityIntent);

                        }
                    }).show();


        }
        // SHOW DATE DIALOG
        if(idDialog == DIALOG_DATE ){
            DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    // Read the date and put it into field
                    calendar.set(year, monthOfYear, dayOfMonth);
                    etCoDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));

                }
            } ,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        }

        // SHOW TIME DIALOG
        if(idDialog == DIALOG_TIME){
            TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    // read the time and put it into field
                    calendar.set(Calendar.HOUR, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    etCoTime.setText(String.format("%02d:%02d", hourOfDay , minute));

                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

            tpd.show();
        }


    }


    /**
     * Sending the Order
     * to SMS of manager resturant (Phonenumber in IDBInfo)
     * @param order
     */
    public void sendOrderViaSMS(Order order){

        SmsManager sms = SmsManager.getDefault();


        // Create ArraysList of PenfingIntent to send Multipart SMS
        // because the order can be long and there are some resctrictions
        ArrayList<PendingIntent> sendList = new ArrayList<>();
            sendList.add(sentPi);
        ArrayList<PendingIntent> deliverList = new ArrayList<>();
            deliverList.add(deliveredPi);
        ArrayList parts = sms.divideMessage(order.toString());

        // SEND SMS WITH ORDER
        sms.sendMultipartTextMessage(IDBInfo.PHONE_OF_RESTAURANT, null, parts,  sendList , deliverList);
    }


    // Get results from sending SMS with Order
    BroadcastReceiver sentReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()){
                case Activity.RESULT_OK:

                    // Show to customer message that sms sending
                    Toast.makeText(getApplicationContext(), "Sending order...", Toast.LENGTH_SHORT).show();

                    // CHANGE STATUS ORDER and save it in DB
                    order.setStatus(new Status(6, "SMS"));
                    dbManager.saveOrder(order);

                    // Close resources
                    dbManager.close();

                    // show dialog with info
                    // that manager will contact you to approve
                    // and go to the MainActivity
                    openDialog(DIALOG_MSG);


                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Error when sending order...", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    // Check if the SMS was sent
    BroadcastReceiver deliveredReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()){
                case Activity.RESULT_OK:
                    Toast.makeText(getApplicationContext(), "Order was delivered", Toast.LENGTH_SHORT).show();
                    openDialog(DIALOG_MSG);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Error when sending order...", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };



    /**
     * Title       : ClickListener class
     * Purpose     : Listener class which proceed severals view on the form
     * Date        : 15.10.2016
     * Input       : order with open status
     * Proccessing : If the button place order was pressed it gather and check and save information
     *               about order and customer after then send it.
     *               If it date ot time show appropriate dialog.
     *               If minus or plus increase or dcrease quantity
     *
     * Output      : action
     *
     * @author Mikhail PASTUSHKOV
     * @author Melchor RELATADO
     */
    class ClickListener implements View.OnClickListener{


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                    /////////////////////////////
                    // PLACE ORDER WAS CLICKED
                    ///////////////////////////
                case R.id.btnCoPlaceOrder:
                    if (AppConst.DEBUG){
                        Log.d(AppConst.LOGD,"<<< CompleteOrderActivity> >>> ::: ClickListnerer ::: PlaceOrder ");
                        Log.d(AppConst.LOGD," PlaceOrder ::: DateTime : "+sdf.format(calendar.getTime()));
                    }
                        // Get data send to the server and receive the global ID
                        // to track the order status
                    if(isFormValid()){
                        // Get the data from the form
                        // add this data to Order object

                            // Set default address of the restaurant
                            // Restaurant  still doesn't have dellivery
                        // // TODO: 10/24/16  Delivery (check the type and show hidden location)
                        Address address = new Address();
                            address.setLocation("8 Pilkington Rd, 'Pinoy Klasiks Rest.'");
                            address.setDistrict(new District(1, "City"));
                            address.setSuburb(new Suburb(38, "Panmure", new District(1, "City")));

                        // save the address in the local DB and fill ID
                            address.setId(dbManager.saveAddress(address));


                        // CREATE CUSTOMER object getting the fields from form
                        Customer customer = new Customer();
                            customer.setCustomerName(etCoCustomerName.getText().toString());
                            customer.setEmail(etCoEmail.getText().toString());
                            customer.setAdrress(address);
                            customer.setPhoneCustomer(etCoPhoneNumber.getText().toString());

                        // SAVE the CUSTOMER in the local DB and fill the id
                            customer.setId(dbManager.saveCustomer(customer));

                        int typeOrderId = dbManager.getTypeOrderIdByName(typeOrder);
                        TypeOrder typeOrder = dbManager.getTypeOrderById(typeOrderId);

                        // update the Order object

                        order.setTypeOrder(typeOrder);
                        order.setAddress(address);
                        order.setCustomer(customer);
                        order.setnumPersons( Integer.valueOf(tvCoQuantityPerson.getText().toString()) );
                        order.setOrderDatetimeNow(new Date());
                        order.setOrderDatetimeFor(calendar.getTime());
                        order.setComment(etCoComment.getText().toString());


                        // Save the ORDER in DB
                        dbManager.saveOrder(order);



                        // Check if customer has internet connection
                        // send the order to the server and receive
                        // global ID save it and start service to
                        // monitor changing status.

                        if(webservice.isOnline()){

                                // Send the object and recieve the the JSON from Server
                            int globalId  = webservice.sendJSONORder(order);

                            // CHECK IF ORDER ID WAS RETURNED 0 = SMTH WRONG

                            if (globalId != 0) { // IF SERVER RETURN RESULT DO


                                // Refresh object PUT GLOBAL ID and STATUS ID
                                // and save it in DB
                                order.setGlobalId(globalId);

                                order.setStatus(new Status(2, "Pending"));

                                // save Order with new status
                                dbManager.saveOrder(order);

                                // close resources
                                dbManager.close();

                                // Start service to check status of order
                                startService(new Intent(getApplicationContext(), CheckStatusService.class).putExtra("order", order));

                                if (AppConst.DEBUG)
                                    Log.d(AppConst.LOGD, "<<< CompleteOrderActivity >>> ::: BTN PLACE ORDER ::: GLOBALID : " + globalId);

                                openDialog(DIALOG_MSG);

                            }else{ // SERVER UNREACHABLE OR RETURN SOMETHING WRONG

                                // Offer to make an order
                                openDialog(DIALOG_PHONE_AND_SMS);

                            }


                        }else{ // NO INTERNET CONNECTION

                            if (AppConst.DEBUG) Log.d(AppConst.LOGD, "<<< CompleteOrderActivity >>> ::: BTN PLACE ORDER ::: NO CONNECTION");


                            // Customer don't have an internet access
                            // Offer to send the order using sms

                           openDialog(DIALOG_SEND_SMS);


                        }

                            if(AppConst.DEBUG){
                                Log.d(AppConst.LOGD, "<<<CompleteOrderActivity >>> ::: BTN PLACEORDER ::Address: "+address+ " Customer : "+customer);
                                Log.d(AppConst.LOGD, "<<<CompleteOrderActivity >>> ::: BTN PLACEORDER ::Order : "+order);
                            }
                    }else{
                        Toast.makeText(getApplicationContext(), R.string.co_form_message_req_field, Toast.LENGTH_SHORT).show();
                    }


                    if(AppConst.DEBUG) Log.d(AppConst.LOGD, "<<< CompleteOrderActivity >>> ::: CLICKED *PLACE ORDER* ");
                    break;

                // BUTTON PLUS
                case R.id.btnCoPlus:
                    // Increase the number of persons
                        tvCoQuantityPerson.setText( String.valueOf(++qCounter) );
                    break;

                // BUTTON MINUS
                case R.id.btnCoMinus:
                    // Decrease the number of persons
                    if(qCounter > 1){
                        tvCoQuantityPerson.setText( String.valueOf( --qCounter ));
                    }
                    break;

                // DATEDIALOG
                case R.id.etCoDate:
                    etCoDate.setError(null);// remove error message
                    etCoDate.clearFocus();
                    openDialog(DIALOG_DATE);

                    break;

                // TIMEDIALOG
                case R.id.etCoTime:
                    etCoTime.setError(null); // remove error message
                    etCoTime.clearFocus();
                    openDialog(DIALOG_TIME);
                    break;
            }
        }
    }


    /**
     * Check if the string with email
     * empty return true (because not required)
     * otherwise check validity of email
     * and return result as true or false
     *
     * @param target String with email
     * @return boolean
     */
    public boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return true;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    /**
     * Title       : TaskFillTheLastCustomer class
     * Purpose     : Fill fields with existed customer
     * Date        : 12.10.2016
     * Input       : none
     * Proccessing : Check in DB for the last customer in DB
     * Output      : action
     *
     * @author Mikhail PASTUSHKOV
     * @author Melchor RELATADO
     */
    class TaskFillTheLastCustomer extends AsyncTask<Void, Customer, Void>{
        @Override
        protected Void doInBackground(Void... params) {
           Customer customer = dbManager.getLastCustomer();
            publishProgress(customer);

            return null;
        }

        @Override
        protected void onProgressUpdate(Customer... values) {
            super.onProgressUpdate(values);

            Customer customer = values[0];
            if(customer != null){

                // If there is customer in the DB
                // get the object and fill the field
                    etCoCustomerName.setText(customer.getCustomerName());
                        etCoPhoneNumber.setText(customer.getPhoneCustomer());
                            etCoEmail.setText(customer.getEmail());
                dbManager.close();
            }

        }
    }

}
