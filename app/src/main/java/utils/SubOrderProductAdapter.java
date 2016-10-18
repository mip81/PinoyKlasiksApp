package utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;;
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
    private final String CLASSNAME = SubOrderProductAdapter.class.getCanonicalName();
    private IDAOManager dbManager;
    private List listProduct;
    private Context context;
    private int orderId;


    // Constructor of customer adapter
    public SubOrderProductAdapter(Context context, List entryProducts, int orderId) {
        super(context, 0, entryProducts);

        listProduct = entryProducts;
        this.context = context;
        this.orderId = orderId;
        dbManager = new DBManager(getContext());

    }




    /**
     * Bind views in layout to fields of AbstractCategory object
     * @param position in ArrayList<AbstractCategory>
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DecimalFormat df = new DecimalFormat("$##.00"); // Format for the price



        // Get the the Entry consist AbstractProduct and quantity
        Map.Entry<AbstractProduct, Integer> entry = getItem(position);

        // Get the product from map
        final AbstractProduct abstractProduct =entry.getKey();
        int quantity = entry.getValue();

        // Calculate the price
        Double cost = abstractProduct.getProduct_price() * Double.valueOf(entry.getValue());

        // Check if this view null inflate it create another one
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lv_suborder_products, parent, false);
        }

        // Get the VIEWS from layout lv_sub_order_product
        // to populate new data
        TextView tvProductName = (TextView)convertView.findViewById(R.id.tvLVSubOrderProductName);
            TextView tvProductPrice = (TextView)convertView.findViewById(R.id.tvLVSubOrderProductPrice);
                TextView tvProductTotalPrice = (TextView)convertView.findViewById(R.id.tvLVSubOrderTotalPrice);
                    TextView tvProductQuantity = (TextView)convertView.findViewById(R.id.tvLVSubOrderQuantity);

        //Get the buttons from layout +, - . delete
        ImageButton btnPlus = (ImageButton)convertView.findViewById(R.id.btnLVSubOrderPlus);
            btnPlus.setOnClickListener(new ClickPlusMinusQuantityListener(abstractProduct, tvProductQuantity));

        ImageButton btnMinus = (ImageButton)convertView.findViewById(R.id.btnLVSubOrderMinus);
            btnMinus.setOnClickListener(new ClickPlusMinusQuantityListener(abstractProduct, tvProductQuantity));

        ImageButton btnDelete = (ImageButton)convertView.findViewById(R.id.btnLVSubOrderDel);
            btnDelete.setOnClickListener(new ClickDeleteProductListener(orderId, abstractProduct));


        ImageView ivPic = (ImageView)convertView.findViewById(R.id.ivLVSubOrderProductPic);


                if(AppConst.DEBUG) Log.d(AppConst.LOGD, " SubOrderProductAdapter : prod. name : "+abstractProduct.getProduct_name()+
                                                        " price : "+abstractProduct.getProduct_price()+ " quant. : "+quantity);


        // Read images from the assets to imageView (names from product object)
        try{
            InputStream is = getContext().getAssets().open(abstractProduct.getProduct_pic()+".jpg");
            Drawable pic = Drawable.createFromStream(is, null);
            ivPic.setImageDrawable(pic);  // Read the pic of product and assign it to view

        }catch (IOException e){
            e.printStackTrace();
            ivPic.setImageResource(R.drawable.nopic);
        }


            //Populate our date into the layout lv_suborder
            tvProductName.setText(abstractProduct.getProduct_name());
            tvProductPrice.setText(df.format(abstractProduct.getProduct_price()) ) ;
            tvProductTotalPrice.setText(" x "+quantity+" : "+df.format(cost));
            tvProductQuantity.setText(""+quantity);


        return convertView;
    }


    /**
     *  + / -
     *   Class reduce or increase amount of product
     */

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

            // reduce quantity
            if(v.getId( ) == R.id.btnLVSubOrderMinus && quantity >  1){
                tvQuantity.setText(String.valueOf(--quantity));
            }


            // increase quatity
            if(v.getId() == R.id.btnLVSubOrderPlus) {
                tvQuantity.setText(String.valueOf(++quantity));
            }

                if(AppConst.DEBUG) Log.d(AppConst.LOGD, CLASSNAME+" ::: Update product +/- in DB "+
                        product+" QUATITY : "+quantity);


            // update DB with new quantity of product
            new UpdateProduct(product, quantity).start();

        }
    }


    /**
     *  Delete product from the tb_suborder
     *  pass the order id and product object to constructor
     */
    class ClickDeleteProductListener implements View.OnClickListener{

        private AbstractProduct product;
        private int orderId;

        /**
         * Construtor get order id and AbstractProduct object
         * as parameter then click method will delete the product
         * from tb_suborder and refresh ListView prom the parent Activity
         * @param orderId ID of order
         * @param product to be deleted
         */

        public ClickDeleteProductListener(int orderId, AbstractProduct product){
            this.product = product;
            this.orderId = orderId;

        }

        @Override
        public void onClick(View v) {
            //delete product and refresh ListView
            dbManager.deleteProductfromSubOrder(product, orderId);
            ((SubOrderActivity)context).updateSubOrder();
        }
    }

    /**
     *  Thread write the changes of quantity to DB
     */
    class UpdateProduct extends Thread{
        AbstractProduct product;
        int quantity;
        Handler handler = new Handler();

        UpdateProduct(AbstractProduct product, int quantity){
            this.product = product;
            this.quantity = quantity;
        }

        @Override
        public void run() {

            if(AppConst.DEBUG) Log.d(AppConst.LOGD, SubOrderProductAdapter.class.getCanonicalName()+" :::"+
                                    " UpdateProduct ::: "+product+" quant.:"+quantity);
            dbManager.addProductToOrder(product, quantity, true);
            dbManager.close();

            // call the method from the parent activity
            // refresh  SubOrder (ListView, Total etc)
            if(context instanceof SubOrderActivity){
                ((SubOrderActivity)context).updateSubOrder();
            }

        }
    }
}
