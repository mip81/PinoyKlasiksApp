package utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import pk.nz.pinoyklasiks.R;
import pk.nz.pinoyklasiks.activities.SubOrderActivity;
import pk.nz.pinoyklasiks.beans.AbstractProduct;
import pk.nz.pinoyklasiks.db.DBManager;
import pk.nz.pinoyklasiks.db.IDAOManager;

/**
 * Product adater for suborder bind the view in layout with data
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class SubOrderProductAdapter extends ArrayAdapter<Map.Entry>{

    private IDAOManager dbManager;

    // Constructor of customer adapter
    public SubOrderProductAdapter(Context context, List entryProducts) {
        super(context, 0, entryProducts);
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

        DecimalFormat df = new DecimalFormat("$##.00"); // Format for the price

        // Get the the Entry consist AbstractProduc and quantity
        Map.Entry<AbstractProduct, Integer> entry = getItem(position);
         // Get the product
        final AbstractProduct abstractProduct =entry.getKey();
        int quantity = entry.getValue();

        // Calculate the price
        Double cost = abstractProduct.getProduct_price() * Double.valueOf(entry.getValue());

        // Check if this view null inflate it create another one
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lv_suborder_products, parent, false);
        }

        // Get the view to populate new data
        TextView tvProductName = (TextView)convertView.findViewById(R.id.tvLVSubOrderProductName);
            TextView tvProductPrice = (TextView)convertView.findViewById(R.id.tvLVSubOrderProductPrice);
                TextView tvProductTotalPrice = (TextView)convertView.findViewById(R.id.tvLVSubOrderTotalPrice);
                    TextView tvProductQuantity = (TextView)convertView.findViewById(R.id.tvLVSubOrderQuantity);

        ImageButton btnPlus = (ImageButton)convertView.findViewById(R.id.btnLVSubOrderPlus);
            btnPlus.setOnClickListener(new ClickPlusMinusQuantityListener(abstractProduct, tvProductQuantity));

        ImageButton btnMinus = (ImageButton)convertView.findViewById(R.id.btnLVSubOrderMinus);
            btnMinus.setOnClickListener(new ClickPlusMinusQuantityListener(abstractProduct, tvProductQuantity));

        ImageView ivPic = (ImageView)convertView.findViewById(R.id.ivLVSubOrderProductPic);


       // Button btnAddToCart = (Button)convertView.findViewById(R.id.btnLVAddToCart);

        if(AppConst.DEBUG) Log.d(AppConst.LOGD, " SubOrderProductAdapter : prod. name : "+abstractProduct.getProduct_name()+
                                                " price : "+abstractProduct.getProduct_price()+ " quant. : "+quantity);

        try{
            InputStream is = getContext().getAssets().open(abstractProduct.getProduct_pic()+".jpg");
            Drawable pic = Drawable.createFromStream(is, null);
            ivPic.setImageDrawable(pic);  // Read the pic of product and assign it to view

        }catch (IOException e){
            e.printStackTrace();
            ivPic.setImageResource(R.drawable.nopic);
        }


            //Populate our date into the layout views
            tvProductName.setText(abstractProduct.getProduct_name());
            tvProductPrice.setText(df.format(abstractProduct.getProduct_price()) ) ;
            tvProductTotalPrice.setText(" x "+quantity+" : "+df.format(cost));
            tvProductQuantity.setText(""+quantity);


        return convertView;
    }


    // Click reduce or increase amount of product
    class ClickPlusMinusQuantityListener implements View.OnClickListener{

        AbstractProduct product; // Product to add
        TextView tvQuantity;

        ClickPlusMinusQuantityListener(AbstractProduct product, TextView tvQuantity){
            this.product = product;
            this.tvQuantity = tvQuantity;
        }

        @Override
        public void onClick(View v) {
            // reduce or increase quatity of product in the cart
            int quantity = Integer.valueOf(tvQuantity.getText().toString());

            if(AppConst.DEBUG) Log.d(AppConst.LOGD, SubOrderProductAdapter.class.getCanonicalName()+" ::: "+
                                    " ClickPlusMinusQuantityListener ::: product : "+product);

                // reduce
            if(v.getId( ) == R.id.btnLVSubOrderMinus && quantity >  1){
                tvQuantity.setText(String.valueOf(--quantity));
            }
              // increase
            if(v.getId() == R.id.btnLVSubOrderPlus) {
                tvQuantity.setText(String.valueOf(++quantity));
            }

            // update DB with new quantity of product
            new UpdateProduct(product, quantity).start();

            //notifyDataSetChanged();

        }
    }

    /**
     * class for doing reduce or increase amount for the product in the cart
     */
    class UpdateProduct extends Thread{
        AbstractProduct product;
        int quantity;

        UpdateProduct(AbstractProduct product, int quantity){
            this.product = product;
            this.quantity = quantity;
        }

        @Override
        public void run() {

            if(AppConst.DEBUG) Log.d(AppConst.LOGD, SubOrderProductAdapter.class.getCanonicalName()+" :::"+
                                    " UpdateProduct ::: "+product+" quant.:"+quantity);
            dbManager = new DBManager(getContext());
            dbManager.addProductToOrder(product, quantity, true);
            dbManager.close();

        }
    }
}
