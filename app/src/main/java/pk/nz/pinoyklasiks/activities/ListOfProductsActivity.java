package pk.nz.pinoyklasiks.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import utils.ProductAdapter;

/**
 *
 * Activity shows the choosen category
 * of products with name, desc and price
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

        // get the extras from intent
            nameCategory = getIntent().getStringExtra("nameCategory");
            idCategory = this.getIntent().getExtras().getInt("idCategory");
        if(DEBUG) Log.d( LOG, "Intent brought : idCategory : "+idCategory+" nameCategory : "+nameCategory );

        // get DbManager to work with DB
        dbManager = new DBManager(getApplicationContext());
        listProducts = dbManager.getProductByIdCat(idCategory);
        productAdapter = new ProductAdapter(getApplicationContext(), listProducts); // bind the list with layout


        //Set toolbar and home button
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_products);
        setSupportActionBar(toolbar);                           // Set toolbar
        getSupportActionBar().setTitle("Menu :: "+nameCategory.toLowerCase());           // Set title for toolbar
        getSupportActionBar().setHomeButtonEnabled(true);      // Show the "GoBack" button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Process it to Up Level
                /** !!! To specify parent activity to put code inside MANIFEST file in this actvity TAG
                *       <meta-data android:name="android.support.PARENT_ACTIVITY" android:value=".MainActivity" />
                */


        //get ListView
        lvProducts = (ListView)findViewById(R.id.lvProducts);
            lvProducts.setAdapter(productAdapter);
                lvProducts.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // read the product and pass it to the ProductView
                AbstractProduct product = listProducts.get(position);
                 if(DEBUG) Log.d(LOG, product.toString());
                    Intent productIntent = new Intent(ListOfProductsActivity.this, ProductActivity.class);
                        productIntent.putExtra("product", product);
                         startActivity(productIntent);

            }
        });

    }




    // Retrieve cursor from DB
    class GetCusorFromDb extends Thread{
        @Override
        public void run() {
            // TODO: 10/9/16  getting ListView here . Later



        }
    }


    //LISTENER FOR CATEGORIES
//    class ClickProductListener implements AdapterView.OnItemClickListener{
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//            if(DEBUG) Log.d(LOG, "Clicked the ListView");
//            // get the id of categories and pass it to the ProductActivity
//            String name =  listProducts.get(position-1).getProduct_name();
//
//            Toast.makeText(getApplicationContext(), " "+name, Toast.LENGTH_SHORT).show();
//            //String nameCategory = listProducts.get(position-1).getCat_name();
//            // position - 1 because of header was attached before

//
//            Intent intentProducts = new Intent(getApplicationContext(), ListOfProductsActivity.class);
//            intentProducts.putExtra("idCategory", idCategory);      // pass the id category
//            intentProducts.putExtra("nameCategory", nameCategory ); // pass the name of category
//            startActivity(intentProducts);
//        }
//    }
}
