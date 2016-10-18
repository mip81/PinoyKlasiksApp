package utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import pk.nz.pinoyklasiks.R;
import pk.nz.pinoyklasiks.beans.AbstractProduct;
import pk.nz.pinoyklasiks.beans.Product;
import pk.nz.pinoyklasiks.beans.SubOrder;
import pk.nz.pinoyklasiks.db.DBManager;
import pk.nz.pinoyklasiks.db.IDAOManager;

/**
 * Product adater bind the view in layout with data
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class ProductAdapter extends ArrayAdapter<AbstractProduct>{

    private Handler handler;
    private DecimalFormat df = new DecimalFormat("$##.00");




    // Constructor of customer adapter
    public ProductAdapter(Context context, List<AbstractProduct> products) {
        super(context, 0, products);
        handler = new Handler();
    }

    /**
     * Bind views in layout to fields of AbstractCategory object
     * @param position in ArrayList<AbstractCategory>
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the AbstractCategory object for this position
        AbstractProduct abstractProduct = getItem(position);

        // Check if this view null inflate it create another one
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lv_products, parent, false);
        }

        // Get the view to populate new data
        TextView tvProductName = (TextView)convertView.findViewById(R.id.tvLVProductName);
        TextView tvProductDesc = (TextView)convertView.findViewById(R.id.tvLVProductDescription);
        TextView tvProductPrice = (TextView)convertView.findViewById(R.id.tvLVProductPrice);
        ImageView ivPic = (ImageView)convertView.findViewById(R.id.ivLVProductPic);
        ImageButton btnAddToCart = (ImageButton)convertView.findViewById(R.id.btnLVAddToCart);
          btnAddToCart.setFocusable(false);
          btnAddToCart.setFocusableInTouchMode(true);



        try{
            InputStream is = getContext().getAssets().open(abstractProduct.getProduct_pic()+".jpg");
            Drawable pic = Drawable.createFromStream(is, null);
            ivPic.setImageDrawable(pic);  // Read the pic of product and assign it to view

        }catch (Exception e){
            e.printStackTrace();
            ivPic.setImageResource(R.drawable.nopic);
            Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Populate our data into the layout views
            tvProductName.setText(abstractProduct.getProduct_name());
            tvProductDesc.setText(abstractProduct.getProduct_desc());
            tvProductPrice.setText( df.format(abstractProduct.getProduct_price()) ) ;
            btnAddToCart.setOnClickListener(new ClickAddProductListener(abstractProduct));


        return convertView;
    }


    // Click "ADD PRODUCT TO CART" button listener
    class ClickAddProductListener implements View.OnClickListener{

        AbstractProduct product; // Product to add

        ClickAddProductListener(AbstractProduct product){
            this.product = product;
        }

        @Override
        public void onClick(View v) {

            // Add the product to cart
            // insert into tb_suborder
            new Thread(new Runnable() {
                @Override
                public void run() {

                    IDAOManager db = new DBManager(getContext());
                    db.addProductToOrder(product, 1 , false);
                    db.close();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), product.getProduct_name()+ " added to the cart", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();
        }
    }

}
