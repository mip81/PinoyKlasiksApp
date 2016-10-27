package pk.nz.pinoyklasiks.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


import java.io.InputStream;
import java.util.List;

import pk.nz.pinoyklasiks.R;
import pk.nz.pinoyklasiks.beans.AbstractProduct;
import pk.nz.pinoyklasiks.beans.Product;
import pk.nz.pinoyklasiks.db.DBManager;
import pk.nz.pinoyklasiks.db.IDAOManager;
import pk.nz.pinoyklasiks.db.IDBInfo;
import pk.nz.pinoyklasiks.db.IDBManager;
import utils.AppConst;
import utils.ProductAdapter;

/**
 * Activity shows the choosen category
 * of products with name, desc and price
 *
 * @Author Mikhail PASTUSHKOV
 * @Author Melchor RELATADO
 */

public class ListOfProductsActivity extends AppCompatActivity {
    private final String LOG = "::: DEBUG :::";
    private static final boolean DEBUG = true; // the variable enable all logging in the class
    private int idCategory;      // the id of AbstractCategory which we get from category
    private String nameCategory; // the Name of choosen AbstractCategory
    private Handler handler;    // handler to work with views in the thread


    private ListView lvProducts; // ListView with information about products in the category
    private IDAOManager dbManager; // DB helper class for working with Database
    private List<AbstractProduct> listProducts;
    private ProductAdapter productAdapter; // custome adapter for listview of products


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_products);

        // get ORDER_ID the extras from intent
        nameCategory = getIntent().getStringExtra("nameCategory");
        idCategory = this.getIntent().getExtras().getInt("idCategory");

        if (DEBUG)
            Log.d(LOG, "Intent brought : idCategory : " + idCategory + " nameCategory : " + nameCategory);

        // get DbManager to work with DB
        dbManager = new DBManager(getApplicationContext());


        //Set toolbar and home button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_products);
        setSupportActionBar(toolbar);                           // Set toolbar
        getSupportActionBar().setTitle("Menu :: " + nameCategory.toLowerCase());           // Set title for toolbar
        getSupportActionBar().setHomeButtonEnabled(true);      // Show the "GoBack" button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Process it to Up Level
        /** !!! To specify parent activity to put code inside MANIFEST file in this actvity TAG
         *       <meta-data android:name="android.support.PARENT_ACTIVITY" android:value=".MainActivity" />
         */


        //get ListView and define  click action on it
        lvProducts = (ListView) findViewById(R.id.lvProducts);
            lvProducts.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                lvProducts.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                // read the product and pass it to the ProductView
                                AbstractProduct product = listProducts.get(position);
                                if (DEBUG) Log.d(LOG, product.toString());
                                Intent productIntent = new Intent(ListOfProductsActivity.this, ProductActivity.class);
                                productIntent.putExtra("product", product);
                                startActivity(productIntent);

                            }
        });

        // get the open order and get adapter for ListView
        new GetSuborderTask().execute();
    }


    // Get the ActionBar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_product, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.tmenu_cart){

            // open suborder activity (with status OPEN)
            startActivity(new Intent("pk.nz.pinoyklasiks.open_suborder"));
            if(AppConst.DEBUG) Log.d(AppConst.LOGD, " OPEN SUBORDER ACTIVITY WITH STATUS OPEN :::" );
        }
        return super.onOptionsItemSelected(item);
    }




    /**
     * Thread get the list of products of category and fill the listview
     */
    class GetSuborderTask extends AsyncTask<Void, List<AbstractProduct>, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            listProducts = dbManager.getProductByIdCat(idCategory);
            publishProgress(listProducts);
            return null;
        }

        @Override
        protected void onProgressUpdate(List... lp) {

            // bind the list with layout and assign adapter with List of products to ListView
            productAdapter = new ProductAdapter(getApplicationContext(), lp[0]);
            lvProducts.setAdapter(productAdapter);
        }
    }


}
