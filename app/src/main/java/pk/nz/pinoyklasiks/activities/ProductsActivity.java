package pk.nz.pinoyklasiks.activities;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.*;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.InputStream;

import pk.nz.pinoyklasiks.*;
import pk.nz.pinoyklasiks.R;
import pk.nz.pinoyklasiks.db.DBManager;
import pk.nz.pinoyklasiks.db.IDBInfo;
import pk.nz.pinoyklasiks.db.IDBManager;

/**
 *
 * Activity shows the choosen category
 * of products with name, desc and price
 * @Author Mikhail PASTUSHKOV
* @Author Melchor RELATADO
*/

public class ProductsActivity extends AppCompatActivity {

    private static final boolean DEBUG = true; // the variable enable all logging in the class
    private int idCategory; // the id category which we get from category
    private Handler handler;    // handler to work with views in the thread
    private Cursor cursor;      // cursor that will be used in the SimpleCursorAdapter
    private ListView lvProducts; // ListView with information about products in the category
    private IDBManager dbManager; // DB helper class for working with Database

    private String[] from = {IDBInfo.TB_PRODUCT_PRODUCT_NAME,
                                IDBInfo.TB_PRODUCT_PRODUCT_DESC,
                                IDBInfo.TB_PRODUCT_PRODUCT_PIC,
                                IDBInfo.TB_PRODUCT_PRODUCT_PRICE};
    private int[] to = {R.id.tvProductName, R.id.tvProductDescription, R.id.ivProduct, R.id.tvProductPrice};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);




        // getting id from the mainActivity
        idCategory = this.getIntent().getExtras().getInt("idCategory");
        Toast.makeText(this, " ID "+idCategory, Toast.LENGTH_SHORT).show();

        // get DbManager to work with DB
        dbManager = new DBManager(getApplicationContext());

        //get ListView
        lvProducts = (ListView)findViewById(R.id.lvProducts);
        cursor = dbManager.getProducts(idCategory);
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, R.layout.activity_lv_products, cursor, from, to, 0);

            // Set VIEWBINDER when we get the data from cursor
            // we read the filename then read the image with that file from assets
            // making drawable object and assign it to the ImageView in ListView of products
        sca.setViewBinder(new SimpleCursorAdapter.ViewBinder(){

            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if(view.getId() == R.id.ivProduct){
                        ImageView iv = (ImageView)view;
                        String filename = cursor.getString(cursor.getColumnIndex(IDBInfo.TB_PRODUCT_PRODUCT_PIC))+".jpg";
                        int resId =  getApplicationContext().getResources().getIdentifier(filename, "drawable", getApplicationContext().getPackageName() );

                    try{
                        //iv.setImageDrawable(ResourcesCompat.getDrawable(getResources(), resId, null));  // getApplicationContext().getDrawable(resId));
                        InputStream is = getAssets().open(filename);
                        Drawable d = Drawable.createFromStream(is, null);
                        iv.setImageDrawable(d);

                    }catch (Exception e){
                        iv.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.nopic, null));
                        if(DEBUG) Log.d("Exception : ", "ViewBinder when attemped to set image "+filename+"  ::  " +e);
                    }
                    return true;

                }
                return false;
            }
        });
        lvProducts.setAdapter(sca);



    }




    // Retrieve cursor from DB
    class GetCusorFromDb extends Thread{
        @Override
        public void run() {



        }
    }
}
