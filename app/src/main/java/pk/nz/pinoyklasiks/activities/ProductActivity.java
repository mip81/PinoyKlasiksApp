package pk.nz.pinoyklasiks.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.text.DecimalFormat;

import pk.nz.pinoyklasiks.R;
import pk.nz.pinoyklasiks.beans.AbstractProduct;
import pk.nz.pinoyklasiks.beans.Product;
import pk.nz.pinoyklasiks.db.IDAOManager;

/**
 * Created 10/11/16.
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class ProductActivity extends AppCompatActivity {

    private final boolean DEBUG = true;
    private final String LOG = "::: DEBUG :::";
    private final String IMG_EXT = ".jpg";

    private AbstractProduct product; // product that will shows its details
    private ImageView ivProduct;  // Picture of the product
    private TextView tvProductDesc;  // Description of the product
    private TextView tvProductName;  // Name of the product
    private TextView tvProductPrice; // Price of the product
    private TextView tvProductQuantity; // Quantity of the product
    private Button btnPlus;
    private Button btnMinus;
    private Button btnAddToCart;

    private int quantity = 1;

    private DecimalFormat dfPrice = new DecimalFormat("$##.00");

    IDAOManager dbManager; // Class serve operatyions with DB


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_product_activity);  // read toolbar added in Activity file (using include tag)
            setSupportActionBar(toolbar);                                       // add toolbar
            getSupportActionBar().setHomeButtonEnabled(true);                   // enable the home button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);              // return user to one level up

        // get views of activity
        ivProduct = (ImageView)findViewById(R.id.ivProductPic);
            tvProductName = (TextView)findViewById(R.id.tvProductName);
                tvProductDesc = (TextView)findViewById(R.id.tvProductDesc);
                    tvProductPrice = (TextView)findViewById(R.id.tvProductPrice);
                        tvProductQuantity = (TextView)findViewById(R.id.tvProductQuantity);

                        btnMinus = (Button)findViewById(R.id.btnMinus);
                        btnMinus.setOnClickListener(new ClickTheQuantityBtn());

                            btnPlus = (Button)findViewById(R.id.btnPlus);
                            btnPlus.setOnClickListener(new ClickTheQuantityBtn());

                                btnAddToCart = (Button)findViewById(R.id.btnPlus);

        //get the product from intent
        product = (AbstractProduct) getIntent().getExtras().get("product");
            if(DEBUG) Log.d(LOG, "ProductActivity : product brought intent : "+product.toString() );

            // check if the object not null
            if(product == null){
                Toast.makeText( getApplicationContext(), "Sorry! This product is not available!", Toast.LENGTH_SHORT).show();
                return;
            }

        // fill the info about the product
        fillTheForm(product);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);


        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Fill the form with info
     * about the product
     * @param product the AbstractProduct class
     */
    private void fillTheForm( AbstractProduct product ){
        tvProductName.setText( product.getProduct_name() );
        tvProductDesc.setText( product.getProduct_desc() );
        tvProductPrice.setText( dfPrice.format(product.getProduct_price()) );
        tvProductQuantity.setText(""+quantity);

        InputStream is = null;
        try{
            is  = getAssets().open(product.getProduct_pic()+IMG_EXT); // read asset with image
            Drawable d = Drawable.createFromStream(is, null); // create Drawable imamge
            ivProduct.setImageDrawable(d);                    // sssign to the ImageView

        }catch (Exception e){
            ivProduct.setImageResource(R.drawable.nopic);   // set nopic image
            Log.e(" ::: ERROR ::: ", "fillTheForm() : ",e);

        }finally {
            try{
                if (is != null )is.close();
            }catch (Exception e){
                Log.e(" ::: ERROR ::: ", "fillTheForm() , close 'IS': ",e);
            }

        }

    }


    class ClickTheQuantityBtn implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btnPlus){
                tvProductQuantity.setText( String.valueOf(++quantity) );
            }
            if(v.getId() == R.id.btnMinus && quantity > 1){
                tvProductQuantity.setText( String.valueOf(--quantity) );
            }
        }
    }
}