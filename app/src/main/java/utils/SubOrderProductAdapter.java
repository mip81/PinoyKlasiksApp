package utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import pk.nz.pinoyklasiks.R;
import pk.nz.pinoyklasiks.beans.AbstractProduct;
import pk.nz.pinoyklasiks.db.DBManager;
import pk.nz.pinoyklasiks.db.IDAOManager;

/**
 * Product adater for suborder bind the view in layout with data
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class SubOrderProductAdapter extends ArrayAdapter<Map.Entry>{

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

        DecimalFormat df = new DecimalFormat("$##.00");

        // Get the the Entry consist AbstractProduc and quantity
        Map.Entry<AbstractProduct, Integer> entry = getItem(position);
         // Get the product
        final AbstractProduct abstractProduct =entry.getKey();
        // Calculate the price
        Double cost = abstractProduct.getProduct_price() * entry.getValue();

        // Check if this view null inflate it create another one
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lv_suborder_products, parent, false);
        }

        // Get the view to populate new data
        TextView tvProductName = (TextView)convertView.findViewById(R.id.tvLVSubOrderProductName);
        TextView tvProductPrice = (TextView)convertView.findViewById(R.id.tvLVSubOrderProductPrice);
        ImageView ivPic = (ImageView)convertView.findViewById(R.id.ivLVSubOrderProductPic);
       // Button btnAddToCart = (Button)convertView.findViewById(R.id.btnLVAddToCart);



        try{
            InputStream is = getContext().getAssets().open(abstractProduct.getProduct_pic()+".jpg");
            Drawable pic = Drawable.createFromStream(is, null);
            ivPic.setImageDrawable(pic);  // Read the pic of product and assign it to view

        }catch (Exception e){
            e.printStackTrace();
           // ivPic.setImageResource(R.drawable.nopic);
            Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

            //Populate our date into the layout views
            tvProductName.setText(abstractProduct.getProduct_name());
            tvProductPrice.setText(df.format(abstractProduct.getProduct_price()) ) ;


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

                    // TODO: 10/13/16 Add action for deletion and reduce increase amount
                }
            }).start();
        }
    }

}
