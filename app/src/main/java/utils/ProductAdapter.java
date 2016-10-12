package utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import pk.nz.pinoyklasiks.R;
import pk.nz.pinoyklasiks.beans.AbstractProduct;

/**
 * Product adater bind the view in layout with data
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class ProductAdapter extends ArrayAdapter<AbstractProduct>{

    // Constructor of customer adapter
    public ProductAdapter(Context context, List<AbstractProduct> products) {
        super(context, 0, products);
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
        final AbstractProduct abstractProduct = getItem(position);
        DecimalFormat df = new DecimalFormat("$##.00");

        // Check if this view null inflate it create another one
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lv_products, parent, false);
        }

        // Get the view to populate new data
        TextView tvProductName = (TextView)convertView.findViewById(R.id.tvLVProductName);
        TextView tvProductDesc = (TextView)convertView.findViewById(R.id.tvLVProductDescription);
        TextView tvProductPrice = (TextView)convertView.findViewById(R.id.tvLVProductPrice);
        ImageView ivPic = (ImageView)convertView.findViewById(R.id.ivLVProductPic);



        try{
            InputStream is = getContext().getAssets().open(abstractProduct.getProduct_pic()+".jpg");
            Drawable pic = Drawable.createFromStream(is, null);
            ivPic.setImageDrawable(pic);  // Read the pic of product and assign it to view

        }catch (Exception e){
            e.printStackTrace();
            ivPic.setImageResource(R.drawable.nopic);
            Toast.makeText(getContext(), "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Populate our date into the layout views
            tvProductName.setText(abstractProduct.getProduct_name());
            tvProductDesc.setText(abstractProduct.getProduct_desc());
            tvProductPrice.setText(df.format(abstractProduct.getProduct_price()) ) ;
            tvProductPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "My price "+abstractProduct.getProduct_price(), Toast.LENGTH_SHORT).show();
                }
            });


        return convertView;
    }
}
